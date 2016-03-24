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

import lightjason.error.CIllegalArgumentException;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * unify action
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

        // check unique variables - get variable frequency especially any variable "_"
        final Map<IVariable<?>, Integer> l_frequency = Stream.concat(
                CCommon.recursiveterm( p_literal.orderedvalues() ),
                CCommon.recursiveliteral( p_literal.annotations() )
        ).filter( i -> i instanceof IVariable<?> )
                                                             .map( i -> (IVariable<?>) i )
                                                             .collect( Collectors.toConcurrentMap( i -> i, i -> 1, Integer::sum ) );

        if ( l_frequency.isEmpty() )
            throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( this, "novariable" ) );

        if ( l_frequency.entrySet().stream().filter( i -> !i.getKey().getFunctor().equals( "_" ) ).filter( i -> i.getValue() > 1 ).findAny().isPresent() )
            throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( this, "uniquevariable" ) );

        // count variables
        m_variablenumber = l_frequency.size();
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
            CCommon.recursiveterm( m_value.values() ).filter( i -> i instanceof IVariable<?> ).forEach( i -> add( ( (IVariable<?>) i ).shallowcopy() ) );
            CCommon.recursiveliteral( m_value.annotations() ).filter( i -> i instanceof IVariable<?> ).forEach(
                    i -> add( ( (IVariable<?>) i ).shallowcopy() ) );

        }};
    }
}
