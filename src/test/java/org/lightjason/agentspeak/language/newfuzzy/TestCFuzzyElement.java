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

package org.lightjason.agentspeak.language.newfuzzy;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.newfuzzy.defuzzyfication.CWOA;
import org.lightjason.agentspeak.language.newfuzzy.defuzzyfication.CCOG;
import org.lightjason.agentspeak.language.newfuzzy.membership.IFuzzyMembership;
import org.lightjason.agentspeak.language.newfuzzy.set.EFourElement;
import org.lightjason.agentspeak.language.newfuzzy.set.EThreeElement;
import org.lightjason.agentspeak.language.newfuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 * test fuzzy sets
 *
 * Stream<IFuzzyValue> -> t-Norm (convert stream to single value) use collector or reduce -> defuzzification
 */
public final class TestCFuzzyElement
{

    /**
     * test of center-of-area
     */
    @Test
    public void coa()
    {
        Assert.assertEquals(
            EFourElement.MEDIUMLOW,

            new CWOA<>( EFourElement.class, EFourElement.HIGH ).defuzzify(
                Stream.of(
                    CFuzzyValue.of( EFourElement.LOW, 0.6 ),
                    CFuzzyValue.of( EFourElement.MEDIUMLOW, 0.4 ),
                    CFuzzyValue.of( EFourElement.MEDIUMHIGH, 0.2 ),
                    CFuzzyValue.of( EFourElement.HIGH, 0.2 )
                )
            )
        );
    }


    /**
     * test of center-of-gravizry
     */
    @Test
    public void ccog()
    {
        new CCOG<>(
            EThreeElement.class,
            EThreeElement.LOW,
            new IFuzzyMembership<>()
            {
                @NonNull
                @Override
                public Stream<IFuzzyValue<?>> success()
                {
                    return Stream.of();
                }

                @NonNull
                @Override
                public Stream<IFuzzyValue<?>> fail()
                {
                    return Stream.of();
                }

                @Override
                public Stream<Number> range( @NonNull final EThreeElement p_value )
                {
                    switch ( p_value )
                    {
                        case LOW:
                            return Stream.of( 0, 10, 20 );

                        case MEDIUM:
                            return Stream.of( 30, 40, 50, 60 );

                        case HIGH:
                            return Stream.of( 70, 80, 90, 100 );

                        default:
                            return Stream.of();
                    }
                }

                @Override
                public Stream<IFuzzyValue<?>> apply( final Number p_number )
                {
                    return Stream.of();
                }

                @Nonnull
                @Override
                public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
                {
                    return p_agent;
                }
            }
        ).defuzzify(
            Stream.of(
                CFuzzyValue.of( EThreeElement.LOW, 0.1 ),
                CFuzzyValue.of( EThreeElement.MEDIUM, 0.2 ),
                CFuzzyValue.of( EThreeElement.HIGH, 0.5 )
            )
        );

    }

}
