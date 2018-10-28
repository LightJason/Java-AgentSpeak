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

package org.lightjason.agentspeak.language.newfuzzy.defuzzyfication;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.stream.Stream;


/**
 * defuzzification with least-of-maximum
 */
public class CLOM<T extends Enum<?>> implements IDefuzzification<T>
{
    @Nonnull
    @Override
    public T defuzzify( @Nonnull final Stream<IFuzzyValue<T>> p_value )
    {
        return null;
    }

    @Override
    public final boolean execution( @Nonnull final Stream<IFuzzyValue<T>> p_value )
    {
        return false;
    }

    @Nonnull
    @Override
    public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return null;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<T>> success()
    {
        return null;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<T>> fail()
    {
        return null;
    }
}
