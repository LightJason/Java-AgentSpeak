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

package org.lightjason.agentspeak.agent;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.generator.IAgentGenerator;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.operator.IFuzzyBundle;
import org.lightjason.agentspeak.language.execution.instantiable.plan.statistic.IPlanStatistic;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.unify.IUnifier;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;


/**
 * agent interface
 *
 * @tparam T agent type
 */
public interface IAgent<T extends IAgent<?>> extends Serializable, Callable<T>
{
    /**
     * empty agent
     */
    IAgent<?> EMPTY = new IAgent<IAgent<?>>()
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -3043602502559853983L;

        @Override
        public final IAgent<?> call() throws Exception
        {
            return this;
        }

        @Nonnull
        @Override
        @SafeVarargs
        @SuppressWarnings( "varargs" )
        public final <N extends IInspector> Stream<N> inspect( @Nonnull final N... p_inspector )
        {
            return Arrays.stream( p_inspector );
        }

        @Nonnull
        @Override
        public final IFuzzyValue<Boolean> trigger( @Nonnull final ITrigger p_trigger, @Nullable final boolean... p_immediately )
        {
            return CFuzzyValue.of( true );
        }

        @Nonnull
        @Override
        public final IView beliefbase()
        {
            return IView.EMPTY;
        }

        @Nonnull
        @Override
        public final Multimap<IPath, ILiteral> runningplans()
        {
            return ImmutableMultimap.of();
        }

        @Override
        public final boolean sleeping()
        {
            return false;
        }

        @Nonnull
        @Override
        public final IAgent<IAgent<?>> sleep( final long p_cycles, @Nullable final ITerm... p_term )
        {
            return this;
        }

        @Nonnull
        @Override
        public final IAgent<IAgent<?>> sleep( final long p_cycles, @Nonnull final Stream<ITerm> p_term )
        {
            return this;
        }

        @Nonnull
        @Override
        public final IAgent<IAgent<?>> wakeup( @Nullable final ITerm... p_term )
        {
            return this;
        }

        @Nonnull
        @Override
        public final IAgent<IAgent<?>> wakeup( @Nonnull final Stream<ITerm> p_term )
        {
            return this;
        }

        @Nonnull
        @Override
        public final Map<String, Object> storage()
        {
            return Collections.emptyMap();
        }

        @Nonnull
        @Override
        public final IUnifier unifier()
        {
            return IUnifier.EMPTY;
        }

        @Nonnegative
        @Override
        public final long cycletime()
        {
            return 0;
        }

        @Nonnull
        @Override
        public final Multimap<ITrigger, IPlanStatistic> plans()
        {
            return ImmutableMultimap.of();
        }

        @Nonnull
        @Override
        public final IFuzzyBundle<Boolean> fuzzy()
        {
            return IAgentGenerator.DEFAULTFUZZYBUNDLE;
        }

        @Nonnull
        @Override
        public final IVariableBuilder variablebuilder()
        {
            return IVariableBuilder.EMPTY;
        }

        @Nonnull
        @Override
        public final Multimap<IPath, IRule> rules()
        {
            return ImmutableMultimap.of();
        }

        @Nonnull
        @Override
        @SuppressWarnings( "unchecked" )
        public final <N extends IAgent<?>> N raw()
        {
            return (N) this;
        }

        @Override
        public final int hashCode()
        {
            return 0;
        }

        @Override
        public final boolean equals( final Object p_object )
        {
            return ( p_object instanceof IAgent<?> ) && ( this.hashCode() == p_object.hashCode() );
        }
    };


    /**
     * inspector method
     *
     * @param p_inspector inspector object
     * @return inspector stream or empty stream
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    <N extends IInspector> Stream<N> inspect( @Nonnull final N... p_inspector );

    /**
     * trigger an event
     *
     * @param p_trigger event trigger
     * @param p_immediately run element immediately
     * @return execution finished correctly
     *
     * @note the trigger is ignored iif the agent is sleeping
     */
    @Nonnull
    IFuzzyValue<Boolean> trigger( @Nonnull final ITrigger p_trigger, @Nullable final boolean... p_immediately );

    /**
     * returns the beliefbase
     *
     * @return beliefbase
     */
    @Nonnull
    IView beliefbase();

    /**
     * returns a map of the current running plans
     *
     * @return map with running plans and the instance literal
     */
    @Nonnull
    Multimap<IPath, ILiteral> runningplans();

    /**
     * returns sleeping state
     *
     * @return sleeping flag
     */
    boolean sleeping();

    /**
     * pushs the agent into sleeping state
     *
     * @param p_cycles number of cycles for sleeping - if is set to maximum the sleeping time is infinity
     * @param p_term literal list for wake-up calls
     * @return agent reference
     */
    @Nonnull
    IAgent<T> sleep( final long p_cycles, @Nullable final ITerm... p_term );

    /**
     * pushs the agent into sleeping state
     *
     * @param p_cycles number of cycles for sleeping - if is set to maximum the sleeping time is infinity
     * @param p_term term stream for wake-up calls
     * @return agent reference
     */
    @Nonnull
    IAgent<T> sleep( final long p_cycles, @Nonnull final Stream<ITerm> p_term );

    /**
     * wake-up the agent by generating wakeup-goal
     *
     * @param p_term any term value which will push into the wake-up plan
     * @return agent reference
     */
    @Nonnull
    IAgent<T> wakeup( @Nullable final ITerm... p_term );

    /**
     * wake-up the agent by generating wakeup-goal
     *
     * @param p_term stream to call wake-up plan
     * @return agent reference
     */
    @Nonnull
    IAgent<T> wakeup( @Nonnull final Stream<ITerm> p_term );


    /**
     * storage access
     *
     * @return storage map
     */
    @Nonnull
    Map<String, Object> storage();

    /**
     * returns an unifier
     *
     * @return unification algorithm
     */
    @Nonnull
    IUnifier unifier();

    /**
     * returns the time in nano
     * seconds at the last cycle
     *
     * @return nano seconds
     */
    @Nonnegative
    long cycletime();

    /**
     * returns the internal map of plans
     *
     * @return plan map
     */
    @Nonnull
    Multimap<ITrigger, IPlanStatistic> plans();

    /**
     * return fuzzy operator
     *
     * @return operator
     */
    @Nonnull
    IFuzzyBundle<Boolean> fuzzy();

    /**
     * returns the variable builder function
     *
     * @return variable builder function
     */
    @Nonnull
    IVariableBuilder variablebuilder();

    /**
     * returns amultimap with literal-rule functor
     * and rle objects
     *
     * @return multimap
     */
    @Nonnull
    Multimap<IPath, IRule> rules();

    /**
     * cast the interface agent object
     * to a specified agent object
     *
     * @return specified agent
     */
    @Nonnull
    <N extends IAgent<?>> N raw();

}
