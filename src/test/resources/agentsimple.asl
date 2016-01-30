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
        Statistic = math/statistic/createstatistic();
        math/statistic/addstatisticvalue(Statistic, RV, L);

        SMax = math/statistic/getstatisticvalue(Statistic, "max");
        SMin = math/statistic/getstatisticvalue(Statistic, "min");
        SCount = math/statistic/getstatisticvalue(Statistic, "count");
        SPopVariance = math/statistic/getstatisticvalue(Statistic, "populationvariance");
        SQuadraticMean = math/statistic/getstatisticvalue(Statistic, "quadraticmean");
        SSecondMom = math/statistic/getstatisticvalue(Statistic, "secondmoment");
        SStd = math/statistic/getstatisticvalue(Statistic, "standarddeviation");
        SSum = math/statistic/getstatisticvalue(Statistic, "sum");
        SSumSq = math/statistic/getstatisticvalue(Statistic, "sumsquare");
        SVar = math/statistic/getstatisticvalue(Statistic, "variance");
        SMean = math/statistic/getstatisticvalue(Statistic, "mean");

        generic/print("statistic", SMax, SMin, SCount, SPopVariance, SQuadraticMean, SSecondMom, SStd, SSum, SSumSq, SVar, SMean );


        // check hash
        HashMD5 = crypto/hash( "md5", "hallo" );
        HashSHA = crypto/hash( "sha-256", "string test1", "second data", 4, 5, 6);
        generic/print("MD5 & SHA-256 hash", HashMD5, HashSHA);


        // check crypto
        //Encrypt = crypto/encrypt( "AES", "my secret password", "my message test 1", 1, 5, 9, "another message part");
        //generic/print("Crypt", Encrypt);


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
