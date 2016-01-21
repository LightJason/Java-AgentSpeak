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

package lightjason.agent.action.buildin.bind;

import lightjason.common.CReflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * class to filter methods *
 */
public final class CFilter implements CReflection.IFilter<Method>
{
    @Override
    public final boolean filter( final Method p_method )
    {
        boolean l_use = true;
        if ( p_method.isAnnotationPresent( IActionBind.class ) )
            l_use = p_method.getAnnotation( IActionBind.class ).bind();

        return l_use && ( !(
                Modifier.isAbstract( p_method.getModifiers() ) || Modifier.isInterface( p_method.getModifiers() ) || ( Modifier.isNative(
                        p_method.getModifiers()
                ) || ( Modifier.isStatic( p_method.getModifiers() ) ) ) ) );
    }

}
