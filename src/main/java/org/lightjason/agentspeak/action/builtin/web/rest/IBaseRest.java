/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.builtin.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.lightjason.agentspeak.action.builtin.web.IBaseWeb;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;


/**
 * base class to read data from the restful service
 *
 * @note all action which inherits this class uses the system property "http.agent" for defining
 * the http user-agent
 */
public abstract class IBaseRest extends IBaseWeb
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3713528201539676487L;
    /**
     * json mapper
     */
    private static final ObjectMapper JSONMAPPER = new ObjectMapper();
    /**
     * xml mapper
     */
    private static final XmlMapper XMLMAPPER = new XmlMapper();

    /**
     * ctor
     * @param p_length length
     */
    protected IBaseRest( final int p_length )
    {
        super( p_length );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    /**
     * reads a json structure from an url
     *
     * @param p_url url
     * @param p_class convert class type
     * @return data object
     *
     * @throws IOException is thrown on io errors
     */
    @Nonnull
    protected static <T> T json( @Nonnull final String p_url, @Nonnull final Class<T> p_class ) throws IOException
    {
        return JSONMAPPER.readValue( IBaseRest.httpgetexecute( p_url ), p_class );
    }

    /**
     * reads a xml structure from an url
     *
     * @param p_url url
     * @return map with xml data
     *
     * @throws IOException is thrown on io errors
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    protected static Map<String, ?> xml( @Nonnull final String p_url ) throws IOException
    {
        return XMLMAPPER.readValue( IBaseRest.httpgetexecute( p_url ), Map.class );
    }

}
