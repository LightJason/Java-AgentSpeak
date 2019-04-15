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

package org.lightjason.agentspeak.language.execution.lambda;

import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * lambda initialize with streaming arguments
 */
public final class CLambdaInitializeStream extends IBaseExecution<IExecution[]>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4625794081981849579L;
    /**
     * lambda generator
     */
    private final ILambdaStreamingGenerator m_lambda;

    /**
     * ctor
     *
     * @param p_value data
     * @param p_lambda lambda generator
     */
    public CLambdaInitializeStream( @Nonnull final Stream<IExecution> p_value, @Nonnull final ILambdaStreamingGenerator p_lambda )
    {
        super( p_value.toArray( IExecution[]::new ) );
        m_lambda = p_lambda;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                           @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_return = CCommon.argumentlist();
        final Pair<List<IFuzzyValue<?>>, Boolean> l_result = CCommon.executesequential( p_parallel, p_context, p_argument, l_return, Arrays.stream( m_value ) );
        if ( !l_result.getValue() )
            return p_context.agent().fuzzy().membership().fail();

        if ( l_return.size() == 0 )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "emptyarguments" )
            );


        p_return.add(
            CRawTerm.of(
                l_return.stream()
                        .flatMap( i -> Objects.isNull( i.raw() ) ? Stream.empty() : m_lambda.apply( i.raw().getClass() ).apply( i.raw() ) )
            )
        );

        return l_result.getKey().stream();
    }


    @Override
    public String toString()
    {
        return Arrays.stream( m_value ).map( Object::toString ).collect( Collectors.joining( ", " ) );
    }
}
