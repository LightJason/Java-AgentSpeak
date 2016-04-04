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
import lightjason.common.IPath;
import lightjason.language.ILiteral;
import lightjason.language.execution.action.unify.IUnifier;
import lightjason.language.execution.fuzzy.IFuzzyCollector;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.instantiable.plan.IPlan;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import lightjason.language.instantiable.rule.IRule;

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
     * @param p_trigger event trigger
     * @param p_immediately run element immediately
     * @return execution finished correctly
     */
    IFuzzyValue<Boolean> trigger( final ITrigger p_trigger, final boolean... p_immediately );

    /**
     * returns the beliefbase
     *
     * @return beliefbase
     */
    IView getBeliefBase();

    /**
     * returns a map of the current running plans
     *
     * @return map with running plans and the instance literal
     */
    Multimap<IPath, ILiteral> getRunningPlans();

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

    /**
     * returns the current cycle
     *
     * @return cycle
     */
    long getCycle();

    /**
     * returns the internal map of plans
     *
     * @return plan map
     */
    Multimap<ITrigger, IPlan> getPlans();

    /**
     * returns the internal map of rules
     */
    Multimap<IPath, IRule> getRules();

    /**
     * returns the fuzzy-collector object
     * to collect plan results
     *
     * @return collector object
     */
    IFuzzyCollector<Boolean> getResultCollector();

}
