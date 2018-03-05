/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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
import org.lightjason.agentspeak.beliefbase.CBeliefbase;
import org.lightjason.agentspeak.beliefbase.storage.CMultiStorage;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.fuzzy.operator.IFuzzyBundle;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.unify.IUnifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
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
    protected static final Logger LOGGER = CCommon.logger( IAgentConfiguration.class );
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
    protected final IFuzzyBundle<Boolean> m_fuzzy;
    /**
     * rules
     */
    protected final Set<IRule> m_rules;


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
    public CDefaultAgentConfiguration( @Nonnull final IFuzzyBundle<Boolean> p_fuzzy, @Nonnull final Collection<ILiteral> p_initalbeliefs,
                                       @Nonnull final Set<IPlan> p_plans, @Nonnull final Set<IRule> p_rules,
                                       @Nullable final ILiteral p_initialgoal, @Nonnull final IUnifier p_unifier
    )
    {
        this( p_fuzzy, p_initalbeliefs, p_plans, p_rules, p_initialgoal, p_unifier, IVariableBuilder.EMPTY );
    }

    /**
     * ctor
     *
     * @param p_fuzzy fuzzy operator
     * @param p_initialbeliefs set with initial beliefs
     * @param p_plans plans
     * @param p_rules rules
     * @param p_initialgoal initial goal
     * @param p_unifier unifier component
     * @param p_variablebuilder variable builder
     */
    public CDefaultAgentConfiguration( @Nonnull final IFuzzyBundle<Boolean> p_fuzzy, @Nonnull final Collection<ILiteral> p_initialbeliefs,
                                       @Nonnull final Set<IPlan> p_plans, @Nonnull final Set<IRule> p_rules,
                                       final ILiteral p_initialgoal, @Nonnull final IUnifier p_unifier,
                                       @Nonnull final IVariableBuilder p_variablebuilder
    )
    {
        m_fuzzy = p_fuzzy;
        m_unifier = p_unifier;
        m_variablebuilder = p_variablebuilder;

        m_plans = Collections.unmodifiableSet( p_plans );
        m_rules = Collections.unmodifiableSet( p_rules );
        m_initialbeliefs = Collections.unmodifiableCollection( p_initialbeliefs );
        m_initialgoal = p_initialgoal != null ? ITrigger.EType.ADDGOAL.builddefault( p_initialgoal ) : null;

        LOGGER.info( MessageFormat.format( "create agent configuration: {0}", this ) );
    }

    @Nonnull
    @Override
    public IView beliefbase()
    {
        final IView l_beliefbase = new CBeliefbase( new CMultiStorage<>() ).create( BELIEFBASEROOTNAME );
        m_initialbeliefs.forEach( i -> l_beliefbase.add( i.shallowcopy() ) );

        // clear all events of the initial beliefs
        l_beliefbase.trigger();

        return l_beliefbase;
    }

    @Nullable
    @Override
    public final ITrigger initialgoal()
    {
        return m_initialgoal;
    }

    @Nonnull
    @Override
    public final IUnifier unifier()
    {
        return m_unifier;
    }

    @Nonnull
    @Override
    public final IVariableBuilder variablebuilder()
    {
        return m_variablebuilder;
    }

    @Nonnull
    @Override
    public final IFuzzyBundle<Boolean> fuzzy()
    {
        return m_fuzzy;
    }

    @Nonnull
    @Override
    public final Collection<ILiteral> initialbeliefs()
    {
        return m_initialbeliefs;
    }

    @Nonnull
    @Override
    public final Set<IPlan> plans()
    {
        return m_plans;
    }

    @Nonnull
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
                Objects.isNull( m_variablebuilder ) ? "" : m_variablebuilder,
                m_initialbeliefs.isEmpty() ? "" : m_initialbeliefs,
                Objects.isNull( m_initialgoal ) ? "" : m_initialgoal,
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
