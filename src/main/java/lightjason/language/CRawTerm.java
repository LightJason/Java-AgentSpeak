/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language;

import lightjason.common.CPath;


/**
 * term structure for simple datatypes
 */
public final class CRawTerm<T> implements ITerm
{
    /**
     * data
     */
    private final T m_data;
    /**
     * functor
     */
    private final CPath m_functor;


    /**
     * ctor
     *
     * @param p_data data
     */
    public CRawTerm( final T p_data )
    {
        m_data = p_data;
        m_functor = new CPath( m_data.toString() );
    }

    @Override
    public final int hashCode()
    {
        return m_data == null ? super.hashCode() : m_data.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return m_data == null ? super.equals( p_object ) : m_data.equals( p_object );
    }

    @Override
    public final String toString()
    {
        return m_data == null ? "" : m_data.toString();
    }

    @Override
    public final String getFunctor()
    {
        return m_functor.getSuffix();
    }

    @Override
    public final CPath getFunctorPath()
    {
        return m_functor.getSubPath( 0, m_functor.size() - 1 );
    }

    @Override
    public final CPath getFQNFunctor()
    {
        return m_functor;
    }
}
