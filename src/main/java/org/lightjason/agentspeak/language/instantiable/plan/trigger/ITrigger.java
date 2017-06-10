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

package org.lightjason.agentspeak.language.instantiable.plan.trigger;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IShallowCopy;
import org.lightjason.agentspeak.language.IStructureHash;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * event definition
 *
 * @note trigger are equal iif type and literal functor are equal
 * and number of arguments are equal, otherwise unification is used
 * to define the literal variables
 */
public interface ITrigger extends IStructureHash, IShallowCopy<ITrigger>, Comparable<ITrigger>
{
    /**
     * empty trigger
     */
    ITrigger EMPTY = new ITrigger()
    {

        @Override
        public final int structurehash()
        {
            return 0;
        }

        @Nonnull
        @Override
        public final ITrigger shallowcopy( final IPath... p_prefix )
        {
            return this;
        }

        @Nonnull
        @Override
        public final ITrigger shallowcopysuffix()
        {
            return this;
        }

        @Override
        public final int compareTo( @Nonnull final ITrigger p_trigger )
        {
            return Integer.compare( p_trigger.hashCode(), this.hashCode() );
        }

        @Override
        public final EType type()
        {
            return EType.EMPTY;
        }

        @Override
        public final ILiteral literal()
        {
            return ILiteral.EMPTY;
        }

        @Override
        public final int variablesize()
        {
            return 0;
        }
    };

    /**
     * returns the type of the event
     *
     * @return type
     */
    EType type();

    /**
     * returns the literal of the event
     *
     * @return literal
     */
    ILiteral literal();

    /**
     * returns variable number
     *
     * @return number
     */
    int variablesize();



    /**
     * event types
     */
    enum EType
    {
        ADDBELIEF( "+" ),
        DELETEBELIEF( "-" ),
        ADDGOAL( "+!" ),
        DELETEGOAL( "-!" ),
        EMPTY( "" );

        /**
         * math with elements for intantiation
         */
        private static final Map<String, EType> ELEMENTS = Collections.unmodifiableMap(
                                                                Arrays.stream( EType.values() )
                                                                      .collect( Collectors.toMap( EType::sequence, i -> i ) )
                                                           );

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
        public String toString()
        {
            return m_name;
        }

        /**
         * returns the trigger sequence
         *
         * @return trigger sequence string
         */
        public final String sequence()
        {
            return m_name;
        }

        /**
         * returns a trigger type by the char sequence
         * @param p_sequence sequence
         * @return trigger type
         */
        public static EType from( @Nonnull final String p_sequence )
        {
            final EType l_type = ELEMENTS.get( p_sequence.trim() );
            if ( l_type == null )
                throw new CIllegalArgumentException( CCommon.languagestring( EType.class, "sequencenotfound", p_sequence ) );

            return l_type;
        }
    }

}
