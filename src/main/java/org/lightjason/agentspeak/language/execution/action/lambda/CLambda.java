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

package org.lightjason.agentspeak.language.execution.action.lambda;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.action.IBaseExecution;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


/**
 * lambda expression
 */
public final class CLambda extends IBaseExecution<IExecution[]>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -7221932027365007256L;
    /**
     * initialize
     */
    private final IExecution m_init;
    /**
     * parallel execution
     */
    private final boolean m_parallel;
    /**
     * return variable
     */
    private final IVariable<?> m_return;
    /**
     * iterator variable
     */
    private final IVariable<?> m_iterator;


    /**
     * ctor
     *
     * @param p_parallel parallel execution
     * @param p_init initialize
     * @param p_iterator p_iterator
     * @param p_value execution list
     * @param p_return return variable
     */
    public CLambda( final boolean p_parallel, @Nonnull final IExpression p_init, @Nonnull final IVariable<?> p_iterator,
                    @Nonnull final IExecution[] p_value, @Nonnull final IVariable<?> p_return )
    {
        super( p_value );
        m_init = p_init;
        m_return = p_return;
        m_parallel = p_parallel;
        m_iterator = p_iterator;
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                         @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_init = new ArrayList<>();
        if ( ( !m_init.execute( p_parallel, p_context, p_argument, l_init ).value() ) || ( l_init.size() != 1 ) )
            return CFuzzyValue.from( false );


        /*
        (
            m_parallel
            ? Arrays.stream( m_value ).parallel()
            : Arrays.stream( m_value )
        )
        */

        return null;
    }

    /**
     * returns the inner variables
     *
     * @return variable stream
     */
    private Stream<IVariable<?>> innervariables()
    {
        return CCommon.streamconcatstrict(
            Arrays.stream( m_value ).flatMap( IExecution::variables ),
            Stream.of( m_iterator ),
            m_return.equals( IVariable.EMPTY )
            ? Stream.of( m_return )
            : Stream.empty()
        );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            m_init.variables(),
            m_return.equals( IVariable.EMPTY )
            ? Stream.of( m_return )
            : Stream.empty()
        );
    }
}
