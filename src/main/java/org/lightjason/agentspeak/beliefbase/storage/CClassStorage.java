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

package org.lightjason.agentspeak.beliefbase.storage;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * belief storage to get access to all class attributes
 *
 * @note all object attributes which are not transient will be read
 * @todo implement recursive descent of properties
 * @todo implement renaming function of properties
 */
public final class CClassStorage<M, T extends IAgent<?>> extends IBaseStorage<ILiteral, M, T>
{
    /**
     * object instance
     */
    private final Object m_instance;
    /**
     * map with functor and field reference
     */
    private final Map<String, Field> m_fields;

    /**
     * ctor
     *
     * @param p_instance object
     */
    public CClassStorage( final Object p_instance )
    {
        this( p_instance, i -> i );
    }

    /**
     * ctor
     *
     * @param p_instance object
     * @param p_fieldnameformater function to reformat field names
     */
    public CClassStorage( final Object p_instance, final Function<String, String> p_fieldnameformater )
    {
        m_instance = p_instance;
        m_fields = Collections.unmodifiableMap(
            Arrays.stream( m_instance.getClass().getDeclaredFields() )
                  .peek( i -> i.setAccessible( true ) )
                  .filter( i -> !Modifier.isTransient( i.getModifiers() ) )
                  .filter( i -> !Modifier.isStatic( i.getModifiers() ) )
                  .collect( Collectors.toMap(
                      i -> p_fieldnameformater.apply( i.getName() ).toLowerCase( Locale.ROOT ).replace( "\\s+", "" ), i -> i )
                  )
        );
    }


    @Override
    public final Stream<ILiteral> streamMultiElements()
    {
        return m_fields.entrySet().stream()
                       .map( i -> this.literal( i.getKey(), i.getValue() ) )
                       .filter( Objects::nonNull );
    }

    @Override
    public final Stream<M> streamSingleElements()
    {
        return Stream.empty();
    }

    @Override
    public final  boolean containsMultiElement( final String p_key )
    {
        return m_fields.containsKey( p_key );
    }

    @Override
    public final boolean containsSingleElement( final String p_key )
    {
        return false;
    }

    @Override
    public final boolean putMultiElement( final String p_key, final ILiteral p_value )
    {
        final Field l_field = m_fields.get( p_key );
        if ( ( l_field == null ) || ( p_value.emptyValues() ) || ( Modifier.isFinal( l_field.getModifiers() ) ) )
            return false;

        try
        {
            l_field.set( m_instance, p_value.values().findFirst().orElseGet( () -> CRawTerm.from( null ) ).raw() );
            return true;
        }
        catch ( final IllegalAccessException l_exception )
        {
            return false;
        }
    }

    @Override
    public final boolean putSingleElement( final String p_key, final M p_value )
    {
        return false;
    }

    @Override
    public final boolean putSingleElementIfAbsent( final String p_key, final M p_value )
    {
        return false;
    }

    @Override
    public final boolean removeMultiElement( final String p_key, final ILiteral p_value )
    {
        return false;
    }

    @Override
    public final boolean removeSingleElement( final String p_key )
    {
        return false;
    }

    @Override
    public final M getSingleElement( final String p_key )
    {
        return null;
    }

    @Override
    public final M getSingleElementOrDefault( final String p_key, final M p_default )
    {
        return p_default;
    }

    @Override
    public Collection<ILiteral> getMultiElement( final String p_key )
    {
        final Field l_field = m_fields.get( p_key );
        return l_field == null ? Collections.emptySet() : Stream.of( this.literal( p_key, l_field ) ).collect( Collectors.toSet() );

    }

    @Override
    public final void clear()
    {
    }

    @Override
    public final boolean empty()
    {
        return m_fields.isEmpty();
    }

    @Override
    public final int size()
    {
        return m_fields.size();
    }

    /**
     * returns a literal definition of the a class field
     *
     * @param p_name literal functor
     * @param p_field field reference
     * @return null or literal
     */
    private ILiteral literal( final String p_name, final Field p_field )
    {
        try
        {
            final Object l_value = p_field.get( m_instance );
            return l_value == null ? CLiteral.from( p_name, CRawTerm.EMPTY ) : CLiteral.from( p_name, CRawTerm.from( l_value ) );
        }
        catch ( final IllegalAccessException l_exception )
        {
            return null;
        }
    }

}
