/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agent;

import lightjason.common.CPath;
import lightjason.language.CLiteral;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/**
 * test for unification
 */
@SuppressWarnings( "serial" )
public final class TestCUnifier
{

    /**
     * traversion of literal value content
     */
    @Test
    public final void testLiteralValueTraversing() throws Exception
    {
        final Set<ILiteral> l_test = new HashSet<ILiteral>()
        {{

            add( CLiteral.parse( "first('Hello')" ) );
            add( CLiteral.parse( "first('Foo')" ) );

        }};

        final ILiteral l_literal = CLiteral.from( "toplevel", new HashSet<ITerm>()
        {{

            addAll( l_test );
            add( CLiteral.parse( "second/sub(1)" ) );
            add( CLiteral.parse( "second/sub(2)" ) );
            add( CLiteral.parse( "second/sub(3)" ) );

        }} );

        final List<ITerm> l_result = l_literal.values( CPath.from( "first" ) ).collect( Collectors.toList() );
        assertEquals( MessageFormat.format( "literal traversing in {0} is wrong", l_literal ), l_result.size(), l_test.size() );
        System.out.println( MessageFormat.format( "literal [{0}] value traversing: {1}", l_literal, l_result ) );
    }


    /**
     * traversion of literal value content
     */
    @Test
    public final void testLiteralValueSequentialTraversing() throws Exception
    {
        final Stack<ILiteral> l_test = new Stack<ILiteral>()
        {{

            add( CLiteral.parse( "first('Hello')" ) );
            add( CLiteral.parse( "first('Foo')" ) );

        }};

        final ILiteral l_literal = CLiteral.from( "toplevel", new HashSet<ITerm>()
        {{

            add( CLiteral.parse( "second/sub(1)" ) );
            add( CLiteral.parse( "second/sub(2)" ) );
            add( CLiteral.parse( "second/sub(3)" ) );
            addAll( l_test );

        }} );

        assertTrue(
                MessageFormat.format( "literal sequential traversing in {0} is wrong for {1}", l_literal, l_test ),
                l_literal.orderedvalues( CPath.from( "first" ) ).map( i -> i.equals( l_test.pop() ) ).allMatch( i -> i )
                && l_test.isEmpty()
        );
        System.out.println( MessageFormat.format( "literal [{0}] value sequential traversing: {1}", l_literal, l_test ) );
    }

    /**
     * traversion of literal annotation content
     */
    @Test
    public final void testLiteralAnnotationTraversing() throws Exception
    {

        final ILiteral l_literal = CLiteral.parse( "foo()[ first(1), first(2), first(foo('hallo')), second('test') ]" );

        final List<ITerm> l_result = l_literal.annotations( CPath.from( "first" ) ).collect( Collectors.toList() );
        assertEquals( MessageFormat.format( "literal traversing in {0} is wrong", l_literal ), l_result.size(), 3 );
        System.out.println( MessageFormat.format( "literal [{0}] annotation traversing: {1}", l_literal, l_result ) );
    }

    /**
     * literal value hashing
     */
    @Test
    public final void testValueHash() throws Exception
    {
        final ILiteral l_first = CLiteral.parse( "foo(sub(3),sub(X),test(1235),data(value('data string')))[ann(1),value('test')]" );
        final ILiteral l_second = CLiteral.parse( "another(sub(3),sub(X),test(123),data(value('data string another value')))[ann(13),value('test2')]" );

        assertEquals(
                MessageFormat.format( "literal value hash of [{0}] and [{1}] is [{2} / {3}] inequal", l_first, l_second, l_first.valuehash(),
                                      l_second.valuehash()
                ),
                l_first.valuehash(),
                l_second.valuehash()
        );
        System.out.println( MessageFormat.format( "literal value hash of [{0}] is equal [{1}]", l_first, l_first.valuehash() ) );


        final ILiteral l_third = CLiteral.parse( "foo()" );
        final ILiteral l_fourth = CLiteral.parse( "hallo()" );
        assertNotEquals(
                MessageFormat.format( "literal value hash of [{0}] and [{1}] are equal [{2}]", l_third, l_fourth ),
                l_third.valuehash(),
                l_fourth.valuehash()
        );
        System.out.println( MessageFormat.format( "literal value hash of [{0}] is inequal [{1}]", l_third, l_fourth ) );
    }


    /**
     * literal annotation
     */
    @Test
    public final void testAnnotationHash() throws Exception
    {
        final ILiteral l_first = CLiteral.parse( "foo(sub(3),sub(X),test(1235),data(value('data string')))[anno(1),xvalue('test')]" );
        final ILiteral l_second = CLiteral.parse( "another(sub(3),sub(X),test(123),data(value('data string another value')))[foo(13),valuenew('test2')]" );


        assertNotEquals(
                MessageFormat.format( "literal annotation hash of [{0}] and [{1}] are equal [{2}]", l_first, l_second ),
                l_first.annotationhash(), l_second.annotationhash()
        );
        System.out.println( MessageFormat.format( "literal annotation hash of [{0}] is inequal [{1}|", l_first, l_second.annotationhash() ) );
    }


    /**
     * manuell running test
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args ) throws Exception
    {
        final TestCUnifier l_test = new TestCUnifier();

        l_test.testLiteralValueTraversing();
        l_test.testLiteralValueSequentialTraversing();
        l_test.testLiteralAnnotationTraversing();
        l_test.testValueHash();
        l_test.testAnnotationHash();
    }

}
