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

package org.lightjason.agentspeak.language.fuzzy;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.language.newfuzzy.set.CCrispBoolean;
import org.lightjason.agentspeak.language.newfuzzy.set.CSmoothBoolean;
import org.lightjason.agentspeak.language.newfuzzy.set.IFuzzySet;
import org.lightjason.agentspeak.language.newfuzzy.value.IFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.element.EBoolean;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test fuzzy sets
 */
public final class TestCFuzzySet
{

    /**
     * success result test
     */
    @Test
    public void success()
    {
        final Set<IFuzzyValue<EBoolean>> l_result = new CSmoothBoolean().success().collect( Collectors.toSet() );
        Assert.assertTrue(
            Stream.of(
                EBoolean.TRUE.apply( 1 ),
                EBoolean.FALSE.apply( 0 )
            ).parallel().allMatch( l_result::contains )
        );

    }

    /**
     * success result test
     */
    @Test
    public void fail()
    {
        final Set<IFuzzyValue<EBoolean>> l_result = new CSmoothBoolean().fail().collect( Collectors.toSet() );
        Assert.assertTrue(
            Stream.of(
                EBoolean.TRUE.apply( 0 ),
                EBoolean.FALSE.apply( 1 )
            ).parallel().allMatch( l_result::contains )
        );
    }

    /**
     * test smooth boolean results
     */
    @Test
    public void smoothboolean()
    {
        final IFuzzySet<Number, EBoolean> l_fuzzyset = new CSmoothBoolean( 0.01 );

        Assert.assertTrue(
            l_fuzzyset.apply( 0 )
                      .parallel()
                      .allMatch( i -> l_fuzzyset.elementequal( i, EBoolean.TRUE.apply( 0.007 ) )
                                      || l_fuzzyset.elementequal( i, EBoolean.FALSE.apply( 0.993 ) ) )
        );

        Assert.assertTrue(
            l_fuzzyset.apply( 1 )
                      .parallel()
                      .allMatch( i -> l_fuzzyset.elementequal( i, EBoolean.FALSE.apply( 0.007 ) )
                                      || l_fuzzyset.elementequal( i, EBoolean.TRUE.apply( 0.993 ) ) )
        );

        Assert.assertTrue(
            l_fuzzyset.apply( 0.5 )
                      .parallel()
                      .allMatch( i -> l_fuzzyset.elementequal( i, EBoolean.FALSE.apply( 0.5 ) )
                                      || l_fuzzyset.elementequal( i, EBoolean.TRUE.apply( 0.5 ) ) )
        );
    }

    /**
     * test crisp boolean results
     */
    @Test
    public void crispboolean()
    {
        final IFuzzySet<Number, EBoolean> l_fuzzyset = new CCrispBoolean( 0.01 );

        Assert.assertTrue(
            l_fuzzyset.apply( 0 )
                      .parallel()
                      .allMatch( i -> l_fuzzyset.elementequal( i, EBoolean.TRUE.apply( 0 ) )
                                      || l_fuzzyset.elementequal( i, EBoolean.FALSE.apply( 1 ) ) )
        );

        Assert.assertTrue(
            l_fuzzyset.apply( 1 )
                      .parallel()
                      .allMatch( i -> l_fuzzyset.elementequal( i, EBoolean.FALSE.apply( 0 ) )
                                      || l_fuzzyset.elementequal( i, EBoolean.TRUE.apply( 1 ) ) )
        );

        Assert.assertTrue(
            l_fuzzyset.apply( 0.5 )
                      .parallel()
                      .allMatch( i -> l_fuzzyset.elementequal( i, EBoolean.FALSE.apply( 0 ) )
                                      || l_fuzzyset.elementequal( i, EBoolean.TRUE.apply( 1 ) ) )
        );
    }

}
