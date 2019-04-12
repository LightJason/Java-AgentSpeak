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

package org.lightjason.agentspeak.language.execution.instantiable.plan.annotation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BiFunction;


/**
 * annotation types
 */
public enum EAnnotation implements BiFunction<String, Object, IAnnotation<?>>
{
    ATOMIC( "@atomic" )
    {
        @Override
        public IAnnotation<?> apply( final String p_text, final Object p_value )
        {
            return new CAtomAnnotation<>( this );
        }
    },
    CONSTANT( "@constant" )
    {
        @Override
        public IAnnotation<?> apply( final String p_text, final Object p_value )
        {
            return new CConstantAnnotation<>( this, p_text, p_value );
        }
    },
    DESCRIPTION( "@description" )
    {
        @Override
        public IAnnotation<?> apply( final String p_text, final Object p_value )
        {
            return new CStringAnnotation( this, p_text );
        }
    },
    EMPTY( "" )
    {
        @Override
        public IAnnotation<?> apply( final String p_text, final Object p_value )
        {
            return IAnnotation.EMPTY;
        }
    },
    PARALLEL( "@parallel" )
    {
        @Override
        public IAnnotation<?> apply( final String p_text, final Object p_value )
        {
            return new CAtomAnnotation<>( this );
        }
    },
    TAG( "@tag" )
    {
        @Override
        public IAnnotation<?> apply( final String p_text, final Object p_value )
        {
            return new CStringAnnotation( this, p_text );
        }
    },
    VARIABLE( "@variable" )
    {
        @Override
        public IAnnotation<?> apply( final String p_text, final Object p_value )
        {
            return new CConstantAnnotation<>( this, p_text, p_value );
        }
    };

    /**
     * text name of the enum
     */
    private final String m_name;

    /**
     * ctor
     *
     * @param p_name text name
     */
    EAnnotation( final String p_name )
    {
        m_name = p_name;
    }

    @Override
    public String toString()
    {
        return m_name;
    }

    /**
     * returns enum value by string
     *
     * @param p_value string value
     * @return type
     */
    public static EAnnotation of( @Nonnull final String p_value )
    {
        return Arrays.stream( EAnnotation.values() )
                     .filter( i -> ( !i.m_name.isEmpty() ) && ( p_value.startsWith( i.m_name ) ) )
                     .findFirst()
                     .orElse( EMPTY );
    }
}
