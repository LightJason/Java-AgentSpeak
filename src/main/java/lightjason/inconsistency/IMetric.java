/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.inconsistency;


import java.util.Collection;


/**
 * metric interface of the inconsistency structure
 *
 * @see http://en.wikipedia.org/wiki/Metric_space
 */
public interface IMetric<T, N>
{

    /**
     * calculates the metric value between two objects
     *
     * @param p_first first object
     * @param p_second second object
     * @return double metric
     */
    double calculate( final T p_first, final T p_second );


    /**
     * returns the selectors
     *
     * @return selector
     *
     * @tparam N returns a collection of belief selectors
     */
    Collection<N> getSelector();

}
