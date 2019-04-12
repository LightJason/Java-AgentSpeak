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


import org.lightjason.agentspeak.language.IAssignable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
        public EAnnotation id()
        {
            return EAnnotation.EMPTY;
        }

        @Nullable
        @Override
        public <N> N value()
        {
            return null;
        }

        @Nonnull
        @Override
        public Stream<IVariable<?>> variables()
        {
            return Stream.empty();
        }

        @Override
        public boolean valueassignableto( @Nonnull final Class<?> p_class )
        {
            return true;
        }

        @Nullable
        @Override
        public Object throwvaluenotassignableto( @Nonnull final Class<?> p_class ) throws TypeNotPresentException
        {
            return null;
        }

        @Override
        public String toString()
        {
            return this.id().toString();
        }

        @Override
        public int hashCode()
        {
            return this.id().hashCode();
        }

        @Override
        public boolean equals( final Object p_object )
        {
            return p_object instanceof IAnnotation<?> && this.hashCode() == p_object.hashCode();
        }
    };

    /**
     * returns the type of the annotation
     *
     * @return type
     */
    @Nonnull
    EAnnotation id();

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

}
