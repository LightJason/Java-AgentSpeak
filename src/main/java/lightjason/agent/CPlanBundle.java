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

package lightjason.agent;

import com.google.common.collect.Multimap;
import lightjason.agent.configuration.IPlanBundleConfiguration;
import lightjason.language.ILiteral;
import lightjason.language.instantiable.plan.IPlan;
import lightjason.language.instantiable.plan.trigger.ITrigger;

import java.util.Map;
import java.util.Set;


/**
 * plan bundle class
 */
public class CPlanBundle implements IPlanBundle
{
    /**
     * thread-safe map with all existing plans
     *
     * @note plan list must be a linked-hashset
     * to store the execution order of the plans
     */
    protected final Multimap<ITrigger, IPlan> m_plans;


    public CPlanBundle( final IPlanBundleConfiguration p_configuration )
    {
        m_plans = p_configuration.getPlans();
    }

    @Override
    public final Set<ILiteral> getInitialBeliefs()
    {
        return null;
    }

    @Override
    public final Multimap<ITrigger, IPlan> getPlans()
    {
        return m_plans;
    }

    @Override
    public final Map<String, Object> getRules()
    {
        return null;
    }
}
