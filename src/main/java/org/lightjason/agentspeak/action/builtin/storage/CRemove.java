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

package org.lightjason.agentspeak.action.builtin.storage;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
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
 * removes an element by name from the storage.
 * The actions removes any value from the storage
 * which is referenced by the key and returns the
 * value, the action never fails
 *
 * {@code [A|B] = storage/remove("foo", "bar");}
 */
public final class CRemove extends IStorage
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7237340367513736766L;

    /**
     * ctor
     */
    public CRemove()
    {
        super();
    }

    /**
     * ctor
     *
     * @param p_resolver resolver function
     */
    public CRemove( @Nonnull final Function<String, Boolean> p_resolver )
    {
        super( p_resolver );
    }

    /**
     * ctor
     *
     * @param p_forbidden forbidden keys
     */
    public CRemove( @Nullable final String... p_forbidden )
    {
        super( p_forbidden );
    }

    /**
     * ctor
     *
     * @param p_fordbidden stream with borbidden keys
     */
    public CRemove( @Nonnull final Stream<String> p_fordbidden )
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
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        CCommon.flatten( p_argument )
               .map( ITerm::<String>raw )
               .forEach( i -> this.remove( p_context.agent(), i, p_return ) );

        return CFuzzyValue.from( true );
    }


    /**
     * removes a value from the storage
     *
     * @param p_agent agent
     * @param p_key key
     * @param p_return return arguments
     */
    private void remove( @Nonnull final IAgent<?> p_agent, @Nonnull final String p_key, @Nonnull final List<ITerm> p_return )
    {
        if ( ( m_resolver.apply( p_key ) ) || ( !p_agent.storage().containsKey( p_key ) ) )
            return;

        p_return.add(
            CRawTerm.from(
                p_agent.storage().remove( p_key )
            )
        );
    }

}
