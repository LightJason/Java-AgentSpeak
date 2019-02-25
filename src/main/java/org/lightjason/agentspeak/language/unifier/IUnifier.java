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

package org.lightjason.agentspeak.language.unifier;

import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;


/**
 * interface of an unification algorithm
 */
public interface IUnifier
{
    /**
     * empty unifier
     */
    IUnifier EMPTY = new IUnifier()
    {
        @Nonnull
        @Override
        public Set<IVariable<?>> unify( @Nonnull final ILiteral p_source, @Nonnull final ILiteral p_target )
        {
            return Collections.emptySet();
        }

        @Nonnull
        @Override
        public Stream<IFuzzyValue<?>> unify( @Nonnull final IContext p_context, @Nonnull final ILiteral p_literal, final long p_variables,
                                           @Nonnull final IExecution p_expression, final boolean p_parallel
        )
        {
            return Stream.of();
        }
    };


    /**
     * unifies a literal
     *
     * @param p_source source literal (with data)
     * @param p_target target literal (with variables)
     * @return set with allocated variables
     *
     * @note check input literal and result of correct unification
     */
    @Nonnull
    Set<IVariable<?>> unify( @Nonnull final ILiteral p_source, @Nonnull final ILiteral p_target );

    /**
     * unifies the literal with the expression
     *
     * @param p_context running context
     * @param p_literal literal with variables (creates a deep-copy)
     * @param p_variables number of unified variables
     * @param p_expression expression
     * @param p_parallel parallel executon
     * @return fuzzy result
     */
    @Nonnull
    Stream<IFuzzyValue<?>> unify( @Nonnull final IContext p_context, @Nonnull final ILiteral p_literal, final long p_variables,
                                  @Nonnull final IExecution p_expression, final boolean p_parallel
    );


    /**
     * unifier algorithm
     */
    interface IAlgorithm
    {

        /**
         * unifier algorithm
         *
         * @param p_variables unified variables
         * @param p_source source stream of terms
         * @param p_target target stream of terms with variables (must be a deep-copy)
         * @return boolean of unifier success
         *
         * @tparam T term type
         */
        <T extends ITerm> boolean unify( final Set<IVariable<?>> p_variables, final Stream<T> p_source, final Stream<T> p_target );

    }

}
