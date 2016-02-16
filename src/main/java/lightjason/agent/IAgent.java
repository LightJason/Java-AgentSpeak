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
import lightjason.beliefbase.IView;
import lightjason.language.ILiteral;
import lightjason.language.execution.IUnifier;
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
     * inspector method
     *
     * @param p_inspector inspector object
     */
    void inspect( final IInspector... p_inspector );

    /**
     * trigger an event
     *
     * @param p_event event
     */
    void trigger( final ITrigger<?> p_event );

    /**
     * returns the beliefbase
     *
     * @return beliefbase
     */
    IView getBeliefBase();

    /**
     * returns a map of the current running plans
     *
     * @return map with running plans
     */
    Multimap<ILiteral, IPlan> getRunningPlans();

    /**
     * returns the hibernate state
     *
     * @return boolean state value
     */
    boolean hibernate();

    /**
     * returns and sets the hibernate state
     *
     * @param p_value new hibernate state
     * @return boolean new state value
     */
    boolean hibernate( final boolean p_value );

    /**
     * storage access
     *
     * @return storage map
     */
    Map<String, ?> getStorage();

    /**
     * returns an unifier
     *
     * @return unification algorithm
     */
    IUnifier getUnifier();

    /**
     * returns the time in nano seconds
     * since the last cycle
     *
     * @return nano seconds
     */
    long getLastCycleTime();

}
