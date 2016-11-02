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

+!slice/take(T)
    : tower/size(TowerNumber) != SliceNumber
        <-
            generic/print( "agent", MyID, "tries to take from tower", T );
            S = tower/pop( T );
            generic/print( "agent", MyID, "gets", S, "from tower", T );

            // clockweise transpose
            $nexttower(T, TowerNumber);
            !slice/push(T, S)

    : tower/size(TowerNumber) == SliceNumber
        <-
            generic/print( "everything done" );
            stop()
.


-!slice/take(T)
    <-
        generic/print( "agent", MyID, "cannot take slice from tower", T );
        $nexttower(T, TowerNumber);
        !slice/take( T )
.


+!slice/push(T, S)
    <-
        generic/print( "agent", MyID, "tries to push on tower", T, S );
        tower/push( T, S );
        generic/print( "agent", MyID, "pushs on tower", T, S, "success" );

        // clockweise next tower
        $nexttower(T, TowerNumber);
        !slice/take(T)
.

-!slice/push(T, S)
    <-
        generic/print( "agent", MyID, "pushing on tower", T, "with", S, "fails" );

        // just try next tower counter
        $nexttower(T, TowerNumber);
        !slice/push( T, S )
.


/*

+!slice/take(T)
    : tower/size(TowerNumber) != SliceNumber
        <-
            generic/print( "agent", MyID, "tries to take from tower", T );
            S = tower/pop( T );
            generic/print( "agent", MyID, "gets", S, "from tower", T );
            +intention( TowerNumber, S );
            !slice/push( TowerNumber, S )

    : tower/size(TowerNumber) == SliceNumber
        <-
            generic/print( "everything done" );
            stop()
.

-!slice/take(T)
    <-
        generic/print( "agent", MyID, "cannot take slice from tower", T );
        T++;
        T = T % TowerNumber;
        !slice/take( T )
.






-!slice/push(T, S)
    <-
        generic/print( "agent", MyID, "pushing on tower", T, "with", S, "fails" );

        R = MyID;
        R++;
        R = R % AgentNumber;
        message/send( R, T,S );

        generic/agent/sleep(2)
.



+!receive(M, from(A))
    : >>( message(T,S), M )
        <-
            generic/print( "agent", MyID, "receives message from agent", A, "that he will put", S, "on tower", T )

.


+!wakeup
    : >>intention(T,S)
        <-
            generic/print( "agent", MyID, "with intention to push", S, "on tower", T);
            !slice/push( T,S )
.

*/