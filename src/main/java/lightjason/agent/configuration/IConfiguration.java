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

package lightjason.agent.configuration;

import com.google.common.collect.SetMultimap;
import lightjason.beliefbase.IBeliefBaseMask;
import lightjason.common.CPath;
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;

import java.util.Map;


/**
 * interface to define the agent configuration
 */
public interface IConfiguration
{

    /**
     * returns a beliefbase of the agent
     */
    public IBeliefBaseMask getBeliefbase();

    /**
     * returns the initial goal
     */
    public ILiteral getInitialGoal();

    /**
     * get a multimap with event-plan matching
     *
     * @return multimap
     */
    public SetMultimap<ITrigger<?>, IPlan> getPlans();

    /**
     * returns the rules / principles
     *
     * @return map with rules
     */
    public Map<String, Object> getRules();

    /**
     * returns an unique name of the agent
     *
     * @return path with agent name
     */
    public CPath getName();

}
