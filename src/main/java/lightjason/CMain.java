package lightjason; /**
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

import lightjason.language.CVariable;
import lightjason.language.arithmetic.CExpression;
import lightjason.language.arithmetic.operator.CDivide;
import lightjason.language.arithmetic.operator.CMinus;
import lightjason.language.arithmetic.operator.CModulo;
import lightjason.language.arithmetic.operator.CMultiply;
import lightjason.language.arithmetic.operator.CPlus;
import lightjason.language.arithmetic.operator.CPow;
import lightjason.runtime.IAction;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public final class CMain
{
    /**
     * map with actions
     */
    private static final Map<String, IAction> c_actions = new HashMap<>();


    /**
     * main
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        final CExpression l_expr = new CExpression( new HashMap()
        {{
            put( "+", new CPlus() );
            put( "-", new CMinus() );
            put( "*", new CMultiply() );
            put( "/", new CDivide() );
            put( "**", new CPow() );
            put( "%", new CModulo() );
        }} );

        // (1+2) * 4 + 8
        l_expr.add( "+", 1, new CVariable<Number>( "X" ) );
        l_expr.add( "*", 4 );
        l_expr.add( "+", 8 );


        final Map<String, Number> l_replace = new HashMap<>();
        l_replace.put( "X", 3 );

        System.out.println( l_expr.evaluate( l_replace ) );


        try (
                final InputStream l_stream = new FileInputStream( p_args[0] );
        )
        {
            //new CAgent( l_stream, c_actions );
        }
        catch ( final IOException l_exception )
        {
            l_exception.printStackTrace();
        }
    }

}