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
// agent for testing deconstruct operator
// -----

// initial-goal
!test.


/**
 * test the deconstruct operator
 */
+!test <-
        [O|P] =.. foo( blub(1), blah(3) );
        .generic/print("first deconstruct", O, P);
        .test/result( .bool/equal( O, "foo" ), "first deconstruct has been failed" );

        [H|I] = P;
        [A|C] =.. H;
        [B|D] =.. I;

        .generic/print("second deconstruct", H, I, A, C, B, D);

        .test/result( .bool/equal( A, "blub" ), "second deconstruct has been failed" );
        .test/result( .bool/equal( B, "blah" ), "third deconstruct has been failed" );

        .test/result( .bool/equal( .collection/list/get(C, 0), 1 ), "deconstruct first value has been failed" );
        .test/result( .bool/equal( .collection/list/get(D, 0), 3 ), "deconstruct second value has been failed" );

        .generic/print("deconstruct executed completly")
.