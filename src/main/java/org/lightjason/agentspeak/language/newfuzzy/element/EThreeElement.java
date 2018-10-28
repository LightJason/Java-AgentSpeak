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

package org.lightjason.agentspeak.language.newfuzzy.element;

import org.lightjason.agentspeak.language.newfuzzy.value.CFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.value.IFuzzyValue;

import javax.annotation.Nullable;


/**
 * three element fuzzy with numerical representation
 */
public enum EThreeElement implements IFuzzyElement<EThreeElement>
{
    LOW( 1 ),
    MEDIUM( 10 ),
    HIGH( 100 );

    /**
     * integer representation of the value
     */
    private final int m_value;

    /**
     * ctor
     *
     * @param p_value value
     */
    EThreeElement( final int p_value )
    {
        m_value = p_value;
    }

    @Nullable
    @Override
    @SuppressWarnings( "unchecked" )
    public <V> V raw()
    {
        return (V) Integer.valueOf( m_value );
    }

    @Override
    public IFuzzyValue<EThreeElement> apply( final Number p_number )
    {
        return new CFuzzyValue<>( this, p_number );
    }
}
