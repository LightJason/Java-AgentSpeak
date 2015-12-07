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


/**
 * default agent configuration
 */
public class CAgentConfiguration implements IAgentConfiguration
{
    /**
     * name of the agent
     */
    private final CPath m_name;
    /**
     * instance of agent plans
     */
    private final SetMultimap<ITrigger<?>, IPlan> m_plans;

    /**
     * ctor
     *
     * @param p_name name
     * @param p_plans plans
     * @todo plans must be cloned
     * @todo beliefbase must be cloned
     */
    public CAgentConfiguration( final CPath p_name, final SetMultimap<ITrigger<?>, IPlan> p_plans )
    {
        m_name = p_name;
        m_plans = p_plans;
    }

    @Override
    public IBeliefBaseMask getBeliefbase()
    {
        return null;
    }

    @Override
    public ILiteral getInitialGoal()
    {
        return null;
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

    @Override
    public CPath getName()
    {
        return m_name;
    }

}
