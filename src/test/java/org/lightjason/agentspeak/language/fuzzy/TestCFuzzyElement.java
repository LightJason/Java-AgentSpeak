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

package org.lightjason.agentspeak.language.fuzzy;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.fuzzy.defuzzyfication.CCOG;
import org.lightjason.agentspeak.language.fuzzy.defuzzyfication.CWOA;
import org.lightjason.agentspeak.language.fuzzy.membership.CCrisp;
import org.lightjason.agentspeak.language.fuzzy.membership.IFuzzyMembership;
import org.lightjason.agentspeak.language.fuzzy.set.ECrisp;
import org.lightjason.agentspeak.language.fuzzy.set.EFourElement;
import org.lightjason.agentspeak.language.fuzzy.set.EThreeElement;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.util.stream.Stream;


/**
 * test fuzzy structure
 */
public final class TestCFuzzyElement extends IBaseTest
{

    /**
     * test of weighted-average-method
     */
    @Test
    public void cwoa()
    {
        Assert.assertEquals(
            1.0,

            new CWOA<>( EFourElement.class, IFuzzyMembership.EMPTY.raw(), CFuzzyValue.of( EFourElement.HIGH, 1 ) ).apply(
                Stream.of(
                    CFuzzyValue.of( EFourElement.LOW, 0.6 ),
                    CFuzzyValue.of( EFourElement.MEDIUMLOW, 0.4 ),
                    CFuzzyValue.of( EFourElement.MEDIUMHIGH, 0.2 ),
                    CFuzzyValue.of( EFourElement.HIGH, 0.2 )
                )
            ).doubleValue(),

            0.01
        );
    }


    /**
     * test of center-of-gravizry
     */
    @Test
    public void ccog()
    {
        Assert.assertEquals(
            67.419,

            new CCOG<>(
                EThreeElement.class,
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
                    public Stream<IFuzzyValue<?>> modify( final Stream<ITerm> p_arguments )
                    {
                        return Stream.of();
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
                        return Stream.of();
                    }

                    @Nonnull
                    @Override
                    public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
                    {
                        return p_agent;
                    }
                },

                CFuzzyValue.of( EThreeElement.HIGH, 1 )

            ).apply(
                Stream.of(
                    CFuzzyValue.of( EThreeElement.LOW, 0.1 ),
                    CFuzzyValue.of( EThreeElement.MEDIUM, 0.2 ),
                    CFuzzyValue.of( EThreeElement.HIGH, 0.5 )
                )
            ).doubleValue(),

            0.01
        );

    }

    /**
     * test crip membership
     */
    @Test
    public void membership()
    {
        Assert.assertArrayEquals(
            Stream.of( CFuzzyValue.of( ECrisp.FALSE, 1 ), CFuzzyValue.of( ECrisp.TRUE, 0 ) ).toArray(),
            new CCrisp<>( ECrisp.class ).apply( 0 ).toArray()
        );

        Assert.assertArrayEquals(
            Stream.of( CFuzzyValue.of( ECrisp.FALSE, 0 ), CFuzzyValue.of( ECrisp.TRUE, 1 ) ).toArray(),
            new CCrisp<>( ECrisp.class ).apply( 1 ).toArray()
        );


        Assert.assertArrayEquals(
            Stream.of(
                CFuzzyValue.of( EThreeElement.LOW, 1 ),
                CFuzzyValue.of( EThreeElement.MEDIUM, 0.5 ),
                CFuzzyValue.of( EThreeElement.HIGH, 0 )
            ).toArray(),
            new CCrisp<>( EThreeElement.class ).apply( 0 ).toArray()
        );

        Assert.assertArrayEquals(
            Stream.of(
                CFuzzyValue.of( EThreeElement.LOW, 0 ),
                CFuzzyValue.of( EThreeElement.MEDIUM, 0.5 ),
                CFuzzyValue.of( EThreeElement.HIGH, 1 )
            ).toArray(),
            new CCrisp<>( EThreeElement.class ).apply( 1 ).toArray()
        );
    }

    /**
     * test empty mempership
     */
    @Test
    public void membershipempty()
    {
        Assert.assertEquals( 0, IFuzzyMembership.EMPTY.apply( 0 ).count() );
        Assert.assertEquals( 0, IFuzzyMembership.EMPTY.apply( 1 ).count() );
    }
}
