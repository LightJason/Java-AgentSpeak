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

package org.lightjason.agentspeak.configuration;

import org.apache.commons.lang3.StringUtils;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.fuzzy.CBoolFuzzy;
import org.lightjason.agentspeak.agent.fuzzy.IFuzzy;
import org.lightjason.agentspeak.agent.unify.CUnifier;
import org.lightjason.agentspeak.beliefbase.CBeliefbasePersistent;
import org.lightjason.agentspeak.beliefbase.storage.CMultiStorage;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.action.unify.IUnifier;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * default agent configuration
 */
public class CDefaultAgentConfiguration<T extends IAgent<?>> implements IAgentConfiguration<T>
{
    /**
     * logger
     */
    protected static final Logger LOGGER = CCommon.logger( CDefaultAgentConfiguration.class );
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
     * ctor
     */
    @SuppressWarnings( "unchecked" )
    public CDefaultAgentConfiguration()
    {
        this(
            new CBoolFuzzy<>(), Collections.emptyList(),
            Collections.emptySet(), Collections.emptySet(),
            null, new CUnifier()
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
     */
    public CDefaultAgentConfiguration( final IFuzzy<Boolean, T> p_fuzzy, final Collection<ILiteral> p_initalbeliefs,
                                       final Set<IPlan> p_plans, final Set<IRule> p_rules,
                                       final ILiteral p_initialgoal, final IUnifier p_unifier
    )
    {
        this( p_fuzzy, p_initalbeliefs, p_plans, p_rules, p_initialgoal, p_unifier, IVariableBuilder.EMPTY );
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
     * @param p_variablebuilder variable builder
     */
    public CDefaultAgentConfiguration( final IFuzzy<Boolean, T> p_fuzzy, final Collection<ILiteral> p_initalbeliefs,
                                       final Set<IPlan> p_plans, final Set<IRule> p_rules,
                                       final ILiteral p_initialgoal, final IUnifier p_unifier,
                                       final IVariableBuilder p_variablebuilder
    )
    {
        m_unifier = p_unifier;
        m_fuzzy = p_fuzzy;
        m_variablebuilder = p_variablebuilder;

        m_plans = Collections.unmodifiableSet( p_plans );
        m_rules = Collections.unmodifiableSet( p_rules );
        m_initialbeliefs = Collections.unmodifiableCollection( p_initalbeliefs );
        m_initialgoal = p_initialgoal != null ? CTrigger.from( ITrigger.EType.ADDGOAL, p_initialgoal ) : null;

        LOGGER.info( MessageFormat.format( "create agent configuration: {0}", this ) );
    }

    @Override
    public IView<T> beliefbase()
    {
        final IView<T> l_beliefbase = new CBeliefbasePersistent<T>( new CMultiStorage<>() ).create( BELIEFBASEROOTNAME );

        System.out.println( this.getClass() + "-> " + m_initialbeliefs );
        m_initialbeliefs.forEach( i -> l_beliefbase.add( i.shallowcopy() ) );

        System.out.println( this.getClass() + " -> " + l_beliefbase.stream().collect( Collectors.toList() ) );

        // clear all events of the initial beliefs
        l_beliefbase.trigger();

        return l_beliefbase;
    }

    @Override
    public final ITrigger initialgoal()
    {
        return m_initialgoal;
    }

    @Override
    public final IUnifier unifier()
    {
        return m_unifier;
    }

    @Override
    public final IVariableBuilder variablebuilder()
    {
        return m_variablebuilder;
    }

    @Override
    public final IFuzzy<Boolean, T> fuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public final Collection<ILiteral> initialbeliefs()
    {
        return m_initialbeliefs;
    }

    @Override
    public final Set<IPlan> plans()
    {
        return m_plans;
    }

    @Override
    public final Set<IRule> rules()
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
            "{0} ( unifier: {1} / {2} {3} )",
            super.toString(),
            m_unifier,
            m_fuzzy,
            l_elements.isEmpty() ? "" : l_elements
        ).trim();
    }
}
