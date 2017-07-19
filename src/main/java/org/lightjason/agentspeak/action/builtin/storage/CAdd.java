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

package org.lightjason.agentspeak.action.builtin.storage;

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * adds or overwrites an element in the agent-storage.
 * The action adds all tuples into the storage, the arguments
 * are tuples of a name and any value, the action never fails
 *
 * @code storage/add( "foo", X, "bar", Y ); @endcode
 */
public final class CAdd extends IStorage
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3102307419115604147L;

    /**
     * ctor
     *
     * @param p_resolver resolver function
     */
    public CAdd( @Nonnull final Function<String, Boolean> p_resolver )
    {
        super( p_resolver );
    }

    /**
     * ctor
     *
     * @param p_forbidden forbidden keys
     */
    public CAdd( @Nullable final String... p_forbidden )
    {
        super( p_forbidden );
    }

    /**
     * ctor
     *
     * @param p_fordbidden stream with borbidden keys
     */
    public CAdd( @Nonnull final Stream<String> p_fordbidden )
    {
        super( p_fordbidden );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        StreamUtils.windowed(
            p_argument.stream(),
            2,
            2
        ).forEach( i -> this.add( p_context.agent(), i.get( 0 ).<String>raw(), i.get( 1 ) ) );

        return CFuzzyValue.from( true );
    }

    /**
     * adds a value into the storage
     *
     * @param p_agent agent
     * @param p_key key
     * @param p_value value
     */
    private void add( @Nonnull final IAgent<?> p_agent, @Nonnull final String p_key, @Nonnull final ITerm p_value )
    {
        if ( m_resolver.apply( p_key ) )
            return;

        p_agent.storage().put( p_key, p_value.raw() );
    }

}
