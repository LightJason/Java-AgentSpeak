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

package org.lightjason.agentspeak.language.execution;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;


/**
 * execution context with local data
 */
public interface IContext
{
    /**
     * empty context with plan
     */
    IContext EMPTYPLAN = new IContext()
    {
        @Nonnull
        @Override
        public final IAgent<?> agent()
        {
            return IAgent.EMPTY;
        }

        @Nonnull
        @Override
        public final IInstantiable instance()
        {
            return IPlan.EMPTY;
        }

        @Nonnull
        @Override
        public final Map<IPath, IVariable<?>> instancevariables()
        {
            return Collections.emptyMap();
        }

        @Nonnull
        @Override
        public final IContext duplicate()
        {
            return this;
        }
    };

    /**
     * empty context with rule
     */
    IContext EMPTYRULE = new IContext()
    {
        @Nonnull
        @Override
        public final IAgent<?> agent()
        {
            return IAgent.EMPTY;
        }

        @Nonnull
        @Override
        public final IInstantiable instance()
        {
            return IRule.EMPTY;
        }

        @Nonnull
        @Override
        public final Map<IPath, IVariable<?>> instancevariables()
        {
            return Collections.emptyMap();
        }

        @Nonnull
        @Override
        public final IContext duplicate()
        {
            return this;
        }
    };


    /**
     * returns the agent of the context
     *
     * @return agent
     */
    @Nonnull
    IAgent<?> agent();

    /**
     * returns the instance object
     *
     * @return instance object plan or rule
     */
    @Nonnull
    IInstantiable instance();

    /**
     * returns the variables names and their current value
     *
     * @return variable names and their current value
     */
    @Nonnull
    Map<IPath, IVariable<?>> instancevariables();

    /**
     * duplicates the context with a shallow-copy
     *
     * @return shallow-copy of the context
     */
    @Nonnull
    IContext duplicate();

}
