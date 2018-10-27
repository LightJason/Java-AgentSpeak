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

package org.lightjason.agentspeak.language.newfuzzy.norm;

import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;


/**
 * fuzzy maximum t-norm
 *
 * @tparam E fuzzy element type
 */
public final class CMax<E extends Enum<?>> implements IFuzzyNorm<E>
{
    @Override
    public IFuzzyValue<E> apply( final IFuzzyValue<E> p_value1, final IFuzzyValue<E> p_value2 )
    {
        return p_value1.fuzzy() > p_value2.fuzzy() ? p_value1 : p_value2;
    }
}
