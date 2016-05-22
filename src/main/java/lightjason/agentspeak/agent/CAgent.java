/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agentspeak.agent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import lightjason.agentspeak.agent.fuzzy.IFuzzy;
import lightjason.agentspeak.beliefbase.IView;
import lightjason.agentspeak.common.CCommon;
import lightjason.agentspeak.common.IPath;
import lightjason.agentspeak.configuration.IAgentConfiguration;
import lightjason.agentspeak.error.CIllegalArgumentException;
import lightjason.agentspeak.language.CLiteral;
import lightjason.agentspeak.language.ILiteral;
import lightjason.agentspeak.language.ITerm;
import lightjason.agentspeak.language.execution.IContext;
import lightjason.agentspeak.language.execution.IVariableBuilder;
import lightjason.agentspeak.language.execution.action.unify.IUnifier;
import lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import lightjason.agentspeak.language.instantiable.plan.IPlan;
import lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import lightjason.agentspeak.language.instantiable.rule.IRule;
import lightjason.agentspeak.language.score.IAggregation;
import lightjason.agentspeak.language.variable.CConstant;
import lightjason.agentspeak.language.variable.IVariable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * agent class
 */
public class CAgent<T extends IAgent<?>> implements IAgent<T>
{
    /**
     * logger
     */
    protected static final Logger LOGGER = CCommon.getLogger( CAgent.class );

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
    private long m_cycle;
    /**
     * nano seconds at the last cycle
     */
    private long m_cycletime;
    /**
     * hibernate state
     */
    private volatile boolean m_hibernate;
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
     *
     * @warning can be set to null
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
    public CAgent( final IAgentConfiguration<T> p_configuration )
    {
        // initialize agent
        m_unifier = p_configuration.getUnifier();
        m_beliefbase = p_configuration.getBeliefbase();
        m_aggregation = p_configuration.getAggregate();
        m_variablebuilder = p_configuration.getVariableBuilder();
        m_fuzzy = p_configuration.getFuzzy();

        // initial plans and rules
        p_configuration.getPlans().parallelStream()
                       .forEach( i -> m_plans.put( i.getTrigger(), new MutableTriple<>( i, new AtomicLong( 0 ), new AtomicLong( 0 ) ) ) );
        p_configuration.getRules().parallelStream()
                       .forEach( i -> m_rules.put( i.getIdentifier().getFQNFunctor(), i ) );

        if ( p_configuration.getInitialGoal() != null )
            m_trigger.add( p_configuration.getInitialGoal() );

        LOGGER.info( MessageFormat.format( "create agent: {0}", this ) );
    }

    @Override
    public final IView<T> getBeliefBase()
    {
        return m_beliefbase;
    }

    @Override
    public void inspect( final IInspector... p_inspector )
    {
        if ( p_inspector == null )
            return;

        Arrays.stream( p_inspector ).parallel().forEach( i -> {
            i.inspectcycle( m_cycle );
            i.inspecthibernate( m_hibernate );
            i.inspectbelief( m_beliefbase.parallelStream() );
            i.inspectplans( m_plans.entries().parallelStream().map( j -> new ImmutableTriple<>( j.getValue().getLeft(), j.getValue().getMiddle().get(),
                                                                                                j.getValue().getRight().get()
            ) ) );
            i.inspectrunningplans( m_runningplans.values().parallelStream() );
            i.inspectstorage( m_storage.entrySet().parallelStream() );
            i.inspectrules( m_rules.values().parallelStream() );
        } );
    }

    @Override
    public final IFuzzyValue<Boolean> trigger( final ITrigger p_trigger, final boolean... p_immediately )
    {
        if ( m_hibernate )
            return CFuzzyValue.from( false );

        // check if literal does not store any variables
        if ( Stream.concat(
            p_trigger.getLiteral().orderedvalues(),
            p_trigger.getLiteral().annotations()
        ).filter( i -> i instanceof IVariable<?> ).findFirst().isPresent() )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "literalvariable", p_trigger ) );

        // run plan immediatly and return
        if ( ( p_immediately != null ) && ( p_immediately.length > 0 ) && ( p_immediately[0] ) )
            return this.execute( this.executionlist( p_trigger ) );

        // add trigger for the next cycle
        m_trigger.add( p_trigger );
        return CFuzzyValue.from( true );
    }

    @Override
    public final Multimap<IPath, ILiteral> getRunningPlans()
    {
        return ImmutableMultimap.copyOf( m_runningplans );
    }

    @Override
    public final boolean isSleeping()
    {
        return m_hibernate;
    }

    @Override
    public final IAgent<T> sleep()
    {
        m_hibernate = true;
        return this;
    }

    @Override
    public final IAgent<T> wakeup( final ITerm... p_value )
    {
        if ( !m_hibernate )
            return this;

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

        m_hibernate = false;
        return this;
    }

    @Override
    public final Map<String, ?> getStorage()
    {
        return m_storage;
    }

    @Override
    public final IUnifier getUnifier()
    {
        return m_unifier;
    }

    @Override
    public final long getLastCycleTime()
    {
        return m_cycletime;
    }

    @Override
    public final long getCycle()
    {
        return m_cycle;
    }

    @Override
    public final Multimap<ITrigger, MutableTriple<IPlan, AtomicLong, AtomicLong>> getPlans()
    {
        return m_plans;
    }

    @Override
    public final IFuzzy<Boolean, T> getFuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public final IAggregation getAggregation()
    {
        return m_aggregation;
    }

    @Override
    public final IVariableBuilder getVariableBuilder()
    {
        return m_variablebuilder;
    }

    @Override
    public final Multimap<IPath, IRule> getRules()
    {
        return m_rules;
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format(
            "{0} ( Cycle: {1} / Trigger: {2} / Running Plans: {3}  Beliefbase: {4} )",
            super.toString(),
            m_cycle,
            m_trigger,
            m_runningplans,
            m_beliefbase
        );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public IAgent<T> call() throws Exception
    {
        LOGGER.info( MessageFormat.format( "agent cycle: {0}", this ) );

        // run beliefbase update, because environment can be changed
        m_beliefbase.update( (T) this );
        if ( m_hibernate )
            // check wakup-event otherwise suspend
            return this;

        // update defuzzification
        m_fuzzy.getDefuzzyfication().update( (T) this );

        // create a list of all possible execution elements, that is a local cache for well-defined execution
        final Collection<Pair<MutableTriple<IPlan, AtomicLong, AtomicLong>, IContext>> l_execution = Stream.concat(
            m_trigger.parallelStream(),
            m_beliefbase.getTrigger().parallel()
        ).flatMap( i -> this.executionlist( i ).parallelStream() ).collect( Collectors.toList() );

        // clear running plan- and trigger list and execute elements
        m_runningplans.clear();
        m_trigger.clear();
        this.execute( l_execution );

        // increment cycle and set the cycle time
        m_cycle++;
        m_cycletime = System.nanoTime();

        return this;
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
                                    == lightjason.agentspeak.language.CCommon.getVariableFrequency( i.getLeft().getLeft().getTrigger().getLiteral() ).size()
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
            i.getLeft().getLeft().getTrigger().getLiteral().getFQNFunctor(),
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

}
