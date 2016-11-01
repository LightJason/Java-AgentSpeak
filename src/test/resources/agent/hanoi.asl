!main.


+!main
    <-
        !!takeslice(0)
.


+!takeslice(X)
    <-
        Y = tower/pop(X);
        generic/print("take from tower", X);
        !takeslice(0)
.


-!takeslice(X)
    <-
        generic/print("cannot take slice from tower", X)
.
