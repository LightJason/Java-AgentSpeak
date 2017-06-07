/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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


package org.lightjason.agentspeak.language.instantiable.plan.statistic;

import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;


/**
 * plan statistic to count execution values
 */
public final class CPlanStatistic implements IPlanStatistic
{
    /**
     * plan reference
     */
    private final IPlan m_plan;
    /**
     * count of successful exeuctions
     */
    private final AtomicLong m_successful = new AtomicLong();
    /**
     * count of fail exeuctions
     */
    private final AtomicLong m_fail = new AtomicLong();


    /**
     * ctor
     *
     * @param p_plan plan reference
     */
    private CPlanStatistic( final IPlan p_plan )
    {
        m_plan = p_plan;
    }

    @Override
    public final IPlan plan()
    {
        return m_plan;
    }

    @Override
    public final long count()
    {
        return m_fail.get() + m_successful.get();
    }

    @Override
    public final long successful()
    {
        return m_successful.get();
    }

    @Override
    public final double successfulratio()
    {
        final double l_sum = m_successful.get() + m_fail.get();
        return l_sum == 0
               ? 0
               : m_successful.get() / l_sum;
    }

    @Override
    public final long fail()
    {
        return m_fail.get();
    }

    @Override
    public final double failratio()
    {
        final double l_sum = m_successful.get() + m_fail.get();
        return l_sum == 0
               ? 0
               : m_fail.get() / l_sum;
    }

    @Override
    public final IPlanStatistic incrementsuccessful()
    {
        m_successful.incrementAndGet();
        return this;
    }

    @Override
    public final IPlanStatistic incrementfail()
    {
        m_fail.incrementAndGet();
        return this;
    }

    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.of(
            new CConstant<>( "PlanSuccessful", m_successful.get() ),
            new CConstant<>( "PlanFail", m_fail.get() ),
            new CConstant<>( "PlanRuns", m_successful.get() + m_fail.get() ),

            // execution ratio
            new CConstant<>( "PlanSuccessfulRatio", this.successfulratio() ),
            new CConstant<>( "PlanFailRatio", this.failratio() )
        );
    }

    @Override
    public final int hashCode()
    {
        return m_plan.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null )
               && ( ( p_object instanceof IPlanStatistic ) || ( p_object instanceof IPlan ) )
               && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "successful [{0}], fail [{1}]: {2}", m_successful.get(), m_fail.get(), m_plan );
    }

    /**
     * factory
     *
     * @param p_plan plan object
     * @return statistic object
     */
    public static IPlanStatistic from( final IPlan p_plan )
    {
        return new CPlanStatistic( p_plan );
    }


    @Override
    public final int compareTo( final IPlanStatistic p_other )
    {
        return Integer.compare( this.hashCode(), p_other.hashCode() );
    }
}
