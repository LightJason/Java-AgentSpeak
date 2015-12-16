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
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;

import java.util.Map;


/**
 * default agent configuration
 */
public class CDefaultConfiguration implements IConfiguration
{
    /**
     * initial goal
     */
    final ILiteral m_initialgoal;
    /**
     * instance of agent plans
     */
    private final SetMultimap<ITrigger<?>, IPlan> m_plans;


    /**
     * ctor
     *
     * @param p_plans plans
     * @param p_initialgoal initial goal
     * @todo plans must be cloned
     * @todo beliefbase must be cloned
     */
    public CDefaultConfiguration( final SetMultimap<ITrigger<?>, IPlan> p_plans, final ILiteral p_initialgoal )
    {
        m_plans = p_plans;
        m_initialgoal = p_initialgoal;
    }

    @Override
    public IBeliefBaseMask getBeliefbase()
    {
        return null;
    }

    @Override
    public ILiteral getInitialGoal()
    {
        return m_initialgoal;
    }

    @Override
    public SetMultimap<ITrigger<?>, IPlan> getPlans()
    {
        return m_plans;
    }

    @Override
    public Map<String, Object> getRules()
    {
        return null;
    }

}
