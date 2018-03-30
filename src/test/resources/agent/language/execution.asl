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
// agent for testing expression execution
// -----

// initial-goal
!test.

/**
 * base test
 */
+!test <-
    !stringvariableempty;
    !stringvariablesingle;
    !stringvariablemultiple
.



/**
 * execute by variable without parameter
 */
+!stringvariableempty <- X = "vartest"; !X.

/**
 * execution by variable with single parameter
 **/
+!stringvariablesingle <- X = "vartest"; !X(5).

/**
 * execution by variable with single parameter
 **/
+!stringvariablemultiple <- X = "vartest"; !X(7,3).



/**
 * variable test
 **/
+!vartest <- .test/result(success, "variable execution fails").

/**
 * variable test
 **/
+!vartest(X) <- .test/result( .bool/equal(X, 5), "variable execution test with 5 fails" ).

/**
 * variable test
 **/
+!vartest(X, Y) <-
    .test/result( .bool/equal(X, 7), "variable execution test with 7 fails" );
    .test/result( .bool/equal(Y, 3), "variable execution test with 3 fails" )
.


