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

package org.lightjason.agentspeak.language.execution.action;

import org.apache.commons.lang3.StringUtils;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * proxy action to encapsulate all actions
 */
public final class CProxyAction implements IExecution
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 4799005052331053271L;
    /**
     * execution
     */
    private final IExecution m_execution;

    /**
     * ctor
     *
     * @param p_actions actions definition
     * @param p_literal literal
     */
    public CProxyAction( @Nonnull final Map<IPath, IAction> p_actions, @Nonnull final ILiteral p_literal )
    {
        m_execution = new CActionWrapper( p_literal, p_actions );
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        return m_execution.execute( p_parallel, p_context, p_argument, p_return );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return m_execution.variables();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}", m_execution );
    }

    /**
     * inner class for encapsulating term values (variable / raw terms)
     */
    private static class CTermWrapper<T extends ITerm> implements IExecution
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 6984535096829821628L;
        /**
         * term value
         */
        private final T m_value;

        /**
         * ctor
         *
         * @param p_value any static term
         */
        CTermWrapper( @Nonnull final T p_value )
        {
            m_value = p_value;
        }

        @Override
        public final int hashCode()
        {
            return m_value.hashCode();
        }

        @Override
        public final String toString()
        {
            return MessageFormat.format( "{0}", m_value );
        }

        @Override
        public final boolean equals( final Object p_object )
        {
            return ( p_object != null ) && ( p_object instanceof IExecution ) && ( this.hashCode() == p_object.hashCode() );
        }

        @Nonnull
        @Override
        public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                   @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
        )
        {
            p_return.add( m_value );
            return CFuzzyValue.from( true );
        }

        @Nonnull
        @Override
        public final Stream<IVariable<?>> variables()
        {
            return this.getVariableSet( m_value );
        }

        /**
         * returns a variable set based on the generic type
         *
         * @param p_value term type
         * @return variable set (empty)
         */
        @Nonnull
        private Stream<IVariable<?>> getVariableSet( final T p_value )
        {
            return Stream.<IVariable<?>>empty();
        }
    }

    /**
     * inner class for encapsulating action execution
     *
     * @warning execution must run variable repacing before action calling
     */
    private static class CActionWrapper implements IExecution
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 5531271525053969711L;
        /**
         * parallel execution flag
         */
        private final boolean m_parallel;
        /**
         * action
         */
        private final IAction m_action;
        /**
         * arguments as map with index for prevent
         * result order on parallel execution
         */
        private final Map<Integer, IExecution> m_arguments;


        /**
         * ctor
         *
         * @param p_literal action literal
         * @param p_actions actions
         */
        CActionWrapper( @Nonnull final ILiteral p_literal, @Nonnull final Map<IPath, IAction> p_actions )
        {
            // check parallel and inner execution
            m_parallel = p_literal.hasAt();


            // resolve action
            m_action = p_actions.get( p_literal.fqnfunctor() );
            if ( m_action == null )
                throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "actionunknown", p_literal ) );

            // check number of arguments and add action to the score cache
            if ( p_literal.orderedvalues().count() < m_action.minimalArgumentNumber() )
                throw new CIllegalArgumentException(
                    org.lightjason.agentspeak.common.CCommon.languagestring( this, "argumentnumber", p_literal, m_action.minimalArgumentNumber() ) );

            // resolve action arguments
            m_arguments = Collections.unmodifiableMap( this.createSubExecutions( p_literal.orderedvalues().collect( Collectors.toList() ), p_actions ) );
        }

        @Override
        public final int hashCode()
        {
            return m_action.hashCode() + m_arguments.hashCode();
        }

        @Override
        public final String toString()
        {
            return MessageFormat.format( "{0}({1})", m_action, StringUtils.join( m_arguments.values(), ", " ) );
        }

        @Override
        public final boolean equals( final Object p_object )
        {
            return ( p_object != null ) && ( p_object instanceof IExecution ) && ( this.hashCode() == p_object.hashCode() );
        }

        @Nonnull
        @Override
        public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                             @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
        )
        {
            return m_action.execute(
                m_parallel, p_context,
                this.subexecute( p_context, m_arguments ),
                p_return
            );
        }

        @Nonnull
        @Override
        public final Stream<IVariable<?>> variables()
        {
            return m_action.variables();
        }

        /**
         * builds the map of execution arguments
         *
         * @param p_elements collection with literal elements (term- / literal list of attributes & annotations)
         * @param p_actions map with actions
         * @return ordered execution structure
         */
        @Nonnull
        private Map<Integer, IExecution> createSubExecutions( @Nonnull final Collection<? extends ITerm> p_elements,
                                                              @Nonnull final Map<IPath, IAction> p_actions )
        {
            // convert collection to list and build map with indices
            final List<? extends ITerm> l_elements = new LinkedList<>( p_elements );
            return IntStream.range( 0, l_elements.size() )
                            .boxed()
                            .collect(
                                Collectors.toMap(
                                    i -> i,
                                    i ->
                                    {
                                        final ITerm l_term = l_elements.get( i );
                                        return l_term instanceof ILiteral
                                               ? new CActionWrapper( l_term.term(), p_actions )
                                               : new CTermWrapper<>( l_term );
                                    }
                                )
                            );
        }

        /**
         * execute inner structures
         *
         * @param p_context context structure
         * @param p_execution map with execution elements
         * @return return arguments of execution (flat list)
         */
        @Nonnull
        private List<ITerm> subexecute( @Nonnull final IContext p_context, @Nonnull final Map<Integer, IExecution> p_execution )
        {
            return Collections.unmodifiableList( CCommon.replaceFromContext(
                p_context,
                ( m_parallel
                  ? p_execution.entrySet().parallelStream()
                  : p_execution.entrySet()
                               .stream() )
                               .flatMap( i ->
                               {
                                   final List<ITerm> l_return = new LinkedList<>();
                                   i.getValue().execute( m_parallel, p_context, Collections.emptyList(), l_return );
                                   return l_return.stream();
                               } )
                               .collect( Collectors.toList() )
            ) );
        }
    }

}
