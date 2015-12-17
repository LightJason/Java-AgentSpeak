routingtype("fast").
scrambling(0.4).
target( longitude(53.3), latitude(-9.3) ).
anno(3)[self("blub"), value(true), xxx(success)].
~neg(5).

!drive.

setSpeed(X) :- setProperty("speed", X, Y).

@fuzzy(0.8)
+!accelerate
    : current_speed(Speed) && distance_predecessor([Distance]) && Distance > Speed && Score > 0.3 <-
        //Speed++;
        setProperty( Speed, "", 1 )[min(1,2),min(9,8)];
        !!drive;
        -+baz("hallo")
    <- true.

@fuzzy(0.5)
+!decelerate
    : current_speed(Speed) && Speed > 10 <-
        //Speed--;
        +foo(5);
        setProperty( Speed, "", "" );
        print(1,2,3,4);
        !!drive.

@atomic
+!decelerate
    : current_speed(Speed) && routingtype(Type) && Speed <= 10 <-
        @print( min(3,5), Type );
        -blub(Speed);
        !!decelerate.


@fuzzy(0.7)
+!drive <- !!accelerate.
