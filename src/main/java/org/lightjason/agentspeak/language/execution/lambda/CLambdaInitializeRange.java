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

package org.lightjason.agentspeak.language.execution.lambda;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;


/**
 * lambda initialize with a range
 */
public final class CLambdaInitializeRange extends IBaseExecution<IExecution[]>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 8945262605702580850L;

    /**
     * ctor
     *
     * @param p_value data
     */
    public CLambdaInitializeRange( @Nonnull final Stream<IExecution> p_value )
    {
        super( p_value.toArray( IExecution[]::new ) );
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                         @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_return = CCommon.argumentlist();
        final IFuzzyValue<Boolean> l_result = Arrays.stream( m_value )
                                                    .map( i -> i.execute( p_parallel, p_context, p_argument, l_return ) )
                                                    .filter( i -> !i.value() )
                                                    .findFirst()
                                                    .orElse( CFuzzyValue.of( true ) );

        if ( !l_result.value() )
            return l_result;

        if ( l_return.size() == 0 || l_return.size() > 3 )
            return CFuzzyValue.of( false );

        if ( l_return.size() == 1 )
            p_return.add(
                CRawTerm.of(
                    LongStream.range( 0, l_return.get( 0 ).<Number>raw().longValue() ).boxed()
                )
            );

        if ( l_return.size() == 2 )
            p_return.add(
                CRawTerm.of(
                    LongStream.range( l_return.get( 0 ).<Number>raw().longValue(), l_return.get( 1 ).<Number>raw().longValue() ).boxed()
                )
            );

        if ( l_return.size() == 3 )
            p_return.add(
                CRawTerm.of(
                    LongStream.range(
                        l_return.get( 0 ).<Number>raw().longValue(),
                        l_return.get( 1 ).<Number>raw().longValue() / l_return.get( 2 ).<Number>raw().longValue()
                    ).map( i -> i * l_return.get( 2 ).<Number>raw().longValue() ).boxed()
                )
            );


        return l_result;
    }
}
