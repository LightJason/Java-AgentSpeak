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

package lightjason.error;

/**
 * illegal state exception
 */
@SuppressWarnings( "serial" )
public final class CIllegalStateException extends IllegalStateException
{
    /**
     * ctor
     */
    public CIllegalStateException()
    {
        super();
    }

    /**
     * ctor
     *
     * @param p_message message
     */
    public CIllegalStateException( final String p_message )
    {
        super( p_message );
    }

    /**
     * ctor
     *
     * @param p_message message
     * @param p_cause throwable
     */
    public CIllegalStateException( final String p_message, final Throwable p_cause )
    {
        super( p_message, p_cause );
    }

    /**
     * ctor
     *
     * @param p_cause throwable
     */
    public CIllegalStateException( final Throwable p_cause )
    {
        super( p_cause );
    }
}
