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

package org.lightjason.agentspeak.language.execution.passing;

import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


/**
 * variable literal pass
 */
public final class CPassVariableLiteral extends IBaseExecution<IVariable<?>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5298598410240776248L;
    /**
     * term list
     */
    private final ITerm[] m_termlist;

    /**
     * ctor
     *
     * @param p_value variable
     */
    public CPassVariableLiteral( @Nonnull final IVariable<?> p_value )
    {
        this( p_value, Stream.empty() );
    }

    /**
     * ctor
     *
     * @param p_value variable
     * @param p_termlist optional term argument list
     */
    public CPassVariableLiteral( @Nonnull final IVariable<?> p_value, @Nonnull final Stream<ITerm> p_termlist )
    {
        super( p_value );
        m_termlist = p_termlist.toArray( ITerm[]::new );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                           @Nonnull final List<ITerm> p_return
    )
    {
        final IVariable<?> l_variable = CCommon.replacebycontext( p_context, m_value ).<IVariable<?>>term().thrownotallocated();

        if ( l_variable.valueassignableto( String.class ) )
        {
            p_return.add( this.bystring( p_context, l_variable.raw() ) );
            return Stream.of();
        }

        if ( l_variable.valueassignableto( ILiteral.class ) )
        {
            p_return.add( this.byliteral( p_context, l_variable.raw() ) );
            return Stream.of();
        }

        throw new CExecutionIllegealArgumentException( p_context, org.lightjason.agentspeak.common.CCommon
            .languagestring( this, "notstringorliteral", l_variable ) );
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            Stream.of( m_value ),
            CCommon.flattenrecursive( Arrays.stream( m_termlist ) )
                   .filter( i -> i instanceof IVariable<?> )
                   .map( ITerm::term )
        );
    }

    /**
     * creates the result literal of an input string
     *
     * @param p_context execution context
     * @param p_value input string (literal functor)
     * @return result literal
     */
    private ILiteral bystring( @Nonnull final IContext p_context, @Nonnull final String p_value )
    {
        return CLiteral.of( p_value, Arrays.stream( m_termlist ) ).bind( p_context );
    }

    /**
     * creates the result literal of an input literal
     *
     * @param p_context execution context
     * @param p_literal input literal
     * @return result literal
     */
    private ILiteral byliteral( @Nonnull final IContext p_context, @Nonnull final ILiteral p_literal )
    {
        return m_termlist.length == 0
               ? p_literal.allocate( p_context )
               : CLiteral.of(
                   p_literal.hasAt(),
                   p_literal.negated(),
                   p_literal.fqnfunctor(),
                   m_termlist
               ).bind( p_context );
    }
}
