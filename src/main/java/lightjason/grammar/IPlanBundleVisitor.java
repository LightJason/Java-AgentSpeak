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

package lightjason.grammar;

import com.google.common.collect.SetMultimap;
import lightjason.agent.event.IEvent;
import lightjason.agent.plan.IPlan;
import lightjason.language.ILiteral;

import java.util.Map;
import java.util.Set;


/**
 * visitor interface of the abstract-syntax-tree (AST) for an plan-bundle
 */
public interface IPlanBundleVisitor extends lightjason.grammar.PlanBundleVisitor<Object>
{

    /**
     * returns the beliefs
     *
     * @return set with beliefs
     */
    public Set<ILiteral> getBeliefs();

    /**
     * get a multimap with event-plan matching
     *
     * @return multimap
     */
    public SetMultimap<IEvent<?>, IPlan> getPlans();

    /**
     * returns the rules / principles
     *
     * @return map with rules
     */
    public Map<String, Object> getRules();

}
