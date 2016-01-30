hallo(123).
second(true).
~lub("hallo").

+!drive
    : @>>(foo(X), X > 2) <- true
    : >>foo(X) && X > 2  <- generic/print("hallo")
    <-
        // test manual individual & internal variables
        generic/print("constants", Score, Cycle, MyConstInt, MyConstString);


        // check collections
        L = collection/list/range(1, 20);
        [A|B|C|_|D|E|F|G] = L;
        generic/print("list elements", A,B,C,D,E,F,G);


        // check literal accessing
        [O|P] =.. foo( blub(1), blah(3) );
        [H|I] = P;
        generic/print("deconstruct", O,P,H,I);


        // check simple arithmetic
        Z = 10 * 4 ** 0.5;
        generic/print("simple expression", Z);


        // check blas arithmetic
        M = math/blas/matrix/create(2,2);
        math/blas/matrix/set(M, 0,0, 1);
        math/blas/matrix/set(M, 0,1, 2);
        math/blas/matrix/set(M, 1,0, 3);
        math/blas/matrix/set(M, 1,1, 4);
        Det = math/blas/matrix/determinant(M);
        EV  = math/blas/matrix/eigenvalue(M);
        generic/print("matrix", M,Det,EV);


        // check random
        Distribution = math/statistic/createdistribution( "normal", -100, 100 );
        RV = math/statistic/randomsample( Distribution, 8 );
        generic/print("random", RV);


        // check statistics
        Statistic = math/statistic/createsummarystatistic();
        math/statistic/addsummarystatisticvalue(Statistic, RV, L);

        SMax = math/statistic/getsummarystatisticvalue(Statistic, "max");
        SMin = math/statistic/getsummarystatisticvalue(Statistic, "min");
        SCount = math/statistic/getsummarystatisticvalue(Statistic, "count");
        SPopVariance = math/statistic/getsummarystatisticvalue(Statistic, "populationvariance");
        SQuadraticMean = math/statistic/getsummarystatisticvalue(Statistic, "quadraticmean");
        SSecondMom = math/statistic/getsummarystatisticvalue(Statistic, "secondmoment");
        SStd = math/statistic/getsummarystatisticvalue(Statistic, "standarddeviation");
        SSum = math/statistic/getsummarystatisticvalue(Statistic, "sum");
        SSumSq = math/statistic/getsummarystatisticvalue(Statistic, "sumsquare");
        SVar = math/statistic/getsummarystatisticvalue(Statistic, "variance");
        SMean = math/statistic/getsummarystatisticvalue(Statistic, "mean");

        generic/print("statistic", SMax, SMin, SCount, SPopVariance, SQuadraticMean, SSecondMom, SStd, SSum, SSumSq, SVar, SMean );


        // check sequencial & parallel lambda expression
        (L) -> Y : generic/print(Y);
        @(L) -> Y | R : R = Y+1;

        generic/print("list", L);
        generic/print("lambda return", R);


        // check sequential & parallel unification
        >>foo(U);
        @>>foo(U);


        // check condition
        if ( Z > 100.0 )
            generic/print("if true")
        else
            generic/print("if false");


        // check plan passing
        Z < 100.0;
        generic/print("plan passed")

        .
