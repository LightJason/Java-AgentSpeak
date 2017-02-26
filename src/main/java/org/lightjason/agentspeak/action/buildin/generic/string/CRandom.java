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

package org.lightjason.agentspeak.action.buildin.generic.string;

import org.apache.commons.lang3.RandomStringUtils;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * action to create random strings, with a definied length.
 * The first argument are the characters, that will be used to create the string,
 * all other arguments are numbers to present the length of the returning string
 * and the action never fails
 * @code [A|B|C] = generic/string/random( "abdefgXYZUI", 5, 3, 6 ); @endcode
 *
 */
public final class CRandom extends IBuildinAction
{

    /**
     * ctor
     */
    public CRandom()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final char[] l_characters = p_argument.get( 0 ).<String>raw().toCharArray();

        CCommon.flatcollection( p_argument )
            .skip( 1 )
            .map( i -> RandomStringUtils.random(
                           i.<Number>raw().intValue(),
                            l_characters
            ) )
            .map( CRawTerm::from )
            .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

}
