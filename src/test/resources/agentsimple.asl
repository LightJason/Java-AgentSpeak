hallo(123).
second(true).

+!drive
    <-
        generic/print(Score, Cycle);

        L = collection/list/range(1, 40);
        [A|B|C|_|D|E|F|G] = L;
        generic/print(A,B,C,D,E,F,G);


        [O|P] =.. foo( blub(1), blah(3) );
        [H|I] = P;
        generic/print(O,P,H,I);

        X = true;
        X = !X;
        generic/print(X);

        Z = 4 ** 0.5;
        Z = 10 * Z;
        generic/print(Z);

        < true, 5 >;

        // sequencial lambda expression
        (L) -> Y : generic/print(Y);
        // parallel lambda expression
        @(L) -> Y | R : R = Y+1;

        generic/print(L);
        generic/print(R);

        // sequential unification
        >>foo(U);
        // parallel unification
        >>@foo(U);

        if ( Z > 100.0 )
            generic/print("if true")
        else
            generic/print("if false")
        .