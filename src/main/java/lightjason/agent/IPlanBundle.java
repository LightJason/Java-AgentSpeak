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
 * # along with this program. If not, see <http://www.gnu.org/licenses/>.               #
 * ######################################################################################
 * @endcond
 */

package lightjason.agent;

import com.google.common.collect.SetMultimap;
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;

import java.util.Map;
import java.util.Set;


/**
 * plan bundle interface
 */
public interface IPlanBundle
{

    /**
     * returns initial beliefs
     *
     * @return set with initial beliefs
     */
    Set<ILiteral> getInitialBeliefs();

    /**
     * get a multimap with event-plan matching
     *
     * @return multimap
     */
    SetMultimap<ITrigger<?>, IPlan> getPlans();

    /**
     * returns the rules / principles
     *
     * @return map with rules
     */
    Map<String, Object> getRules();

}
