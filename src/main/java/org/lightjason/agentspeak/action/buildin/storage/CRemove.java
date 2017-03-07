/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * removes an element by name from the storage.
 * The actions removes any value from the storage
 * which is referenced by the key and returns the
 * value, the action fails on forbidden key or non
 * exisiting elements
 *
 * @code [A|B] = storage/remove("foo", "bar"); @endcode
 */
public final class CRemove extends IStorage
{

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
     * @param p_forbidden forbidden keys
     */
    public CRemove( final String... p_forbidden )
    {
        super( Arrays.asList( p_forbidden ) );
    }

    /**
     * ctor
     *
     * @param p_fordbidden forbidden keys
     */
    public CRemove( final Collection<String> p_fordbidden )
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
        CCommon.flatcollection( p_argument )
               .map( ITerm::<String>raw )
               .allMatch( i -> this.remove( p_context.agent(), i, p_return ) )
        );
    }


    /**
     * removes a value from the storage
     *
     * @param p_agent agent
     * @param p_key key
     * @param p_return return arguments
     * @return boolean flag if the item is not forbidden and is exists
     */
    private boolean remove( final IAgent<?> p_agent, final String p_key, final List<ITerm> p_return )
    {
        if ( ( m_forbidden.contains( p_key ) ) || ( !p_agent.storage().containsKey( p_key ) ) )
            return false;

        p_return.add(
            CRawTerm.from(
                p_agent.storage().remove( p_key )
            )
        );
        return true;
    }

}
