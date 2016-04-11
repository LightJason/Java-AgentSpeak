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

package lightjason.agent.configuration;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lightjason.agent.fuzzy.CBoolFuzzy;
import lightjason.agent.fuzzy.IFuzzy;
import lightjason.agent.unify.CUnifier;
import lightjason.beliefbase.CBeliefBase;
import lightjason.beliefbase.CStorage;
import lightjason.beliefbase.IView;
import lightjason.common.CCommon;
import lightjason.common.IPath;
import lightjason.language.ILiteral;
import lightjason.language.execution.IVariableBuilder;
import lightjason.language.execution.action.unify.IUnifier;
import lightjason.language.instantiable.plan.IPlan;
import lightjason.language.instantiable.plan.trigger.CTrigger;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import lightjason.language.instantiable.rule.IRule;
import lightjason.language.score.CZeroAggregation;
import lightjason.language.score.IAggregation;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;


/**
 * default agent configuration
 */
public class CDefaultAgentConfiguration implements IAgentConfiguration
{
    /**
     * logger
     */
    protected final static Logger LOGGER = CCommon.getLogger( CDefaultAgentConfiguration.class );
    /**
     * name of the root beliefbase
     */
    protected static final String BELIEFBASEROOTNAME = "beliefbase";
    /**
     * unifier instance
     */
    protected final IUnifier m_unifier;
    /**
     * initial goal trigger
     */
    protected final ITrigger m_initialgoal;
    /**
     * instance of the aggregate function
     */
    protected final IAggregation m_aggregation;
    /**
     * instance of agent plans
     */
    protected final Multimap<ITrigger, IPlan> m_plans;
    /**
     * instance of agent rules
     */
    protected final Multimap<IPath, IRule> m_rules;
    /**
     * instance of variable builder
     *
     * @warning can be set to null
     */
    protected final IVariableBuilder m_variablebuilder;
    /**
     * instance of initial beliefs
     */
    protected final Collection<ILiteral> m_initialbeliefs;
    /**
     * fuzzy operator
     */
    protected final IFuzzy<Boolean> m_fuzzy;


    /**
     * ctor
     */
    public CDefaultAgentConfiguration()
    {
        this(
                new CBoolFuzzy(), Collections.<ILiteral>emptyList(), Collections.<IPlan>emptySet(), Collections.<IRule>emptySet(), null,
                new CUnifier(), new CZeroAggregation()
        );
    }

    /**
     * ctor
     *
     * @param p_fuzzy fuzzy operator
     * @param p_initalbeliefs set with initial beliefs
     * @param p_plans plans
     * @param p_rules rules
     * @param p_initialgoal initial goal
     * @param p_unifier unifier component
     * @param p_aggregation aggregation function
     */
    public CDefaultAgentConfiguration( final IFuzzy<Boolean> p_fuzzy, final Collection<ILiteral> p_initalbeliefs,
                                       final Set<IPlan> p_plans,
                                       final Set<IRule> p_rules, final ILiteral p_initialgoal, final IUnifier p_unifier, final IAggregation p_aggregation
    )
    {
        this( p_fuzzy, p_initalbeliefs, p_plans, p_rules, p_initialgoal, p_unifier, p_aggregation, null );
    }

    /**
     * ctor
     *
     * @param p_fuzzy fuzzy operator
     * @param p_initalbeliefs set with initial beliefs
     * @param p_plans plans
     * @param p_rules rules
     * @param p_initialgoal initial goal
     * @param p_aggregation aggregation function
     * @param p_unifier unifier component
     * @param p_variablebuilder variable builder
     */
    public CDefaultAgentConfiguration( final IFuzzy<Boolean> p_fuzzy, final Collection<ILiteral> p_initalbeliefs,
                                       final Set<IPlan> p_plans,
                                       final Set<IRule> p_rules, final ILiteral p_initialgoal, final IUnifier p_unifier, final IAggregation p_aggregation,
                                       final IVariableBuilder p_variablebuilder
    )
    {
        m_unifier = p_unifier;
        m_aggregation = p_aggregation;
        m_fuzzy = p_fuzzy;
        m_variablebuilder = p_variablebuilder;
        m_initialbeliefs = Collections.unmodifiableCollection( p_initalbeliefs );

        final Multimap<ITrigger, IPlan> l_plans = HashMultimap.create();
        p_plans.stream().forEach( i -> l_plans.put( i.getTrigger(), i ) );
        m_plans = ImmutableMultimap.copyOf( l_plans );

        final Multimap<IPath, IRule> l_rules = HashMultimap.create();
        p_rules.stream().forEach( i -> l_rules.put( i.getIdentifier(), i ) );
        m_rules = ImmutableMultimap.copyOf( l_rules );

        m_initialgoal = p_initialgoal != null ? CTrigger.from( ITrigger.EType.ADDGOAL, p_initialgoal ) : null;

        LOGGER.info( MessageFormat.format( "create agent configuration: {0}", this ) );
    }

    @Override
    public final IView getBeliefbase()
    {
        final IView l_beliefbase = new CBeliefBase( new CStorage<>() ).create( BELIEFBASEROOTNAME );
        m_initialbeliefs.parallelStream().forEach( i -> l_beliefbase.add( i.shallowcopy() ) );

        // clear all events of the initial beliefs
        l_beliefbase.getTrigger();

        return l_beliefbase;
    }

    @Override
    public final ITrigger getInitialGoal()
    {
        return m_initialgoal;
    }

    @Override
    public final IAggregation getAggregate()
    {
        return m_aggregation;
    }

    @Override
    public final IUnifier getUnifier()
    {
        return m_unifier;
    }

    @Override
    public final IVariableBuilder getVariableBuilder()
    {
        return m_variablebuilder;
    }

    @Override
    public final IFuzzy<Boolean> getFuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public final Multimap<ITrigger, IPlan> getPlans()
    {
        return m_plans;
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
                "{0} ( unifier: {1} / aggregation {2} / {3} / variable-builder: {4} / initial-goal: {5} / initial beliefs: {6} / plan-trigger: {7} / rule-trigger: {8} )",
                super.toString(),
                m_unifier,
                m_aggregation,
                m_fuzzy,
                m_variablebuilder,
                m_initialgoal,
                m_initialbeliefs,
                m_plans.keySet(),
                m_rules.keySet()
        );
    }
}
