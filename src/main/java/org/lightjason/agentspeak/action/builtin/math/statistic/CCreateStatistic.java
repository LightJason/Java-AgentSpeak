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

package org.lightjason.agentspeak.action.builtin.math.statistic;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedDescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.error.CEnumConstantNotPresentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * action to create a statistic.
 * The action creates statistic objects to collect data,
 * each argument must be a string with "summary" or "descriptive"
 * for a summary or descriptive statistic object, on no arguments
 * a summary statistic object is created
 *
 * {@code [S1|S2] = .math/statistic/createstaistic("summary", ["descriptive"]);}
 * @see http://commons.apache.org/proper/commons-math/userguide/stat.html
 */
public final class CCreateStatistic extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8275199738531829131L;

    /**
     * ctor
     */
    public CCreateStatistic()
    {
        super( 3 );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {

        (
            p_argument.size() == 0
            ? Stream.of( EType.SUMMARY )
            : CCommon.flatten( p_argument ).map( ITerm::<String>raw ).map( EType::of )
        ).map( i -> i.apply( p_parallel ) )
         .map( CRawTerm::of ).forEach( p_return::add );

        return Stream.of();
    }


    /**
     * enume statistic type
     */
    private enum EType implements Function<Boolean, StatisticalSummary>
    {
        SUMMARY,
        DESCRIPTIVE;

        /**
         * additional factory
         *
         * @param p_value string
         * @return enum
         */
        @Nonnull
        public static EType of( @Nonnull final String p_value )
        {
            return EType.valueOf( p_value.trim().toUpperCase( Locale.ROOT ) );
        }

        @Override
        public StatisticalSummary apply( final Boolean p_parallel )
        {
            switch ( this )
            {
                case SUMMARY:
                    return p_parallel
                           ? new SynchronizedSummaryStatistics()
                           : new SummaryStatistics();

                case DESCRIPTIVE:
                    return p_parallel
                           ? new SynchronizedDescriptiveStatistics()
                           : new DescriptiveStatistics();

                default:
                    throw new CEnumConstantNotPresentException( this.getClass(), this.toString() );
            }
        }
    }
}
