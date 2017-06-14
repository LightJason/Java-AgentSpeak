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

package org.lightjason.agentspeak.action.buildin.math.statistic;

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
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * creates a distribution object.
 * The action creates a distribution objects, with an individual
 * pseudo-random generator and different distribution paramter,
 * the action never fails, the following distributions are supported
 * with the following number of numeric arguments
 *
 * + beta distribution with 2 arguments (\f$ \alpha \f$ and \f$ \beta \f$)
 * + cauchy distribution with 2 arguments (media and scale)
 * + chi-square distribution with 1 argument (degree off freedom)
 * + exponential distribution with 1 argument (mean)
 * + f distribution with 2 arguments (degrees of freedom, denominator degrees of freedom)
 * + gamma distribution with 2 arguments (shape and scale)
 * + gumble distribution with 2 arguments (\f$ \mu \f$ and \f$ \beta \f$)
 * + laplace distirbution with 2 arguments (\f$ \mu \f$ and \f$ \beta \f$)
 * + levy distirbution with 2 arguments (\f$ \mu \f$ and \f$ c \f$)
 * + logistic distirbution with 2 arguments (\f$ \mu \f$ and \f$ s \f$)
 * + lognormal distirbution with 2 arguments (scale and shape)
 * + nakagami distribution with 2 arguments (\f$ \mu \f$ and \f$ \omega \f$)
 * + normal distribution with 2 arguments (expected value, variance)
 * + pareto distribution with 2 arguments (scale and shape)
 * + t distribution with 1 argument (degrees of freedom)
 * + triangular distribution with 3 arguments (a, b, c)
 * + uniform distribution with 2 arguments (lower and upper)
 * + weibull distribution with 2 arguments (\f$ \alpha \f$ and \f$ \beta \f$)
 *
 * The following pseudo-random number generators are supported:
 *
 * + mersennetwister (default)
 * + synchronizedmersennetwister
 * + isaac
 * + synchronizedisaac
 * + internal
 * + synchronizedinternal
 * + well512a
 * + synchronizedwell512a
 * + well1024a
 * + synchronizedwell1024a
 * + well19937a
 * + synchronizedwell19937a
 * + well19937c
 * + synchronizedwell19937c
 * + well4449a
 * + synchronizedwell4449a
 * + well44497b
 * + synchronizedwell44497b
 *
 * @code [D1|D2] = math/statistic/createdistribution( "normal", 20, 10, ["beta", "isaac", [8, 12]] ); @endcode
 * @see https://en.wikipedia.org/wiki/Beta_distribution
 * @see https://en.wikipedia.org/wiki/Cauchy_distribution
 * @see https://en.wikipedia.org/wiki/Chi-squared_distribution
 * @see https://en.wikipedia.org/wiki/Exponential_distribution
 * @see https://en.wikipedia.org/wiki/F-distribution
 * @see https://en.wikipedia.org/wiki/Gamma_distribution
 * @see https://en.wikipedia.org/wiki/Gumbel_distribution
 * @see https://en.wikipedia.org/wiki/L%C3%A9vy_distribution
 * @see https://en.wikipedia.org/wiki/Logistic_distribution
 * @see https://en.wikipedia.org/wiki/Log-normal_distribution
 * @see https://en.wikipedia.org/wiki/Nakagami_distribution
 * @see https://en.wikipedia.org/wiki/Normal_distribution
 * @see https://en.wikipedia.org/wiki/Pareto_distribution
 * @see https://en.wikipedia.org/wiki/Student%27s_t-distribution
 * @see https://en.wikipedia.org/wiki/Triangular_distribution
 * @see https://en.wikipedia.org/wiki/Uniform_distribution_(continuous)
 * @see https://en.wikipedia.org/wiki/Weibull_distribution
 */
