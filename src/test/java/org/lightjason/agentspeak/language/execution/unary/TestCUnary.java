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

package org.lightjason.agentspeak.language.execution.unary;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.Collections;


/**
 * test unary operators
 */
public final class TestCUnary extends IBaseTest
{

    /**
     * decrement
     */
    @Test
    public final void decrement()
    {
        final IVariable<Object> l_value = new CVariable<>( "Value" );
        l_value.set( 5 );

        Assert.assertTrue(
            new CDecrement( l_value ).execute(
                false,
                new CLocalContext( l_value ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        Assert.assertEquals( 4, l_value.<Number>raw().intValue() );
    }

    /**
     * decrement
     */
    @Test
    public final void increment()
    {
        final IVariable<Object> l_value = new CVariable<>( "Value" );
        l_value.set( 7 );

        Assert.assertTrue(
            new CIncrement( l_value ).execute(
                false,
                new CLocalContext( l_value ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        Assert.assertEquals( 8, l_value.<Number>raw().intValue() );
    }
}
