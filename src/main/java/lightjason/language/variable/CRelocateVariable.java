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

package lightjason.language.variable;

import com.rits.cloning.Cloner;
import lightjason.common.CPath;
import lightjason.common.IPath;
import lightjason.language.ITerm;

import java.text.MessageFormat;


/**
 * class for a relocated variable
 *
 * @tparam T variable type
 */
public final class CRelocateVariable<T> extends CVariable<T> implements IRelocateVariable
{
    /**
     * reference to relocated variable
     */
    private final IVariable<?> m_relocate;

    /**
     * ctor
     *
     * @param p_variable original variable, which should be used
     * @param p_relocate variable which should be relocated
     */
    public CRelocateVariable( final IVariable<?> p_variable, final IVariable<?> p_relocate )
    {
        super( p_variable.getFunctor(), p_variable.getTyped() );
        m_relocate = p_relocate;
    }

    /**
     * private ctor for creating object-copy
     *
     * @param p_functor functor
     * @param p_value value
     * @param p_variable referenced variable
     */
    private CRelocateVariable( final IPath p_functor, final T p_value, final IVariable<?> p_variable )
    {
        super( p_functor, p_value );
        m_relocate = p_variable;
    }

    @Override
    public final IVariable<?> relocate()
    {
        return m_relocate.set( this.getTyped() );
    }

    @Override
    public final IVariable<T> shallowcopy( final IPath... p_prefix )
    {
        return ( p_prefix == null ) || ( p_prefix.length == 0 )
               ? new CRelocateVariable<T>( m_functor, m_value, m_relocate )
               : new CRelocateVariable<T>( p_prefix[0].append( m_functor ), m_value, m_relocate );
    }

    @Override
    public final IVariable<T> shallowcopySuffix()
    {
        return new CRelocateVariable<>( this, m_relocate );
    }

    @Override
    public final ITerm deepcopy( final IPath... p_prefix )
    {
        return new CRelocateVariable<>(
            ( p_prefix == null ) || ( p_prefix.length == 0 )
            ? m_functor
            : m_functor.append( p_prefix[0] ),
            new Cloner().deepClone( m_value ),
            m_relocate
        );
    }

    @Override
    public final ITerm deepcopySuffix()
    {
        return new CRelocateVariable<>( CPath.from( m_functor.getSuffix() ), new Cloner().deepClone( m_value ), m_relocate );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})>{2}", m_functor, m_value == null ? "" : m_value, m_relocate );
    }
}
