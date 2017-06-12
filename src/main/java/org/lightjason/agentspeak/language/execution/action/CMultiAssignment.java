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

package org.lightjason.agentspeak.language.execution.action;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * assignment action of a multi-variable list
 */
public final class CMultiAssignment<M extends IExecution> extends IBaseExecution<List<IVariable<?>>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -6123210880356077509L;
    /**
     * right-hand argument
     */
    private final M m_righthand;

    /**
     * ctor
     *
     * @param p_lefthand left-hand variable list
     * @param p_righthand right-hand argument
     */
    public CMultiAssignment( @Nonnull final List<IVariable<?>> p_lefthand, @Nonnull final M p_righthand )
    {
        super( p_lefthand );
        m_righthand = p_righthand;
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_result = new LinkedList<>();
        if ( ( !m_righthand.execute( p_parallel, p_context, Collections.<ITerm>emptyList(), l_result ).value() )
             || ( l_result.isEmpty() ) )
            return CFuzzyValue.from( false );


        // position matching on list index
        final List<ITerm> l_flatresult = CCommon.flatcollection( l_result ).collect( Collectors.toList() );
        final List<ITerm> l_assign = CCommon.replaceFromContext( p_context, m_value );

        IntStream.range( 0, Math.min( l_assign.size(), l_flatresult.size() ) )
                 .boxed()
                 .forEach( i -> ( (IVariable<Object>) l_assign.get( i ) ).set( l_flatresult.get( i ).raw() ) );


        // tail matching
        if ( l_assign.size() < l_flatresult.size() )
            ( (IVariable<Object>) l_assign.get( l_assign.size() - 1 ) ).set( l_flatresult.subList( l_assign.size() - 1, l_flatresult.size() ) );

        return CFuzzyValue.from( true );
    }

    @Override
    public final int hashCode()
    {
        return ( m_value == null ? 0 : m_value.hashCode() ) ^ m_righthand.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IExecution ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} = {1}", m_value, m_righthand );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            m_value == null ? Stream.empty() : m_value.stream(),
            m_righthand.variables()
        );
    }
}
