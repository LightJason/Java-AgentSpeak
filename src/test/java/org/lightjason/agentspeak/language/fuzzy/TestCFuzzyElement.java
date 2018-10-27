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
import org.lightjason.agentspeak.language.newfuzzy.element.EBoolean;
import org.lightjason.agentspeak.language.newfuzzy.element.EThreeElement;
import org.lightjason.agentspeak.language.newfuzzy.value.IFuzzyValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test fuzzy sets
 *
 * Stream<IFuzzyValue> -> t-Norm (convert stream to single value) use collector or reduce -> defuzzification
 */
public final class TestCFuzzyElement
{

    /**
     * success fuzzy boolean
     */
    @Test
    public void successboolean()
    {
        final Set<IFuzzyValue<EBoolean>> l_result = EBoolean.TRUE.success().collect( Collectors.toSet() );
        Assert.assertTrue(
            Stream.of(
                EBoolean.TRUE.apply( 1 ),
                EBoolean.FALSE.apply( 0 )
            ).parallel().allMatch( l_result::contains )
        );
    }

    /**
     * fail fuzzy boolean
     */
    @Test
    public void failboolean()
    {
        final Set<IFuzzyValue<EBoolean>> l_result = EBoolean.FALSE.fail().collect( Collectors.toSet() );
        Assert.assertTrue(
            Stream.of(
                EBoolean.TRUE.apply( 0 ),
                EBoolean.FALSE.apply( 1 )
            ).parallel().allMatch( l_result::contains )
        );
    }




    /**
     * success fuzzy three element
     */
    @Test
    public void successthreeelement()
    {
        final Set<IFuzzyValue<EThreeElement>> l_result = EThreeElement.HIGH.success().collect( Collectors.toSet() );
        Assert.assertTrue(
            Stream.of(
                EThreeElement.HIGH.apply( 1 ),
                EThreeElement.MEDIUM.apply( 0 ),
                EThreeElement.LOW.apply( 0 )
            ).parallel().allMatch( l_result::contains )
        );
    }

    /**
     * fail fuzzy three element
     */
    @Test
    public void failthreeelement()
    {
        final Set<IFuzzyValue<EThreeElement>> l_result = EThreeElement.HIGH.fail().collect( Collectors.toSet() );
        Assert.assertTrue(
            Stream.of(
                EThreeElement.HIGH.apply( 0 ),
                EThreeElement.MEDIUM.apply( 0 ),
                EThreeElement.LOW.apply( 1 )
            ).parallel().allMatch( l_result::contains )
        );
    }

}
