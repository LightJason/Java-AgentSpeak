package lightjason;

import lightjason.language.arithmetic.CExpression;
import lightjason.language.arithmetic.operator.CDivide;
import lightjason.language.arithmetic.operator.CMinus;
import lightjason.language.arithmetic.operator.CModulo;
import lightjason.language.arithmetic.operator.CMultiply;
import lightjason.language.arithmetic.operator.CPlus;
import lightjason.language.arithmetic.operator.CPow;
import lightjason.language.arithmetic.operator.IArithmeticOperator;

import java.util.HashMap;
import java.util.Map;


/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

public final class CMain
{
    private static final Map<String, IArithmeticOperator> c_operator = new HashMap()
    {{
        put( "+", new CPlus() );
        put( "-", new CMinus() );
        put( "*", new CMultiply() );
        put( "/", new CDivide() );
        put( "**", new CPow() );
        put( "%", new CModulo() );
    }};

    /**
     * main
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        final CExpression l_expression1 = new CExpression( c_operator );
        l_expression1.add( "*" );
        l_expression1.add( "**", 3, 4 );
        l_expression1.add( 2 );

        System.out.println( l_expression1.evaluate() );
    }

}