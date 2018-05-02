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
// agent for testing trigger execution
// -----

// initial-goal
!test.

/**
 * base test
 */
+!test <-

    +belief(1);
    +belief(2);

    +belief(3);
    +belief(3);

    +delbelief("test");

    !goaloneparameter( 1 );
    !goaltwoparameter( 1, 2 );
    !goaltwoparametererror( 2, 3 );

    !goalconditionone( 5 );
    !goalconditiontwo( 8, 9 );

    !equalname( "foobar" );
    !equalname( "foo", "bar" );

    !hashcollision/Ea;
    !hashcollision/FB;

    !goaltrigger;
    !errortrigger
.


/**
 * test plan with hash collision
 **/
+!hashcollision/Ea <-
    .test/result( success )
.

+!hashcollision/FB <-
    .test/result( success )
.

/**
 * add belief trigger
 **/
+belief(N)
   : N == 1 <- .test/result( success )
   : N == 2 <- .test/result( success )
   : N == 3 <- .test/result( success )
.

/**
 * add belief trigger
 **/
+delbelief(X) <-
    .test/result( bool/equal( X, "test" ) );
    -delbelief(X)
.

/**
 * delete belief
 **/
-delbelief(X) <-
    .test/result( bool/equal( X, "test" ) )
.


/**
 * goal trigger
 **/
+!goaltrigger <-
    .test/result( success )
.

/**
 * error trigger
 **/
+!errortrigger <-
    .test/result( success );
    fail
.

/**
 * error trigger
 **/
-!errortrigger <-
    .test/result( success )
.


/**
 * goal with one parameter
 **/
+!goaloneparameter(X) <-
    .test/result( .bool/equal(X, 1) )
.

/**
 * goal with two parameter
 **/
+!goaltwoparameter(X, Y) <-
    .test/result( .bool/equal(X, 1) );
    .test/result( .bool/equal(Y, 2) )
.

/**
 * goal with two parameter and fail plan
 **/
+!goaltwoparametererror(X, Y) <-
    fail
.

-!goaltwoparametererror(X, Y) <-
    .test/result( .bool/equal(X, 2) );
    .test/result( .bool/equal(Y, 3) )
.

/**
 * goal with condition
 **/
+!goalconditionone(X)
    : X < 10 <- .test/result( .bool/equal(X, 5) )
.

/**
 * goal with condition and two parameter
 **/
+!goalconditiontwo(X, Y)
    : X < Y <-
        .test/result( .bool/equal(X, 8) );
        .test/result( .bool/equal(Y, 9) )
.

/**
 * goal with euqal name
 **/
+!equalname( X ) <-
    .test/result( .bool/equal(X, "foobar") )
.

+!equalname( X, Y ) <-
    .test/result( .bool/equal(X, "foo") );
    .test/result( .bool/equal(X, "bar") )
.
