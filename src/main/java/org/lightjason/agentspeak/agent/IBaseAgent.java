/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.agent;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.agent.fuzzy.IFuzzy;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.action.unify.IUnifier;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.score.IAggregation;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * agent base structure
 *
 * @tparam T agent type
 */
public abstract class IBaseAgent<T extends IAgent<?>> implements IAgent<T>
{
    /**
     * beliefbase
     */
    protected final IView<T> m_beliefbase;
    /**
     * storage map
     *
     * @note must be thread-safe
     */
    protected final Map<String, ?> m_storage = new ConcurrentHashMap<>();
    /**
     * execution trigger
     */
    protected final Set<ITrigger> m_trigger = Sets.newConcurrentHashSet();
    /**
     * multimap with rules
     */
    protected final Multimap<IPath, IRule> m_rules = Multimaps.synchronizedMultimap( LinkedHashMultimap.create() );
    /**
     * map with all existing plans and successful / fail runs
     */
    protected final Multimap<ITrigger, MutableTriple<IPlan, AtomicLong, AtomicLong>> m_plans = Multimaps.synchronizedMultimap( HashMultimap.create() );
    /**
     * curent agent cycle
     */
    private final AtomicLong m_cycle = new AtomicLong();
    /**
     * nano seconds at the last cycle
     */
    private final AtomicLong m_cycletime = new AtomicLong();
    /**
     * number of sleeping cycles
     *
     * @note values >= 0 defines the sleeping time, Long.MAX_VALUE is infinity sleeping
     * negative values defines the activity
     */
    private final AtomicLong m_sleepingcycles = new AtomicLong( Long.MIN_VALUE );
    /**
     * unifier
     */
    private final IUnifier m_unifier;
    /**
     * aggregation function
     */
    private final IAggregation m_aggregation;
    /**
     * variable builder
     */
    private final IVariableBuilder m_variablebuilder;
    /**
     * fuzzy result collector
     */
    private final IFuzzy<Boolean, T> m_fuzzy;
    /**
     * running plans (thread-safe)
     */
    private final Multimap<IPath, ILiteral> m_runningplans = Multimaps.synchronizedSetMultimap( HashMultimap.create() );


    /**
     * ctor
     *
     * @param p_configuration agent configuration
     */
    public IBaseAgent( final IAgentConfiguration<T> p_configuration )
    {
        // initialize agent
        m_unifier = p_configuration.unifier();
        m_beliefbase = p_configuration.beliefbase();
        m_aggregation = p_configuration.aggregation();
        m_variablebuilder = p_configuration.variablebuilder();
        m_fuzzy = p_configuration.fuzzy();

        // initial plans and rules
        p_configuration.plans().parallelStream()
                       .forEach( i -> m_plans.put( i.getTrigger(), new MutableTriple<>( i, new AtomicLong( 0 ), new AtomicLong( 0 ) ) ) );
        p_configuration.rules().parallelStream()
                       .forEach( i -> m_rules.put( i.getIdentifier().fqnfunctor(), i ) );

        if ( p_configuration.initialgoal() != null )
            m_trigger.add( p_configuration.initialgoal() );
    }

    @Override
    public final IView<T> beliefbase()
    {
        return m_beliefbase;
    }

    @Override
    public <N extends IInspector> Stream<N> inspect( final N... p_inspector )
    {
        if ( p_inspector == null )
            return Stream.of();

        return Arrays.stream( p_inspector ).parallel().map( i -> {
            i.inspectcycle( m_cycle.get() );
            i.inspectsleeping( m_sleepingcycles.get() );
            i.inspectbelief( m_beliefbase.stream().parallel() );
            i.inspectplans( m_plans.entries().parallelStream().map( j -> new ImmutableTriple<>( j.getValue().getLeft(), j.getValue().getMiddle().get(),
                                                                                                j.getValue().getRight().get()
            ) ) );
            i.inspectrunningplans( m_runningplans.values().parallelStream() );
            i.inspectstorage( m_storage.entrySet().parallelStream() );
            i.inspectrules( m_rules.values().parallelStream() );
            return i;
        } );
    }

