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
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
        return new ObjectMapper().readValue( IBaseRest.httpget( p_url ), p_class );
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
        return new XmlMapper().readValue( IBaseRest.httpget( p_url ), Map.class );
    }

    /**
     * creates a literal structure from a stream of string elements,
     * the string stream will be build in a tree structure
     *
     * @param p_functor stream with functor strings
     * @param p_values value stream
     * @return term
     */
    @Nonnull
    protected static ITerm baseliteral( @Nonnull final Stream<String> p_functor, @Nonnull final Stream<ITerm> p_values )
    {
        final Stack<String> l_tree = p_functor.collect( Collectors.toCollection( Stack::new ) );

        ILiteral l_literal = CLiteral.from( l_tree.pop(), p_values );
        while ( !l_tree.isEmpty() )
            l_literal = CLiteral.from( l_tree.pop(), l_literal );

        return l_literal;
    }

    /**
     * converts an object into a term stream
     *
     * @param p_object object
     * @return term stream
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    protected static Stream<ITerm> flatterm( @Nullable final Object p_object )
    {
        if ( ( p_object == null ) || ( ( p_object instanceof Map ) && ( ( (Map<String, ?>) p_object ).isEmpty() ) ) )
            return Stream.empty();

        return p_object instanceof Map
               ? flatmap( (Map<String, ?>) p_object )
               : p_object instanceof Collection
                 ? flatcollection( (Collection) p_object )
                 : Stream.of( CRawTerm.from( p_object ) );
    }



    /**
     * transformas a map into a literal
     *
     * @param p_map input map
     * @return term stream
     */
    @Nonnull
    private static Stream<ITerm> flatmap( @Nonnull final Map<String, ?> p_map )
    {
        return p_map.entrySet()
                    .stream()
                    .map( i ->
                              CLiteral.from(
                                  i.getKey().toLowerCase().replaceAll( "[^([a-z][0-9]\\-/_)]]", "_" ),
                                  flatterm( i.getValue() )
                              )
                    );
    }

    /**
     * transforms a collection into a term stream
     *
     * @param p_collection collection
     * @return term stream
     */
    @Nonnull
    private static Stream<ITerm> flatcollection( @Nonnull final Collection<?> p_collection )
    {
        return p_collection.stream().flatMap( IBaseRest::flatterm );
    }

}
