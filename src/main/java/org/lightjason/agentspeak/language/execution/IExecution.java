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

import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;


/**
 * internal execution interface
 */
public interface IExecution extends Serializable
{

    /**
     * defines a plan-body operation
     *
     * @param p_parallel parallel execution
     * @param p_context current execution context
     * @param p_argument parameter of the action
     * @param p_return return values
     * @return fuzzy boolean
     */
    @Nonnull
    IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                  @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    );

    /**
     * returns a stream with all used variables
     *
     * @return variable stream (variables will be cloned on instantiation)
     */
    @Nonnull
    Stream<IVariable<?>> variables();

    /**
     * check if the expression contains only bounded variables
     *
     * @param p_context execution context
     * @return only bounded variables exists
     *
    boolean boundvariables( @Nonnull final IContext p_context );
    */
}
