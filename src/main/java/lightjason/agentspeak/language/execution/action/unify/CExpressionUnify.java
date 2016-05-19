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

package lightjason.agentspeak.language.execution.action.unify;

import lightjason.agentspeak.language.ILiteral;
import lightjason.agentspeak.language.ITerm;
import lightjason.agentspeak.language.execution.IContext;
import lightjason.agentspeak.language.execution.expression.IExpression;
import lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import lightjason.agentspeak.language.variable.IVariable;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;


/**
 * unify expression
 */
public final class CExpressionUnify extends CDefaultUnify
{
    /**
     * unification expression
     */
    private final IExpression m_expression;

    /**
     * ctor
     *
     * @param p_parallel parallel execution
     * @param p_literal literal
     * @param p_constraint expression
     */
    public CExpressionUnify( final boolean p_parallel, final ILiteral p_literal, final IExpression p_constraint )
    {
        super( p_parallel, p_literal );
        m_expression = p_constraint;
    }


    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}>>({1}, {2})", m_parallel ? "@" : "", m_value, m_expression );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        return m_parallel
               ? p_context.getAgent().getUnifier().parallel( p_context, m_value, m_variablenumber, m_expression )
               : p_context.getAgent().getUnifier().sequential( p_context, m_value, m_variablenumber, m_expression );
    }

    @Override
    public final Stream<IVariable<?>> getVariables()
    {
        return Stream.concat(
            m_expression.getVariables(),
            CExpressionUnify.super.getVariables()
        );
    }

}
