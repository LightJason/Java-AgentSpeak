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
// agent for testing rules
// -----

// initial-goal
!test.


// --- logical rules -------------------------------------------------------------------------------------------------------------------------------------------

fibonacci(X, R)
    // order of the rules are indeterministic, so for avoid indeterministic behaviour
    // add the condition, when the rule can be executed first
    :- X <= 2;  R = 1
    :- X > 2;   TA = X - 1; TB = X - 2; $fibonacci(TA,A); $fibonacci(TB,B); R = A+B
.

ackermann(N, M, R)
    :- N == 0; R = M+1
    :- M == 0; TN = N - 1; $ackermann(TN, 1, R)
    :- TM = M - 1; $ackermann(N, TM, RI); TN = N - 1; $ackermann(TN, RI, R)
.

factorial(N,R)
    :- N == 1; R = 1
    :- N--; $factorial(N,O); R = R * O
.

myfunction(X) :- .generic/print("my logical rule", X).

// -------------------------------------------------------------------------------------------------------------------------------------------------------------



/**
 * base test
 */
+!test <-
    !testdirectcall;
    !testruledirect;
    !testrulevariable
.


/**
 * test direct rule call
 */
+!testdirectcall <-
    $myfunction("fooooooo");
    .test/result( success )
.


/**
 * test rule call with variable argument
 */
+!testruledirect <-
    $fibonacci(8, FIB);
    R = FIB == 21.0;
    .test/result( R, "rule direct call has been failed" );
    .generic/print("rule execution (fibonacci)", FIB )
.


/**
 * test rule variable call
 */
+!testrulevariable <-
    RULE = "fibonacci";
    $.RULE(8,FIB);
    R = FIB == 21.0;
    .test/result( R, "rule variable call has been failed" );
    .generic/print("rule execution (fibonacci)", FIB )
.
