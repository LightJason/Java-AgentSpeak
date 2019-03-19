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
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * returns the size of the collection.
 * All arguments must be collections and the action returns
 * the size of each collection.
 * On non-collection type the action returns a zero value
 *
 * {@code [A|B|C|D] = .collection/mapsize( Collection, Map, MultiMap, Tupel );}
 */
public final class CMapSize extends IBaseAction
{

    /**
     * serial id
     */
    private static final long serialVersionUID = -8185185045612045570L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CMapSize.class, "collection" );

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
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        // any term type
        p_argument.stream()
                  .map( CMapSize::size )
                  .map( CRawTerm::of )
                  .forEach( p_return::add );

        return Stream.of();
    }

    /**
     * returns the size of a collection type
     * or zero on other
     *
     * @param p_term term value
     * @return element number
     */
    private static long size( @Nonnull final ITerm p_term )
    {
        if ( CCommon.isssignableto( p_term, Map.class ) )
            return p_term.<Map<?, ?>>raw().size();

        if ( CCommon.isssignableto( p_term, Multimap.class ) )
            return p_term.<Multimap<?, ?>>raw().size();

        return 0;
    }
}
