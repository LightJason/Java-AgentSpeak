hallo(123).
second(true).

!drive.

+!drive
    <-
        generic/print("drive default");
        generic/print( math/min(200,3,4,5,67) );

        X = true;
        Y = 10;

        [A|B|C] = [1,2,3];
        generic/print(A,B,C);

        [O|P] =.. foo( blub(1) );
        generic/print(O, P);

        !!drive.