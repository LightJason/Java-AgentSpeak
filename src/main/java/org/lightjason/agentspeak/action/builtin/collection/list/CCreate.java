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

package org.lightjason.agentspeak.action.builtin.collection.list;

import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * creates a list.
 * Creates a list of the arguments, so each argument of the action is put to the list,
 * is the argument empty an empty-list object will be returned, the action fails never
 *
 * @code L1 = collection/list/create("a", 1, ["b", 2]);
 * L2 = collection/list/create();
 * @endcode
 */
public final class CCreate extends IBuiltinAction
{

    /**
     * serial id
     */
    private static final long serialVersionUID = 4537731951256173941L;

    /**
     * ctor
     */
    public CCreate()
    {
        super( 3 );
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<?> l_list = p_argument.isEmpty()
                               ? new ArrayList<>()
                               : CCommon.flatten( p_argument ).map( ITerm::raw ).collect( Collectors.toList() );

        p_return.add( CRawTerm.from(
            p_parallel
            ? Collections.synchronizedList( l_list )
            : l_list
        ) );

        return CFuzzyValue.from( true );
    }

}
