/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.builtin.string;

import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;


/**
 * action to replace all occurence within a string.
 * The action replaces the first argument with the second argument
 * on each string beginning at the third argument and returns
 * all replaced strings, the action never fails. The first argument
 * of the action be defined as a regular expression
 *
 * {@code [A|B] = string/replace( "search", "replace with", "this is a search string", "this is another string" );}
 * @see https://en.wikipedia.org/wiki/Regular_expression
 */
public final class CReplace extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5445088831145525666L;

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 3;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final String l_search = p_argument.get( 0 ).<String>raw();
        final String l_replace = p_argument.get( 1 ).<String>raw();

        CCommon.flatten( p_argument )
               .skip( 2 )
               .map( i -> i.<String>raw().replaceAll( l_search, l_replace ) )
               .map( CRawTerm::of )
               .forEach( p_return::add );

        return CFuzzyValue.of( true );
    }

}
