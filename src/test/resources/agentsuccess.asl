routingtype("fast").
scrambling(0.4).
target( longitude(53.3), latitude(-9.3) ).
anno(3)[self("blub"), value(true), xxx(success)].
~neg(5).


!drive.


setSpeed(X) :- generic/print("speed", X, Y).


@fuzzy(0.8)
@score(0.2)
+!accelerate
    : !current_speed(Speed) && distance_predecessor([Distance]) && Distance > Speed && Score > 0.3 <-
        Speed = 5;
        Speed++;
        X = 3 * (5 + Speed);
        [H|T|V] = generic/min(3,4);
        [A|_|C] = [1,2,3];
        generic/print( Speed, generic/min(5, 9, 3), "test" )[ generic/min(1,2), generic/min(9,8) ];
        generic/print(Speed);
        !!drive;
        -+baz("hallo")
    <- true.

@fuzzy(0.5)
@score(0.4)
+!decelerate
    : current_speed(Speed) ^ Speed > 10 <-
        Speed = 5;
        Speed--;
        +foo( generic/min(5) );
        generic/print( Speed, "", "" );
        generic/print(1,2,3,4);
        generic/print(Speed);
        !!drive.

@atomic
@score(0.6)
+!decelerate
    : current_speed(Speed) || routingtype(Type) && Speed <= 10 <-
        @generic/print( generic/min(101, 102, 103, 104, 105), generic/min(1, 2, 3), generic/min(4, 5), generic/min(6, 7) );
        -blub(Speed);
        !!decelerate.

@fuzzy(0.7)
@score(infinity)
+!drive <- !!accelerate.
