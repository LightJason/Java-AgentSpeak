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

package lightjason.language.instantiable.plan.trigger;

import lightjason.common.IPath;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.text.MessageFormat;


/**
 * event with literal data
 */
public final class CTrigger implements ITrigger<Pair<Boolean, IPath>>
{
    /**
     * literal path and negated flag
     */
    private final Pair<Boolean, IPath> m_data;
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
    public CTrigger( final EType p_event, final IPath p_data )
    {
        this( p_event, false, p_data );
    }

    /**
     * ctor
     *
     * @param p_event type
     * @param p_negated negated element
     * @param p_data data
     */
    public CTrigger( final EType p_event, final boolean p_negated, final IPath p_data )
    {
        m_data = new ImmutablePair<>( p_negated, p_data );
        m_event = p_event;
    }

    /**
     * creates a trigger event
     *
     * @param p_event event
     * @param p_negated negated element
     * @param p_path path of the literal
     * @return trigger object
     */
    public static ITrigger<Pair<Boolean, IPath>> from( final EType p_event, final boolean p_negated, final IPath p_path )
    {
        return new CTrigger( p_event, p_negated, p_path );
    }

    /**
     * creates a trigger event
     *
     * @param p_event event
     * @param p_path path of the literal
     * @return trigger object
     */
    public static ITrigger<Pair<Boolean, IPath>> from( final EType p_event, final IPath p_path )
    {
        return new CTrigger( p_event, p_path );
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
    public final Pair<Boolean, IPath> getData()
    {
        return m_data;
    }
}
