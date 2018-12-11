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

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * abstract class for apply multiple elements to a single maps
 *
 * @tparam T map instance
 */
public abstract class IMapApplyMultiple<T> extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7048059456586091660L;

    /**
     * ctor
     */
    protected IMapApplyMultiple()
    {
        super( 3 );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 3;
    }

    @Nonnull
    @Override
    public final Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                 @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {

        final List<ITerm> l_list = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        if ( l_list.size() % 2 == 0 )
            return CFuzzyValue.of( false );

        StreamUtils.windowed(
            l_list.stream()
                  .skip( 1 ),
            2,
            2
        )
                   .forEach( i ->  this.apply( l_list.get( 0 ).<T>raw(), i.get( 0 ).raw(), i.get( 1 ).raw() ) );

        return Stream.of();
    }

    /**
     * apply operation
     *
     * @param p_instance object instance
     * @param p_key key
     * @param p_value value
     */
    protected abstract void apply( @Nonnull final T p_instance, @Nonnull final Object p_key, @Nullable final Object p_value );
}
