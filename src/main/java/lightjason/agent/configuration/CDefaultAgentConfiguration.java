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

package lightjason.agent.configuration;

import com.google.common.collect.SetMultimap;
import lightjason.beliefbase.CBeliefBase;
import lightjason.beliefbase.CBeliefStorage;
import lightjason.beliefbase.IMask;
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;
import lightjason.language.score.IAggregation;

import java.util.Collection;
import java.util.Map;


/**
 * default agent configuration
 */
public class CDefaultAgentConfiguration implements IAgentConfiguration
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
     * instance of initial beliefs
     */
    private final Collection<ILiteral> m_initialbeliefs;
    /**
     * instance of the aggregate function
     */
    private final IAggregation m_aggregation;


    /**
     * ctor
     *
     * @param p_aggregation aggregation function
     * @param p_plans plans
     * @param p_initalbeliefs set with initial beliefs
     * @param p_initialgoal initial goal
     */
    public CDefaultAgentConfiguration( final IAggregation p_aggregation, final SetMultimap<ITrigger<?>, IPlan> p_plans,
                                       final Collection<ILiteral> p_initalbeliefs, final ILiteral p_initialgoal
    )
    {
        m_plans = p_plans;
        m_aggregation = p_aggregation;
        m_initialgoal = p_initialgoal;
        m_initialbeliefs = p_initalbeliefs;
    }

    @Override
    public IMask getBeliefbase()
    {
        final IMask l_beliefbase = new CBeliefBase( new CBeliefStorage<>() ).create( "root" );
        m_initialbeliefs.parallelStream().forEach( i -> l_beliefbase.add( i.clone() ) );
        return l_beliefbase;
    }

    @Override
    public ILiteral getInitialGoal()
    {
        return m_initialgoal;
    }

    @Override
    public IAggregation getAggregate()
    {
        return m_aggregation;
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
