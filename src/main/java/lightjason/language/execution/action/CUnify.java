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

package lightjason.language.execution.action;

import lightjason.language.CRawTerm;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.List;


/**
 * test goal action
 */
public final class CUnify extends IBaseExecution<ILiteral>
{
    /**
     * parallel unification
     */
    private final boolean m_parallel;
    /**
     * unification expression
     */
    private final IExpression m_expression;

    /**
     * ctor
     *
     * @param p_parallel parallel execution
     * @param p_literal literal
     */
    public CUnify( final boolean p_parallel, final ILiteral p_literal )
    {
        this( p_parallel, p_literal, null );
    }

    /**
     * ctor
     *
     * @param p_parallel parallel execution
     * @param p_literal literal
     * @param p_expression expression based on the unification result
     */
    public CUnify( final boolean p_parallel, final ILiteral p_literal, final IExpression p_expression )
    {
        super( p_literal );
        m_parallel = p_parallel;
        m_expression = p_expression;
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "?{0}", m_value );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final Object l_result = m_parallel
                                ? p_context.getAgent().getUnifier().parallelunify( p_context.getAgent(), m_value, m_expression )
                                : p_context.getAgent().getUnifier().sequentialunify( p_context.getAgent(), m_value, m_expression );

        if ( l_result == null )
            return CBoolean.from( false );

        p_return.add( CRawTerm.from( l_result ) );
        return CBoolean.from( true );
    }

}
