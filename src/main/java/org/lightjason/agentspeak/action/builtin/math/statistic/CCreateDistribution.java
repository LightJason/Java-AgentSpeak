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

package org.lightjason.agentspeak.action.builtin.math.statistic;

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
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


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
 * {@code [D1|D2] = .math/statistic/createdistribution( "normal", 20, 10, ["beta", "isaac", [8, 12]] );}
 *
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
public final class CCreateDistribution extends IBuiltinAction
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
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        IntStream.range( 0, l_arguments.size() )
                 .filter( i -> CCommon.isssignableto( l_arguments.get( i ), String.class ) )
                 .mapToObj( i -> new AbstractMap.SimpleImmutableEntry<>( i, l_arguments.get( i ).<String>raw() ) )
                 .filter( i -> EDistribution.exist( i.getValue() ) )
                 .map( i -> new AbstractMap.SimpleImmutableEntry<>( i.getKey(), EDistribution.of( i.getValue() ) ) )
                 .map( i ->
                 {

                     // check if next argument to the distribution name a generator name
                     final int l_skip;
                     final EGenerator l_generator;

                     if ( i.getKey() < l_arguments.size() - 1 && CCommon.isssignableto( l_arguments.get( i.getKey() + 1 ), String.class ) )
                     {
                         l_skip = 1;
                         l_generator = EGenerator.of( l_arguments.get( i.getKey() + 1 ).raw() );
                     }
                     else
                     {
                         l_skip = 0;
                         l_generator = EGenerator.MERSENNETWISTER;
                     }

                     // generate distribution object, arguments after distribution are the initialize parameter
                     return i.getValue()
                             .apply(
                                 l_generator.get(),
                                 l_arguments.stream()
                                            .skip( i.getKey() + 1 + l_skip )
                                            .limit( i.getValue().getArgumentNumber() )
                                            .map( ITerm::<Number>raw )
                                            .mapToDouble( Number::doubleValue )
                                            .toArray()
                             );

                 } )
                 .map( CRawTerm::of )
                 .forEach( p_return::add );

        return Stream.of();
    }


    /**
     * usable distributions
     */
    private enum EDistribution implements BiFunction<RandomGenerator, double[], AbstractRealDistribution>
    {
        BETA( 2 )
        {

            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new BetaDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        CAUCHY( 2 )
        {

            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new CauchyDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        CHISQUARE( 1 )
        {

            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new ChiSquaredDistribution( p_generator, p_arguments[0] );
            }

        },
        EXPONENTIAL( 1 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new ExponentialDistribution( p_generator, p_arguments[0] );
            }

        },
        F( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new FDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        GAMMA( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new GammaDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        GUMBLE( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new GumbelDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        LAPLACE( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new LaplaceDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        LEVY( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new LevyDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        LOGISTIC( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new LogisticDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        LOGNORMAL( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new LogNormalDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        NAKAGAMI( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new NakagamiDistribution(
                        p_generator, p_arguments[0], p_arguments[1],
                        NakagamiDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY
                );
            }

        },
        NORMAL( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new NormalDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        PARETO( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new ParetoDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        T( 1 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new TDistribution( p_generator, p_arguments[0] );
            }

        },
        TRIANGULAR( 3 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new TriangularDistribution( p_generator, p_arguments[0], p_arguments[1], p_arguments[2] );
            }

        },
        UNIFORM( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new UniformRealDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        },
        WEIBULL( 2 )
        {
            @Nonnull
            @Override
            public AbstractRealDistribution apply( @Nonnull final RandomGenerator p_generator, final double[] p_arguments )
            {
                return new WeibullDistribution( p_generator, p_arguments[0], p_arguments[1] );
            }

        };

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
        public static EDistribution of( @Nonnull final String p_value )
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

    }

    /**
     * number generator
     */
    private enum EGenerator implements Supplier<RandomGenerator>
    {
        MERSENNETWISTER
        {
            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new MersenneTwister();
            }

        },
        SYNCHRONIZEDMERSENNETWISTER
        {
            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new SynchronizedRandomGenerator( new MersenneTwister() );
            }

        },
        ISAAC
        {
            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new ISAACRandom();
            }

        },
        SYNCHRONIZEDISAAC
        {
            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new SynchronizedRandomGenerator( new ISAACRandom() );
            }

        },
        INTERNAL
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new JDKRandomGenerator();
            }

        },
        SYNCHRONIZEDINTERNAL
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new SynchronizedRandomGenerator( new JDKRandomGenerator() );
            }

        },
        WELL512A
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new Well512a();
            }

        },
        SYNCHRONIZEDWELL512A
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new SynchronizedRandomGenerator( new Well512a() );
            }

        },
        WELL1024A
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new Well1024a();
            }

        },
        SYNCHRONIZEDWELL1024A
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new SynchronizedRandomGenerator( new Well1024a() );
            }

        },
        WELL19937A
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new Well19937a();
            }

        },
        SYNCHRONIZEDWELL19937A
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new SynchronizedRandomGenerator( new Well19937a() );
            }

        },
        WELL19937C
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new Well19937c();
            }

        },
        SYNCHRONIZEDWELL19937C
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new SynchronizedRandomGenerator( new Well19937c() );
            }

        },
        WELL4449A
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new Well44497a();
            }

        },
        SYNCHRONIZEDWELL4449A
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new SynchronizedRandomGenerator( new Well44497a() );
            }

        },
        WELL44497B
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new Well44497b();
            }

        },
        SYNCHRONIZEDWELL44497B
        {

            @Nonnull
            @Override
            public RandomGenerator get()
            {
                return new SynchronizedRandomGenerator( new Well44497b() );
            }

        };

        /**
         * additional factory
         *
         * @param p_value string
         * @return enum
         */
        @Nonnull
        public static EGenerator of( @Nonnull final String p_value )
        {
            return EGenerator.valueOf( p_value.trim().toUpperCase( Locale.ROOT ) );
        }
    }
}
