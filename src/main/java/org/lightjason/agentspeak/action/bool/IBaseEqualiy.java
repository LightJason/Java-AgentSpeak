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

package org.lightjason.agentspeak.action.bool;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * abstract class for equality / inequality action
 */
public abstract class IBaseEqualiy extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4730062017961570138L;

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public final Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                 @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        if ( CCommon.isssignableto( p_argument.get( 0 ), Collection.class ) )
            return this.pack(
                p_return,
                p_argument.stream()
                          .skip( 1 )
                          .map( i -> p_argument.get( 0 ).equals( i )
                                     || CCommon.isssignableto( i, Collection.class )
                                        && equalcollection( p_argument.get( 0 ).<Collection<?>>raw().toArray(), i.raw() )
                          )
            );

        if ( CCommon.isssignableto( p_argument.get( 0 ), Map.class ) )
            return this.pack(
                p_return,
                p_argument.stream()
                          .skip( 1 )
                          .map( i -> p_argument.get( 0 ).equals( i )
                                     || CCommon.isssignableto( i, Map.class )
                                        && equalmap( p_argument.get( 0 ).raw(), i.raw() )
                          )
            );

        if ( CCommon.isssignableto( p_argument.get( 0 ), Multimap.class ) )
            return this.pack(
                p_return,
                p_argument.stream()
                          .skip( 1 )
                          .map( i -> p_argument.get( 0 ).equals( i )
                                     || CCommon.isssignableto( i, Multimap.class )
                                        && equalmultimap( p_argument.get( 0 ).raw(), i.raw() )
                          )
            );


        return this.pack(
            p_return,
            p_argument.stream()
                      .skip( 1 )
                      .map( i -> equalobject( p_argument.get( 0 ).<Object>raw(), i.<Object>raw() ) )
        );
    }

    /**
     * pack the result values into term
     *
     * @param p_return return item list
     * @param p_stream boolean input stream
     * @return boolean flag
     */
    private Stream<IFuzzyValue<?>> pack( @Nonnull final List<ITerm> p_return, @Nonnull final Stream<Boolean> p_stream )
    {
        p_stream.map( this::apply )
                .map( CRawTerm::of )
                .forEach( p_return::add );

        return Stream.of();
    }


    /**
     * compare any objects
     *
     * @param p_source source object
     * @param p_target object to compare
     * @return equality boolean flag
     */
    private static boolean equalobject( @Nonnull final Object p_source, @Nonnull final Object p_target )
    {
        return p_source.equals( p_target );
    }


    /**
     * compares collections
     *
     * @param p_source source array (converted collection to array)
     * @param p_target collection to compare
     * @return equality boolean flag
     */
    private static boolean equalcollection( @Nonnull final Object[] p_source, @Nonnull final Collection<?> p_target )
    {
        return Arrays.equals( p_source, p_target.toArray() );
    }


    /**
     * compare maps
     *
     * @param p_source source map
     * @param p_target map to compare
     * @return equality boolean flag
     */
    private static boolean equalmap( @Nonnull final Map<?, ?> p_source, @Nonnull final Map<?, ?> p_target )
    {
        return Arrays.equals( p_source.keySet().toArray(), p_target.keySet().toArray() )
               && Arrays.equals( p_source.values().toArray(), p_target.values().toArray() );
    }


    /**
     * compare multimap
     *
     * @param p_source source multimap
     * @param p_target multimap to compare
     * @return equality boolean flag
     */
    private static boolean equalmultimap( @Nonnull final Multimap<?, ?> p_source, @Nonnull final Multimap<?, ?> p_target )
    {
        return Arrays.equals( p_source.asMap().keySet().toArray(), p_target.asMap().keySet().toArray() )
               && Arrays.equals( p_source.values().toArray(), p_target.values().toArray() );
    }

    /**
     * apply to change boolean result
     *
     * @param p_value boolean result
     * @return boolean value
     */
    protected abstract boolean apply( final boolean p_value );
}
