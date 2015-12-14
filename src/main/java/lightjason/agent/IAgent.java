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

package lightjason.agent;

import com.google.common.collect.SetMultimap;
import lightjason.beliefbase.IBeliefBaseMask;
import lightjason.common.CPath;
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;

import java.util.Map;
import java.util.concurrent.Callable;


/**
 * agent interface
 */
public interface IAgent extends Callable<IAgent>
{
    /**
     * returns the current cycle
     *
     * @return cycle number
     */
    public long getCycle();

    /**
     * returns the agent name
     *
     * @return agent name
     */
    public CPath getName();

    /**
     * returns the beliefbase
     *
     * @return beliefbase
     */
    public IBeliefBaseMask getBeliefBase();

    /**
     * trigger an event
     *
     * @param p_event event
     */
    public void trigger( final ITrigger<?> p_event );

    /**
     * sets the agent to a suspend state
     *
     * @note only the beliefbase update is called
     * but the agent cycle is not run, but before
     * the suspeding state is reached the plan "+!sleep"
     * is called
     */
    public void suspend();

    /**
     * returns a boolean if the agent is suspending
     *
     * @return boolean for suspending
     */
    public boolean isSuspending();

    /**
     * wakes-up the agent from the suspend state
     *
     * @note the plan "+!wakeup" will be triggered
     */
    public void resume();

    /**
     * returns a map of the current running plans
     *
     * @return map with running plans
     */
    public SetMultimap<ILiteral, IPlan> getRunningPlans();

    /**
     * returns a map of all plans
     *
     * @return map with plans
     */
    public SetMultimap<ITrigger<?>, IPlan> getPlans();

    /**
     * storage access
     *
     * @return storage map
     */
    public Map<String, ?> getStorage();

}
