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

package lightjason.common.benchmark;


import lightjason.common.CCommon;


/**
 * timer class for benchmarking
 */
public final class CTimer
{
    /**
     * start time
     **/
    private long m_start = -1;


    /**
     * start timer
     *
     * @return timer object
     */
    public CTimer start()
    {
        if ( m_start >= 0 )
            throw new IllegalArgumentException( CCommon.getLanguageString( this, "start" ) );

        m_start = this.getTime();
        return this;
    }

    /**
     * stop time
     *
     * @param p_label label of the timer value
     * @return timer object
     */
    public CTimer stop( final String p_label )
    {
        if ( m_start < 0 )
            throw new IllegalStateException( CCommon.getLanguageString( this, "stop" ) );

        CSummary.getInstance().setTime( p_label, this.getTime() - m_start );
        m_start = -1;
        return this;
    }

    /**
     * returns the current time
     *
     * @return time value
     */
    private long getTime()
    {
        return System.nanoTime();
    }
}
