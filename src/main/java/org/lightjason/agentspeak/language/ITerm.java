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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;


/**
 * term interface
 */
public interface ITerm extends Serializable, IStructureHash, IDeepCopy<ITerm>
{
    /**
     * serial id
     */
    long serialVersionUID = -3640918490398129717L;

    /**
     * empty term
     */
    ITerm EMPTY = new ITerm()
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -3396420174718981909L;

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

        @Nullable
        @Override
        public <T> T raw()
        {
            return null;
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

        @Override
        public int structurehash()
        {
            return 0;
        }
    };

    /**
     * returns the functor without path
     *
     * @return functor
     */
    @Nonnull
    String functor();

    /**
     * returns the path of the functor
     *
     * @return path
     */
    @Nonnull
    IPath functorpath();

    /**
     * returns the full-qualified functor
     * with path and name
     *
     * @return fqn functor
     */
    @Nonnull
    IPath fqnfunctor();

    /**
     * checks if the literal has variables
     *
     * @return variable flag
     */
    boolean hasVariable();

    /**
     * cast to any raw value type
     *
     * @tparam raw type
     * @return any type
     */
    @Nullable
    <T> T raw();

    /**
     * casts the object to a term-type
     *
     * @return casted object
     *
     * @tparam T term type
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    default <T extends ITerm> T term()
    {
        return (T) this;
    }

}
