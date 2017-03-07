/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.util.List;


/**
 * removes a plan by the plan trigger.
 * The action moves all plan based on the
 * input trigger arguments, the action fails
 * on wrong input
 *
 * @code agent/removeplan( "+!", "myplan(X)", "-!", Literal ); @endcode
 */
public final class CRemovePlan extends IBuildinAction
{
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                         final List<ITerm> p_annotation
    )
    {
        return CFuzzyValue.from(
            StreamUtils.windowed(
                CCommon.flatcollection( p_argument ),
                2
            ).allMatch( i -> CRemovePlan.remove( ITrigger.EType.from( i.get( 0 ).<String>raw() ), i.get( 1 ), p_context.agent() ) )
        );
    }

    /**
     * removes the plan based on the trigger
     *
     * @param p_trigger trigger type
     * @param p_literal literal as string or literal object
     * @param p_agent agent
     * @return flag to remove plan successfully
     */
    private static boolean remove( final ITrigger.EType p_trigger, final ITerm p_literal, final IAgent<?> p_agent )
    {
        final ILiteral l_literal;
        try
        {

            l_literal = CCommon.rawvalueAssignableTo( p_literal, ILiteral.class )
                        ? p_literal.<ILiteral>raw()
                        : CLiteral.parse( p_literal.<String>raw() );

        }
        catch ( final Exception l_exception )
        {
            return false;
        }


        final ITrigger l_trigger = CTrigger.from( p_trigger, l_literal );
        if ( !p_agent.plans().containsKey( l_literal ) )
            return false;

        p_agent.plans().removeAll( l_trigger );
        return true;
    }
}
