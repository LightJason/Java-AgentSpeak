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
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * creates the complement between lists.
 * The action uses two input arguments \f$ \mathbb{A} \f$ and \f$ \mathbb{B} \f$ and returns a
 * list of all elements which contains \f$ \mathbb{A} \setminus \mathbb{B} \f$, the action fails
 * on empty lists
 *
 * {@code L = collection/list/complement( [1,2,3], [3,4,5] );}
 * @see https://en.wikipedia.org/wiki/Complement_(set_theory)
 */
public final class CComplement extends IBuiltinAction
{

    /**
     * serial id
     */
    private static final long serialVersionUID = 8021131769211400348L;

    /**
     * ctor
     */
    public CComplement()
    {
        super( 3 );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        if ( ( p_argument.get( 0 ).<List<?>>raw().isEmpty() ) && ( p_argument.get( 1 ).<List<?>>raw().isEmpty() ) )
            return CFuzzyValue.from( false );

        // all arguments must be lists, first argument is the full list
        final Collection<Object> l_result = new LinkedList<>( p_argument.get( 0 ).<Collection<Object>>raw() );
        l_result.removeAll( p_argument.get( 1 ).<Collection<Object>>raw() );
        p_return.add( CRawTerm.from( p_parallel ? Collections.synchronizedCollection( l_result ) : l_result ) );

        return CFuzzyValue.from( true );
    }

}
