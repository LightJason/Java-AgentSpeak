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

package org.lightjason.agentspeak.language.fuzzy.membership;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * membership which represent a strict crisp structure (on odd fuzzy set elements the bias is used for splitt element
 * in failing / successfull structure)
 *
 * @tparam E fuzzy enum
 */
public final class CCrisp<E extends Enum<?>> implements IFuzzyMembership<E>
{
    /**
     * enum class
     */
    private final Class<? extends IFuzzySet<E>> m_class;
    /**
     * even / odd element flag
     */
    private final boolean m_even;
    /**
     * index value of the middle
     */
    private final int m_splitindex;


    /**
     * ctor
     *
     * @param p_class enum class type
     */
    public CCrisp( final Class<? extends IFuzzySet<E>> p_class )
    {
        m_class = p_class;
        m_even = m_class.getEnumConstants().length % 2 == 0;
        m_splitindex = m_class.getEnumConstants().length / 2;
    }

    @NonNull
    @Override
    public Stream<IFuzzyValue<?>> success()
    {
        return m_even
               ? Stream.concat(
                   IntStream.range( 0, m_splitindex )
                            .mapToObj( i -> CFuzzyValue.of( m_class.getEnumConstants()[i].rawenum(), 0 ) ),
                   IntStream.range( m_splitindex, m_class.getEnumConstants().length )
                            .mapToObj( i -> CFuzzyValue.of( m_class.getEnumConstants()[i].rawenum(), 1 ) )
                 )
               : CCommon.streamconcatstrict(
                    IntStream.range( 0, m_splitindex )
                             .mapToObj( i -> CFuzzyValue.of( m_class.getEnumConstants()[i].rawenum(), 0 ) ),
                    Stream.of( CFuzzyValue.of( m_class.getEnumConstants()[m_splitindex].rawenum(), 0.5 ) ),
                    IntStream.range( m_splitindex + 1, m_class.getEnumConstants().length )
                             .mapToObj( i -> CFuzzyValue.of( m_class.getEnumConstants()[i].rawenum(), 1 ) )
                 );
    }

    @NonNull
    @Override
    public Stream<IFuzzyValue<?>> fail()
    {
        return m_even
               ? Stream.concat(
                    IntStream.range( 0, m_splitindex )
                             .mapToObj( i -> CFuzzyValue.of( m_class.getEnumConstants()[i].rawenum(), 1 ) ),
                    IntStream.range( m_splitindex, m_class.getEnumConstants().length )
                             .mapToObj( i -> CFuzzyValue.of( m_class.getEnumConstants()[i].rawenum(), 0 ) )
                )
               : CCommon.streamconcatstrict(
                    IntStream.range( 0, m_splitindex )
                             .mapToObj( i -> CFuzzyValue.of( m_class.getEnumConstants()[i].rawenum(), 1 ) ),
                    Stream.of( CFuzzyValue.of( m_class.getEnumConstants()[m_splitindex].rawenum(), 0.5 ) ),
                    IntStream.range( m_splitindex + 1, m_class.getEnumConstants().length )
                             .mapToObj( i -> CFuzzyValue.of( m_class.getEnumConstants()[i].rawenum(), 0 ) )
                );
    }

    @Override
    public Stream<Number> range( @NonNull final E p_value )
    {
        return Stream.of( 1 );
    }

    @Override
    public Stream<IFuzzyValue<?>> modify( final Stream<ITerm> p_arguments )
    {
        return Stream.empty();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends Enum<?>> IFuzzyMembership<T> raw()
    {
        return (IFuzzyMembership<T>) this;
    }

    @Override
    public Stream<IFuzzyValue<?>> apply( final Number p_number )
    {
        return p_number.doubleValue() < 0.5
               ? this.fail()
               : this.success();
    }

    @Nonnull
    @Override
    public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return p_agent;
    }
}
