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


package org.lightjason.agentspeak.language.execution.instantiable.plan.statistic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
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
    private CPlanStatistic( @Nonnull final IPlan p_plan )
    {
        m_plan = p_plan;
    }

    @Nonnull
    @Override
    public IPlan plan()
    {
        return m_plan;
    }

    @Override
    @Nonnegative
    public long count()
    {
        return m_fail.get() + m_successful.get();
    }

    @Override
    @Nonnegative
    public long successful()
    {
        return m_successful.get();
    }

    @Override
    @Nonnegative
    public double successfulratio()
    {
        final double l_sum = m_successful.get() + m_fail.get();
        return l_sum == 0
               ? 0
               : m_successful.get() / l_sum;
    }

    @Override
    @Nonnegative
    public long fail()
    {
        return m_fail.get();
    }

    @Override
    public double failratio()
    {
        final double l_sum = m_successful.get() + m_fail.get();
        return l_sum == 0
               ? 0
               : m_fail.get() / l_sum;
    }

    @Nonnull
    @Override
    public IPlanStatistic incrementsuccessful()
    {
        m_successful.incrementAndGet();
        return this;
    }

    @Nonnull
    @Override
    public IPlanStatistic incrementfail()
    {
        m_fail.incrementAndGet();
        return this;
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
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
    public int hashCode()
    {
        return m_plan.hashCode();
    }

    @Override
    @SuppressFBWarnings( "EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS" )
    public boolean equals( final Object p_object )
    {
        return ( p_object instanceof IPlanStatistic || p_object instanceof IPlan ) && this.hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "successful [{0}], fail [{1}]: {2}", m_successful.get(), m_fail.get(), m_plan );
    }

    @Override
    public int compareTo( @Nonnull final IPlanStatistic p_other )
    {
        return Integer.compare( this.hashCode(), p_other.hashCode() );
    }

    /**
     * factory
     *
     * @param p_plan plan object
     * @return statistic object
     */
    @Nonnull
    public static IPlanStatistic of( @Nonnull final IPlan p_plan )
    {
        return new CPlanStatistic( p_plan );
    }

}
