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
// agent for testing single & parallel execution structure
// -----


// initial-goal
!test.


/**
 * base test for staring executions
 */
+!test <-
    .log( "main" );

    !doublecall;
    !doublecall;

    !multiple( "first" );
    !multiple( "second" );

    !twovaluesequaltype( 1, 2 );
    !twovaluesdiffenttype( 5, "test" );

    L = twovaluesliteral( 5, foo(3) );
    !L
.


/**
 * double plan call but single execution
 */
+!doublecall <-
    .log("single run")
.

/**
 * called mutiple times
 */
+!multiple(X) <-
    .log(X);
    !single
.

/*
 * called single times
 */
+!single <-
    .log("single");
    .stop
.

/**
 * test plan
 */
+!twovaluesequaltype(X,Y) <-
    .log("twovalues equal type")
.

/**
 * test plan
 */
+!twovaluesdiffenttype(N,M) <-
    .log("twovalues different type")
.

/**
 * test plan
 */
+!twovaluesliteral(X, foo(Y)) <-
    .log("twovalues with literal")
.