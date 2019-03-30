/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.fuzzy.set;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.Locale;


/**
 * fuzzy crisp
 */
public enum ECrisp implements IFuzzySet<ECrisp>
{
    FALSE( false ),
    TRUE( true );

    /**
     * native type
     */
    private final Boolean m_value;

    /**
     * ctor
     *
     * @param p_value value
     */
    ECrisp( final boolean p_value )
    {
        m_value = p_value;
    }

    @NonNull
    @Override
    @SuppressWarnings( "unchecked" )
    public <V> V raw()
    {
        return (V) m_value;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <U extends Enum<?>> U rawenum()
    {
        return (U) this;
    }

    @NonNull
    @Override
    public IFuzzyValue<?> apply( @NonNull final Number p_number )
    {
        return CFuzzyValue.of( this, p_number );
    }

    @NonNull
    @Override
    public IFuzzyValue<?> apply( @NonNull final String p_name, @NonNull final Number p_number )
    {
        return ECrisp.valueOf( p_name.toUpperCase( Locale.ROOT ) ).apply( p_number );
    }

}
