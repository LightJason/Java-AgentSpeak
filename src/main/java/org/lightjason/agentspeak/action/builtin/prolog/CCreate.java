/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.builtin.prolog;

import alice.tuprolog.Prolog;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.IntStream;


/**
 * creates a Prolog program instance.
 * The action creates a prolog program instance
 * and never fails
 *
 * @code
   P = prolog/create();
   [A|B|C] = prolog/create( 3 );
 * @endcode
 *
 * @see https://en.wikipedia.org/wiki/Prolog
 * @see http://apice.unibo.it/xwiki/bin/view/Tuprolog/
 */
public final class CCreate extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7990126612530537888L;

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                         @Nonnull final List<ITerm> p_return
    )
    {
        IntStream.range( 0, p_argument.isEmpty() ? 1 : p_argument.get( 0 ).<Number>raw().intValue() )
                 .mapToObj( i -> new Prolog() )
                 .map( i ->
                 {
                     i.setException( false );
                     i.setWarning( false );
                     return i;
                 } )
                 .map( CRawTerm::from )
                 .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
