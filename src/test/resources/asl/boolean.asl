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

// -----
// agent for testing boolean calls
// @iteration 2
// @testcount 3
// -----

// initial-goal
!test.

/**
 * test boolean operator
 */
+!test <-
    BAnd = .bool/and( true, false, true );
    BAnd = ~BAnd;
    .test/result( BAnd, "bool-and has been failed" );

    .test/result( .bool/or( true, false, false ), "bool-or has been failed" );
    .test/result( .bool/xor( true, false, true, false ), "bool-xor has been failed" );

    .generic/print("bool executed completly")
.