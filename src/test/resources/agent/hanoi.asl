pusherror(0).


!main.



nexttower(T, M) :-
    T--;
    T = T < 0 ? M - 1 + T : T
.

incrementpusherror(E, T) :-
    -pusherror(E);
    E++;
    E = E % T;
    +pusherror(E)
.

resetpusherror() :-
    >>pusherror(E);
    -pusherror(E);
    +pusherror(0)
.


+!main
    <-
        !!slice/take(0)
.


// https://de.wikipedia.org/wiki/T%C3%BCrme_von_Hanoi#Iterativer_Algorithmus

+!slice/take( T )
    : tower/size(TowerMaxIndex) != SliceCount
        <-
            generic/print( "agent", MyID, "tries to take from tower", T );
            S = tower/pop( T );
            generic/print( "agent", MyID, "gets", S, "from tower", T );

            // clockweise transpose
            $nexttower( T, TowerMaxIndex );
            !slice/push( T, S )

    : tower/size(TowerMaxIndex) == SliceCount
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
        generic/print( "agent", MyID, "pushs on tower", T, S, "success" );

        // try to clear the current tower
        $resetpusherror;
        !slice/take( T )
.

-!slice/push( T, S )
    : >>( pusherror(E), E < TowerMaxIndex )
        <-
            $incrementpusherror( E, TowerCount );
            generic/print( "agent", MyID, "pushing on tower", T, "with", S, "fails - current fails", E );

            // just try next tower counter

            $nexttower( T, TowerMaxIndex );
            !slice/push( T, S )

    : >>( pusherror(E), E >= TowerMaxIndex )
        <-
            $incrementpusherror( E, TowerCount );
            generic/print( "agent", MyID, "tested all towers but no one can passed, pushing on tower", T, "with", S, "fails - current fails", E );

            !!slice/push( T, S );

            $nexttower( T, TowerMaxIndex );
            !slice/take( T )
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