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

package org.lightjason.agentspeak.language.execution;

import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * defines an execution element with a repair call
 */
public class CRepair extends IBaseExecution<IExecution[]>
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
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        return Arrays.stream( m_value )
                     .map( i -> i.execute( p_parallel, p_context, p_argument, p_return ) )
                     .filter( i -> !p_context.agent().fuzzy().getValue().defuzzify( i ) )
                     .findFirst()
                     .orElseGet( () -> CFuzzyValue.of( false ) );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Arrays.stream( m_value ).flatMap( IExecution::variables );
    }

    @Override
    public final String toString()
    {
        return Arrays.stream( m_value )
                     .map( Object::toString )
                     .collect( Collectors.joining( " << " ) );
    }
}
