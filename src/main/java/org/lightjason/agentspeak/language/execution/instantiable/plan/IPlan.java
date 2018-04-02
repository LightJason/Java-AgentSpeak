/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.instantiable.plan;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;


/**
 * interface of plan
 */
public interface IPlan extends IInstantiable
{
    /**
     * empty plan
     */
    IPlan EMPTY = new IPlan()
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -8137749419571919583L;

        @Nonnull
        @Override
        public final ITrigger trigger()
        {
            return ITrigger.EMPTY;
        }

        @Nonnull
        @Override
        public final IFuzzyValue<Boolean> condition( final IContext p_context )
        {
            return CFuzzyValue.of( true );
        }

        @Nonnull
        @Override
        public final IContext instantiate( @Nonnull final IAgent<?> p_agent, @Nonnull final Stream<IVariable<?>> p_variable )
        {
            return IContext.EMPTYPLAN;
        }

        @Nonnull
        @Override
        public final String description()
        {
            return "";
        }

        @Nonnull
        @Override
        public final Stream<String> tags()
        {
            return Stream.empty();
        }

        @Nonnull
        @Override
        public final Stream<IVariable<?>> variabledescription()
        {
            return Stream.empty();
        }

        @Nonnull
        @Override
        public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                   @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
        {
            return CFuzzyValue.of( true );
        }

        @Nonnull
        @Override
        public final Stream<IVariable<?>> variables()
        {
            return Stream.empty();
        }
    };

    /**
     * returns the trigger event
     *
     * @return trigger event
     */
    @Nonnull
    ITrigger trigger();

    /**
     * execute the plan condition
     *
     * @param p_context execution context
     * @return execution result
     */
    @Nonnull
    IFuzzyValue<Boolean> condition( final IContext p_context );

}
