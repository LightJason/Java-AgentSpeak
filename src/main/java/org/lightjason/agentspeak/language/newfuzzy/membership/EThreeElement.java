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

package org.lightjason.agentspeak.language.newfuzzy.membership;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.newfuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;

import java.util.stream.Stream;


/**
 * three element fuzzy with numerical representation
 */
public enum EThreeElement implements IFuzzyMembership<EThreeElement>
{
    LOW,
    MEDIUM,
    HIGH;

    @NonNull
    @Override
    @SuppressWarnings( "unchecked" )
    public <V> V raw()
    {
        return (V) Integer.valueOf( this.ordinal() );
    }

    @Override
    public Stream<EThreeElement> members( @NonNull final Number p_value )
    {
        return Stream.of();
    }

    @Override
    public IFuzzyValue<EThreeElement> apply( @NonNull final Number p_value )
    {
        return CFuzzyValue.of( this, p_value );
    }
}
