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

package org.lightjason.agentspeak.agent;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * test for unification
 */
public final class TestCUnifier extends IBaseTest
{

    /**
     * traversion of literal value content
     *
     * @throws Exception on parsing exception
     */
    @Test
    public final void literalvaluetraversing() throws Exception
    {
        final Set<ILiteral> l_test = Stream.of(
            CLiteral.parse( "first('Hello')" ),
            CLiteral.parse( "first('Foo')" )
        ).collect( Collectors.toSet() );

        final ILiteral l_literal = CLiteral.from( "toplevel", Stream.concat( l_test.stream(), Stream.of(
            CLiteral.parse( "second/sub(1)" ),
            CLiteral.parse( "second/sub(2)" ),
            CLiteral.parse( "second/sub(3)" )
        ) ).collect( Collectors.toSet() ) );

        final List<ITerm> l_result = l_literal.values( CPath.from( "first" ) ).collect( Collectors.toList() );
        assertEquals( MessageFormat.format( "literal traversing in {0} is wrong", l_literal ), l_result.size(), l_test.size() );
    }


    /**
     * traversion of literal value content
     *
     * @throws Exception parser exeception
     */
    @Test
    public final void literalvaluesequentialtraversing() throws Exception
    {
        final ILiteral[] l_test = Stream.of(
            CLiteral.parse( "first('Hello')" ),
            CLiteral.parse( "first('Foo')" )
        ).toArray( ILiteral[]::new );

        final ILiteral l_literal = CLiteral.from( "toplevel", Stream.concat(
            Arrays.stream( l_test ),
            Stream.of(
                CLiteral.parse( "second/sub(1)" ),
                CLiteral.parse( "second/sub(2)" ),
                CLiteral.parse( "second/sub(3)" )
            )
        ).collect( Collectors.toSet() ) );

        Assert.assertArrayEquals(
            MessageFormat.format( "literal sequential traversing in {0} is wrong for", l_literal ),
            l_literal.orderedvalues( CPath.from( "first" ) ).toArray(),
            l_test
        );
    }

    /**
     * literal value hashing
     *
     * @throws Exception parser exception
     */
    @Test
    public final void structurehash() throws Exception
    {
        final ILiteral l_first = CLiteral.parse( "foo(sub(3),sub(X),test(1235),data(value('data string')))[ann(1),value('test')]" );
        final ILiteral l_second = CLiteral.parse( "foo(sub(3),sub(X),test(123),data(value('data string another value')))[ann(13),value('test2')]" );

        assertEquals(
            MessageFormat.format( "literal value hash of [{0}] and [{1}] is [{2} / {3}] inequal", l_first, l_second, l_first.structurehash(),
                                  l_second.structurehash()
            ),
            l_first.structurehash(),
            l_second.structurehash()
        );

        final ILiteral l_third = CLiteral.parse( "foo()" );
        final ILiteral l_fourth = CLiteral.parse( "hallo()" );
        assertNotEquals(
            MessageFormat.format( "literal value hash of [{0}] and [{1}] are equal [{2}]", l_third, l_fourth, l_third.structurehash() ),
            l_third.structurehash(),
            l_fourth.structurehash()
        );
    }


    /**
     * manuell running test
     *
     * @param p_args arguments
     * @throws Exception parser exception
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new TestCUnifier().invoketest();
    }

}
