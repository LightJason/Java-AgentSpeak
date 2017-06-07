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

package org.lightjason.agentspeak.language.execution.action.unify;

import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.Set;


/**
 * interface of an unification algorithm
 */
public interface IUnifier
{
    /**
     * unifies a literal
     *
     * @param p_literal literal with variables (creates a deep-copy)
     * @param p_value source literal with values
     * @return set with allocated variables
     *
     * @note check input literal and result of correct unification
     */
    Set<IVariable<?>> unify( final ILiteral p_literal, final ILiteral p_value );

    /**
     * unifies a literal - first found literal matches within the beliefbase
     *
     * @param p_context running context
     * @param p_literal literal with variables (creates a deep-copy)
     * @param p_variables number of unified variables
     */
    IFuzzyValue<Boolean> unify( final IContext p_context, final ILiteral p_literal, final long p_variables );

    /**
     * unifies a literal - checks all possible literals with the given expression, first expression
     * which finish successful is the result
     *
     * @param p_context running context
     * @param p_literal literal with variables (creates a deep-copy)
     * @param p_variables number of unified variables
     * @param p_expression expression
     * @param p_parallel parallel executon
     * @return boolean if a unify can be done
     */
    IFuzzyValue<Boolean> unify( final IContext p_context, final ILiteral p_literal, final long p_variables, final IExpression p_expression, final boolean p_parallel );

}
