hallo(123).
second(true).

+!drive
    <-
        [A|B|C] = collection/list/range(1, 6);

        [O|P] =.. foo( blub(1), blah(3) );
        [H|I] = P;
        generic/print(A,B,C,  O,P,H,I);

        bind/cbinding/first();

        X = true || true;
        generic/print(X);

        // sequencial lambda expression
        // (O) -> Y | generic/print(Y);
        // parallel lambda expression
        // @(O) -> Y | { generic/print(Y); generic/print(O); };

        !!drive.