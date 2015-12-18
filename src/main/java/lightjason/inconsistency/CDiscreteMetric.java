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


import lightjason.common.CPath;

import java.util.Collection;


/**
 * generic discrete metric
 *
 * @see http://mathworld.wolfram.com/DiscreteMetric.html
 */
public final class CDiscreteMetric<T> extends IDefaultMetric<T>
{

    /**
     * ctor
     *
     * @param p_paths path list
     */
    public CDiscreteMetric( final CPath... p_paths )
    {
        super( p_paths );
    }

    /**
     * copy-ctor
     *
     * @param p_metric metric
     */
    public CDiscreteMetric( final IDefaultMetric<T> p_metric )
    {
        super( p_metric );
    }

    /**
     * ctor
     *
     * @param p_paths collection of path
     */
    public CDiscreteMetric( final Collection<CPath> p_paths )
    {
        super( p_paths );
    }

    @Override
    public double calculate( final T p_first, final T p_second )
    {
        // equal objects create zero value
        if ( p_first.equals( p_second ) )
            return 0;

        return 1;
    }

}
