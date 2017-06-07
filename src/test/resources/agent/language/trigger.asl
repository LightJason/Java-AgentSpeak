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

    !goaltrigger;
    !errortrigger
.


/**
 * add belief trigger
 **/
+belief(N)
   : N == 1 <- test/result( success )
   : N == 2 <- test/result( success )
   : N == 3 <- test/result( success )
.

/**
 * delete belief trigger
 **/
+delbelief(X) <- test/result( bool/equal( X, "test" ) ); -delbelief(X).

/**
 * delete belief
 **/
-delbelief(X) <- test/result( bool/equal( X, "test" ) ).


/**
 * goal trigger
 **/
+!goaltrigger <- test/result( success ).

/**
 * error trigger
 **/
+!errortrigger <- test/result( success ); fail.

/**
 * error trigger
 **/
-!errortrigger <- test/result( success ).