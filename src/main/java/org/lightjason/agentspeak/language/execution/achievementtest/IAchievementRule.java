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

package org.lightjason.agentspeak.language.execution.achievementtest;

import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IRelocateVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;


/**
 * abstract class for execute a logical-rule
 */
public abstract class IAchievementRule<T> extends IBaseExecution<T>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -315973892409409832L;

    /**
     * ctor
     *
     * @param p_type value of the achievment-goal
     */
    protected IAchievementRule( @Nonnull final T p_type )
    {
        super( p_type );
    }

    /**
     * find and execute rule of context
     *
     * @param p_parallel parallel execution
     * @param p_context execution context
     * @param p_value execution literal
     * @return boolean result
     */
    @Nonnull
    protected static IFuzzyValue<Boolean> findandexecute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final ILiteral p_value )
    {
        // read current rules, if not exists execution fails
        final Collection<IRule> l_rules = p_context.agent().rules().get( p_value.fqnfunctor() );
        if ( Objects.isNull( l_rules ) )
            return CFuzzyValue.of( false );

        // first step allocate all variables of the literal with the current context variables
        final ILiteral l_allocate = p_value.bind( p_context );

        // second step execute backtracking rules sequential
        return l_rules.stream()
                      .map( i -> executerule( p_context, l_allocate, i ) )
                      .filter( IFuzzyValue::value )
                      .findFirst()
                      .orElse( CFuzzyValue.of( false ) );
    }

    /**
     * execute single rule content
     *
     * @param p_context context
     * @param p_literal binding literal
     * @param p_rule rule
     * @return execution result
     */
    private static IFuzzyValue<Boolean> executerule( @Nonnull final IContext p_context, @Nonnull final ILiteral p_literal, @Nonnull final IRule p_rule )
    {
        final Set<IVariable<?>> l_variables = p_context.agent().unifier().unify( p_literal, p_rule.identifier() );

        if ( p_rule.execute(
            false,
            p_rule.instantiate( p_context.agent(), l_variables.stream() ),
            Collections.emptyList(),
            Collections.emptyList()
        ).value() )
        {
            l_variables.parallelStream()
                       .filter( i -> i instanceof IRelocateVariable<?> )
                       .forEach( i -> i.<IRelocateVariable<?>>term().relocate() );
            return CFuzzyValue.of( true );
        }

        return CFuzzyValue.of( false );
    }

    @Override
    public final int hashCode()
    {
        return Objects.isNull( m_value ) ? 0 : m_value.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return p_object instanceof IExecution && this.hashCode() == p_object.hashCode();
    }

}
