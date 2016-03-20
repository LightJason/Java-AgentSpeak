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

import lightjason.language.CCommon;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * unify action
 *
 * @bug incomplete
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
     * number of variables
     */
    private final long m_variablenumber;

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

        m_variablenumber = CCommon.recursiveterm( p_literal.orderedvalues() ).filter( i -> i instanceof IVariable<?> ).count()
                           + CCommon.recursiveliteral( p_literal.annotations() ).filter( i -> i instanceof IVariable<?> ).count();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}>>{1}", m_parallel ? "@" : "", m_value );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        return m_parallel
               ? p_context.getAgent().getUnifier().parallelunify( p_context, m_value, m_variablenumber, m_expression )
               : p_context.getAgent().getUnifier().sequentialunify( p_context, m_value, m_variablenumber, m_expression );
    }

    @Override
    @SuppressWarnings( "serial" )
    public final Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{

            if ( m_expression != null )
                addAll( m_expression.getVariables() );

            // create a shallow-copy of all variables within the value- and annotation-definition
            m_value.values().filter( i -> i instanceof IVariable<?> ).forEach( i -> add( ( (IVariable<?>) i ).shallowcopy() ) );
            m_value.annotations().flatMap( i -> i.values() ).filter( i -> i instanceof IVariable<?> ).forEach( i -> add( ( (IVariable<?>) i ).shallowcopy() ) );

        }};
    }
}
