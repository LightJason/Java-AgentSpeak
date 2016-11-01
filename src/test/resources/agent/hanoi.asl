!main.


+!main
    <-
        !!slice/take(0)
.


+!slice/take(T)
    <-
        generic/print("try take from tower", T);
        S = tower/pop(T);
        generic/print("get", S, "from tower", T);
        !slice/push(3, S)
.


+!push/slice(T, S)
    <-
        generic/print("try push on tower", T, S);
        tower/push(T,S);
        generic/print("push on tower", T, S, "success")
.


-!push/slice(T, S)
    : T > 1 <-
        generic/print("pushing on tower", T, "with", S, "fails");
        T--;
        !push/slice(T, S)

    : T <= 0 <-
        !push/slice(3, S)
.


-!slice/take(X)
    <-
        generic/print("cannot take slice from tower", X)
.
