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

package org.lightjason.agentspeak.action.buildin.storage;

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * adds or overwrites an element in the agent-storage.
 * The action adds all tuples into the storage, the arguments
 * are tuples of a name and any value, if the key is forbidden
 * or the arguments number is odd, the action fails
 *
 * @code storage/add( "foo", X, "bar", Y ); @endcode
 */
public final class CAdd extends IStorage
{

    /**
     * ctor
     */
    public CAdd()
    {
        super();
    }

    /**
     * ctor
     *
     * @param p_forbidden forbidden keys
     */
    public CAdd( final String... p_forbidden )
    {
        super( Arrays.asList( p_forbidden ) );
    }

    /**
     * ctor
     *
     * @param p_fordbidden forbidden keys
     */
    public CAdd( final Collection<String> p_fordbidden )
    {
        super( p_fordbidden );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        return CFuzzyValue.from(
            StreamUtils.windowed(
                p_argument.stream(),
                2,
                2
            ).allMatch( i -> this.add( p_context.agent(), i.get( 0 ).<String>raw(), i.get( 1 ) ) )
        );

    }

    /**
     * adds a value into the storage
     *
     * @param p_agent agent
     * @param p_key key
     * @param p_value value
     * @return boolean flag if the item is not forbidden
     */
    private boolean add( final IAgent<?> p_agent, final String p_key, final ITerm p_value )
    {
        if ( m_forbidden.contains( p_key ) )
            return false;

        p_agent.storage().put( p_key, p_value.raw() );
        return true;
    }

}
