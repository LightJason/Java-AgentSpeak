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

package lightjason.common.benchmark;

import javassist.NotFoundException;

import java.lang.instrument.Instrumentation;


/**
 * premain executable class for benchmarking
 *
 * @note Jar file must be performtemplate with @code java -javaagent:<Jar> -jar <Jar> @endcode
 */
public final class CBenchmark
{

    /**
     * private ctor
     */
    private CBenchmark()
    {
    }

    /**
     * premain for benchmarking
     *
     * @param p_args arguments of the agent - will pass to the normal main
     * @param p_instrumentation instrumentation to inject class data
     * @throws NotFoundException on class not found
     */
    public static void premain( final String p_args, final Instrumentation p_instrumentation ) throws NotFoundException
    {
        p_instrumentation.addTransformer( new CInjection() );
    }

}
