/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.error;

import org.lightjason.agentspeak.common.CCommon;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.logging.Logger;


/**
 * no-such-element exception
 */
public class CNoSuchElementException extends NoSuchElementException implements IException
{
    /**
     * logger
     */
    private static final Logger LOGGER = CCommon.logger( CNoSuchElementException.class );
    /**
     * serial uid
     */
    private static final transient long serialVersionUID = -8986035121952708243L;

    /**
     * ctor
     */
    public CNoSuchElementException()
    {
        super();
        LOGGER.warning( "exception is thrown" );
    }

    /**
     * ctor
     *
     * @param p_message message
     */
    public CNoSuchElementException( @Nonnull final String p_message )
    {
        super( p_message );
        LOGGER.warning( p_message );
    }
}
