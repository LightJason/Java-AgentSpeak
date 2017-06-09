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

package org.lightjason.agentspeak.action.buildin.collection;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * clears all elements of the collection.
 * The action removes all elements of each collection arguments,
 * the action fails on a non-collection argument
 *
 * @code collection/clear( Map, MultiMap, Set, List ); @endcode
 */
public final class CClear extends IBuildinAction
{
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( @Nonnull final IContext p_context, final boolean p_parallel,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        return CFuzzyValue.from(
            p_argument.parallelStream()
                  .allMatch( CClear::clear )
        );
    }

    /**
     * clears element
     *
     * @param p_term term
     * @return clearing successful
     */
    private static boolean clear( @Nonnull final ITerm p_term )
    {
        if ( CCommon.rawvalueAssignableTo( p_term, Collection.class ) )
        {
            p_term.<Collection<?>>raw().clear();
            return true;
        }

        if ( CCommon.rawvalueAssignableTo( p_term, Map.class ) )
        {
            p_term.<Map<?, ?>>raw().clear();
            return true;
        }

        if ( CCommon.rawvalueAssignableTo( p_term, Multimap.class ) )
        {
            p_term.<Multimap<?, ?>>raw().clear();
            return true;
        }

        return false;
    }

}