    @Override
    public final IFuzzyValue<Boolean> trigger( final ITrigger p_trigger, final boolean... p_immediately )
    {
        if ( m_sleepingcycles.get() > 0 )
            return CFuzzyValue.from( false );

        // check if literal does not store any variables
        if ( Stream.concat(
            p_trigger.getLiteral().orderedvalues(),
            p_trigger.getLiteral().annotations()
        ).anyMatch( i -> i instanceof IVariable<?> ) )
            throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "literalvariable", p_trigger ) );

        // run plan immediatly and return
        if ( ( p_immediately != null ) && ( p_immediately.length > 0 ) && ( p_immediately[0] ) )
            return this.execute( this.executionlist( p_trigger ) );

        // add trigger for the next cycle
        m_trigger.add( p_trigger );
        return CFuzzyValue.from( true );
    }

    @Override
    public final Multimap<IPath, ILiteral> runningplans()
    {
        return ImmutableMultimap.copyOf( m_runningplans );
    }

    @Override
    public final boolean sleeping()
    {
        return m_sleepingcycles.get() > 0;
    }

    @Override
    public final IAgent<T> sleep( final long p_cycles )
    {
        m_sleepingcycles.set( p_cycles );
        return this;
    }

    @Override
    public final IAgent<T> wakeup( final ITerm... p_value )
    {
        this.active( true, p_value );
        return this;
    }

    @Override
    public final Map<String, ?> storage()
    {
        return m_storage;
    }

    @Override
    public final IUnifier unifier()
    {
        return m_unifier;
    }

    @Override
    public final long cycletime()
    {
        return m_cycletime.get();
    }

    @Override
    public final long cycle()
    {
        return m_cycle.get();
    }

    @Override
    public final Multimap<ITrigger, MutableTriple<IPlan, AtomicLong, AtomicLong>> plans()
    {
        return m_plans;
    }

    @Override
    public final IFuzzy<Boolean, T> fuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public final IAggregation aggregation()
    {
        return m_aggregation;
    }

    @Override
    public final IVariableBuilder variablebuilder()
    {
        return m_variablebuilder;
    }

    @Override
    public final Multimap<IPath, IRule> rules()
    {
        return m_rules;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final <N extends IAgent<?>> N raw()
    {
        return (N) this;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format(
            "{0} ( Cycle: {1} / {2} )",
            super.toString(),
            m_cycle,
            StringUtils.join(
                StreamUtils.zip(
                    Stream.of( "Trigger", "Running Plans", "Beliefbase" ),
                    Stream.of( m_trigger, m_runningplans.keySet(), m_beliefbase ),
                    ( l, c ) -> MessageFormat.format( "{0}: {1}", l, c )
                ).toArray(),
                " / "
            )
        );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public T call() throws Exception
    {
        // run beliefbase update, because environment can be changed and decrement sleeping value
        m_beliefbase.update( (T) this );
        if ( !this.active( false ) )
            // check wakup-event otherwise suspend
            return (T) this;

        // update defuzzification
        m_fuzzy.getDefuzzyfication().update( (T) this );

        // create a list of all possible execution elements, that is a local cache for well-defined execution
        final Collection<Pair<MutableTriple<IPlan, AtomicLong, AtomicLong>, IContext>> l_execution = Stream.concat(
            m_trigger.parallelStream(),
            m_beliefbase.trigger().parallel()
        ).flatMap( i -> this.executionlist( i ).parallelStream() ).collect( Collectors.toList() );

        // clear running plan- and trigger list and execute elements
        m_runningplans.clear();
        m_trigger.clear();
        this.execute( l_execution );

        // increment cycle and set the cycle time
        m_cycle.incrementAndGet();
        m_cycletime.set( System.nanoTime() );

        return (T) this;
    }


    /**
     * create execution list with plan and context
     *
     * @param p_trigger trigger
     * @return list with tupel of plan-triple and context for execution
     */
    @SuppressWarnings( "unchecked" )
    private Collection<Pair<MutableTriple<IPlan, AtomicLong, AtomicLong>, IContext>> executionlist( final ITrigger p_trigger )
    {
        return m_plans.get( p_trigger ).parallelStream()

                      // filter for possible trigger
                      .filter( i -> i.getLeft().getTrigger().getType().equals( p_trigger.getType() ) )

                      // filter trigger-literal for avoid duplicated instantiation on non-existing values / annotations
                      .filter( i -> ( i.getLeft().getTrigger().getLiteral().emptyValues() == p_trigger.getLiteral().emptyValues() )
                                    && ( i.getLeft().getTrigger().getLiteral().emptyAnnotations() == p_trigger.getLiteral().emptyAnnotations() )
                      )

                      // unify variables in plan definition
                      .map( i -> new ImmutablePair<>( i, m_unifier.literal( i.getLeft().getTrigger().getLiteral(), p_trigger.getLiteral() ) ) )

                      // avoid uninstantiated variables
                      .filter( i -> i.getRight().size()
                                    == CCommon.variablefrequency( i.getLeft().getLeft().getTrigger().getLiteral() ).size()
                      )

                      // initialize context
                      .map( i -> {
                          final double l_fails = i.getLeft().getMiddle().get();
                          final double l_succeed = i.getLeft().getRight().get();
                          final double l_sum = l_succeed + l_fails;

                          return new ImmutablePair<>( i.getLeft(), i.getLeft().getLeft().instantiate(
                              this,
                              (Stream<IVariable<?>>) Stream.of(
                                  i.getRight().stream(),

                                  // execution count
                                  Stream.of( new CConstant<>( "PlanSuccessful", i.getLeft().getMiddle() ) ),
                                  Stream.of( new CConstant<>( "PlanFail", i.getLeft().getRight() ) ),
                                  Stream.of( new CConstant<>( "PlanRuns", l_sum ) ),

                                  // execution ratio
                                  Stream.of( new CConstant<>( "PlanSuccessfulRatio", l_sum == 0 ? 0 : l_succeed / l_sum ) ),
                                  Stream.of( new CConstant<>( "PlanFailRatio", l_sum == 0 ? 0 : l_fails / l_sum ) )

                              )
                                                           .reduce( Stream::concat )
                                                           .orElseGet( Stream::<IVariable<?>>empty )
                          ) );
                      } )

                      // check plan condition
                      .filter( i -> m_fuzzy.getDefuzzyfication().defuzzify( i.getLeft().getLeft().condition( i.getRight() ) ) )

                      // create execution collection
                      .collect( Collectors.toList() );
    }

    /**
     * execute list of plans
     *
     * @param p_execution execution list
     * @return fuzzy result
     */
    private IFuzzyValue<Boolean> execute( final Collection<Pair<MutableTriple<IPlan, AtomicLong, AtomicLong>, IContext>> p_execution )
    {
        // update executable plan list, so that test-goals are defined all the time
        p_execution.parallelStream().forEach( i -> m_runningplans.put(
            i.getLeft().getLeft().getTrigger().getLiteral().fqnfunctor(),
            i.getLeft().getLeft().getTrigger().getLiteral().unify( i.getRight() )
        ) );

        // execute plan and return values and return execution result
        return p_execution.parallelStream().map( i -> {

            final IFuzzyValue<Boolean> l_result = i.getLeft().getLeft().execute( i.getRight(), false, null, null, null );
            if ( m_fuzzy.getDefuzzyfication().defuzzify( l_result ) )
                // increment successful runs
                i.getLeft().getMiddle().getAndIncrement();
            else
                // increment failed runs and create delete goal-event
                i.getLeft().getRight().getAndIncrement();

            return l_result;
        } ).collect( m_fuzzy.getResultOperator() );
    }

    /**
     * runs the wakeup goal
     *
     * @param p_immediatly runs the wake always
     * @param p_value wakeup term
     * @return returns true if the agent is active
     */
    private boolean active( final boolean p_immediatly, final ITerm... p_value )
    {
        // if the sleeping time ends or the agent will wakedup by a hard call,
        // create the trigger and reset the time value
        if ( ( m_sleepingcycles.compareAndSet( 0, Long.MIN_VALUE ) ) || p_immediatly )
            m_trigger.add(
                CTrigger.from(
                    ITrigger.EType.ADDGOAL,
                    CLiteral.from(
                        "wakeup",
                        p_value == null
                        ? Stream.<ITerm>empty()
                        : Arrays.stream( p_value )
                    )
                )
            );

        // if the sleeping time is not infinity decrese the counter
        if ( ( m_sleepingcycles.get() > 0 ) && ( m_sleepingcycles.get() != Long.MAX_VALUE ) )
            m_sleepingcycles.decrementAndGet();

        return m_sleepingcycles.get() <= 0;
    }

}
