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

package org.lightjason.agentspeak.beliefbase;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.language.ILiteral;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;


/**
 * class of an empty beliefbase
 *
 * @tparam T agent type
 */
public final class CEmptyBeliefbase<T extends IAgent<?>> extends IBaseBeliefbase<T>
{
    /**
     * singleton instance
     */
    private static final IBeliefbase<?> INSTANCE = new CEmptyBeliefbase<>();


    /**
     * ctor
     */
    private CEmptyBeliefbase()
    {
    }

    @Override
    public final Stream<ILiteral> streamLiteral()
    {
        return Stream.empty();
    }

    @Override
    public final Stream<IView<T>> streamView()
    {
        return Stream.empty();
    }

    @Nonnull
    @Override
    public final IBeliefbase<T> clear()
    {
        return this;
    }

    @Nonnull
    @Override
    public final IView<T> add( final IView<T> p_view )
    {
        return p_view;
    }

    @Nonnull
    @Override
    public final IView<T> remove( final IView<T> p_view )
    {
        return p_view;
    }

    @Override
    public final boolean containsLiteral( final String p_key )
    {
        return false;
    }

    @Override
    public final boolean containsView( final String p_key )
    {
        return false;
    }

    @Nonnull
    @Override
    public final IView<T> view( final String p_key )
    {
        return null;
    }

    @Override
    public final Collection<ILiteral> literal( final String p_key )
    {
        return Collections.emptySet();
    }

    @Override
    public final IView<T> viewOrDefault( final String p_key, final IView<T> p_default )
    {
        return null;
    }

    @Override
    public final boolean empty()
    {
        return true;
    }

    @Override
    public final int size()
    {
        return 0;
    }

    @Override
    public final int hashCode()
    {
        return 1;
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IBeliefbase<?> ) && ( p_object.hashCode() == this.hashCode() );
    }

    @Override
    public final String toString()
    {
        return super.toString();
    }

    /**
     * return instance
     *
     * @tparam T agent type
     * @return type instance of empty beliefbase
     */
    @SuppressWarnings( "unchecked" )
    public static <T extends IAgent<?>> IBeliefbase<T> instance()
    {
        return (IBeliefbase<T>) INSTANCE;
    }
}
