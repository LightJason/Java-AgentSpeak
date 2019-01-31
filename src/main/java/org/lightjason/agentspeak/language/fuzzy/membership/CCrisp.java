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

package org.lightjason.agentspeak.language.fuzzy.membership;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.stream.Stream;


/**
 * membership which represent a strict crisp structure
 *
 * @tparam E fuzzy enum
 */
public final class CCrisp<E extends Enum<?>> implements IFuzzyMembership<E>
{
    @NonNull
    @Override
    public Stream<IFuzzyValue<?>> success()
    {
        return Stream.of(

        );
    }

    @NonNull
    @Override
    public Stream<IFuzzyValue<?>> fail()
    {
        return Stream.of(

        );
    }

    @Override
    public Stream<Number> range( @NonNull final E p_value )
    {
        return null;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends Enum<?>> IFuzzyMembership<T> raw()
    {
        return (IFuzzyMembership<T>) this;
    }

    @Override
    public Stream<IFuzzyValue<?>> apply( final Number p_number )
    {
        return p_number.doubleValue() <= 0.5
               ? this.fail()
               : this.success();
    }

    @Nonnull
    @Override
    public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return p_agent;
    }
}
