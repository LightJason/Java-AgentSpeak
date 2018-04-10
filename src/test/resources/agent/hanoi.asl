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
// agent for playing towers-of-hanoi
// @see https://en.wikipedia.org/wiki/Tower_of_Hanoi
// ----


// initial-goal
!main.


/**
 * rule for calculating next tower index
 *
 * @param T current tower index
 * @param M number of towers
 **/
nexttower(T, M) :-
    T--;
    T = T < 0 ? M - 1 + T : T;
.


/**
 * main-plan
 **/
+!main
    <-
        !!slice/take(0)
.


/**
 * taking plan to pop slice from tower
 * or stopping execution if all slices
 * are moved
 *
 * @param T tower index
 **/
+!slice/take( T )

    // if not all slices are moved, try to get a slice
    : .tower/size(TowerMaxIndex) != SliceCount
        <-
            // try to get a slice from tower
            .generic/print( "agent", MyID, "tries to take slice from tower", T );
            S = .tower/pop( T );
            .generic/print( "agent", MyID, "gets", S, "from tower", T );

            // move the slice clockwise and push it if possible
            $nexttower( T, TowerMaxIndex );
            !slice/push( T, S )


    // all slices are moved so stop execution
    : .tower/size(TowerMaxIndex) == SliceCount
        <-
            .generic/print( "everything done" );
            .stop
.


/**
 * getting slice was failing, so try the next twoer clockwise
 *
 * @param T tower index
 **/
-!slice/take( T )
    <-
        .generic/print( "agent", MyID, "cannot take slice from tower", T );
        $nexttower( T, TowerCount );
        !slice/take( T )
.


/**
 * push a slice to a tower
 *
 * @param T tower index
 * @param S slice
 **/
+!slice/push( T, S )
    <-
        // try to push the slice on tower
        .generic/print( "agent", MyID, "tries to push on tower", T, S );
        .tower/push( T, S );
        .generic/print( "agent", MyID, "pushs on tower", T, S, "successfully" );

        // pushing is successful, just go to the next tower clockwise
        $nexttower( T, TowerMaxIndex );
        !slice/take( T )
.


/**
 * pushing to the was failing so try the next tower clockwise
 *
 * @param T tower index
 * @param S slice
 **/
-!slice/push( T, S )
    <-
        .generic/print( "agent", MyID, "pushing on tower", T, "with", S, "fails" );

        // just try next tower counter
        $nexttower( T, TowerMaxIndex );
        !slice/push( T, S )
.
