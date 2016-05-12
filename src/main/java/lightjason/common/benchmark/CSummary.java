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

package lightjason.common.benchmark;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedDescriptiveStatistics;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


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
     */
    @SuppressWarnings( {"serial", "unchecked"} )
    public final Map<String, Map<String, Double>> get()
    {
        return Collections.unmodifiableMap(
                m_result.entrySet().parallelStream()
                        .map( i ->
                                      new AbstractMap.SimpleImmutableEntry<>(
                                              i.getKey(),
                                              Collections.unmodifiableMap( new HashMap<String, Double>()
                                              {{
                                                  put( "max", i.getValue().getMax() );
                                                  put( "min", i.getValue().getMin() );
                                                  put( "kurtosis", i.getValue().getKurtosis() );
                                                  put( "arithmetic mean", i.getValue().getMean() );
                                                  put( "geometric mean", i.getValue().getGeometricMean() );
                                                  put( "50-percentile", i.getValue().getPercentile( 50 ) );
                                                  put( "25-percentile", i.getValue().getPercentile( 25 ) );
                                                  put( "75-percentile", i.getValue().getPercentile( 75 ) );
                                                  put( "standard deviation", i.getValue().getStandardDeviation() );
                                                  put( "skewness", i.getValue().getSkewness() );
                                                  put( "count", (double) i.getValue().getN() );
                                                  put( "sum", i.getValue().getSum() );
                                                  put( "sum square", i.getValue().getSumsq() );
                                                  put( "variance", i.getValue().getVariance() );
                                              }} )
                                      )
                        )
                        .collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) )
        );
    }

}
