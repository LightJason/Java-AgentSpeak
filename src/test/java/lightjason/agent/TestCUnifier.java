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

import lightjason.language.CLiteral;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;

import java.text.MessageFormat;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;


/**
 * test for unification
 */
@SuppressWarnings( "serial" )
public final class TestCUnifier
{

    /**
     * traversion of literal content
     */
    //@Test
    public void testLiteralTraversing()
    {
        final ILiteral l_deep = CLiteral.from( "second/sub/sub1" );
        final ILiteral l_literal = CLiteral.from( "toplevel", new HashSet<ITerm>()
        {{

            add( CLiteral.from( "first/sub1" ) );
            add( CLiteral.from( "first/sub2" ) );
            add( CLiteral.from( "second/sub1" ) );
            add( CLiteral.from( "second/sub2" ) );
            add( l_deep );

        }} );

        assertEquals( MessageFormat.format( "literal traversing in {0} is {1} not found", l_literal, l_deep ), l_literal.values(), l_deep );
    }

    /**
     * manuell running test
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args )
    {
        final TestCUnifier l_test = new TestCUnifier();

        l_test.testLiteralTraversing();
    }

}
