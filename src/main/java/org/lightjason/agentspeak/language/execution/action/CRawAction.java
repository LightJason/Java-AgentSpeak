/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.action;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;


/**
 * encapsulate class for any non-executable data type e.g. boolean
 * and caching of execution results
 */
public final class CRawAction<T> extends IBaseExecution<T>
{
    /**
     * ctor
     *
     * @param p_data any object data
     */
    public CRawAction( final T p_data )
    {
        super( p_data );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final Stream<IVariable<?>> variables()
    {
        if ( m_value instanceof IVariable<?> )
            return Stream.of( (IVariable<?>) m_value );

        if ( m_value instanceof IExpression )
            return ( (IExpression) m_value ).variables();

        return super.variables();
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode();
    }

    @Override
    public final String toString()
    {
        return m_value.toString();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        if ( m_value instanceof Boolean )
            return this.getTypedResult( (Boolean) m_value, p_return );
        if ( m_value instanceof IVariable<?> )
            return this.getTypedResult( (IVariable<?>) m_value, p_context, p_return );
        if ( m_value instanceof IExpression )
            return this.getTypedResult( (IExpression) m_value, p_context, p_parallel, p_argument, p_return, p_annotation );

        return this.getTypedResult( m_value, p_return );
    }

    /**
     * fixed type result
     *
     * @param p_execution boolean value
     * @param p_return native return
     * @return fuzzy-boolean
     */
    private IFuzzyValue<Boolean> getTypedResult( final Boolean p_execution, final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( p_execution ) );
        return CFuzzyValue.from( p_execution );
    }

    /**
     * fixed type result
     *
     * @param p_execution variable value
     * @param p_context context
     * @param p_return native return
     * @return fuzzy-boolean
     */
    private IFuzzyValue<Boolean> getTypedResult( final IVariable<?> p_execution, final IContext p_context,
                                                 final List<ITerm> p_return
    )
    {
        final IVariable<?> l_value = (IVariable<?>) CCommon.replaceFromContext( p_context, p_execution );

        if ( !l_value.allocated() )
            return CFuzzyValue.from( false );

        if ( l_value.valueAssignableTo( Boolean.class ) )
            return CFuzzyValue.from( l_value.toAny() );

        p_return.add( CRawTerm.from( l_value.toAny() ) );
        return CFuzzyValue.from( true );
    }


    /**
     * fixed type result
     *
     * @param p_execution execution element
     * @param p_context context
     * @param p_parallel paralle execution
     * @param p_argument arguments
     * @param p_return native return
     * @param p_annotation annotations
     * @return fuzzy-boolean
     */
    private IFuzzyValue<Boolean> getTypedResult( final IExpression p_execution, final IContext p_context, final Boolean p_parallel,
                                                 final List<ITerm> p_argument, final List<ITerm> p_return,
                                                 final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_return = new LinkedList<>();
        if ( ( !p_execution.execute( p_context, p_parallel, p_argument, l_return, p_annotation ).value() ) || ( l_return.isEmpty() ) )
            return CFuzzyValue.from( false );

        return CFuzzyValue.from( CCommon.raw( l_return.get( 0 ) ) );
    }



    /**
     * fixed type result
     *
     * @param p_execution any other value
     * @param p_return native return
     * @return fuzzy-boolean
     */
    private IFuzzyValue<Boolean> getTypedResult( final T p_execution, final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( p_execution ) );
        return CFuzzyValue.from( true );
    }
}
