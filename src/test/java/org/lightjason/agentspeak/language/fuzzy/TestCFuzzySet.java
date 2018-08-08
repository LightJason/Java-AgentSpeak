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
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.newfuzzy.value.IFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.set.EBoolean;

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
        final Set<IFuzzyValue<EBoolean>> l_result = EBoolean.TRUE.success( IAgent.EMPTY, IPlan.EMPTY ).collect( Collectors.toSet() );
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
        final Set<IFuzzyValue<EBoolean>> l_result = EBoolean.TRUE.fail( IAgent.EMPTY, IPlan.EMPTY ).collect( Collectors.toSet() );
        Assert.assertTrue(
            Stream.of(
                EBoolean.TRUE.apply( 0 ),
                EBoolean.FALSE.apply( 1 )
            ).parallel().allMatch( l_result::contains )
        );

    }
}
