routingtype("fast").
scrambling(0.4).
target( longitude(53.3), latitude(-9.3) ).
anno(3)[self("blub"), value(true), xxx(success)].
~neg(5).


!drive.


setSpeed(X) :- setProperty("speed", X, Y).


@fuzzy(0.8)
@score(0.2)
+!accelerate
    : !(current_speed(Speed)) && distance_predecessor([Distance]) && Distance > Speed && Score > 0.3 <-
        Speed = 5;
        Speed++;
        X = 5 + Speed * 3;
        setProperty( Speed, min(5, 9, 3), "test" )[ min(1,2), min(9,8) ];
        print(Speed);
        !!drive;
        -+baz("hallo")
    <- true.

@fuzzy(0.5)
@score(0.4)
+!decelerate
    : current_speed(Speed) ^ Speed > 10 <-
        Speed = 5;
        Speed--;
        +foo(min(5));
        setProperty( Speed, "", "" );
        print(1,2,3,4);
        print(Speed);
        !!drive.

@atomic
@score(0.6)
+!decelerate
    : current_speed(Speed) || routingtype(Type) && Speed <= 10 <-
        @print( min(101, 102, 103, 104, 105), min(1, 2, 3), min(4, 5), min(6, 7) );
        -blub(Speed);
        !!decelerate.

@fuzzy(0.7)
@score(infinity)
+!drive <- !!accelerate.
