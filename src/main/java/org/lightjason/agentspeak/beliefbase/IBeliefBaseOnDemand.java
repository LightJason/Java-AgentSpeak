/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.beliefbase;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;


/**
 * on-demand beliefbase for creating trigger-events
 * without any percistency
 */
public abstract class IBeliefBaseOnDemand<T extends IAgent<?>> extends IBaseBeliefBase<T>
{

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public final IView<T> getView( final String p_key )
    {
        throw new CIllegalStateException(  );
    }

    @Override
    public final Collection<ILiteral> getLiteral( final String p_key )
    {
        return Collections.<ILiteral>emptySet();
    }

    @Override
    public final IView<T> getViewOrDefault( final String p_key, final IView<T> p_default )
    {
        throw new CIllegalStateException(  );
    }

    @Override
    public final Stream<ITrigger> getTrigger( final IView<T> p_view )
    {
        return null;
    }

    @Override
    public Stream<ILiteral> streamLiteral()
    {
        return Stream.of();
    }

    @Override
    public final Stream<IView<T>> streamView()
    {
        return Stream.of();
    }

    @Override
    public final IBeliefBase<T> clear()
    {
        return this;
    }



    @Override
    public ILiteral add( final ILiteral p_literal )
    {
        return p_literal;
    }

    @Override
    public ILiteral remove( final ILiteral p_literal )
    {
        return null;
    }

    @Override
    public boolean containsLiteral( final String p_key )
    {
        return false;
    }



    @Override
    public final IView<T> add( final IView<T> p_view )
    {
        throw new CIllegalStateException(  );
    }

    @Override
    public final IView<T> remove( final IView<T> p_view )
    {
        throw new CIllegalStateException(  );
    }

    @Override
    public final boolean containsView( final String p_key )
    {
        return false;
    }
}
