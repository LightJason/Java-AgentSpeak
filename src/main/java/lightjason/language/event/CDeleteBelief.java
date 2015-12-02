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

package lightjason.language.event;

import lightjason.common.CPath;

import java.text.MessageFormat;


/**
 * event to describe belief-delete
 */
public class CDeleteBelief implements IEvent<CPath>
{
    /**
     * event name
     */
    public static final String ID = "delete belief";
    /**
     * event data
     **/
    private final CPath m_data;

    /**
     * ctor
     *
     * @param p_functor name of the functor
     */
    public CDeleteBelief( final CPath p_functor )
    {
        m_data = p_functor;
    }


    @Override
    public int hashCode()
    {
        return m_data.hashCode() + this.getName().hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return p_object.hashCode() == this.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}( {1} )", ID, m_data );
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    public CPath getData()
    {
        return m_data;
    }
}
