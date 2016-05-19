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

package lightjason.agentspeak.common.benchmark;

import com.codepoetics.protonpack.StreamUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedDescriptiveStatistics;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * class to store benchmarking content
 *
 * @see http://www.objc.io/issues/11-android/dependency-injection-in-java/
 */
public final class CSummary
{
    /**
     * singleton instance
     **/
    public static final CSummary INSTANCE = new CSummary();
    /**
     * statistic object
     **/
    private final Map<String, DescriptiveStatistics> m_result = new ConcurrentHashMap<>();

    /**
     * ctor
     */
    private CSummary()
    {
    }

    /**
     * sets the time value
     *
     * @param p_label full-qualified method name
     * @param p_time elapsed time value
     * @warning check explict of null value to avoid NPE on initialization call
     */
    public final void setTime( final String p_label, final long p_time )
    {
        m_result.putIfAbsent( p_label, new SynchronizedDescriptiveStatistics() ).addValue( p_time );
    }


    /**
     * returns the current data
     *
     * @return statistic map
     */
    public final Map<String, Map<String, Double>> get()
    {
        return Collections.unmodifiableMap(
            m_result.entrySet().parallelStream()
                    .map( i ->
                              new AbstractMap.SimpleImmutableEntry<>(
                                  i.getKey(),
                                  Collections.unmodifiableMap(
                                      StreamUtils.zip(

                                          Stream.of(
                                              "max",
                                              "min",
                                              "kurtosis",
                                              "arithmetic mean",
                                              "geometric mean",
                                              "50-percentile",
                                              "25-percentile",
                                              "75-percentile",
                                              "standard deviation",
                                              "skewness",
                                              "count",
                                              "sum",
                                              "sum square",
                                              "variance"
                                          ),

                                          Stream.of(
                                              i.getValue().getMax(),
                                              i.getValue().getMin(),
                                              i.getValue().getKurtosis(),
                                              i.getValue().getMean(),
                                              i.getValue().getGeometricMean(),
                                              i.getValue().getPercentile( 50 ),
                                              i.getValue().getPercentile( 25 ),
                                              i.getValue().getPercentile( 75 ),
                                              i.getValue().getStandardDeviation(),
                                              i.getValue().getSkewness(),
                                              i.getValue().getN(),
                                              i.getValue().getSum(),
                                              i.getValue().getSumsq(),
                                              i.getValue().getVariance()
                                          ),

                                          ( k, v ) -> new AbstractMap.SimpleImmutableEntry<>( k, v.doubleValue() )

                                      ).collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) )
                                  )
                              )
                    )
                    .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) )
        );
    }

}
