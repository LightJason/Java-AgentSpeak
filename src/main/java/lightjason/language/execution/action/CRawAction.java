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
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * encapsulate class for any non-executable data type e.g. boolean
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
    public final int hashCode()
    {
        return m_value != null ? m_value.hashCode() : super.hashCode();
    }

    @Override
    public final String toString()
    {
        return m_value != null ? m_value.toString() : super.toString();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final List<ITerm> p_annotation, final List<ITerm> p_argument,
                                               final List<ITerm> p_return
    )
    {
        if ( m_value instanceof Boolean )
            return this.getTypedResult( (Boolean) m_value, p_return );
        if ( m_value instanceof IVariable<?> )
            return this.getTypedResult( (IVariable<?>) m_value, p_return, p_context );

        return this.getTypedResult( m_value, p_return );
    }

    /**
     * fixed type result
     *
     * @param p_value boolean value
     * @param p_return native return
     * @return fuzzy-boolean
     */
    private CBoolean getTypedResult( final Boolean p_value, final List<ITerm> p_return )
    {
        p_return.add( new CRawTerm<>( m_value ) );
        return CBoolean.from( p_value );
    }

    /**
     * fixed type result
     *
     * @param p_value variable value
     * @param p_return native return
     * @param p_context context
     * @return fuzzy-boolean
     */
    private CBoolean getTypedResult( final IVariable<?> p_value, final List<ITerm> p_return, final IContext<?> p_context )
    {
        final IVariable<?> l_value = (IVariable<?>) CCommon.replaceVariableFromContext( p_context, p_value );

        if ( !l_value.isAllocated() )
            return CBoolean.from( false );

        if ( l_value.isValueAssignableTo( Boolean.class ) )
            return CBoolean.from( l_value.getTyped() );

        p_return.add( CRawTerm.from( l_value.get() ) );
        return CBoolean.from( true );
    }

    /**
     * fixed type result
     *
     * @param p_value any other value
     * @param p_return native return
     * @return fuzzy-boolean
     */
    private CBoolean getTypedResult( final T p_value, final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( m_value ) );
        return CBoolean.from( true );
    }
}
