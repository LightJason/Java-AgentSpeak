hallo(123).
second(true).

+!drive
    <-
        generic/print(Score, Cycle);

        [A|B|C|_|D|E|F|G] = collection/list/range(1, 20);
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
        (O) -> Y | generic/print(Y);
        // parallel lambda expression
        @(O) -> Y | { generic/print(Y); generic/print(O); };

        // sequential unification
        >>foo(U);
        // parallel unification
        >>@foo(U)

        .