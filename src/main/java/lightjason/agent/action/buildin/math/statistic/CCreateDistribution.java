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

package lightjason.agent.action.buildin.math.statistic;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.math3.distribution.AbstractRealDistribution;

import java.util.List;


/**
 * creates a distribution object
 *
 * @bug incomplete
 * @see http://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/distribution/package-summary.html
 */
public final class CCreateDistribution extends IBuildinAction
{

    /**
     * ctor
     */
    public CCreateDistribution()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // http://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/random/ISAACRandom.html
        // http://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/random/MersenneTwister.html

        return CBoolean.from( true );
    }


    /**
     * usable distributions
     */
    private enum EDistirbution
    {
        BETA,
        BINOMIAL,
        CAUCHY,
        CHISQUARE,
        EXPONENTIAL,
        F,
        GAMMA,
        GEOMETRIC,
        GUMBLE,
        HYPERGEOMETRIC,
        LAPLACE,
        LEVY,
        LOGISTIC,
        LOGNORMAL,
        NAKAGAMI,
        NORMAL,
        PARETO,
        PASCAL,
        POISSON,
        T,
        TRIANGULAR,
        UNIFORMREAL,
        WEIBULL,
        ZIPF;

        /**
         * returns the distribution object
         *
         * @return real distribution
         */
        public final AbstractRealDistribution get()
        {
            switch ( this )
            {

            }

            return null;
        }
    }
}