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

package org.lightjason.agentspeak.action.builtin.graph;


import com.codepoetics.protonpack.StreamUtils;
import com.google.common.base.Function;
import edu.uci.ics.jung.graph.Graph;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.error.context.CExecutionIllegalStateExcepton;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * abstract class to define path / distance graph algorithms
 */
public abstract class IApplyPathAlgorithm extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1424878255126290894L;
    /**
     * default weight field
     */
    private static final String DEFAULTWEIGHT = "defaultweight";

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                 @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        if ( l_arguments.size() < 3 )
            throw new CExecutionIllegealArgumentException( p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "wrong number of arguments" ) );

        final Map<Object, Number> l_weights = l_arguments.parallelStream()
                                                         .filter( i -> CCommon.isssignableto( i, Map.class ) )
                                                         .findFirst()
                                                         .map( ITerm::<Map<Object, Number>>raw )
                                                         .orElseGet( Collections::emptyMap );

        final Number l_defaultvalue = StreamUtils.windowed( l_arguments.stream(), 2 )
                                                 .filter( i -> CCommon.isssignableto( i.get( 0 ), String.class )
                                                               && DEFAULTWEIGHT.equalsIgnoreCase( i.get( 0 ).<String>raw() )
                                                               && CCommon.isssignableto( i.get( 1 ), Number.class )
                                                 )
                                                 .findFirst()
                                                 .map( i -> i.get( 1 ).<Number>raw() )
                                                 .orElse( 0D );

        final Function<Object, Number> l_weightfunction = e -> l_weights.getOrDefault( e, l_defaultvalue );

        final List<ITerm> l_vertices = StreamUtils.windowed( l_arguments.stream(), 2 )
                                                   .filter( i -> !( CCommon.isssignableto( i.get( 0 ), String.class )
                                                                 && DEFAULTWEIGHT.equalsIgnoreCase( i.get( 0 ).<String>raw() )
                                                                 && CCommon.isssignableto( i.get( 1 ), Number.class ) )
                                                                 && !( CCommon.isssignableto( i.get( 0 ), Graph.class )
                                                                 || CCommon.isssignableto( i.get( 1 ), Graph.class ) )
                                                   )
                                                   .findFirst()
                                                   .orElseGet( Collections::emptyList );

        if ( l_vertices.isEmpty() )
            throw new CExecutionIllegalStateExcepton( p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "stateerror" ) );


        l_arguments.stream()
                   .filter( i -> CCommon.isssignableto( i, Graph.class ) )
                   .map( ITerm::<Graph<Object, Object>>raw )
                   .map( i -> this.apply( l_vertices, i, l_weightfunction ) )
                   .map( CRawTerm::of )
                   .forEach( p_return::add );

        return Stream.of();
    }


    /**
     * apply function
     *
     * @param p_vertices list with vertices
     * @param p_graph graph
     * @param p_weightfunction weight function
     * @return result of action
     */
    protected abstract Object apply( @Nonnull final List<ITerm> p_vertices, @Nonnull final Graph<Object, Object> p_graph,
                                     @Nonnull final Function<Object, Number> p_weightfunction );


}
