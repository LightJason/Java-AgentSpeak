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

package org.lightjason.agentspeak.action.map;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * clears all elements of the collection.
 * The action removes all elements of each collection arguments
 *
 * {@code .collection/mapclear( Map, MultiMap );}
 */
public final class CMapClear extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -1749636918394412139L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CMapClear.class, "collection" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        p_argument.parallelStream().forEach( CMapClear::clear );
        return Stream.of();
    }

    /**
     * clears element
     *
     * @param p_term term
     */
    private static void clear( @Nonnull final ITerm p_term )
    {

        if ( CCommon.isssignableto( p_term, Map.class ) )
            p_term.<Map<?, ?>>raw().clear();

        if ( CCommon.isssignableto( p_term, Multimap.class ) )
            p_term.<Multimap<?, ?>>raw().clear();

    }

}
