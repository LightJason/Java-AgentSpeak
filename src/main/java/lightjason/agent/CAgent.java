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

import lightjason.agent.score.IAgentPlanScore;
import lightjason.beliefbase.IBeliefBaseMask;
import lightjason.common.CPath;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * agent class
 */
public class CAgent implements IAgent
{
    /**
     * agent name
     */
    protected final CPath m_name;
    /**
     * thread-safe map with all existing plans
     *
     * @note plan list must be a linked-hashset
     * to store the execution order of the plans
     */
    protected final Map<String, Set<IPlan>> m_plans = new ConcurrentHashMap<>();
    /**
     * beliefbase
     */
    protected final IBeliefBaseMask m_beliefbase;
    /**
     * curent agent cycle
     */
    protected int m_cycle = 0;
    /**
     * score sum of the actions
     */
    protected final IAgentPlanScore m_score;
    /**
     * suspending state
     */
    private volatile boolean m_suspend = false;


    /**
     * ctor
     *
     * @param p_configuration agent configuration
     * @bug remove test plan execution
     * @bug score function not working
     */
    public CAgent( final IAgentConfiguration p_configuration ) throws IOException
    {
        // initialize agent
        m_beliefbase = p_configuration.getBeliefbase();
        m_name = p_configuration.getName();
        m_score = null;

        //System.out.println( p_astvisitor.getInitialGoal() );
        //System.out.println( p_astvisitor.getInitialBeliefs() );
        //System.out.println( p_astvisitor.getPlans() );

        p_configuration.getPlans().values().stream().forEach( i -> {
            System.out.println( i );
            System.out.println( i.execute( null, null ) );
        } );

    }

    @Override
    public final long getCycle()
    {
        return m_cycle;
    }

    @Override
    public final CPath getName()
    {
        return m_name;
    }

    @Override
    public IBeliefBaseMask getBeliefBase()
    {
        return m_beliefbase;
    }

    @Override
    public void trigger( final ITrigger<?> p_event )
    {

    }

    @Override
    public final synchronized void suspend()
    {
        m_suspend = true;
    }

    @Override
    public final boolean isSuspending()
    {
        return m_suspend;
    }

    @Override
    public final synchronized void resume()
    {
        m_suspend = false;
    }

    @Override
    public Set<IPlan> getCurrentPlans()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0} ( Cycle: {1} / Beliefbase: {2} / Plans: {3} )", m_name, m_cycle, m_beliefbase, m_plans );
    }

    @Override
    public IAgent call() throws Exception
    {
        // run beliefbase update, because environment can be changed
        m_beliefbase.update();
        if ( m_suspend )
            return this;

        // increment cycle
        m_cycle++;

        return this;
    }

}
