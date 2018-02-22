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

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * solve the given Prolog program.
 * The action solves each given Prolog program and
 * returns the boolean of the execution, the action never
 * fails
 *
 * @code [A|B|C] = prolog/solve( Theory, ["Test1", "test2", "test3"] ); @endcode
 */
public final class CSolve extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 902243292596197085L;

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                               @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        if ( l_arguments.size() < 2 )
            return CFuzzyValue.from( false );

        final Prolog l_prolog = new Prolog();
        l_prolog.setException( true );
        l_prolog.setWarning( false );
        try
        {
            l_prolog.setTheory( l_arguments.get( 0 ).raw() );

            final SolveInfo[] l_result = l_arguments.stream()
                                                    .skip( 1 )
                                                    .map( i -> solve( l_prolog, i.<String>raw() ) )
                                                    .toArray( SolveInfo[]::new );

            if ( Arrays.stream( l_result ).anyMatch( i -> !i.isSuccess() ) )
                return CFuzzyValue.from( false );

            Arrays.stream( l_result )
                  .map( i -> i.getSolution(). )
            return CFuzzyValue.from( true );
        }
        catch ( final Exception l_exception )
        {
            return CFuzzyValue.from( false );
        }
    }

    private static SolveInfo solve( @Nonnull final Prolog p_prolog, @Nonnull final String p_query )
    {
        try
        {
            return p_prolog.solve( p_query );
        }
        catch ( final MalformedGoalException l_exception )
        {
            throw new RuntimeException( l_exception );
        }
    }
}
