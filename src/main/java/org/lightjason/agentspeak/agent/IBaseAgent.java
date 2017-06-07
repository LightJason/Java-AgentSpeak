/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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
import com.google.common.collect.TreeMultimap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.agent.fuzzy.IFuzzy;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IStructureHash;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.action.unify.IUnifier;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.plan.statistic.CPlanStatistic;
import org.lightjason.agentspeak.language.instantiable.plan.statistic.IPlanStatistic;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.instantiable.plan.IInjection;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
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
    protected final Map<String, Object> m_storage = new ConcurrentHashMap<>();
    /**
     * execution trigger with content hash
     */
    protected final Map<Integer, ITrigger> m_trigger = new ConcurrentHashMap<>();
    /**
     * multimap with rules
     */
    protected final Multimap<IPath, IRule> m_rules = Multimaps.synchronizedMultimap( LinkedHashMultimap.create() );
    /**
     * map with all existing plans and successful / fail runs
     */
    protected final Multimap<ITrigger, IPlanStatistic> m_plans = Multimaps.synchronizedMultimap(
                                                                    TreeMultimap.create( IStructureHash.COMPARATOR, Comparator.<IPlanStatistic>naturalOrder() ) );
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
     * set for waking-up literals
     */
    private final Set<ITerm> m_sleepingterm = Collections.synchronizedSet( new HashSet<>() );
    /**
     * unifier
     */
    private final IUnifier m_unifier;
    /**
     * aggregation function
     */
    private final IInjection m_aggregation;
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
        p_configuration.plans().parallelStream().forEach( i -> m_plans.put( i.trigger(), CPlanStatistic.from( i ) ) );
        p_configuration.rules().parallelStream().forEach( i -> m_rules.put( i.identifier().fqnfunctor(), i ) );
        if ( p_configuration.initialgoal() != null )
            m_trigger.put( p_configuration.initialgoal().hashCode(), p_configuration.initialgoal() );
    }

    @Override
    public final IView<T> beliefbase()
    {
        return m_beliefbase;
    }

    @Override
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    public final <N extends IInspector> Stream<N> inspect( final N... p_inspector )
    {
        if ( p_inspector == null )
            return Stream.empty();

        return Arrays.stream( p_inspector ).parallel().peek( i -> {
            i.inspectcycle( m_cycle.get() );
            i.inspectsleeping( m_sleepingcycles.get() );
            i.inspectbelief( m_beliefbase.stream() );
            i.inspectplans( m_plans.values().stream() );
            i.inspectrunningplans( m_runningplans.values().stream() );
            i.inspectstorage( m_storage.entrySet().stream() );
            i.inspectrules( m_rules.values().stream() );
        } );
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
    public final IAgent<T> sleep( final long p_cycles, final ITerm... p_term )
    {
        return this.sleep(
            p_cycles,
            ( p_term == null ) || ( p_term.length == 0 )
            ? Stream.of()
            : Arrays.stream( p_term )
        );
    }

    @Override
    public final IAgent<T> sleep( final long p_cycles, final Stream<ITerm> p_literal )
    {
        m_sleepingcycles.set( p_cycles );
        p_literal.filter( i -> !i.hasVariable() ).forEach( m_sleepingterm::add );
        return this;
    }

    @Override
    public final IAgent<T> wakeup( final ITerm... p_term )
    {
        return this.wakeup(
            ( p_term == null ) || ( p_term.length == 0 )
            ? Stream.of()
            : Arrays.stream( p_term )
        );
    }

    @Override
    public final IAgent<T> wakeup( final Stream<ITerm> p_term )
    {
        p_term.forEach( m_sleepingterm::add );
        this.active( true );
        return this;
    }

    @Override
    public final Map<String, Object> storage()
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
    public final Multimap<ITrigger, IPlanStatistic> plans()
    {
        return m_plans;
    }

    @Override
    public final IFuzzy<Boolean, T> fuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public final IInjection aggregation()
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
                    Stream.of( m_trigger.values(), m_runningplans.keySet(), m_beliefbase ),
                    ( l, c ) -> MessageFormat.format( "{0}: {1}", l, c )
                ).toArray(),
                " / "
            )
        );
    }

    @Override
    public final IFuzzyValue<Boolean> trigger( final ITrigger p_trigger, final boolean... p_immediately )
    {
        if ( m_sleepingcycles.get() > 0 )
            return CFuzzyValue.from( false );

        // check if literal does not store any variables
        if ( p_trigger.literal().hasVariable() )
            throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "literalvariable", p_trigger ) );

        // run plan immediatly and return
        if ( ( p_immediately != null ) && ( p_immediately.length > 0 ) && ( p_immediately[0] ) )
            return this.execute( this.generateexecution( Stream.of( p_trigger ) ) );

        // add trigger for the next cycle must be synchronized to avoid indeterministic state during execution
        synchronized ( this )
        {
            m_trigger.putIfAbsent( p_trigger.hashCode(), p_trigger );
        }

        return CFuzzyValue.from( true );
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

        // clear running plan- and trigger list and execute elements
        this.execute( this.generateexecutionlist() );


        // increment cycle and set the cycle time
        m_cycle.incrementAndGet();
        m_cycletime.set( System.nanoTime() );

        return (T) this;
    }

    /**
     * create the plan executionlist with clearing internal structures
     *
     * @note must be synchronized for avoid indeterministic trigger list
     *
     * @return collection with execution plan and context
     */
    private synchronized Collection<Pair<IPlanStatistic, IContext>> generateexecutionlist()
    {
        m_runningplans.clear();
        final Collection<Pair<IPlanStatistic, IContext>> l_execution = this.generateexecution(
            Stream.concat(
                m_trigger.values().parallelStream(),
                m_beliefbase.trigger().parallel()
            )
        );
        m_trigger.clear();

        return l_execution;
    }


    /**
     * create execution list with plan and context
     *
     * @param p_trigger trigger stream
     * @return collection with excutable plans, instantiated execution context and plan statistic
     */
    private Collection<Pair<IPlanStatistic, IContext>> generateexecution( final Stream<ITrigger> p_trigger )
    {
        return p_trigger
            .filter( Objects::nonNull )
            // get all possible plans
            .flatMap( i -> m_plans.get( i ).stream().map( j -> new ImmutablePair<>( i, j ) ) )
            .parallel()
            // tries to unify trigger literal and filter of valid unification (returns set of unified variables)
            .map( i -> new ImmutablePair<>( i, CCommon.unifytrigger( m_unifier, i.getLeft(), i.getRight().plan().trigger() ) ) )
            // check if unification was possible
            .filter( i -> i.getRight().getLeft() )
            // create execution context
            .map( i -> CCommon.instantiateplan( i.getLeft().getRight(), this, i.getRight().getRight() ) )
            // check plan-condition
            .filter( i -> m_fuzzy.getDefuzzyfication().defuzzify( i.getLeft().plan().condition( i.getRight() ) ) )
            // collectors-call must be toList not toSet because plan-execution can be have equal elements
            // so a set avoid multiple plan-execution
            .collect( Collectors.toList() );
    }

    /**
     * execute list of plans
     *
     * @param p_execution execution collection with instantiated plans and context
     * @return fuzzy result for each executaed plan
     */
    private IFuzzyValue<Boolean> execute( final Collection<Pair<IPlanStatistic, IContext>> p_execution )
    {
        // update executable plan list, so that test-goals are defined all the time
        p_execution.parallelStream().forEach( i -> m_runningplans.put(
            i.getLeft().plan().trigger().literal().fqnfunctor(),
            i.getLeft().plan().trigger().literal().unify( i.getRight() )
        ) );

        // execute plan and return values and return execution result
        return p_execution.parallelStream().map( i -> {

            final IFuzzyValue<Boolean> l_result = i.getLeft().plan().execute( i.getRight(), false, null, null );
            if ( m_fuzzy.getDefuzzyfication().defuzzify( l_result ) )
                // increment successful runs
                i.getLeft().incrementsuccessful();
            else
                // increment failed runs and create delete goal-event
                i.getLeft().incrementfail();

            return l_result;
        } ).collect( m_fuzzy.getResultOperator() );
    }

    /**
     * runs the wakeup goal
     *
     * @param p_immediatly runs the wake always
     * @return returns true if the agent is active
     */
    private boolean active( final boolean p_immediatly )
    {
        // if the sleeping time ends or the agent will wakedup by a hard call,
        // create the trigger and reset the time value
        if ( ( m_sleepingcycles.compareAndSet( 0, Long.MIN_VALUE ) ) || p_immediatly )
        {
            (
                m_sleepingterm.isEmpty()

                ? Stream.of( CTrigger.from(
                    ITrigger.EType.ADDGOAL, CLiteral.from(
                    "wakeup"
                    )
                ) )

                : m_sleepingterm.parallelStream()
                                .map( i -> CTrigger.from(
                                    ITrigger.EType.ADDGOAL,
                                    CLiteral.from( "wakeup", i )
                                ) )

            ).forEach( i -> m_trigger.put( i.structurehash(), i ) );

            m_sleepingterm.clear();
        }

        // if the sleeping time is not infinity decrese the counter
        if ( ( m_sleepingcycles.get() > 0 ) && ( m_sleepingcycles.get() != Long.MAX_VALUE ) )
            m_sleepingcycles.decrementAndGet();

        return m_sleepingcycles.get() <= 0;
    }

}
