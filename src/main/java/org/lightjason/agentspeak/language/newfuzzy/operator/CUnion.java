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

package org.lightjason.agentspeak.language.newfuzzy.operator;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;


/**
 * fuzzy union operator
 *
 * @tparam T fuzzy type
 */
public final class CUnion<T extends Enum<?>> implements IFuzzyOperator<T>
{
    @Override
    public IFuzzyValue<T> apply( @NonNull final IFuzzyValue<T> p_value1, @NonNull final IFuzzyValue<T> p_value2 )
    {
        return ( p_value1.fuzzy() + p_value2.fuzzy()
                 - p_value1.fuzzy() * p_value2.fuzzy()
                 - Math.min( p_value1.fuzzy(), p_value2.fuzzy() ) )
               / Math.max( 1 - p_value1.fuzzy(), 1 - p_value2.fuzzy() );
    }
}
