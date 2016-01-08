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

package lightjason.language.execution.expression.arithmetic;

import lightjason.common.CPath;
import lightjason.language.CVariable;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Random;

import static org.junit.Assert.assertTrue;


/**
 * test arithmetic expression
 */
@SuppressWarnings( "serial" )
public final class Test_CExpression
{

    /**
     * test simple expression with random values
     */
    @Test
    public void test_SimpleExpressionWithVariable()
    {
        final Random l_random = new Random();
        final CExpression l_expression = new CExpression();

        // (1+X) * 4 + 8
        l_expression.push( "+" ).push( 1 ).push( new CVariable<Number>( "X" ) );
        l_expression.push( "*" ).push( 4 ).push( "+" ).push( 8 );

        for ( int i = 0; i < 500; i++ )
        {
            final int l_inputvalue = l_random.nextInt( 100000 );
            final int l_successoutput = ( l_inputvalue + 1 ) * 4 + 8;

            l_expression.getVariables().get( CPath.from( "X" ) ).set( new Integer( l_inputvalue ) );
            final int l_output = l_expression.evaluate().intValue();

            assertTrue( MessageFormat.format( "value in run [{0}] should be [{1}] but is [{2}]", i, l_successoutput, l_output ), l_successoutput == l_output );
        }

    }


    /**
     * test complex expression with fixed value
     */
    @Test
    public void test_ComplexExpression()
    {
        // 2 * (3 ** 4) and ( 2 ** 3 ) * 4
        final CExpression l_expression1 = new CExpression();
        l_expression1.push( "*", "**" ).push( 3, 4, 2 );

        final int l_value1 = l_expression1.evaluate().intValue();
        assertTrue( MessageFormat.format( "value should be [{0}] but is [{1}]", 162, l_value1 ), l_value1 == 162 );
    }

}
