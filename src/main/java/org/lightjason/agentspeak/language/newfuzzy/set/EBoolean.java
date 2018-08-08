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

package org.lightjason.agentspeak.language.newfuzzy.set;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.newfuzzy.value.CFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.value.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.stream.Stream;


/**
 * fuzzy boolean
 */
public enum EBoolean implements IFuzzySet<EBoolean>
{
    TRUE( true ),
    FALSE( false );

    /**
     * native type
     */
    private final boolean m_value;

    /**
     * ctor
     * @param p_value value
     */
    EBoolean( final boolean p_value )
    {
        m_value = p_value;
    }

    @Override
    public final IFuzzyValue<EBoolean> apply( @NonNull final Number p_value )
    {
        return new CFuzzyValue<>( this, p_value );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<EBoolean>> success( @Nonnull final IAgent<?> p_agent, @Nonnull final IExecution p_execution )
    {
        return Stream.of( TRUE.apply( 1 ), FALSE.apply( 0 ) );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<EBoolean>> fail( @Nonnull final IAgent<?> p_agent, @Nonnull final IExecution p_execution )
    {
        return Stream.of( FALSE.apply( 1 ), TRUE.apply( 0 ) );
    }
}
