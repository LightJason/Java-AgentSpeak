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

package org.lightjason.agentspeak.language.execution.base;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * defines an execution element with a repair call
 */
public final class CRepair extends IBaseExecution<IExecution[]>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7095678561033158953L;

    /**
     * ctor
     *
     * @param p_chain execution chain
     */
    public CRepair( @Nonnull final Stream<IExecution> p_chain )
    {
        super( p_chain.toArray( IExecution[]::new ) );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        // each repair statement creates a stream, the stream must be cached as array and return again,
        // because the stream must be defuzzified, after that the stream is closed
        return Arrays.stream( m_value )
                     .map( i -> execute( p_context, i ) )
                     .filter( i -> p_context.agent().fuzzy().defuzzification().success(
                         p_context.agent().fuzzy().defuzzification().apply(
                             Arrays.stream( i )
                         )
                     ) )
                     .findFirst()
                     .map( Arrays::stream )
                     .orElseGet( () -> p_context.agent().fuzzy().membership().fail() );
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Arrays.stream( m_value ).flatMap( IExecution::variables );
    }

    @Override
    public String toString()
    {
        return Arrays.stream( m_value )
                     .map( Object::toString )
                     .collect( Collectors.joining( " << " ) );
    }

    /**
     * execute single execution
     *
     * @param p_context context
     * @param p_execution execution
     * @return execution result
     */
    private static IFuzzyValue<?>[] execute( @Nonnull final IContext p_context, @Nonnull final IExecution p_execution )
    {
        final List<ITerm> l_return = CCommon.argumentlist();
        final IFuzzyValue<?>[] l_fuzzyreturn = p_execution.execute( false, p_context, Collections.emptyList(), l_return ).toArray( IFuzzyValue[]::new );

        // if an return argument exist and it contains an boolean this value is used, ...
        if ( l_return.size() == 1 && CCommon.isssignableto( l_return.get( 0 ), Boolean.class ) )
            return l_return.get( 0 ).<Boolean>raw()
                   ? p_context.agent().fuzzy().membership().success().toArray( IFuzzyValue[]::new )
                   : p_context.agent().fuzzy().membership().fail().toArray( IFuzzyValue[]::new );

        // ... otherwise the fuzzy result is used
        return l_fuzzyreturn;
    }

}