public final class CCreateDistribution extends IBuildinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 614460992147593598L;

    /**
     * ctor
     */
    public CCreateDistribution()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        IntStream.range( 0, l_arguments.size() )
                 .filter( i -> CCommon.rawvalueAssignableTo( l_arguments.get( i ), String.class ) )
                 .mapToObj( i -> new AbstractMap.SimpleImmutableEntry<>( i, l_arguments.get( i ).<String>raw() ) )
                 .filter( i -> EDistribution.exist( i.getValue() ) )
                 .map( i -> new AbstractMap.SimpleImmutableEntry<>( i.getKey(), EDistribution.from( i.getValue() ) ) )
                 .map( i -> {

                     // check if next argument to the distribution name a generator name
                     final int l_skip;
                     final EGenerator l_generator;

                     if ( ( i.getKey() < l_arguments.size() - 1 ) && ( CCommon.rawvalueAssignableTo( l_arguments.get( i.getKey() + 1 ), String.class ) ) )
                     {
                         l_skip = 1;
                         l_generator = EGenerator.from( l_arguments.get( i.getKey() + 1 ).<String>raw() );
                     }
                     else
                     {
                         l_skip = 0;
                         l_generator = EGenerator.MERSENNETWISTER;
                     }

                     // generate distribution object, arguments after distribution are the initialize parameter
                     return i.getValue()
                             .get(
                                 l_generator.get(),
                                 l_arguments.stream()
                                            .skip( i.getKey() + 1 + l_skip )
                                            .limit( i.getValue().getArgumentNumber() )
                                            .map( ITerm::<Number>raw )
                                            .mapToDouble( Number::doubleValue )
                                            .toArray()
                             );

                 } )
                 .map( CRawTerm::from )
                 .forEach( p_return::add );

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
         * enum name list
         */
        private static final Set<String> NAMES = Collections.unmodifiableSet(
                                                    Arrays.stream( EDistribution.values() )
                                                          .map( i -> i.name().toUpperCase( Locale.ROOT ) )
                                                          .collect( Collectors.toSet() )
        );

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
         * additional factory
         *
         * @param p_value string
         * @return enum
         */
        @Nonnull
        public static EDistribution from( @Nonnull final String p_value )
        {
            return EDistribution.valueOf( p_value.trim().toUpperCase( Locale.ROOT ) );
        }

        /**
         * checks if a name exists within the enum
         *
         * @param p_value string name
         * @return exist boolean
         */
        public static boolean exist( @Nonnull final String p_value )
        {
            return NAMES.contains( p_value.trim().toUpperCase( Locale.ROOT ) );
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
        @Nonnull
        public final AbstractRealDistribution get( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
        {
            switch ( this )
            {
                case BETA:
                    return new BetaDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case CAUCHY:
                    return new CauchyDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case CHISQUARE:
                    return new ChiSquaredDistribution( p_generator, p_arguments[0] );

                case EXPONENTIAL:
                    return new ExponentialDistribution( p_generator, p_arguments[0] );

                case F:
                    return new FDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case GAMMA:
                    return new GammaDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case GUMBLE:
                    return new GumbelDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case LAPLACE:
                    return new LaplaceDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case LEVY:
                    return new LevyDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case LOGISTIC:
                    return new LogisticDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case LOGNORMAL:
                    return new LogNormalDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case NAKAGAMI:
                    return new NakagamiDistribution(
                        p_generator, p_arguments[0], p_arguments[1],
                        NakagamiDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY
                    );

                case NORMAL:
                    return new NormalDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case PARETO:
                    return new ParetoDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case T:
                    return new TDistribution( p_generator, p_arguments[0] );

                case TRIANGULAR:
                    return new TriangularDistribution( p_generator, p_arguments[0], p_arguments[1], p_arguments[2] );

                case UNIFORM:
                    return new UniformRealDistribution( p_generator, p_arguments[0], p_arguments[1] );

                case WEIBULL:
                    return new WeibullDistribution( p_generator, p_arguments[0], p_arguments[1] );

                default:
                    throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "unknown", this ) );
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
        ISAAC,
        SYNCHRONIZEDISAAC,
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
         * additional factory
         *
         * @param p_value string
         * @return enum
         */
        @Nonnull
        public static EGenerator from( @Nonnull final String p_value )
        {
            return EGenerator.valueOf( p_value.trim().toUpperCase( Locale.ROOT ) );
        }

        /**
         * returns a number generator
         *
         * @return generator
         */
        @Nonnull
        public final RandomGenerator get()
        {
            switch ( this )
            {
                case MERSENNETWISTER:
                    return new MersenneTwister();

                case SYNCHRONIZEDMERSENNETWISTER:
                    return new SynchronizedRandomGenerator( new MersenneTwister() );

                case ISAAC:
                    return new ISAACRandom();

                case SYNCHRONIZEDISAAC:
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
                    throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "unknown", this ) );
            }
        }
    }
}
