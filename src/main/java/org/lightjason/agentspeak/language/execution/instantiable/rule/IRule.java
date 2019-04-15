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

package org.lightjason.agentspeak.language.execution.instantiable.rule;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;


/**
 * interface of logical rule
 */
public interface IRule extends IInstantiable
{
    /**
     * empty rule
     **/
    IRule EMPTY = new IRule()
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 6850403064097706468L;

        @Nonnull
        @Override
        public ILiteral identifier()
        {
            return ILiteral.EMPTY;
        }

        @Override
        public ILiteral literal()
        {
            return ILiteral.EMPTY;
        }

        @Nonnull
        @Override
        public IContext instantiate( @Nonnull final IAgent<?> p_agent, @Nonnull final Stream<IVariable<?>> p_variable )
        {
            return IContext.EMPTYRULE;
        }

        @Nonnull
        @Override
        public String description()
        {
            return "";
        }

        @Nonnull
        @Override
        public Stream<String> tags()
        {
            return Stream.empty();
        }

        @Nonnull
        @Override
        public Stream<IVariable<?>> variabledescription()
        {
            return Stream.empty();
        }

        @Nonnull
        @Override
        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
        {
            return Stream.empty();
        }

        @Nonnull
        @Override
        public Stream<IVariable<?>> variables()
        {
            return Stream.empty();
        }
    };


    /**
     * returns the identifier of the rule
     *
     * @return literal
     */
    @Nonnull
    ILiteral identifier();

}
