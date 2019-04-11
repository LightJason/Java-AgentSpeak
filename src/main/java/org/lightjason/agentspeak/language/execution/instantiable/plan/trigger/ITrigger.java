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

package org.lightjason.agentspeak.language.execution.instantiable.plan.trigger;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CNoSuchElementException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IShallowCopy;
import org.lightjason.agentspeak.language.IStructureHash;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;


/**
 * event definition
 *
 * @note trigger are equal iif type and literal functor are equal
 * and number of arguments are equal, otherwise unification is used
 * to define the literal variables
 */
public interface ITrigger extends Serializable, IStructureHash, IShallowCopy<ITrigger>, Comparable<ITrigger>
{
    /**
     * empty trigger
     */
    ITrigger EMPTY = new ITrigger()
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -4216254162765675258L;

        @Override
        public int structurehash()
        {
            return 0;
        }

        @Nonnull
        @Override
        public ITrigger shallowcopy( @Nullable final IPath... p_prefix )
        {
            return this;
        }

        @Nonnull
        @Override
        public ITrigger shallowcopysuffix()
        {
            return this;
        }

        @Nonnull
        @Override
        public ITrigger shallowcopywithoutsuffix()
        {
            return this;
        }

        @Override
        public boolean hasShallowcopywithoutsuffix()
        {
            return false;
        }

        @Override
        public int compareTo( @Nonnull final ITrigger p_trigger )
        {
            return Integer.compare( p_trigger.hashCode(), this.hashCode() );
        }

        @Override
        public EType type()
        {
            return EType.EMPTY;
        }

        @Override
        public ILiteral literal()
        {
            return ILiteral.EMPTY;
        }

        @Override
        public int hashCode()
        {
            return 0;
        }

        @Override
        public boolean equals( final Object p_object )
        {
            return p_object instanceof ITrigger && this.hashCode() == p_object.hashCode();
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
         * text name of the enum
         */
        private final String m_operator;

        /**
         * ctor
         *
         * @param p_operator text name
         */
        EType( final String p_operator )
        {
            m_operator = p_operator;
        }

        @Override
        public String toString()
        {
            return m_operator;
        }

        /**
         * returns the trigger sequence
         *
         * @return trigger sequence string
         */
        public final String sequence()
        {
            return m_operator;
        }

        /**
         * returns a trigger type by the char sequence
         *
         * @param p_sequence sequence
         * @return trigger type
         */
        public static EType of( @Nonnull final String p_sequence )
        {
            return Arrays.stream( EType.values() )
                         .filter( i -> i.m_operator.equals( p_sequence ) )
                         .findFirst()
                         .orElseThrow( () -> new CNoSuchElementException( CCommon.languagestring( EType.class, "unknowntrigger", p_sequence ) ) );
        }

        /**
         * build trigger based on a literal
         *
         * @param p_literal literal
         * @return default trigger object
         */
        @Nonnull
        public ITrigger builddefault( @Nonnull final ILiteral p_literal )
        {
            return CTrigger.of( this, p_literal );
        }
    }

}
