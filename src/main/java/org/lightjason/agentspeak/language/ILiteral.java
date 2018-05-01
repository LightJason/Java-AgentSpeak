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

package org.lightjason.agentspeak.language;

import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.execution.IContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;


/**
 * literal interface
 *
 * @note closed world assumption, no negation marker needed
 */
public interface ILiteral extends ITerm, IShallowCopy<ILiteral>, Comparable<ILiteral>
{
    /**
     * empty literal
     */
    ILiteral EMPTY = new ILiteral()
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -3396471300120317809L;

        @Nonnull
        @Override
        public Stream<ITerm> values( @Nullable final IPath... p_path )
        {
            return Stream.empty();
        }

        @Nonnull
        @Override
        public Stream<ITerm> orderedvalues( @Nullable final IPath... p_path )
        {
            return Stream.empty();
        }

        @Override
        public boolean emptyValues()
        {
            return true;
        }

        @Override
        public boolean negated()
        {
            return false;
        }

        @Override
        public boolean hasAt()
        {
            return false;
        }

        @Nonnull
        @Override
        public ILiteral allocate( @Nonnull final IContext p_context )
        {
            return this;
        }

        @Override
        public int compareTo( @Nonnull final ILiteral p_literal )
        {
            return Integer.compare( p_literal.hashCode(), this.hashCode() );
        }

        @Nonnull
        @Override
        public String functor()
        {
            return "";
        }

        @Nonnull
        @Override
        public IPath functorpath()
        {
            return IPath.EMPTY;
        }

        @Nonnull
        @Override
        public IPath fqnfunctor()
        {
            return IPath.EMPTY;
        }

        @Override
        public boolean hasVariable()
        {
            return false;
        }

        @Nonnull
        @Override
        @SuppressWarnings( "unchecked" )
        public <T> T raw()
        {
            return (T) this;
        }

        @Nonnull
        @Override
        public ITerm deepcopy( @Nullable final IPath... p_prefix )
        {
            return this;
        }

        @Nonnull
        @Override
        public ITerm deepcopysuffix()
        {
            return this;
        }

        @Nonnull
        @Override
        public ILiteral shallowcopy( @Nullable final IPath... p_prefix )
        {
            return this;
        }

        @Nonnull
        @Override
        public ILiteral shallowcopysuffix()
        {
            return this;
        }

        @Override
        public int structurehash()
        {
            return 0;
        }

        @Override
        public int hashCode()
        {
            return 0;
        }

        @Override
        public boolean equals( final Object p_object )
        {
            return p_object instanceof ILiteral && this.hashCode() == p_object.hashCode();
        }
    };

    /**
     * returns a stream over value items
     *
     * @param p_path optional filtering value names
     * (filtering values within values)
     * @return stream
     */
    @Nonnull
    Stream<ITerm> values( final IPath... p_path );

    /**
     * returns a stream over the ordered values
     * in sequential ordering
     *
     * @return term stream
     */
    @Nonnull
    Stream<ITerm> orderedvalues( @Nullable final IPath... p_path );

    /**
     * check for empty values
     *
     * @return empty flag
     */
    boolean emptyValues();

    /**
     * getter of the literal for the negation
     *
     * @return negated flag
     */
    boolean negated();

    /**
     * returns if the literal has an @ prefix
     *
     * @return prefix is set
     */
    boolean hasAt();

    /**
     * allocates all variables of the literal with
     * the variables of the current context
     *
     * @param p_context cpmzexz
     * @return literal with allocated variables
     */
    @Nonnull
    ILiteral allocate( @Nonnull final IContext p_context  );

}
