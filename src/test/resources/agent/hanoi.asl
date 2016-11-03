pusherror(0).


!main.



nexttower(T, M) :-
    T--;
    T = T < 0 ? M - 1 + T : T
.

incrementpusherror(X, T) :-
    -pusherror(X);
    X++;
    X = X % T;
    +pusherror(X)
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
            C = T;
            $nexttower(T, TowerCount);
            !slice/push(C, T, S)

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


+!slice/push( C, T, S )
    <-
        generic/print( "agent", MyID, "tries to push on tower", T, S );
        tower/push( T, S );
        generic/print( "agent", MyID, "pushs on tower", T, S, "success" );

        // try to clear the current tower
        !slice/take( C )
.

-!slice/push(C, T, S)
    : >>( pusherror(X), X <= TowerCount )
        <-
            generic/print( "agent", MyID, "pushing on tower", T, "with", S, "fails - current fails", X );

            // just try next tower counter
            $incrementpusherror( X, TowerCount );
            $nexttower( T, TowerCount );
            !slice/push( C, T, S )

    : >>( pusherror(X), X == TowerMaxIndex )
        <-
            generic/print( "agent", MyID, "tested all towers but no one can passed, pushing on tower", T, "with", S, "fails - current fails", X );
            $incrementpusherror( X, TowerCount );
            !!slice/push( C, T, S );

            $nexttower( C, TowerCount );
            !slice/take( C )
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