!main.



nexttower(T, M) :-
    T--;
    T = T < 0 ? M - 1 + T : T
.



+!main
    <-
        !!slice/take(0)
.


// https://de.wikipedia.org/wiki/T%C3%BCrme_von_Hanoi#Iterativer_Algorithmus

+!slice/take( T )
    : tower/size(TowerMaxIndex) != 3
        <-
            generic/print( "agent", MyID, "tries to take from tower", T );
            S = tower/pop( T );
            generic/print( "agent", MyID, "gets", S, "from tower", T );

            // clockweise transpose
            $nexttower( T, TowerMaxIndex );
            !slice/push( T, S )

    : tower/size(TowerMaxIndex) == 3
        <-
            generic/print( "everything done" );
            stop()
.


-!slice/take( T )
    <-
        generic/print( "agent", MyID, "cannot take slice from tower", T );
        $nexttower( T, TowerCount );
        !slice/take( T )
.


+!slice/push( T, S )
    <-
        generic/print( "agent", MyID, "tries to push on tower", T, S );
        tower/push( T, S );
        generic/print( "agent", MyID, "pushs on tower", T, S, "successfully" );

        // pushing is successful, just go to the next tower
        $nexttower( T, TowerMaxIndex );
        !slice/take( T )
.

-!slice/push( T, S )
        <-
            generic/print( "agent", MyID, "pushing on tower", T, "with", S, "fails" );

            // just try next tower counter
            $nexttower( T, TowerMaxIndex );
            !slice/push( T, S )
.
