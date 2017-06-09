/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.agent;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.statistic.CPlanStatistic;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * adds a plan to the plan-base.
 * The actions adds all arguments which
 * are plans to the plan-base of the current
 * agent, the action never fails
 *
 * @code agent/addplan( Plan1, Plan2, [Plan3, [Plan4]] ); @endcode
 */
public final class CAddPlan extends IBuildinAction
{
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( @Nonnull final IContext p_context, final boolean p_parallel,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        CCommon.flatcollection( p_argument )
               .parallel()
               .map( ITerm::<IPlan>raw )
               .map( CPlanStatistic::from )
               .forEach( i -> p_context.agent().plans().put( i.plan().trigger(), i ) );

        return CFuzzyValue.from( true );
    }
}
