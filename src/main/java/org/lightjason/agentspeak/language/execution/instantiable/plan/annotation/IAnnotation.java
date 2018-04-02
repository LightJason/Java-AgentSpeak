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

package org.lightjason.agentspeak.language.execution.instantiable.plan.annotation;


import org.lightjason.agentspeak.language.IAssignable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 * annotation interface
 *
 * @tparam T annotation data type
 */
public interface IAnnotation<T> extends IAssignable<T>
{
    /**
     * empty annotation
     */
    IAnnotation<?> EMPTY = new IAnnotation<>()
    {
        @Nonnull
        @Override
        public final EType id()
        {
            return EType.EMPTY;
        }

        @Nullable
        @Override
        public final <N> N value()
        {
            return null;
        }

        @Nonnull
        @Override
        public final Stream<IVariable<?>> variables()
        {
            return Stream.empty();
        }

        @Override
        public final boolean valueassignableto( @Nonnull final Class<?>... p_class )
        {
            return true;
        }

        @Nullable
        @Override
        public final Object throwvaluenotassignableto( @Nonnull final Class<?>... p_class ) throws IllegalArgumentException
        {
            return null;
        }

        @Override
        public final String toString()
        {
            return this.id().toString();
        }

        @Override
        public final int hashCode()
        {
            return this.id().hashCode();
        }

        @Override
        public final boolean equals( final Object p_object )
        {
            return ( p_object instanceof IAnnotation<?> ) && ( this.hashCode() == p_object.hashCode() );
        }
    };

    /**
     * returns the type of the annotation
     *
     * @return type
     */
    @Nonnull
    EType id();

    /**
     * returns the data of the annotation if exists
     *
     * @return data or null
     */
    @Nullable
    <N> N value();

    /**
     * returns a stream of variables
     *
     * @return variabel stream
     */
    @Nonnull
    Stream<IVariable<?>> variables();


    /**
     * annotation types
     */
    enum EType
    {
        CONSTANT( "@constant" ),
        ATOMIC( "@atomic" ),
        PARALLEL( "@parallel" ),
        VARIABLE( "@variable" ),
        DESCRIPTION( "@description" ),
        TAG( "@tag" ),
        EMPTY( "" );

        /**
         * text name of the enum
         */
        private final String m_name;

        /**
         * ctor
         *
         * @param p_name text name
         */
        EType( final String p_name )
        {
            m_name = p_name;
        }

        @Override
        public final String toString()
        {
            return m_name;
        }

        /**
         * returns enum value by string
         *
         * @param p_value string value
         * @return type
         */
        public static EType of( @Nonnull final String p_value )
        {
            return Arrays.stream( EType.values() )
                         .filter( i -> ( !i.m_name.isEmpty() ) && ( p_value.startsWith( i.m_name ) ) )
                         .findFirst()
                         .orElseGet( () -> EMPTY );
        }
    }

}
