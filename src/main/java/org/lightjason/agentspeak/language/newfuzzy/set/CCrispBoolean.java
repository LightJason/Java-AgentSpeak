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
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.execution.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.newfuzzy.element.EBoolean;
import org.lightjason.agentspeak.language.newfuzzy.value.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * fuzzy set of crisp boolean
 */
public final class CCrispBoolean implements IFuzzySet<Number, EBoolean>
{
    /**
     * precision for vlaue equality
     */
    private final Number m_precision;
    /**
     * funciton translate any number value into [0,1] for the true fuzzy value
     */
    private final Function<Number, Number> m_true;
    /**
     * funciton translate any number value into [0,1] for the false fuzzy value
     */
    private final Function<Number, Number> m_false;


    /**
     * ctor
     */
    public CCrispBoolean()
    {
        this( 0.00000001 );
    }

    /**
     * ctor
     */
    public CCrispBoolean( @NonNull final Number p_precision )
    {
        this(
            i -> i.doubleValue() >= 0.5 ? 1 : 0,
            i -> i.doubleValue() < 0.5 ? 1 : 0,
            p_precision
        );
    }

    /**
     * ctor
     *
     * @param p_true characteristic funciton of true value
     * @param p_false characteristic funciton of false value
     * @param p_precision equality value
     */
    private CCrispBoolean( @NonNull final Function<Number, Number> p_true, @NonNull final Function<Number, Number> p_false, @NonNull final Number p_precision )
    {
        m_true = p_true;
        m_false = p_false;
        m_precision = p_precision;
    }


    @Nonnull
    @Override
    public Stream<IFuzzyValue<EBoolean>> success()
    {
        return Stream.of( EBoolean.TRUE.apply( 1 ), EBoolean.FALSE.apply( 0 ) );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<EBoolean>> fail()
    {
        return Stream.of( EBoolean.FALSE.apply( 1 ), EBoolean.TRUE.apply( 0 ) );
    }

    @Override
    public boolean elementequal( @NonNull final IFuzzyValue<EBoolean> p_lhs, @NonNull final IFuzzyValue<EBoolean> p_rhs )
    {
        return p_lhs.type().equals( p_rhs.type() ) && CCommon.floatingequal( p_lhs.fuzzy(), p_rhs.fuzzy(), m_precision );
    }

    @Override
    public Stream<IFuzzyValue<EBoolean>> apply( final Number p_number )
    {
        return Stream.of(
            EBoolean.FALSE.apply(
                Math.max( 0, Math.min( 1, m_false.apply( p_number ).doubleValue() ) )
            ),
            EBoolean.TRUE.apply(
                Math.max( 0, Math.min( 1, m_true.apply( p_number ).doubleValue() ) )
            )
        );
    }

    @Override
    public void accept( @NonNull final IAgent<?> p_agent, @NonNull final IInstantiable p_instance )
    {
    }

}
