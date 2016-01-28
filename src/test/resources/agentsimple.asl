hallo(123).
second(true).
~lub("hallo").

+!drive
    <-
        // test manual individual & internal variables
        generic/print(Score, Cycle, MyConstInt, MyConstString);


        // check collections
        L = collection/list/range(1, 40);
        [A|B|C|_|D|E|F|G] = L;
        generic/print(A,B,C,D,E,F,G);


        // check literal accessing
        [O|P] =.. foo( blub(1), blah(3) );
        [H|I] = P;
        generic/print(O,P,H,I);


        // check simple arithmetic
        Z = 10 * 4 ** 0.5;
        generic/print(Z);


        // check blas arithmetic
        /*
        M = math/blas/matrix/create(2,2);
        math/blas/matrix/set(M, 0,0, 1);
        math/blas/matrix/set(M, 0,1, 2);
        math/blas/matrix/set(M, 1,0, 3);
        math/blas/matrix/set(M, 1,1, 4);
        generic/print(M);
        */

        // check sequencial & parallel lambda expression
        (L) -> Y : generic/print(Y);
        @(L) -> Y | R : R = Y+1;

        generic/print(L);
        generic/print(R);


        // sequential & parallel unification
        //>>foo(U);
        //@>>foo(U);


        // check condition and plan passing
        if ( Z > 100.0 )
            generic/print("if true")
        else
            generic/print("if false");

        Z < 100.0;
        generic/print("plan passed")

        .