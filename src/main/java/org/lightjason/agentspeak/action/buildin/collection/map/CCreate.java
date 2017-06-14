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

package org.lightjason.agentspeak.action.buildin.collection.map;

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * creates a hashmap.
 * Returns an empty hashmap (key-value pair) and
 * optional arguments must be even and it will create a key-value structure, the
 * action fails on an odd number of arguments except zero only
 *
 * @code M = collection/map/create(); @endcode
 */
public final class CCreate extends IBuildinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 6246832787713042203L;

    /**
     * ctor
     */
    public CCreate()
    {
        super( 3 );
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        if ( ( l_arguments.size() > 0 ) && ( l_arguments.size() % 2 == 1 ) )
            return CFuzzyValue.from( false );

        final Map<Object, Object> l_map = p_parallel ? new ConcurrentHashMap<>() : new HashMap<>();
        StreamUtils.windowed( l_arguments.stream(), 2, 2 )
                   .forEach( i -> l_map.put( i.get( 0 ).raw(), i.get( 1 ).raw() ) );
        p_return.add( CRawTerm.from( l_map ) );

        return CFuzzyValue.from( true );
    }

}
