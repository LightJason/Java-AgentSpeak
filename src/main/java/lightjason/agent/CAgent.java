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

package lightjason.agent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import lightjason.agent.configuration.IAgentConfiguration;
import lightjason.agent.fuzzy.IFuzzy;
import lightjason.beliefbase.IView;
import lightjason.common.CCommon;
import lightjason.common.IPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.CConstant;
import lightjason.language.ILiteral;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IVariableBuilder;
import lightjason.language.execution.action.unify.IUnifier;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.instantiable.plan.IPlan;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import lightjason.language.instantiable.rule.IRule;
import lightjason.language.score.IAggregation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * agent class
 *
 * @bug inspector call incomplete
 */
@SuppressWarnings( "serial" )
public class CAgent implements IAgent
{
    /**
     * logger
     */
    protected final static Logger LOGGER = CCommon.getLogger( CAgent.class );

    /**
     * beliefbase
     *
     * @warning need not to be null
     */
    protected final IView m_beliefbase;
    /**
     * unifier
     *
     * @warning need not to be null
     */
    protected final IUnifier m_unifier;
    /**
     * aggregation function
     *
     * @warning need not to be null
     */
    protected final IAggregation m_aggregation;
    /**
     * variable builder
     *
     * @warning can be set to null
     */
    protected final IVariableBuilder m_variablebuilder;
    /**
     * map with all existing plans
     */
    protected final Multimap<IPath, IRule> m_rules;
    /**
     * map with all existing plans and successful / fail runs
     */
    protected final Multimap<ITrigger, MutableTriple<IPlan, AtomicLong, AtomicLong>> m_plans = Multimaps.synchronizedMultimap( HashMultimap.create() );
    /**
     * storage map
     *
     * @note must be thread-safe and need not to be null
     */
    protected final Map<String, ?> m_storage = new ConcurrentHashMap<>();
    /**
     * fuzzy result collector
     */
    protected final IFuzzy<Boolean> m_fuzzy;
    /**
     * curent agent cycle
     */
    protected long m_cycle;
    /**
     * nano seconds at the last cycle
     */
    protected long m_cycletime;
    /**
     * execution trigger
     */
    protected final Set<ITrigger> m_trigger = Sets.newConcurrentHashSet();
    /**
     * running plans (thread-safe)
     */
    protected final Multimap<IPath, ILiteral> m_runningplans = Multimaps.synchronizedSetMultimap( HashMultimap.create() );
    /**
     * hibernate state
     */
    private volatile boolean m_hibernate;



    /**
     * ctor
     *
     * @param p_configuration agent configuration
     */
    public CAgent( final IAgentConfiguration p_configuration )
    {
        // initialize agent
        m_unifier = p_configuration.getUnifier();
        m_beliefbase = p_configuration.getBeliefbase();
        m_aggregation = p_configuration.getAggregate();
        m_variablebuilder = p_configuration.getVariableBuilder();
        m_fuzzy = p_configuration.getFuzzy();
        m_rules = Multimaps.synchronizedMultimap( HashMultimap.create( p_configuration.getRules() ) );

        // initial plans with default values
        p_configuration.getPlans().asMap().entrySet().parallelStream()
                       .forEach( i -> i.getValue().stream()
                                       .forEach( j -> m_plans.put( i.getKey(), new MutableTriple<>( j, new AtomicLong( 0 ), new AtomicLong( 0 ) ) ) )
                       );

        if ( p_configuration.getInitialGoal() != null )
            m_trigger.add( p_configuration.getInitialGoal() );

        LOGGER.info( MessageFormat.format( "create agent: {0}", this ) );
    }

    @Override
    public final IView getBeliefBase()
    {
        return m_beliefbase;
    }

    @Override
    public void inspect( final IInspector... p_inspector )
    {
        if ( p_inspector == null )
            return;

        final Multimap<IInspector.EValue, Object> l_map = new ImmutableSetMultimap.Builder<IInspector.EValue, Object>()
                .put( IInspector.EValue.CYCLE, m_cycle )
                .put( IInspector.EValue.HIBERNATE, m_hibernate )
                .putAll( IInspector.EValue.STORAGE, m_storage.entrySet().stream().collect( Collectors.toList() ).iterator() )
                .putAll( IInspector.EValue.RUNNINGPLAN, m_storage.entrySet().stream().collect( Collectors.toList() ).iterator() )
                .build();

        Arrays.stream( p_inspector ).parallel().forEach( i -> i.inspect( l_map ) );
    }

