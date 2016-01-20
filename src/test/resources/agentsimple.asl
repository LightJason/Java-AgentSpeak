hallo(123).
second(true).

+!drive
    <-
        [A|B|C] = [1,2,3];

        [O|P] =.. foo( blub(1), blah(3) );
        [H|I] = P;
        generic/print(A,B,C,  O,P,H,I);

        !!drive.