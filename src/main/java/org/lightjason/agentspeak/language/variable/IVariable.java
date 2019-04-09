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

package org.lightjason.agentspeak.language.variable;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CNoSuchElementException;
import org.lightjason.agentspeak.language.IRawStructure;
import org.lightjason.agentspeak.language.IShallowCopy;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * variable defintion
 *
 * @tparam T data type
 */
public interface IVariable<T> extends ITerm, IRawStructure<IVariable<T>>, IShallowCopy<IVariable<T>>
{
    /**
     * empty variable
     */
    IVariable<?> EMPTY = new IVariable<>()
    {
        @Nonnull
        @Override
        public IVariable<Object> set( @Nullable final Object p_value )
        {
            return this;
        }

        @Override
        public boolean any()
        {
            return true;
        }

        @Override
        public boolean mutex()
        {
            return false;
        }

        @Override
        public boolean allocated()
        {
            return false;
        }

        @Nonnull
        @Override
        public IVariable<Object> thrownotallocated() throws IllegalStateException
        {
            if ( !this.allocated() )
                throw new CNoSuchElementException( CCommon.languagestring( IVariable.class, "notallocated", "EMPTY" ) );

            return this;
        }

        @Override
        public boolean valueassignableto( @Nonnull final Class<?> p_class )
        {
            return true;
        }

        @Nullable
        @Override
        public IVariable<Object> throwvaluenotassignableto( @Nonnull final Class<?> p_class ) throws IllegalStateException
        {
            return this;
        }

        @Nonnull
        @Override
        public IVariable<Object> shallowcopy( @Nullable final IPath... p_prefix )
        {
            return this;
        }

        @Nonnull
        @Override
        public IVariable<Object> shallowcopysuffix()
        {
            return this;
        }

        @Nonnull
        @Override
        public IVariable<Object> shallowcopywithoutsuffix()
        {
            return this;
        }

        @Override
        public boolean hasShallowcopywithoutsuffix()
        {
            return false;
        }

        @Nonnull
        @Override
        public String functor()
        {
            return IPath.EMPTY.toString();
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
     * sets the value
     *
     * @param p_value value
     * @return the object itself
     */
    @Nonnull
    IVariable<T> set( @Nullable final T p_value );

    /**
     * flag to define a "any variable"
     *
     * @return flag for any variable
     */
    boolean any();

    /**
     * flag to check if variable has is
     * concurrency- / thread-safe
     *
     * @return mutex flag
     */
    boolean mutex();

}
