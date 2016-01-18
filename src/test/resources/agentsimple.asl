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

        [O|P] =.. foo( blub(1) );
        //H = 1;
        H = collection/list/get(P, 0);

        generic/print(X,Y, A,B,C, O,P,H);

        !!drive.