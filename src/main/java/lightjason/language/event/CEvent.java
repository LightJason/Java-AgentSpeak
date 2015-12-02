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
 * event with literal data
 */
public final class CEvent implements IEvent<CPath>
{
    /**
     * literal path
     */
    private final CPath m_data;
    /**
     * event type
     */
    private final EType m_event;

    public CEvent( final EType p_event, final CPath p_data )
    {
        m_data = p_data;
        m_event = p_event;
    }

    @Override
    public int hashCode()
    {
        return 11 * m_event.hashCode() + 17 * m_data.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}{1}", m_event, m_data );
    }

    @Override
    public EType getID()
    {
        return m_event;
    }

    @Override
    public CPath getData()
    {
        return m_data;
    }
}
