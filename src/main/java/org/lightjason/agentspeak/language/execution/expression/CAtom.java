/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.expression;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.List;
import java.util.stream.Stream;


/**
 * atom expression
 */
public class CAtom implements IExpression
{
    /**
     * value
     */
    private final ITerm m_value;

    /**
     * ctor
     *
     * @param p_value any atomic value
     */
    @SuppressWarnings( "unchecked" )
    public <T> CAtom( final T p_value )
    {
        m_value = ( p_value instanceof IVariable<?> ) ? (IVariable<?>) p_value : CRawTerm.from( p_value );
    }


    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final ITerm l_term = CCommon.replaceFromContext( p_context, m_value );
        p_return.add( CRawTerm.from( l_term.toAny() ).throwNotAllocated( l_term.fqnfunctor().toString() ) );
        return CFuzzyValue.from( true );
    }

    @Override
    public final double score( final IAgent<?> p_agent )
    {
        return 0;
    }

    @Override
    public final Stream<IVariable<?>> variables()
    {
        return m_value instanceof IVariable<?>
               ? Stream.of( (IVariable<?>) m_value )
               : Stream.empty();
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IExpression ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return m_value.toString();
    }
}
