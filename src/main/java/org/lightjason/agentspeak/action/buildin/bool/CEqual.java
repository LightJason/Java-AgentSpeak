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

package org.lightjason.agentspeak.action.buildin.bool;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * checks elements of equality.
 * The actions checks the first argument
 * to all others arguments of equality,
 * list structures won't be unflaten, but
 * elementwise compared, the action never fails
 *
 * @code [E1|E2] = bool/equal( "this is equal", "this is equal", [123, "test"] ); @endcode
 * @note on number arguments not the value must equal, also the type (double / integral) must be equal,
 * so keep in mind, that you use the correct number type on the argument input
 */
public class CEqual extends IBuildinAction
{

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return )
    {
        if ( CCommon.rawvalueAssignableTo( p_argument.get( 0 ), Collection.class ) )
            return this.pack(
                p_return,
                p_argument.stream()
                          .skip( 1 )
                          .map( i -> p_argument.get( 0 ).equals( i )
                                     || ( CCommon.rawvalueAssignableTo( i, Collection.class )
                                     && equalcollection(  p_argument.get( 0 ).<Collection<?>>raw().toArray(), i.raw() ) )
                          )
            );

        if ( CCommon.rawvalueAssignableTo( p_argument.get( 0 ), Map.class ) )
            return this.pack(
                p_return,
                p_argument.stream()
                          .skip( 1 )
                          .map( i -> p_argument.get( 0 ).equals( i )
                                     || ( CCommon.rawvalueAssignableTo( i, Map.class )
                                     && equalmap(  p_argument.get( 0 ).<Map<?, ?>>raw(), i.<Map<?, ?>>raw() ) )
                          )
            );

        if ( CCommon.rawvalueAssignableTo( p_argument.get( 0 ), Multimap.class ) )
            return this.pack(
                p_return,
                p_argument.stream()
                          .skip( 1 )
                          .map( i -> p_argument.get( 0 ).equals( i )
                                     || ( CCommon.rawvalueAssignableTo( i, Multimap.class )
                                     && equalmultimap(  p_argument.get( 0 ).<Multimap<?, ?>>raw(), i.<Multimap<?, ?>>raw() ) )
                          )
            );


        return this.pack(
            p_return,
            p_argument.stream()
                      .skip( 1 )
                      .map( i -> equalobject(  p_argument.get( 0 ).<Object>raw(), i.<Object>raw() ) )
        );
    }


    /**
     * apply to change boolean result
     *
     * @param p_value boolean result
     * @return boolean value
     */
    protected boolean apply( final boolean p_value )
    {
        return p_value;
    }


    /**
     * pack the result values into term
     *
     * @param p_return return item list
     * @param p_stream boolean input stream
     * @return boolean flag
     */
    private IFuzzyValue<Boolean> pack( final List<ITerm> p_return, final Stream<Boolean> p_stream )
    {
        p_stream.map( this::apply )
                .map( CRawTerm::from )
                .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }


    /**
     * compare any objects
     * @param p_source source object
     * @param p_target object to compare
     * @return equality boolean flag
     */
    private static boolean equalobject( final Object p_source, final Object p_target )
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
    private static boolean equalcollection( final Object[] p_source, final Collection<?> p_target )
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
    private static boolean equalmap( final Map<?, ?> p_source, final Map<?, ?> p_target )
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
    private static boolean equalmultimap( final Multimap<?, ?> p_source, final Multimap<?, ?> p_target )
    {
        return Arrays.equals( p_source.asMap().keySet().toArray(), p_target.asMap().keySet().toArray() )
               && Arrays.equals( p_source.values().toArray(), p_target.values().toArray() );
    }

}