    @Override
    public IFuzzyValue<Boolean> trigger( final ITrigger p_trigger, final boolean... p_immediately )
    {
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
    public final boolean hibernate()
    {
        return m_hibernate;
    }

    @Override
    public final boolean hibernate( final boolean p_value )
    {
        m_hibernate = p_value;
        return m_hibernate;
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
    public final Multimap<IPath, IRule> getRules()
    {
        return m_rules;
    }

    @Override
    public final IFuzzy<Boolean> getFuzzy()
    {
        return m_fuzzy;
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
    public IAgent call() throws Exception
    {
        LOGGER.info( MessageFormat.format( "agent cycle: {0}", this ) );

        // run beliefbase update, because environment can be changed
        m_beliefbase.update( this );
        if ( m_hibernate )
            // check wakup-event otherwise suspend
            return this;

        // update defuzzification
        m_fuzzy.getDefuzzyfication().update( this );

        // create a list of all possible execution elements, that is a local cache for well-defined execution
        final Collection<Pair<MutableTriple<IPlan, AtomicLong, AtomicLong>, IContext>> l_execution = Stream.concat(
                m_trigger.parallelStream(),
                m_beliefbase.getTrigger().parallelStream()
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
    protected Collection<Pair<MutableTriple<IPlan, AtomicLong, AtomicLong>, IContext>> executionlist( final ITrigger p_trigger )
    {
        return m_plans.get( p_trigger ).parallelStream()

                      // filter for possible trigger
                      .filter( i -> i.getLeft().getTrigger().getType().equals( p_trigger.getType() ) )

                      // filter trigger-literal for avoid duplicated instantiation on non-existing values / annotations
                      .filter( i -> ( i.getLeft().getTrigger().getLiteral().emptyValues() == p_trigger.getLiteral().emptyValues() )
                                    && ( i.getLeft().getTrigger().getLiteral().emptyAnnotations() == p_trigger.getLiteral().emptyAnnotations() )
                      )

                      // unify variables in plan definition
                      .map( i -> new ImmutablePair<>( i, m_unifier.literalunify( i.getLeft().getTrigger().getLiteral(), p_trigger.getLiteral() ) ) )

                      // avoid uninstantiated variables
                      .filter( i -> i.getRight().size() == lightjason.language.CCommon.getVariableFrequency( i.getLeft().getLeft().getTrigger().getLiteral() )
                                                                                      .size() )

                      // initialize context
                      .map( i -> new ImmutablePair<>( i.getLeft(), i.getLeft().getLeft().getContext(
                              this,
                              m_aggregation,
                              m_variablebuilder,
                              new HashSet<IVariable<?>>()
                              {{
                                  addAll( i.getRight() );

                                  // execution count
                                  add( new CConstant<>( "PlanSuccessful", i.getLeft().getMiddle() ) );
                                  add( new CConstant<>( "PlanFail", i.getLeft().getRight() ) );

                                  // execution ratio
                                  double l_fails = i.getLeft().getMiddle().get();
                                  double l_succeed = i.getLeft().getRight().get();
                                  double l_sum = l_succeed + l_fails;
                                  add( new CConstant<>( "PlanSuccessfulRatio", l_sum == 0 ? 0 : l_succeed / l_sum ) );
                                  add( new CConstant<>( "PlanFailRatio", l_sum == 0 ? 0 : l_fails / l_sum ) );
                                  add( new CConstant<>( "PlanRuns", l_sum ) );
                              }}
                      ) ) )

                      // check plan condition
                      .filter( i -> i.getLeft().getLeft().condition( i.getRight() ).getValue() )

                      // create execution collection
                      .collect( Collectors.toList() );
    }

    /**
     * execute list of plans
     *
     * @param p_execution execution list
     * @return fuzzy result
     */
    protected IFuzzyValue<Boolean> execute( final Collection<Pair<MutableTriple<IPlan, AtomicLong, AtomicLong>, IContext>> p_execution )
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
                i.getLeft().getMiddle().getAndIncrement();
            else
                i.getLeft().getRight().getAndIncrement();

            return l_result;
        } ).collect( m_fuzzy.getResultOperator() );
    }

}
