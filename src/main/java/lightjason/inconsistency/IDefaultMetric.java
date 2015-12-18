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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * default metric with an optional set of path values
 */
public abstract class IDefaultMetric<T> implements IMetric<T, CPath>
{
    /**
     * set with paths
     */
    protected final Set<CPath> m_paths = new HashSet<>();

    /**
     * ctor
     *
     * @param p_paths for reading agent value
     */
    public IDefaultMetric( final CPath... p_paths )
    {
        if ( p_paths != null )
            m_paths.addAll( Arrays.asList( p_paths ) );
    }

    /**
     * ctor
     *
     * @param p_paths collection of path
     */
    public IDefaultMetric( final Collection<CPath> p_paths )
    {
        if ( p_paths != null )
            m_paths.addAll( p_paths );
    }

    /**
     * copy-ctor
     *
     * @param p_metric default metric
     */
    public IDefaultMetric( final IDefaultMetric<T> p_metric )
    {
        m_paths.addAll( p_metric.m_paths );
    }

    @Override
    public abstract double calculate( final T p_first, final T p_second );

    @Override
    public final Collection<CPath> getSelector()
    {
        return m_paths;
    }
}
