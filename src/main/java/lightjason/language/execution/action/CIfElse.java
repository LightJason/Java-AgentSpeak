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
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.CContext;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * if-else structure
 */
public final class CIfElse extends IBaseExecution<IExpression>
{

    /**
     * true execution block
     */
    private final List<IExecution> m_true;
    /**
     * false execution block
     */
    private final List<IExecution> m_false;


    /**
     * ctor
     *
     * @param p_expression expression
     * @param p_true true execution block
     */
    public CIfElse( final IExpression p_expression, final List<IExecution> p_true )
    {
        this( p_expression, p_true, null );
    }

    /**
     * ctor
     *
     * @param p_expression expression
     * @param p_true true execution block
     * @param p_false false execution block
     */
    public CIfElse( final IExpression p_expression, final List<IExecution> p_true, final List<IExecution> p_false )
    {
        super( p_expression );
        m_true = Collections.unmodifiableList( p_true );
        m_false = p_false;
    }


    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_argument = new LinkedList<>();
        if ( ( !m_value.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_argument, Collections.<ITerm>emptyList() ).getValue() ) ||
             ( l_argument.size() != 1 ) )
            return CBoolean.from( false );

        // create local execution context depend on the expression result and run the
        final Set<IVariable<?>> l_variables = new HashSet<>( p_context.getInstanceVariables().values() );

        // true-execution
        if ( CCommon.getRawValue( l_argument.get( 0 ) ) )
        {
            l_variables.addAll( m_true.stream().flatMap( i -> i.getVariables().stream() ).collect( Collectors.toList() ) );
            final IContext<?> l_context = new CContext<>( p_context.getAgent(), p_context.getInstance(), l_variables );
            return CBoolean.from(
                    m_true.stream().map( i -> i.execute( l_context, p_parallel, p_argument, p_return, p_annotation ) ).allMatch( CBoolean.isTrue() ) );
        }

        // false-execution if exists
        if ( m_false != null )
        {
            l_variables.addAll( m_false.stream().flatMap( i -> i.getVariables().stream() ).collect( Collectors.toList() ) );
            final IContext<?> l_context = new CContext<>( p_context.getAgent(), p_context.getInstance(), l_variables );
            return CBoolean.from(
                    m_false.stream().map( i -> i.execute( l_context, p_parallel, p_argument, p_return, p_annotation ) ).allMatch( CBoolean.isTrue() ) );
        }

        return CBoolean.from( true );
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode() + m_true.hashCode() + m_false.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "if ( {0} ) {1}{2}", m_value, m_true, m_false.isEmpty() ? "" : MessageFormat.format( ", {0}", m_false ) );
    }
}
