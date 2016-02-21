/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.language.execution.action;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import lightjason.agent.IAgent;
import lightjason.agent.action.IAction;
import lightjason.common.CCommon;
import lightjason.common.CPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * proxy action to encapsulate all actions
 *
 * @note inner annotations cannot be used on the
 * grammer definition, so the inner annotations are ignored
 * @todo check subexecution order and results
 */
public final class CProxyAction implements IExecution
{
    /**
     * execution
     */
    private final IExecution m_execution;
    /**
     * cache list of all used actions for calculating score value
     */
    private final Multiset<IAction> m_scoringcache;

    /**
     * ctor
     *
     * @param p_actions actions definition
     * @param p_literal literal
     */
    public CProxyAction( final Map<CPath, IAction> p_actions, final ILiteral p_literal )
    {
        // create cache for scoring action and define action
        final Multiset<IAction> l_scoringcache = HashMultiset.create();
        m_execution = new CActionWrapper( p_literal, p_actions, l_scoringcache );

        // scoring set is created so build-up to an unmodifieable set
        m_scoringcache = ImmutableMultiset.copyOf( l_scoringcache );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        return m_execution.execute( p_context, p_parallel, p_argument, p_return, p_annotation );
    }

    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return p_aggregate.evaluate( p_agent, m_scoringcache );
    }


    @Override
    @SuppressWarnings( "serial" )
    public final Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{
            addAll( m_execution.getVariables() );
        }};
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
         * term value
         */
        private final T m_value;

        /**
         * ctor
         *
         * @param p_value any static term
         */
        public CTermWrapper( final T p_value )
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
            return this.hashCode() == p_object.hashCode();
        }

        @Override
        public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final boolean p_parallel, final List<ITerm> p_argument,
                                                   final List<ITerm> p_return,
                                                   final List<ITerm> p_annotation
        )
        {
            p_return.add( m_value );
            return CBoolean.from( true );
        }

        @Override
        public final double score( final IAggregation p_aggregate, final IAgent p_agent )
        {
            return 0;
        }

        @Override
        public final Set<IVariable<?>> getVariables()
        {
            return this.getVariableSet( m_value );
        }

        /**
         * returns a variable set based on the generic type
         *
         * @param p_value variable type
         * @return variable set (empty)
         */
        @SuppressWarnings( "serial" )
        private Set<IVariable<?>> getVariableSet( final IVariable<?> p_value )
        {
            return new HashSet<IVariable<?>>()
            {{
                add( p_value.shallowclone() );
            }};
        }

        /**
         * returns a variable set based on the generic type
         *
         * @param p_value term type
         * @return variable set (empty)
         */
        private Set<IVariable<?>> getVariableSet( final T p_value )
        {
            return Collections.emptySet();
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
         * annotation as map with index for prevent
         * result order on parallel execution
         */
        private final Map<Integer, IExecution> m_annotation;


        /**
         * ctor
         *
         * @param p_literal action literal
         * @param p_actions actions
         * @param p_scorecache score cache
         */
        public CActionWrapper( final ILiteral p_literal, final Map<CPath, IAction> p_actions, final Multiset<IAction> p_scorecache )
        {
            // check parallel and inner execution
            m_parallel = p_literal.hasAt();


            // resolve action
            m_action = p_actions.get( p_literal.getFQNFunctor() );
            if ( m_action == null )
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "actionunknown", p_literal ) );

            // check number of arguments and add action to the score cache
            if ( p_literal.orderedvalues().count() < m_action.getMinimalArgumentNumber() )
                throw new CIllegalArgumentException(
                        CCommon.getLanguageString( this, "argumentnumber", p_literal, m_action.getMinimalArgumentNumber() ) );

            p_scorecache.add( m_action );


            // resolve action arguments and annotation
            m_arguments = Collections.unmodifiableMap(
                    this.createSubExecutions( p_literal.orderedvalues().collect( Collectors.toList() ), p_actions, p_scorecache ) );
            m_annotation = Collections.unmodifiableMap(
                    this.createSubExecutions( p_literal.annotations().collect( Collectors.toSet() ), p_actions, p_scorecache ) );
        }

        @Override
        public final int hashCode()
        {
            return m_action.hashCode() + m_arguments.hashCode();
        }

        @Override
        public final String toString()
        {
            return MessageFormat.format( "{0}({1})[{2}]", m_action, StringUtils.join( m_arguments.values(), ", " ), "" );
        }

        @Override
        public final boolean equals( final Object p_object )
        {
            return this.hashCode() == p_object.hashCode();
        }

        @Override
        public IFuzzyValue<Boolean> execute( final IContext<?> p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                             final List<ITerm> p_annotation
        )
        {
            return m_action.execute(
                    p_context, m_parallel,
                    this.subexecute( p_context, m_arguments ), p_return,
                    this.subexecute( p_context, m_annotation )
            );
        }

        @Override
        public final double score( final IAggregation p_aggregate, final IAgent p_agent )
        {
            return 0;
        }

        @Override
        public final Set<IVariable<?>> getVariables()
        {
            return m_action.getVariables();
        }

        /**
         * builds the map of execution arguments
         *
         * @param p_elements collection with literal elements (term- / literal list of attributes & annotations)
         * @param p_actions map with actions
         * @param p_scorecache store cache
         * @return ordered execution structure
         */
        @SuppressWarnings( "unchecked" )
        private Map<Integer, IExecution> createSubExecutions( final Collection<? extends ITerm> p_elements, final Map<CPath, IAction> p_actions,
                                                              final Multiset<IAction> p_scorecache
        )
        {
            // convert collection to list and build map with indices
            final List<? extends ITerm> l_elements = new LinkedList<>( p_elements );
            return IntStream.range( 0, l_elements.size() ).boxed().collect( Collectors.toMap( i -> i, i -> {
                final ITerm l_term = l_elements.get( i );
                return l_term instanceof ILiteral ? new CActionWrapper( (ILiteral) l_term, p_actions, p_scorecache ) : new CTermWrapper<>( l_term );
            } ) );
        }

        /**
         * execute inner structures
         *
         * @param p_context context structure
         * @param p_execution map with execution elements
         * @return return arguments of execution (flat list)
         */
        @SuppressWarnings( "unchecked" )
        private List<ITerm> subexecute( final IContext<?> p_context, final Map<Integer, IExecution> p_execution )
        {
            return Collections.unmodifiableList( lightjason.language.CCommon.replaceFromContext(
                    p_context,
                    (Collection<? extends ITerm>) ( m_parallel ? p_execution.entrySet().parallelStream() : p_execution.entrySet().stream() )
                            .flatMap( i -> {

                                final List<ITerm> l_return = new LinkedList<ITerm>();
                                i.getValue().execute( p_context, m_parallel, Collections.<ITerm>emptyList(), l_return, Collections.<ITerm>emptyList() );
                                return l_return.stream();

                            } ).collect( Collectors.toList() )
            ) );
        }
    }

}
