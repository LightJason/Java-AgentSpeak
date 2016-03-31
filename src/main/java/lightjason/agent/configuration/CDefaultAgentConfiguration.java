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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lightjason.beliefbase.CBeliefBase;
import lightjason.beliefbase.CStorage;
import lightjason.beliefbase.IView;
import lightjason.common.IPath;
import lightjason.language.ILiteral;
import lightjason.language.execution.IVariableBuilder;
import lightjason.language.execution.action.unify.IUnifier;
import lightjason.language.instantiable.plan.IPlan;
import lightjason.language.instantiable.plan.trigger.CTrigger;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import lightjason.language.instantiable.rule.IRule;
import lightjason.language.score.IAggregation;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;


/**
 * default agent configuration
 */
public class CDefaultAgentConfiguration implements IAgentConfiguration
{
    /**
     * name of the root beliefbase
     */
    protected static final String BELIEFBASEROTTNAME = "beliefbase";
    /**
     * unifier instance
     */
    protected final IUnifier m_unifier;
    /**
     * initial goal trigger
     */
    protected final ITrigger m_initialgoal;
    /**
     * instance of the aggregate function
     */
    protected final IAggregation m_aggregation;
    /**
     * instance of agent plans
     */
    protected final Multimap<ITrigger, IPlan> m_plans;
    /**
     * instance of agent rules
     */
    protected final Multimap<IPath, IRule> m_rules;
    /**
     * instance of variable builder
     *
     * @warning can be set to null
     */
    protected final IVariableBuilder m_variablebuilder;
    /**
     * instance of initial beliefs
     */
    protected final Collection<ILiteral> m_initialbeliefs;



    /**
     * ctor
     *
     * @param p_initalbeliefs set with initial beliefs
     * @param p_plans plans
     * @param p_rules rules
     * @param p_initialgoal initial goal
     * @param p_unifier unifier component
     * @param p_aggregation aggregation function
     */
    public CDefaultAgentConfiguration( final Collection<ILiteral> p_initalbeliefs, final Set<IPlan> p_plans, final Set<IRule> p_rules,
                                       final ILiteral p_initialgoal, final IUnifier p_unifier, final IAggregation p_aggregation
    )
    {
        this( p_initalbeliefs, p_plans, p_rules, p_initialgoal, p_unifier, p_aggregation, null );
    }

    /**
     * ctor
     *
     * @param p_initalbeliefs set with initial beliefs
     * @param p_plans plans
     * @param p_rules rules
     * @param p_initialgoal initial goal
     * @param p_aggregation aggregation function
     * @param p_unifier unifier component
     * @param p_variablebuilder variable builder
     */
    public CDefaultAgentConfiguration( final Collection<ILiteral> p_initalbeliefs, final Set<IPlan> p_plans, final Set<IRule> p_rules,
                                       final ILiteral p_initialgoal, final IUnifier p_unifier, final IAggregation p_aggregation,
                                       final IVariableBuilder p_variablebuilder
    )
    {
        m_unifier = p_unifier;
        m_aggregation = p_aggregation;
        m_variablebuilder = p_variablebuilder;
        m_initialbeliefs = Collections.unmodifiableCollection( p_initalbeliefs );

        final Multimap<ITrigger, IPlan> l_plans = HashMultimap.create();
        p_plans.stream().forEach( i -> l_plans.put( i.getTrigger(), i ) );
        m_plans = ImmutableMultimap.copyOf( l_plans );

        final Multimap<IPath, IRule> l_rules = HashMultimap.create();
        p_rules.stream().forEach( i -> l_rules.put( i.getIdentifier().getFQNFunctor(), i ) );
        m_rules = ImmutableMultimap.copyOf( l_rules );

        m_initialgoal = p_initialgoal != null ? CTrigger.from( ITrigger.EType.ADDGOAL, p_initialgoal ) : null;
    }

    @Override
    public final IView getBeliefbase()
    {
        final IView l_beliefbase = new CBeliefBase( new CStorage<>() ).create( BELIEFBASEROTTNAME );
        m_initialbeliefs.parallelStream().forEach( i -> l_beliefbase.add( i.shallowcopy() ) );

        // clear all events of the initial beliefs
        l_beliefbase.getTrigger();

        return l_beliefbase;
    }

    @Override
    public final ITrigger getInitialGoal()
    {
        return m_initialgoal;
    }

    @Override
    public final IAggregation getAggregate()
    {
        return m_aggregation;
    }

    @Override
    public final IUnifier getUnifier()
    {
        return m_unifier;
    }

    @Override
    public final IVariableBuilder getVariableBuilder()
    {
        return m_variablebuilder;
    }

    @Override
    public final Multimap<ITrigger, IPlan> getPlans()
    {
        return m_plans;
    }

    @Override
    public final Multimap<IPath, IRule> getRules()
    {
        return m_rules;
    }

}
