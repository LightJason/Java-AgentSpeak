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

package org.lightjason.agentspeak.language.execution.assignment;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * test deconstruct
 */
public final class TestCDeconstruct extends IBaseTest
{
    /**
     * test deconstruct static argument
     */
    @Test
    public void staticargument()
    {
        final IVariable<?> l_outer = new CVariable<>( "Outer" );
        final IVariable<?> l_inner = new CVariable<>( "Inner" );

        Assert.assertTrue(
            execute(
                new CDeconstruct( Stream.of( l_outer, l_inner ), CLiteral.of( "foobar", CRawTerm.of( 5 ), CRawTerm.of( "test" ) ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_outer,
                l_inner
            )
        );

        Assert.assertEquals( "foobar", l_outer.raw() );
        Assert.assertTrue( l_inner.raw() instanceof List<?> );
        Assert.assertEquals( 2, l_inner.<List<?>>raw().size() );
        Assert.assertArrayEquals( Stream.of( 5, "test" ).toArray(), l_inner.<List<?>>raw().toArray() );
    }

    /**
     * test deconstruct variable argument
     */
    @Test
    public void variableargument()
    {
        final IVariable<?> l_outer = new CVariable<>( "Outer" );
        final IVariable<?> l_inner = new CVariable<>( "Inner" );
        final IVariable<Object> l_argument = new CVariable<>( "Arg" );

        l_argument.set( CLiteral.of( "foo", CRawTerm.of( "bar" ), CRawTerm.of( 7 ) ) );

        Assert.assertTrue(
            execute(
                new CDeconstruct( Stream.of( l_outer, l_inner ), l_argument ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_outer,
                l_inner,
                l_argument
            )
        );

        Assert.assertEquals( "foo", l_outer.raw() );
        Assert.assertTrue( l_inner.raw() instanceof List<?> );
        Assert.assertEquals( 2, l_inner.<List<?>>raw().size() );
        Assert.assertArrayEquals( Stream.of( "bar", 7 ).toArray(), l_inner.<List<?>>raw().toArray() );
    }


}
