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

package org.lightjason.agentspeak.language.fuzzy.operator.bool;

import org.lightjason.agentspeak.language.fuzzy.defuzzification.IDefuzzification;
import org.lightjason.agentspeak.language.fuzzy.operator.IFuzzyOperator;
import org.lightjason.agentspeak.language.fuzzy.operator.IFuzzyBundle;

import javax.annotation.Nonnull;
import java.util.AbstractMap;


/**
 * boolean fuzzy bundle
 */
public final class CBundle extends AbstractMap.SimpleImmutableEntry<IFuzzyOperator<Boolean>, IDefuzzification<Boolean>> implements IFuzzyBundle<Boolean>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 646232211237092457L;

    /**
     * ctor
     *
     * @param p_key fuzzy operator
     * @param p_value defuzzification
     */
    public CBundle( @Nonnull final IFuzzyOperator<Boolean> p_key, @Nonnull final IDefuzzification<Boolean> p_value )
    {
        super( p_key, p_value );
    }

}
