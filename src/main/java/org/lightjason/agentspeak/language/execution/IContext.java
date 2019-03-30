/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.execution;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.execution.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;


/**
 * execution context with local data
 */
public interface IContext extends Serializable
{
    /**
     * empty context with plan
     */
    IContext EMPTYPLAN = new IContext()
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -1053856178724776159L;

        @Nonnull
        @Override
        public IAgent<?> agent()
        {
            return IAgent.EMPTY;
        }

        @Nonnull
        @Override
        public IInstantiable instance()
        {
            return IPlan.EMPTY;
        }

        @Nonnull
        @Override
        public Map<IPath, IVariable<?>> instancevariables()
        {
            return Collections.emptyMap();
        }

        @Nonnull
        @Override
        public IContext duplicate( @Nullable final IVariable<?>... p_variables )
        {
            return this;
        }

        @Nonnull
        @Override
        public IContext duplicate( @Nonnull final Stream<IVariable<?>> p_variables )
        {
            return this;
        }
    };

    /**
     * empty context with rule
     */
    IContext EMPTYRULE = new IContext()
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -1053856178757676139L;

        @Nonnull
        @Override
        public IAgent<?> agent()
        {
            return IAgent.EMPTY;
        }

        @Nonnull
        @Override
        public IInstantiable instance()
        {
            return IRule.EMPTY;
        }

        @Nonnull
        @Override
        public Map<IPath, IVariable<?>> instancevariables()
        {
            return Collections.emptyMap();
        }

        @Nonnull
        @Override
        public IContext duplicate( @Nullable final IVariable<?>... p_variables )
        {
            return this;
        }

        @Nonnull
        @Override
        public IContext duplicate( @Nonnull final Stream<IVariable<?>> p_variables )
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
     * @param p_variables replacing variables
     * @return shallow-copy of the context
     */
    @Nonnull
    IContext duplicate( @Nullable final IVariable<?>... p_variables );

    /**
     * duplicates the context with a shallow-copy
     *
     * @param p_variables stream with replacing variables
     * @return shallow-copy of the context
     */
    @Nonnull
    IContext duplicate( @Nonnull final Stream<IVariable<?>> p_variables );

}
