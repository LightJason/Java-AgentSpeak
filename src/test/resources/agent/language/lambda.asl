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

// -----
// agent for testing lambda expression
// -----

// initial-goal
!test.


/**
 * test lambda expression
 */
+!test <-
    L = .collection/list/range(1, 20);
    (L) -> Y : .generic/print( "lambda value", Y);

    @(L) -> Y | R : R = Y+1;
    .generic/print("lambda return", R);

    BL = .agent/belieflist( "hallo" );
    (BL) -> Y : .generic/print( "lambda belief items", Y);


    PL = .agent/planlist;
    (PL) -> Y : .generic/print( "lambda plan items", Y);

    .test/result( success );
    .generic/print("lambda executed completly")
.
