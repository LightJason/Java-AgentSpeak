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

package org.lightjason.agentspeak.action.builtin.prolog;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * solve the given Prolog program.
 * The action solves each given Prolog program and
 * returns the boolean of the execution, the action never
 * fails
 *
 * {@code [A|B] = prolog/solve( Theory, ["foo(X)", "bar(Y)"] );}
 */
public final class CSolveAll extends IBuiltinAction
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
        // https://github.com/bolerio/hgdb/wiki/TuProlog
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205
        // /tuProlog-3.2.1-mvn/src/alice/tuprolog/?at=master
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205/tuProlog-3.2.1-mvn/src/alice/
        // tuprolog/Struct.java?at=master&fileviewer=file-view-default
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205/tuProlog-3.2.1-mvn/src/alice/
        // tuprolog/Term.java?at=master&fileviewer=file-view-default
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205/tuProlog-3.2.1-mvn/src/alice/
        // tuprolog/Theory.java?at=master&fileviewer=file-view-default
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205/tuProlog-3.2.1-mvn/src/alice/
        // tuprolog/Var.java?at=master&fileviewer=file-view-default


        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        try
        {
            // create new theory with current agent beliefbase and append given theory
            final Theory l_theory = new Theory( new Struct( p_context.agent().beliefbase().stream().map( CSolveAll::toprologterm ).toArray( Term[]::new ) ) );

            final int l_skip;
            if ( l_arguments.get( 0 ).raw() instanceof Theory )
            {
                l_skip = 1;
                l_theory.append( l_arguments.get( 0 ).raw() );
            }
            else
                l_skip = 0;


            final SolveInfo[] l_result = l_arguments.stream()
                                                    .skip( l_skip )
                                                    .map( i -> solve( l_theory, i.raw() ) )
                                                    .toArray( SolveInfo[]::new );

            if ( Arrays.stream( l_result ).anyMatch( i -> !i.isSuccess() ) )
                return CFuzzyValue.from( false );



            //Arrays.stream( l_result )
            //      .map( i -> i.getSolution(). )
            return CFuzzyValue.from( true );
        }
        catch ( final Exception l_exception )
        {
            LOGGER.warning( l_exception.getMessage() );
            return CFuzzyValue.from( false );
        }
    }

    /**
     * run solver
     *
     * @param p_theory theory
     * @param p_query query
     * @return solver
     */
    private static SolveInfo solve( @Nonnull final Theory p_theory, @Nonnull final String p_query )
    {
        final Prolog l_prolog = new Prolog();
        l_prolog.setException( true );
        l_prolog.setWarning( false );

        try
        {
            l_prolog.addTheory( p_theory );
            return l_prolog.solve( p_query );
        }
        catch ( final InvalidTheoryException | MalformedGoalException l_exception )
        {
            throw new RuntimeException( l_exception );
        }
    }

    /**
     * converts a agentspeak term to a prolog term
     *
     * @param p_term agentspeak term
     * @return prolog term
     */
    private static Term toprologterm( @Nonnull final ITerm p_term )
    {
        System.out.println( p_term );

        if ( p_term instanceof IVariable<?> )
            return new Var( p_term.functor() );

        if ( ( p_term instanceof ILiteral ) && ( !p_term.<ILiteral>term().emptyValues() ) )
            return new Struct( p_term.functor(), p_term.<ILiteral>term().orderedvalues().map( CSolveAll::toprologterm ).toArray( Term[]::new ) );

        if ( ( p_term instanceof IRawTerm<?> ) && ( p_term.<IRawTerm<?>>term().valueassignableto( Double.class ) ) )
            return new alice.tuprolog.Double( p_term.raw() );

        if ( ( p_term instanceof IRawTerm<?> ) && ( p_term.<IRawTerm<?>>term().valueassignableto( Float.class ) ) )
            return new alice.tuprolog.Float( p_term.raw() );

        if ( ( p_term instanceof IRawTerm<?> ) && ( p_term.<IRawTerm<?>>term().valueassignableto( Long.class ) ) )
            return new alice.tuprolog.Long( p_term.raw() );

        if ( ( p_term instanceof IRawTerm<?> ) && ( p_term.<IRawTerm<?>>term().valueassignableto( Integer.class ) ) )
            return new alice.tuprolog.Int( p_term.raw() );

        return new Struct( p_term.functor() );
    }
}
