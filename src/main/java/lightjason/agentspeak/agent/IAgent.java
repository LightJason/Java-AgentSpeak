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

package lightjason.agentspeak.agent;

import com.google.common.collect.Multimap;
import lightjason.agentspeak.agent.fuzzy.IFuzzy;
import lightjason.agentspeak.beliefbase.IView;
import lightjason.agentspeak.common.IPath;
import lightjason.agentspeak.language.ILiteral;
import lightjason.agentspeak.language.ITerm;
import lightjason.agentspeak.language.execution.IVariableBuilder;
import lightjason.agentspeak.language.execution.action.unify.IUnifier;
import lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import lightjason.agentspeak.language.instantiable.plan.IPlan;
import lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import lightjason.agentspeak.language.instantiable.rule.IRule;
import lightjason.agentspeak.language.score.IAggregation;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;


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
     *
     * @note the trigger is ignored iif the agent is sleeping / hibernating
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
     * returns hibernate / sleeping state
     *
     * @return sleeping flag
     */
    boolean isSleeping();

    /**
     * pushs the agent into hibernating / sleeping state
     *
     * @return agent reference
     */
    IAgent sleep();

    /**
     * wake-up the agent by generating wakeup-goal
     *
     * @param p_value any term value which will push into the wake-up plan
     * @return agent reference
     */
    IAgent wakeup( final ITerm... p_value );

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
    Multimap<ITrigger, MutableTriple<IPlan, AtomicLong, AtomicLong>> getPlans();

    /**
     * return fuzzy operator
     *
     * @return operator
     */
    IFuzzy<Boolean> getFuzzy();

    /**
     * returns the aggregation function
     *
     * @return aggregation function
     */
    IAggregation getAggregation();

    /**
     * returns the variable builder function
     *
     * @return variable builder function
     */
    IVariableBuilder getVariableBuilder();

    /**
     * returns amultimap with literal-rule functor
     * and rle objects
     *
     * @return multimap
     */
    Multimap<IPath, IRule> getRules();

}
