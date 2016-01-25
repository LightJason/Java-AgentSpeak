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

import lightjason.agent.IAgent;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.CContext;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * lambda expression definition
 */
public final class CLambdaExpression extends IBaseExecution<IVariable<?>>
{
    /**
     * initialization expression
     */
    private final IExecution m_initialize;
    /**
     * execution body
     */
    private final List<IExecution> m_body;
    /**
     * flag of parallel execution
     */
    private boolean m_parallel;
    /**
     * return variable
     */
    private final IVariable<?> m_return;


    /**
     * ctor
     *
     * @param p_parallel parallel execution flag
     * @param p_initialize expression
     * @param p_iterator iteration variable
     * @param p_body execution body
     */
    public CLambdaExpression( final boolean p_parallel, final IExecution p_initialize, final IVariable<?> p_iterator, final List<IExecution> p_body )
    {
        this( p_parallel, p_initialize, null, p_iterator, p_body );
    }

    /**
     * ctor
     *
     * @param p_parallel parallel execution flag
     * @param p_initialize expression
     * @param p_iterator iteration variable
     * @param p_return return variable
     * @param p_body execution body
     */
    public CLambdaExpression( final boolean p_parallel, final IExecution p_initialize, final IVariable<?> p_iterator, final IVariable<?> p_return,
                              final List<IExecution> p_body
    )
    {
        super( p_iterator );
        m_parallel = p_parallel;
        m_initialize = p_initialize;
        m_return = p_return;
        m_body = Collections.unmodifiableList( p_body );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // run initialization
        final List<ITerm> l_return = new LinkedList<>();
        m_initialize.execute( p_context, p_parallel, p_argument, l_return, p_annotation );


        // run lambda expression
        if ( m_parallel )
            p_return.addAll(
                    CCommon.flatList( l_return ).parallelStream().map( i -> {

                        final Triple<IContext<?>, IVariable<?>, IVariable<?>> l_localcontext = this.getLocalContext( p_context, m_value, m_return );
                l_localcontext.getMiddle().set( CCommon.getRawValue( i ) );
                m_body.stream().forEach(
                        j -> j.execute(
                                l_localcontext.getLeft(), m_parallel, Collections.<ITerm>emptyList(), new LinkedList<>(), Collections.<ITerm>emptyList() )
                );
                        return l_localcontext.getRight() != null ? CRawTerm.from( CCommon.getRawValue( l_localcontext.getRight() ) ) : null;

                    } ).filter( i -> i instanceof ITerm ).collect( Collectors.toList() )
            );

        else
        {
            final Triple<IContext<?>, IVariable<?>, IVariable<?>> l_localcontext = this.getLocalContext( p_context, m_value, m_return );

            CCommon.flatList( l_return ).stream().forEach( i -> {
                l_localcontext.getMiddle().set( CCommon.getRawValue( i ) );
                m_body.stream().forEach(
                        j -> j.execute(
                                l_localcontext.getLeft(), m_parallel, Collections.<ITerm>emptyList(), new LinkedList<>(), Collections.<ITerm>emptyList() ) );
            } );
        }

        return CBoolean.from( true );
    }

    @Override
    public final int hashCode()
    {
        return m_initialize.hashCode() + m_value.hashCode() + m_body.hashCode() + ( m_parallel ? 9931 : 0 );
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return 0;
    }

    @Override
    @SuppressWarnings( "serial" )
    public final Set<IVariable<?>> getVariables()
    {
        return m_return == null ? m_initialize.getVariables() : new HashSet<IVariable<?>>()
        {{
            add( m_return );
            addAll( m_initialize.getVariables() );
        }};
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}({1}) -> {2} | {3}", m_parallel ? "@" : "", m_initialize, m_value, m_body );
    }


    /**
     * create the local context structure of the expression
     *
     * @param p_context local context
     * @param p_iterator iterator variable
     * @param p_return return variable or null
     * @return tripel with context, iterator variable and return variable
     */
    private Triple<IContext<?>, IVariable<?>, IVariable<?>> getLocalContext( final IContext<?> p_context, final IVariable<?> p_iterator,
                                                                             final IVariable<?> p_return
    )
    {
        final IVariable<?> l_iterator = p_iterator.clone();
        final IVariable<?> l_return = p_return != null ? p_return.clone() : null;
        final Set<IVariable<?>> l_variables = new HashSet<>( p_context.getInstanceVariables().values() );

        l_variables.addAll( m_body.stream().flatMap( i -> i.getVariables().stream() ).collect( Collectors.toList() ) );
        l_variables.remove( l_iterator );
        l_variables.add( l_iterator );


        if ( l_return != null )
        {
            l_variables.remove( l_return );
            l_variables.add( l_return );
        }

        return new ImmutableTriple<>( new CContext<>( p_context.getAgent(), p_context.getInstance(), l_variables ), l_iterator, l_return );
    }
}
