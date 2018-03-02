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
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Number;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.TermVisitor;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * prolog solving structure
 */
public abstract class IBaseSolve extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 316729237221926520L;

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

        final Theory l_theory;
        try
        {
            l_theory = new Theory( new Struct( p_context.agent().beliefbase().stream().map( CSolveAll::toprologterm ).toArray( Term[]::new ) ) );
        }
        catch ( final Exception l_exception )
        {
            LOGGER.warning( l_exception.getMessage() );
            return CFuzzyValue.from( false );
        }

        // add theory objects to the current theory
        l_arguments.stream()
                   .filter( i -> i.raw() instanceof Theory )
                   .forEach( i ->
                   {
                       try
                       {
                           l_theory.append( i.raw() );
                       }
                       catch ( final InvalidTheoryException l_exception )
                       {
                           LOGGER.warning( l_exception.getMessage() );
                       }
                   } );

        // execute prolog engine with theory
        final SolveInfo[] l_result = l_arguments.stream()
                                                .filter( i -> i.raw() instanceof String )
                                                .map( i -> solve( l_theory, i.raw() ) )
                                                .toArray( SolveInfo[]::new );

        // result checking
        if ( this.result( l_result  ) )
            return CFuzzyValue.from( false );

        // result extraction to result values
        Arrays.stream( l_result )
              .filter( SolveInfo::isSuccess )
              .flatMap( i ->
              {
                  try
                  {
                      return i.getBindingVars().stream();
                  }
                  catch ( final NoSolutionException l_exception )
                  {
                      return null;
                  }
              } )
              .filter( Var::isVar )
              .map( CSolveAll::fromprologterm )
              .map( CRawTerm::from )
              .forEach( p_return::add );

        return CFuzzyValue.from( true );

    }

    /**
     * method to define at which definition the action fails or succeed
     *
     * @param p_solveinfos solverinfos
     * @return successful or fail execution
     */
    protected abstract boolean result( @Nonnull final SolveInfo[] p_solveinfos );

    /**
     * run solver
     *
     * @param p_theory theory
     * @param p_query query
     * @return solver
     */
    protected static SolveInfo solve( @Nonnull final Theory p_theory, @Nonnull final String p_query )
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
     * @todo tuprolog cannot deal with java native objects
     */
    //Checkstyle:OFF:NPathComplexity
    protected static Term toprologterm( @Nonnull final ITerm p_term )
    {
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

        return new Struct( p_term.functor().trim() );
    }
    //Checkstyle:ON:NPathComplexity


    /**
     * extract data of a variable
     *
     * @param p_term variable
     * @tparam T return type
     * @return value
     */
    @Nullable
    protected static Object fromprologterm( @Nonnull final Var p_term )
    {
        if ( !p_term.isBound() )
            return null;

        final AtomicReference<Object> l_data = new AtomicReference<>();
        p_term.accept( new CTermVisitor( l_data ) );
        return l_data.get();
    }


    /**
     * term visitor with data reference
     */
    protected static final class CTermVisitor implements TermVisitor
    {
        /**
         * data reference
         */
        private final AtomicReference<Object> m_data;

        CTermVisitor( @Nonnull final AtomicReference<Object> p_data )
        {
            m_data = p_data;
        }

        @Override
        public void visit( final Struct p_struct )
        {
            if ( !p_struct.getName().trim().isEmpty() )
                m_data.compareAndSet( null, p_struct.getName().trim() );
        }

        @Override
        public void visit( final Var p_var )
        {
            p_var.getLink()
                 .accept( new CTermVisitor( m_data ) );
        }

        @Override
        public void visit( final Number p_number )
        {
            m_data.compareAndSet( null, p_number.doubleValue() );
        }
    }
}
