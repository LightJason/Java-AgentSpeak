!main.


+!main
    <-
        !!slice/take(0)
.


+!slice/take(T)
    : tower/size(MaxTowerNumber) \== MaxSliceNumber
        <-
            generic/print( "agent", MyID, "tries to take from tower", T );
            S = tower/pop( T );
            generic/print( "agent", MyID, "gets", S, "from tower", T );
            !slice/push( MaxTowerNumber, S )

    : tower/size(MaxTowerNumber) == MaxSliceNumber
        <-
            generic/print( "everything done" )
.

-!slice/take(T)
    <-
        generic/print( "agent", MyID, "cannot take slice from tower", T );
        T++;
        !slice/take( T )
.



+!slice/push(T, S)
    <-
        generic/print( "agent", MyID, "tries to push on tower", T, S );
        tower/push( T, S );
        generic/print( "agent", MyID, "pushs on tower", T, S, "success" );
        !slice/take(0)
.


-!slice/push(T, S)
    : T > 1 <-
        generic/print( "agent", MyID, "pushing on tower", T, "with", S, "fails" );
        T--;
        !slice/push( T, S )

    : T <= 0 <-
        !slice/push( MaxTowerNumber, S)
.



