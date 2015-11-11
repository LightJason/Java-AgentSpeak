routingtype("fast").
scrambling(0.4).
target( longitude(53.3), latitude(-9.3) ).

!drive.

setSpeed(X) :- setProperty("speed", X).


@fuzzy(0.8)
+!accelerate :
    current_speed(Speed) &&
    distance_predecessor([Distance|_]) &&
    Distance > Speed <-
    Speed++;
    setSpeed( Speed );
    !!drive.

@fuzzy(0.5)
+!decelerate :
    current_speed(Speed) &&
    Speed > 10 <-
    Speed--;
    setSpeed( Speed )
    !!drive.

+!decelerate :
    current_speed(Speed) &&
    routingtype(Type) &&
    Speed <= 10 <-
    reroute( target, Type );
    !!decelerate.


@fuzzy(0.7)
+!drive <- !!accelerate.
