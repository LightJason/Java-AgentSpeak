/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agent.action.buildin.math.linearprogram;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * solves the linear program
 */
public final class CSolve extends IBuildinAction
{

    /**
     * ctor
     */
    public CSolve()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first argument is the LP pair object, second argument is the goal-type (maximize / minimize),
        // third & four argument can be the number of iterations or string with "non-negative" variables
        final List<OptimizationData> l_settings = new LinkedList<>();

        final Pair<LinearObjectiveFunction, Collection<LinearConstraint>> l_default = CCommon.<Pair<LinearObjectiveFunction, Collection<LinearConstraint>>, ITerm>getRawValue(
                p_argument.get( 0 ) );
        l_settings.add( l_default.getLeft() );
        l_settings.add( new LinearConstraintSet( l_default.getRight() ) );

        l_settings.addAll( p_argument.subList( 1, p_argument.size() ).stream()
                                     .map( i -> {
                                         if ( CCommon.isRawValueAssignableTo( i, Number.class ) )
                                             return new MaxIter( CCommon.getRawValue( i ) );

                                         if ( CCommon.isRawValueAssignableTo( i, String.class ) )
                                             switch ( CCommon.<String, ITerm>getRawValue( i ).trim().toLowerCase() )
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
                                     .filter( i -> i != null ).collect( Collectors.toList() )
        );


        // optimze and return
        final SimplexSolver l_lp = new SimplexSolver();
        final PointValuePair l_result = l_lp.optimize( l_settings.toArray( new OptimizationData[l_settings.size()] ) );

        p_return.add( CRawTerm.from( l_result.getValue() ) );
        p_return.add( CRawTerm.from( l_result.getPoint().length ) );
        p_return.addAll(
                Arrays.stream( l_result.getPoint() ).boxed().map( i -> CRawTerm.from( i ) ).collect( Collectors.toList() )
        );

        return CBoolean.from( true );
    }

}
