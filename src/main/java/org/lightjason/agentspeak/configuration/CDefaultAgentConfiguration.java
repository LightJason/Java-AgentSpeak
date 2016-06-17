/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.configuration;

import org.apache.commons.lang3.StringUtils;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.fuzzy.CBoolFuzzy;
import org.lightjason.agentspeak.agent.fuzzy.IFuzzy;
import org.lightjason.agentspeak.agent.unify.CUnifier;
import org.lightjason.agentspeak.beliefbase.CBeliefBase;
import org.lightjason.agentspeak.beliefbase.IBeliefPerceive;
import org.lightjason.agentspeak.beliefbase.IView;
import org.lightjason.agentspeak.beliefbase.storage.CMultiStorage;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.action.unify.IUnifier;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.score.IAggregation;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;


/**
 * default agent configuration
 */
public class CDefaultAgentConfiguration<T extends IAgent<?>> implements IAgentConfiguration<T>
{
    /**
     * logger
     */
    protected static final Logger LOGGER = CCommon.getLogger( CDefaultAgentConfiguration.class );
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
    protected final Set<IPlan> m_plans;
    /**
     * instance of variable builder
     */
    protected final IVariableBuilder m_variablebuilder;
    /**
     * instance of initial beliefs
     */
    protected final Collection<ILiteral> m_initialbeliefs;
    /**
     * fuzzy operator
     */
    protected final IFuzzy<Boolean, T> m_fuzzy;
    /**
     * rules
     */
    protected final Set<IRule> m_rules;
    /**
     * perceiveable
     */
    protected final Set<IBeliefPerceive<T>> m_perceivable;


    /**
     * ctor
     */
    @SuppressWarnings( "unchecked" )
    public CDefaultAgentConfiguration()
    {
        this(
            new CBoolFuzzy<>(), Collections.<ILiteral>emptyList(),
            Collections.<IBeliefPerceive<T>>emptySet(),
            Collections.<IPlan>emptySet(), Collections.<IRule>emptySet(),
            null, new CUnifier(), IAggregation.EMPTY
        );
    }

    /**
     * ctor
     *
     * @param p_fuzzy fuzzy operator
     * @param p_initalbeliefs set with initial beliefs
     * @param p_beliefperceive belief perceiver object
     * @param p_plans plans
     * @param p_rules rules
     * @param p_initialgoal initial goal
     * @param p_unifier unifier component
     * @param p_aggregation aggregation function
     */
    public CDefaultAgentConfiguration( final IFuzzy<Boolean, T> p_fuzzy, final Collection<ILiteral> p_initalbeliefs,
                                       final Set<IBeliefPerceive<T>> p_beliefperceive, final Set<IPlan> p_plans, final Set<IRule> p_rules,
                                       final ILiteral p_initialgoal, final IUnifier p_unifier, final IAggregation p_aggregation
    )
    {
        this( p_fuzzy, p_initalbeliefs, p_beliefperceive, p_plans, p_rules, p_initialgoal, p_unifier, p_aggregation, IVariableBuilder.EMPTY );
    }

    /**
     * ctor
     *
     * @param p_fuzzy fuzzy operator
     * @param p_initalbeliefs set with initial beliefs
     * @param p_beliefperceive belief perceiver object
     * @param p_plans plans
     * @param p_rules rules
     * @param p_initialgoal initial goal
     * @param p_aggregation aggregation function
     * @param p_unifier unifier component
     * @param p_variablebuilder variable builder
     */
    public CDefaultAgentConfiguration( final IFuzzy<Boolean, T> p_fuzzy, final Collection<ILiteral> p_initalbeliefs,
                                       final Set<IBeliefPerceive<T>> p_beliefperceive, final Set<IPlan> p_plans, final Set<IRule> p_rules,
                                       final ILiteral p_initialgoal, final IUnifier p_unifier, final IAggregation p_aggregation,
                                       final IVariableBuilder p_variablebuilder
    )
    {
        m_unifier = p_unifier;
        m_aggregation = p_aggregation;
        m_fuzzy = p_fuzzy;
        m_variablebuilder = p_variablebuilder;

        m_initialbeliefs = Collections.unmodifiableCollection( p_initalbeliefs );
        m_perceivable = p_beliefperceive;

        m_plans = Collections.unmodifiableSet( p_plans );
        m_rules = Collections.unmodifiableSet( p_rules );
        m_initialgoal = p_initialgoal != null ? CTrigger.from( ITrigger.EType.ADDGOAL, p_initialgoal ) : null;

        LOGGER.info( MessageFormat.format( "create agent configuration: {0}", this ) );
    }

    @Override
    public IView<T> getBeliefbase()
    {
        final IView<T> l_beliefbase = new CBeliefBase<>( new CMultiStorage<>( m_perceivable ) ).create( BELIEFBASEROOTNAME );
        m_initialbeliefs.parallelStream().forEach( i -> l_beliefbase.add( i.shallowcopy() ) );

        // clear all events of the initial beliefs
        l_beliefbase.trigger();

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
    public final IFuzzy<Boolean, T> getFuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public final Collection<ILiteral> getInitialBeliefs()
    {
        return m_initialbeliefs;
    }

    @Override
    public final Set<IBeliefPerceive<T>> getPerceivable()
    {
        return m_perceivable;
    }

    @Override
    public final Set<IPlan> getPlans()
    {
        return m_plans;
    }

    @Override
    public final Set<IRule> getRules()
    {
        return m_rules;
    }

    @Override
    public final String toString()
    {
        final String l_elements = StringUtils.join(
            Stream.of(
                m_variablebuilder == null ? "" : m_variablebuilder,
                m_initialbeliefs.isEmpty() ? "" : m_initialbeliefs,
                m_initialgoal == null ? "" : m_initialgoal,
                m_plans.isEmpty() ? "" : m_plans,
                m_rules.isEmpty() ? "" : m_rules
            ).filter( i -> !i.toString().trim().isEmpty() ).toArray(),
            " / "
        ).trim();

        return MessageFormat.format(
            "{0} ( unifier: {1} / aggregation {2} / {3} {4} )",
            super.toString(),
            m_unifier,
            m_aggregation,
            m_fuzzy,
            l_elements.isEmpty() ? "" : l_elements
        ).trim();
    }
}
