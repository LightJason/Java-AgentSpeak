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

package org.lightjason.agentspeak.language.execution.unary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Collections;
import java.util.NoSuchElementException;


/**
 * test unary operators
 */
public final class TestCUnary extends IBaseTest
{

    /**
     * decrement
     */
    @Test
    public void decrement()
    {
        final IVariable<Object> l_value = new CVariable<>( "Value" );
        l_value.set( 5 );

        Assertions.assertTrue(
            execute(
                new CDecrement( l_value ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_value
            )
        );

        Assertions.assertEquals( 4, l_value.<Number>raw().intValue() );
    }

    /**
     * decrement
     */
    @Test
    public void increment()
    {
        final IVariable<Object> l_value = new CVariable<>( "Value" );
        l_value.set( 7 );

        Assertions.assertTrue(
            execute(
                new CIncrement( l_value ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_value
            )
        );

        Assertions.assertEquals( 8, l_value.<Number>raw().intValue() );
    }

    /**
     * test increment not-allocated
     */
    @Test
    public void incrementnotallocated()
    {
        final IVariable<Object> l_value = new CVariable<>( "Not" );

        Assertions.assertThrows(
            NoSuchElementException.class,
            () -> execute(
                new CIncrement( l_value ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_value
            )
        );
    }

    /**
     * test decrement not-allocated
     */
    @Test
    public void decrementnotallocated()
    {
        final IVariable<Object> l_value = new CVariable<>( "Not" );

        Assertions.assertThrows(
            NoSuchElementException.class,
            () -> execute(
                new CDecrement( l_value ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_value
            )
        );
    }

    /**
     * test increment wrong type
     */
    @Test
    public void incrementwrongtype()
    {
        final IVariable<Object> l_value = new CVariable<>( "Type" );
        l_value.set( "v" );

        Assertions.assertThrows(
            TypeNotPresentException.class,
            () -> execute(
                new CIncrement( l_value ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_value
            )
        );
    }

    /**
     * test decrement wrong type
     */
    @Test
    public void decrementwrongtype()
    {
        final IVariable<Object> l_value = new CVariable<>( "Type" );
        l_value.set( "v" );

        Assertions.assertThrows(
            TypeNotPresentException.class,
            () -> execute(
                new CDecrement( l_value ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_value
            )
        );
    }

    /**
     * test tostring
     */
    @Test
    public void unarytostring()
    {
        Assertions.assertEquals( "X()++", new CIncrement( new CVariable<>( "X" ) ).toString() );
        Assertions.assertEquals( "Y()--", new CDecrement( new CVariable<>( "Y" ) ).toString() );
    }

    /**
     * test stream
     */
    @Test
    public void stream()
    {
        final IVariable<?> l_var = new CVariable<>( "A" );

        Assertions.assertEquals( l_var, new CIncrement( l_var ).variables().findFirst().get() );
        Assertions.assertEquals( l_var, new CDecrement( l_var ).variables().findFirst().get() );
    }
}
