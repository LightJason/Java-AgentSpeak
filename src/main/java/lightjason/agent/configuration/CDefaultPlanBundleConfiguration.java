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
import lightjason.common.IPath;
import lightjason.language.ILiteral;
import lightjason.language.instantiable.plan.IPlan;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import lightjason.language.instantiable.rule.IRule;

import java.util.Collections;
import java.util.Set;


/**
 *
 */
public class CDefaultPlanBundleConfiguration implements IPlanBundleConfiguration
{
    /**
     * instance of plans
     */
    private final Multimap<ITrigger, IPlan> m_plans;
    /**
     * instance of rules
     */
    private final Multimap<IPath, IRule> m_rules;
    /**
     * instance of initial beliefs
     */
    private final Set<ILiteral> m_initialbeliefs;

    /**
     * ctor
     *
     * @param p_plans plans
     * @param p_rules rules
     * @param p_initalbeliefs initial beliefs
     */
    public CDefaultPlanBundleConfiguration( final Set<IPlan> p_plans, final Set<IRule> p_rules, final Set<ILiteral> p_initalbeliefs )
    {
        m_initialbeliefs = Collections.unmodifiableSet( p_initalbeliefs );

        final Multimap<ITrigger, IPlan> l_plans = HashMultimap.create();
        p_plans.stream().forEach( i -> l_plans.put( i.getTrigger(), i ) );
        m_plans = ImmutableMultimap.copyOf( l_plans );

        final Multimap<IPath, IRule> l_rules = HashMultimap.create();
        p_rules.stream().forEach( i -> l_rules.put( i.getIdentifier().getFQNFunctor(), i ) );
        m_rules = ImmutableMultimap.copyOf( l_rules );
    }

    @Override
    public final Set<ILiteral> getBeliefs()
    {
        return m_initialbeliefs;
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
