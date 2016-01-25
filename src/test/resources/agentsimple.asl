hallo(123).
second(true).

+!drive
    <-
        generic/print(Score, Cycle);

        L = collection/list/range(1, 20);
        [A|B|C|_|D|E|F|G] = L;
        generic/print(A,B,C,D,E,F,G);


        [O|P] =.. foo( blub(1), blah(3) );
        [H|I] = P;
        generic/print(O,P,H,I);

        X = true;
        X = !X;
        generic/print(X);

        Z = 4 ** 0.5;
        Z = 100 * Z;
        generic/print(Z);

        // sequencial lambda expression
        (L) -> Y : generic/print(Y);
        generic/print("-------------");
        // parallel lambda expression
        @(L) -> Y : { generic/print(Y); };

        // sequential unification
        >>foo(U);
        // parallel unification
        >>@foo(U)

        .