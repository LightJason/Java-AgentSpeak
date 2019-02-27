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

package org.lightjason.agentspeak.action.builtin.map;

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
 * checks a collection is empty.
 * All arguments are collection elements and for each argument
 * a boolean flag for empty is returned, on all non-collection
 * types empty is always false
 *
 * {@code [A|B] = .collection/mapisempty( Map, MultiMap );}
 */
public final class CMapIsEmpty extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3479069391247895544L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CMapIsEmpty.class, "collection" );

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
        p_argument.stream()
                  .map( CMapIsEmpty::empty )
                  .map( CRawTerm::of )
                  .forEach( p_return::add );

        return Stream.of();
    }


    /**
     * checks a collection is empty
     *
     * @param p_term term value
     * @return empty flag
     */
    private static boolean empty( @Nonnull final ITerm p_term )
    {
        if ( CCommon.isssignableto( p_term, Map.class ) )
            return p_term.<Map<?, ?>>raw().isEmpty();

        return CCommon.isssignableto( p_term, Multimap.class ) && p_term.<Multimap<?, ?>>raw().isEmpty();

    }

}
