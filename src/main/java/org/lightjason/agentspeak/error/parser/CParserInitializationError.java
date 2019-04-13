/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.error.parser;

import javax.annotation.Nonnull;


/**
 * parser initialization exception
 */
public final class CParserInitializationError extends RuntimeException implements IParserException
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -7834327699522051867L;

    /**
     * ctor
     */
    public CParserInitializationError()
    {
        super();
    }

    /**
     * ctor
     *
     * @param p_message error string
     */
    public CParserInitializationError( @Nonnull final String p_message )
    {
        super( p_message );
    }

    /**
     * ctor
     *
     * @param p_message error string
     * @param p_cause throwable
     */
    public CParserInitializationError( @Nonnull final String p_message, @Nonnull final Throwable p_cause )
    {
        super( p_message, p_cause );
    }

    /**
     * ctor
     *
     * @param p_cause throwable
     */
    public CParserInitializationError( @Nonnull final Throwable p_cause )
    {
        super( p_cause );
    }

}
