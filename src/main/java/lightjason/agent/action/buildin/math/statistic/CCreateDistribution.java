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
import lightjason.error.CIllegalArgumentException;
import lightjason.error.CIllegalStateException;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.GumbelDistribution;
import org.apache.commons.math3.distribution.LaplaceDistribution;
import org.apache.commons.math3.distribution.LevyDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.LogisticDistribution;
import org.apache.commons.math3.distribution.NakagamiDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.ParetoDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.random.ISAACRandom;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.SynchronizedRandomGenerator;
import org.apache.commons.math3.random.Well1024a;
import org.apache.commons.math3.random.Well19937a;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.random.Well44497a;
import org.apache.commons.math3.random.Well44497b;
import org.apache.commons.math3.random.Well512a;

import java.util.List;
import java.util.stream.Collectors;



/**
 * creates a distribution object
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
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final EDistribution l_distribution = EDistribution.valueOf( CCommon.<String, ITerm>getRawValue( p_argument.get( 0 ) ).trim().toUpperCase() );
        final int l_requiredarguments = this.getMinimalArgumentNumber() + l_distribution.getArgumentNumber();

        if ( p_argument.size() < l_requiredarguments )
            throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( this, "distributionarguments" ) );


        p_return.add( CRawTerm.from(
                l_distribution.get(
                        ( p_argument.size() > l_requiredarguments
                          ? EGenerator.valueOf( CCommon.<String, ITerm>getRawValue( p_argument.get( l_requiredarguments + 1 ) ).trim().toUpperCase() )
                          : EGenerator.MERSENNETWISTER ).get(),
                        p_argument.subList( 1, l_requiredarguments ).stream().mapToDouble( i -> CCommon.<Number, ITerm>getRawValue( i ).doubleValue() ).boxed()
                                  .collect( Collectors.toList() )
                )
        ) );

        return CFuzzyValue.from( true );
    }


    /**
     * usable distributions
     */
    private enum EDistribution
    {
        BETA( 2 ),
        CAUCHY( 2 ),
        CHISQUARE( 1 ),
        EXPONENTIAL( 1 ),
        F( 2 ),
        GAMMA( 2 ),
        GUMBLE( 2 ),
        LAPLACE( 2 ),
        LEVY( 2 ),
        LOGISTIC( 2 ),
        LOGNORMAL( 2 ),
        NAKAGAMI( 2 ),
        NORMAL( 2 ),
        PARETO( 2 ),
        T( 1 ),
        TRIANGULAR( 3 ),
        UNIFORM( 2 ),
        WEIBULL( 2 );

        /**
         * number of arguments
         */
        private final int m_arguments;

        /**
         * ctor
         *
         * @param p_arguments number of arguments
         */
        EDistribution( final int p_arguments )
        {
            m_arguments = p_arguments;
        }

        /**
         * return number of arguments
         *
         * @return argument number
         */
        public final int getArgumentNumber()
        {
            return m_arguments;
        }

        /**
         * returns the distribution object
         *
         * @param p_generator random generator
         * @param p_arguments arguments
         * @return real distribution
         */
        public final AbstractRealDistribution get( final RandomGenerator p_generator, final List<Double> p_arguments )
        {
            switch ( this )
            {
                case BETA:
                    return new BetaDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case CAUCHY:
                    return new CauchyDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case CHISQUARE:
                    return new ChiSquaredDistribution( p_generator, p_arguments.get( 0 ) );

                case EXPONENTIAL:
                    return new ExponentialDistribution( p_generator, p_arguments.get( 0 ) );

                case F:
                    return new FDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case GAMMA:
                    return new GammaDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case GUMBLE:
                    return new GumbelDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case LAPLACE:
                    return new LaplaceDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case LEVY:
                    return new LevyDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case LOGISTIC:
                    return new LogisticDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case LOGNORMAL:
                    return new LogNormalDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case NAKAGAMI:
                    return new NakagamiDistribution(
                            p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ), NakagamiDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY );

                case NORMAL:
                    return new NormalDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case PARETO:
                    return new ParetoDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case T:
                    return new TDistribution( p_generator, p_arguments.get( 0 ) );

                case TRIANGULAR:
                    return new TriangularDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ), p_arguments.get( 2 ) );

                case UNIFORM:
                    return new UniformRealDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                case WEIBULL:
                    return new WeibullDistribution( p_generator, p_arguments.get( 0 ), p_arguments.get( 1 ) );

                default:
                    throw new CIllegalStateException( lightjason.common.CCommon.getLanguageString( this, "unknown", this ) );
            }
        }
    }

    /**
     * number generator
     */
    private enum EGenerator
    {
        MERSENNETWISTER,
        SYNCHRONIZEDMERSENNETWISTER,
        ISSAAC,
        SYNCHRONIZEDISSAAC,
        INTERNAL,
        SYNCHRONIZEDINTERNAL,
        WELL512A,
        SYNCHRONIZEDWELL512A,
        WELL1024A,
        SYNCHRONIZEDWELL1024A,
        WELL19937A,
        SYNCHRONIZEDWELL19937A,
        WELL19937C,
        SYNCHRONIZEDWELL19937C,
        WELL4449A,
        SYNCHRONIZEDWELL4449A,
        WELL44497B,
        SYNCHRONIZEDWELL44497B;

        /**
         * returns a number generator
         *
         * @return generator
         */
        public final RandomGenerator get()
        {
            switch ( this )
            {
                case MERSENNETWISTER:
                    return new MersenneTwister();

                case SYNCHRONIZEDMERSENNETWISTER:
                    return new SynchronizedRandomGenerator( new MersenneTwister() );

                case ISSAAC:
                    return new ISAACRandom();

                case SYNCHRONIZEDISSAAC:
                    return new SynchronizedRandomGenerator( new ISAACRandom() );

                case INTERNAL:
                    return new JDKRandomGenerator();

                case SYNCHRONIZEDINTERNAL:
                    return new SynchronizedRandomGenerator( new JDKRandomGenerator() );

                case WELL512A:
                    return new Well512a();

                case SYNCHRONIZEDWELL512A:
                    return new SynchronizedRandomGenerator( new Well512a() );

                case WELL1024A:
                    return new Well1024a();

                case SYNCHRONIZEDWELL1024A:
                    return new SynchronizedRandomGenerator( new Well1024a() );

                case WELL19937A:
                    return new Well19937a();

                case SYNCHRONIZEDWELL19937A:
                    return new SynchronizedRandomGenerator( new Well19937a() );

                case WELL19937C:
                    return new Well19937c();

                case SYNCHRONIZEDWELL19937C:
                    return new SynchronizedRandomGenerator( new Well19937c() );

                case WELL4449A:
                    return new Well44497a();

                case SYNCHRONIZEDWELL4449A:
                    return new SynchronizedRandomGenerator( new Well44497a() );

                case WELL44497B:
                    return new Well44497b();

                case SYNCHRONIZEDWELL44497B:
                    return new SynchronizedRandomGenerator( new Well44497b() );

                default:
                    throw new CIllegalStateException( lightjason.common.CCommon.getLanguageString( this, "unknown", this ) );
            }
        }
    }
}