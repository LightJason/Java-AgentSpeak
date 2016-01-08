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

package lightjason.language.plan.trigger;

import lightjason.common.CPath;

import java.text.MessageFormat;


/**
 * event with literal data
 */
public final class CTrigger implements ITrigger<CPath>
{
    /**
     * literal path
     */
    private final CPath m_data;
    /**
     * event type
     */
    private final EType m_event;

    /**
     * ctor
     *
     * @param p_event type
     * @param p_data data
     */
    public CTrigger( final EType p_event, final CPath p_data )
    {
        m_data = p_data;
        m_event = p_event;
    }

    @Override
    public final int hashCode()
    {
        return m_event.hashCode() + m_data.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}{1}", m_event, m_data );
    }

    @Override
    public final EType getID()
    {
        return m_event;
    }

    @Override
    public final CPath getData()
    {
        return m_data;
    }
}
