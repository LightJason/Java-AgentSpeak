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

package org.lightjason.agentspeak.agent;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;


/**
 * default play towers of hanoi with an agent
 */
public final class TestCHanoiTowers extends IBaseHanoiTowers
{

    /**
     * test initialize
     *
     * @throws Exception on initialize error
     */
    @Before
    public void initialize() throws Exception
    {
        this.initialize(
            1, 3, 3,
            "src/test/resources/agent/hanoi.asl", Stream.of() );
    }


    /**
     * test execution
     *
     * @throws InterruptedException is thrown on execution error
     */
    @Test
    public void play() throws InterruptedException
    {
        this.execute();
    }

    /**
     * main method for manual test
     *
     * @param p_args CLI arguments
     * @throws Exception is thrown on any error
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new TestCHanoiTowers().invoketest();
    }
}
