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

package org.lightjason.agentspeak.action.linearprogram;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * solves the linear program and returns the solution.
 * The action solves the linear program and returns the
 * solution. The first argument is the linear program,
 * all other arguments can be a number or a string with
 * the definition:
 *
 * + maximize / minimize defines the optimization goal
 * + non-negative defines all variables with non-negative values
 * + number is the number of iteration for solving
 *
 * The return arguments are at the first the value, second
 * the number of all referenced \f$ x_i \f$ points and after
 * that all arguments the values of \f$ x_i \f$
 *
 * {@code [Value|CountXi|Xi] = .math/linearprogram/solve( LP, "maximize", "non-negative" );}
 *
 * @see https://en.wikipedia.org/wiki/Linear_programming
 * @see http://commons.apache.org/proper/commons-math/userguide/optimization.html
 */
public final class CSolve extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -9105794980077188037L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CSolve.class, "math", "linearprogram" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        // first argument is the LP pair object, second argument is the goal-type (maximize / minimize),
        // third & fourth argument can be the number of iterations or string with "non-negative" variables
        final List<OptimizationData> l_settings = new LinkedList<>();

        final Pair<LinearObjectiveFunction, Collection<LinearConstraint>> l_default = p_argument.get( 0 ).raw();
        l_settings.add( l_default.getLeft() );
        l_settings.add( new LinearConstraintSet( l_default.getRight() ) );

        p_argument.subList( 1, p_argument.size() ).stream()
                  .map( i ->
                  {
                      if ( CCommon.isssignableto( i, Number.class ) )
                          return new MaxIter( i.raw() );

                      if ( CCommon.isssignableto( i, String.class ) )
                          switch ( i.<String>raw().trim().toLowerCase( Locale.ROOT ) )
                          {
                              case "non-negative":
                                  return new NonNegativeConstraint( true );
                              case "maximize":
                                  return GoalType.MAXIMIZE;
                              case "minimize":
                                  return GoalType.MINIMIZE;

                              default:
                                  return null;
                          }

                      return null;
                  } )
                  .filter( Objects::nonNull )
                  .forEach( l_settings::add );

        // optimze and return
        final SimplexSolver l_lp = new SimplexSolver();
        final PointValuePair l_result = l_lp.optimize( l_settings.toArray( new OptimizationData[l_settings.size()] ) );

        p_return.add( CRawTerm.of( l_result.getValue() ) );
        p_return.add( CRawTerm.of( l_result.getPoint().length ) );
        Arrays.stream( l_result.getPoint() ).boxed().map( CRawTerm::of ).forEach( p_return::add );

        return Stream.of();
    }

}
