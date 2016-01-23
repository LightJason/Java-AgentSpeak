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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import lightjason.agent.configuration.IAgentConfiguration;
import lightjason.beliefbase.IMask;
import lightjason.language.ILiteral;
import lightjason.language.execution.CContext;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;
import lightjason.language.score.IAggregation;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * agent class
 *
 * @bug remove test plan execution
 * @bug inspector call incomplete
 */
public class CAgent implements IAgent
{
    /**
     * map with all existing plans
     */
    protected final SetMultimap<ITrigger<?>, IPlan> m_plans;
    /**
     * running plans (thread-safe)
     */
    protected final SetMultimap<ILiteral, IPlan> m_runningplans = HashMultimap.create();
    /**
     * storage map
     *
     * @note must be thread-safe
     */
    protected final Map<String, ?> m_storage = new ConcurrentHashMap<>();
    /**
     * beliefbase
     */
    protected final IMask m_beliefbase;
    /**
     * execution goal list
     *
     * @todo incompelete usage
     */
    protected final Set<ILiteral> m_goals = Collections.newSetFromMap( new ConcurrentHashMap<>() );
    /**
     * curent agent cycle
     */
    protected long m_cycle;
    /**
     * aggregation function
     */
    protected final IAggregation m_aggregation;
    /**
     * hibernate state
     */
    private volatile boolean m_hibernate;


    /**
     * ctor
     *
     * @param p_configuration agent configuration
     */
    public CAgent( final IAgentConfiguration p_configuration )
    {
        // initialize agent
        m_beliefbase = p_configuration.getBeliefbase();
        m_plans = p_configuration.getPlans();
        m_aggregation = p_configuration.getAggregate();

        if ( p_configuration.getInitialGoal() != null )
            m_goals.add( p_configuration.getInitialGoal() );
    }

    @Override
    public final IMask getBeliefBase()
    {
        return m_beliefbase;
    }

    @Override
    public void inspect( final IInspector... p_inspector )
    {
        if ( p_inspector == null )
            return;

        final Multimap<IInspector.EValue, Object> l_map = new ImmutableSetMultimap.Builder<IInspector.EValue, Object>()
                .put( IInspector.EValue.CYCLE, m_cycle )
                .put( IInspector.EValue.HIBERNATE, m_hibernate )
                .putAll( IInspector.EValue.STORAGE, m_storage.entrySet().stream().collect( Collectors.toList() ).iterator() )
                .putAll( IInspector.EValue.RUNNINGPLAN, m_storage.entrySet().stream().collect( Collectors.toList() ).iterator() )
                .build();

        Arrays.stream( p_inspector ).forEach( i -> i.inspect( ImmutableMultimap.copyOf( l_map ) ) );
    }

    @Override
    public void trigger( final ITrigger<?> p_event )
    {

    }

    @Override
    public final SetMultimap<ILiteral, IPlan> getRunningPlans()
    {
        return m_runningplans;
    }

    @Override
    public final boolean hibernate()
    {
        return m_hibernate;
    }

    @Override
    public final boolean hibernate( final boolean p_value )
    {
        m_hibernate = p_value;
        return p_value;
    }

    @Override
    public final Map<String, ?> getStorage()
    {
        return m_storage;
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} ( Cycle: {1} / Beliefbase: {2} )", super.toString(), m_cycle, m_beliefbase );
    }

    @Override
    public IAgent call() throws Exception
    {
        // run beliefbase update, because environment can be changed
        m_beliefbase.update( this );
        if ( m_hibernate )
            // check wakup-event otherwise suspend
            return this;

        // collect belief events
        // collect plan/goal events
        // create execution list


        System.out.println( "=====>> " + this );

        m_plans.values().stream().forEach( i -> {

            System.out.println( "=====>> " + i + " ===\n" );
            System.out.println( "Score " + i.score( m_aggregation, this ) + "\n" );
            System.out.println(
                    "\n--> "
                    + i.execute(
                            new CContext<>( this, i, i.getVariables(),
                                            Collections.unmodifiableMap( new HashMap<>() )
                            ), null, null, null, null
                    )
                    + " <--\n" );
            System.out.println( "===================================================================" );

        } );

        System.out.println( "=====>> " + this );

        // increment cycle
        m_cycle++;

        return this;
    }

}
