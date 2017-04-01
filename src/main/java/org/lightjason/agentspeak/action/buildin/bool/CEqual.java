/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.bool;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * checks elements of equality.
 * The actions checks the first argument
 * to all others arguments of equality,
 * list structures won't be unflaten, but
 * elementwise compared, the action never fails
 *
 * @code [E1|E2] = bool/equal( "this is equal", "this is equal", [123, "test"] ); @endcode
 * @note on number arguments not the value must equal, also the type (double / integral) must be equal,
 * so keep in mind, that you use the correct number type on the argument input
 */
public final class CEqual extends IBuildinAction
{

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        if ( CCommon.rawvalueAssignableTo( p_argument.get( 0 ), Collection.class ) )
        {

            final Object[] l_argument = p_argument.get( 0 ).<Collection<?>>raw().toArray();

            p_argument.stream()
                      .skip( 1 )
                      .map( i -> CCommon.rawvalueAssignableTo( i, Collection.class ) && Arrays.equals( l_argument, i.<Collection<?>>raw().toArray() ) )
                      .map( CRawTerm::from )
                      .forEach( p_return::add );

        }
        else

            p_argument.stream()
                      .skip( 1 )
                      .map( i -> i.equals( p_argument.get( 0 ) ) )
                      .map( CRawTerm::from )
                      .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

}
