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

package org.lightjason.agentspeak.beliefbase;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.language.ILiteral;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;


/**
 * on-demand beliefbase for creating trigger-events
 * without any percistency
 */
public abstract class IBeliefbaseOnDemand<T extends IAgent<?>> extends IBaseBeliefbase<T>
{

    @Override
    public boolean empty()
    {
        return true;
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public Stream<ILiteral> streamLiteral()
    {
        return Stream.of();
    }

    @Override
    public ILiteral add( final ILiteral p_literal )
    {
        return super.add( p_literal );
    }

    @Override
    public ILiteral remove( final ILiteral p_literal )
    {
        return super.remove( p_literal );
    }

    @Override
    public boolean containsLiteral( final String p_key )
    {
        return false;
    }

    @Override
    public Collection<ILiteral> literal( final String p_key )
    {
        return Collections.<ILiteral>emptySet();
    }


    @Override
    public final IView<T> view( final String p_key )
    {
        throw new CIllegalStateException( CCommon.languagestring( IBeliefbaseOnDemand.class, "nostorage", this, p_key ) );
    }

    @Override
    public final IView<T> viewOrDefault( final String p_key, final IView<T> p_default )
    {
        throw new CIllegalStateException( CCommon.languagestring( IBeliefbaseOnDemand.class, "nostorage", this, p_key ) );
    }

    @Override
    public final Stream<IView<T>> streamView()
    {
        return Stream.of();
    }

    @Override
    public final IBeliefbase<T> clear()
    {
        return this;
    }

    @Override
    public final IView<T> add( final IView<T> p_view )
    {
        throw new CIllegalStateException( CCommon.languagestring( IBeliefbaseOnDemand.class, "nostorage", this, p_view.name() ) );
    }

    @Override
    public final IView<T> remove( final IView<T> p_view )
    {
        throw new CIllegalStateException( CCommon.languagestring( IBeliefbaseOnDemand.class, "nostorage", this, p_view.name() ) );
    }

    @Override
    public final boolean containsView( final String p_key )
    {
        return false;
    }

}
