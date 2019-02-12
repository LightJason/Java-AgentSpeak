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

package org.lightjason.agentspeak.action.builtin.collection;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * clears all elements of the collection.
 * The action removes all elements of each collection arguments
 *
 * {@code .collection/clear( Map, MultiMap, Set, List );}
 */
public final class CClear extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -1749636918394412139L;

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
        if ( !p_argument.parallelStream().allMatch( CClear::clear ) )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "argumenterror" )
            );

        return Stream.of();
    }

    /**
     * clears element
     *
     * @param p_term term
     * @return clearing successful
     */
    private static boolean clear( @Nonnull final ITerm p_term )
    {
        if ( CCommon.isssignableto( p_term, Collection.class ) )
        {
            p_term.<Collection<?>>raw().clear();
            return true;
        }

        if ( CCommon.isssignableto( p_term, Map.class ) )
        {
            p_term.<Map<?, ?>>raw().clear();
            return true;
        }

        if ( CCommon.isssignableto( p_term, Multimap.class ) )
        {
            p_term.<Multimap<?, ?>>raw().clear();
            return true;
        }

        return false;
    }

}
