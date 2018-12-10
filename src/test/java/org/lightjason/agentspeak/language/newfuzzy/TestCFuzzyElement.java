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

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.language.newfuzzy.defuzzyfication.CCOA;
import org.lightjason.agentspeak.language.newfuzzy.set.EFourElement;

import java.util.stream.Stream;


/**
 * test fuzzy sets
 *
 * Stream<IFuzzyValue> -> t-Norm (convert stream to single value) use collector or reduce -> defuzzification
 */
public final class TestCFuzzyElement
{

    /**
     * test for defuzzification
     */
    @Test
    public void coa()
    {
        Assert.assertEquals(
            EFourElement.MEDIUMLOW,

            new CCOA<>( EFourElement.class ).defuzzify(
                Stream.of(
                    CFuzzyValue.of( EFourElement.LOW, 0.6 ),
                    CFuzzyValue.of( EFourElement.MEDIUMLOW, 0.4 ),
                    CFuzzyValue.of( EFourElement.MEDIUMHIGH, 0.2 ),
                    CFuzzyValue.of( EFourElement.HIGH, 0.2 )
                )
            )
        );
    }


}
