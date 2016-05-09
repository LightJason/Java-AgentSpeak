// --- initial beliefs -----------------------------------------------------------------------------------------------------------------------------------------

~hallo("text").
hallo(123)[abc(8),value('xxxx')].
hallo(666)[abc(8)].
hallo(123).
hallo("foo").
hallo(1111).
hallo(600).
hallo(999).
hallo(900).
hallo(888).
hallo(777).
hallo(700).
hallo(foo(3)).
foo(blub(1),hallo("test")).
second(true).

// -------------------------------------------------------------------------------------------------------------------------------------------------------------



// --- initial goal --------------------------------------------------------------------------------------------------------------------------------------------

!main.

// -------------------------------------------------------------------------------------------------------------------------------------------------------------



// --- logical rules -------------------------------------------------------------------------------------------------------------------------------------------

fibonacci(X, R)
    // order of the rules are indeterministic, so for avoid indeterminisitic behaviour
    // add the condition, when the rule can be executed first
    :- X <= 2;  R = 1
    :- X > 2;   TA = X - 1; TB = X - 2; $fibonacci(TA,A); $fibonacci(TB,B); R = A+B
.

ackermann(N, M, R)
    :- N == 0; M > 0; R = M+1
    :- M == 0; N > 0; TN = N-1; $ackermann(TN, 1, RA); R = RA
    :- N > 0; M > 0; TN = N-1; TM = M-1; $ackermann(N, TM, RI); $ackermann(TN, RI, RO); R = RO
.

myfunction(X) :- generic/print("my logical rule", X).

// -------------------------------------------------------------------------------------------------------------------------------------------------------------



// --- plans ---------------------------------------------------------------------------------------------------------------------------------------------------

+counter(X) <- generic/print("belief 'counter' added with variable value [", X, "] in Cycle [", Cycle, "]").

+!mytest <- generic/print("my test plan without variable in cycle [", Cycle, "]").

+!mytest(X)
    <- generic/print("my test plan with variable value [", X, "] in cycle[", Cycle, "]");
       Y = X-1;
       !mytest(Y)
.

-!errorplan <- generic/print("fail plan (deletion goal) in cycle [", Cycle, "]").

+!errorplan
    <- generic/print("fail plan is failing in cycle [", Cycle, "]");
       fail
.

+!main

    : >>( hallo(X), generic/typ/isstring(X) ) <-
            generic/print("---", "first plan", "---", "unification variables", X)

    : >>( hallo(X), generic/typ/isnumeric(X) && X > 1000 )  <-
        generic/print("---", "second plan", "---", "unification variables", X)

    <-

        // --- manual individual & internal variables ----------------------------------------------------------------------------------------------------------

        generic/print("constants", Score, Cycle, "    ", MyConstInt, MyConstString, "    ", PlanFail, PlanFailRatio, PlanSuccessful, PlanSuccessfulRatio);

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- collections -------------------------------------------------------------------------------------------------------------------------------------

        L = collection/list/range(1, 20);
        [ A|B|C| _ |D|E|F|G ] = L;
        Intersect = collection/list/intersect( [1,2,3,4,5], [3,4,5,6,7], [3,8,9,5] );
        Union = collection/list/union( [1,2,3], [2,3,4], [3,4,5] );
        SD = collection/list/symmetricdifference( [1,2,3], [3,4] );
        CP = collection/list/complement( [1,2,3,4,5], [1,2] );

        generic/print("list elements", A,B,C,D,E,F,G, L);
        generic/print("intersection & union & symmetric difference & complement", Intersect, "--", Union, "--", SD, "--", CP);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- literal accessing -------------------------------------------------------------------------------------------------------------------------------

        [O|P] =.. foo( blub(1), blah(3) );
        [H|I] = P;
        generic/print("deconstruct", O,P,H,I);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- simple arithmetic -------------------------------------------------------------------------------------------------------------------------------

        Z = 10 * 4 ** 0.5;
        generic/print("simple expression", Z);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- string ------------------------------------------------------------------------------------------------------------------------------------------

        SBase64 = generic/string/base64encode( "Base64 encoded string" );
        SReverse = generic/string/reverse( "abcdefg" );
        SUpper = generic/string/upper("AbCdefg");
        SLower = generic/string/lower("AbCdefg");
        SReplace = generic/string/replace( "a1b1defg1xyz1ui", "1", "-" );
        SRand = generic/string/random( 20, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" );
        generic/print("string", SBase64, "--", SReverse, "--", SUpper, "--", SLower, "--", SRand, "--", SReplace);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- blas arithmetic ---------------------------------------------------------------------------------------------------------------------------------

        M = math/blas/matrix/create(2,2);
        math/blas/matrix/set(M, 0,0, 1);
        math/blas/matrix/set(M, 0,1, 2);
        math/blas/matrix/set(M, 1,0, 3);
        math/blas/matrix/set(M, 1,1, 4);
        Det = math/blas/matrix/determinant(M);
        EV  = math/blas/matrix/eigenvalue(M);
        generic/print("matrix", M,Det,EV);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- random ------------------------------------------------------------------------------------------------------------------------------------------

        Distribution = math/statistic/createdistribution( "normal", 20, 100 );
        RV = math/statistic/randomsample( Distribution, 8 );
        generic/print("random", RV);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- date / time -------------------------------------------------------------------------------------------------------------------------------------

        [Hour | Minute | Second | Nano] = generic/datetime/time();
        [Day | Month | Year | DayOfWeek | DayOfYear] = generic/datetime/date();
        generic/print("date & time", Hour, Minute, Second, Nano, "--", Day, Month, Year, DayOfWeek, DayOfYear);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- math functions ----------------------------------------------------------------------------------------------------------------------------------

        MinIdx = math/minindex(RV);
        MaxIdx = math/maxindex(RV);
        InRect = math/shape/inrectangle( 2,1,  0,0, 4,5);
        InCircle = math/shape/incircle( 2,1,  2,2, 1);
        InTriangle = math/shape/intriangle( 160,270,  350,320,  25,375,  40,55 );

        generic/print("min & max index", MinIdx, MaxIdx);
        generic/print("shapes (in)", "", "rectangle", InRect, "circle", InCircle, "triangle", InTriangle);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- statistics --------------------------------------------------------------------------------------------------------------------------------------

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
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- LP solver ---------------------------------------------------------------------------------------------------------------------------------------

        LP1 = math/linearprogram/create( 2, 2, 1, 0 );
        math/linearprogram/valueconstraint( LP1, 1, 1, 0, ">=", 1 );
        math/linearprogram/valueconstraint( LP1, 1, 0, 1, ">=", 1 );
        math/linearprogram/valueconstraint( LP1, 0, 1, 0, ">=", 1 );
        [LP1Value | LP1PointCount | LP1Points] = math/linearprogram/solve( LP1, "minimize", "non-negative" );

        LP2 = math/linearprogram/create( 0.8, 0.2, 0.7, 0.3, 0.6, 0.4, 0 );
        math/linearprogram/valueconstraint( LP2, 1, 0, 1, 0, 1, 0,  "=", 23 );
        math/linearprogram/valueconstraint( LP2, 0, 1, 0, 1, 0, 1,  "=", 23 );
        math/linearprogram/valueconstraint( LP2, 1, 0, 0, 0, 0, 0, ">=", 10 );
        math/linearprogram/valueconstraint( LP2, 0, 0, 1, 0, 0, 0, ">=", 8 );
        math/linearprogram/valueconstraint( LP2, 0, 0, 0, 0, 1, 0, ">=", 5 );
        [LP2Value | LP2PointCount | LP2Points] = math/linearprogram/solve( LP2, "maximize", "non-negative" );

        generic/print("LP solve minimize", LP1Value, LP1PointCount, LP1Points);
        generic/print("LP solve maximize", LP2Value, LP2PointCount, LP2Points);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- polynomial interpolation ------------------------------------------------------------------------------------------------------------------------

        PI = math/interpolate/create("neville", [-5,1,2,8,14], [7,3,7,4,8]);
        [PIV] = math/interpolate/interpolate( PI, 3 , 5, 10, -3);

        generic/print("interpolate", PIV);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- hash --------------------------------------------------------------------------------------------------------------------------------------------

        HashMD5 = crypto/hash( "md5", "hallo" );
        HashMurmur = crypto/hash( "murmur3-32", "hallo" );
        HashAdler = crypto/hash( "adler-32", "hallo" );
        HashCrc = crypto/hash( "crc-32", "hallo" );
        HashSHA = crypto/hash( "sha-256", "string test1", "second data", 4, 5, 6);
        generic/print("MD5 & SHA-256 & Murmur & Adler & CRC hash", HashMD5, HashSHA, HashMurmur, HashAdler, HashCrc);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // ---- crypto (AES & DES) -----------------------------------------------------------------------------------------------------------------------------

        DESKey = crypto/createkey( "DES" );
        DESEncrypt = crypto/encrypt( DESKey, "DES uncrypted message");
        DESDecrypt = crypto/decrypt( DESKey, DESEncrypt);
        generic/print( "crypto des", DESEncrypt, DESDecrypt );

        AESKey = crypto/createkey( "AES" );
        AESEncrypt = crypto/encrypt( AESKey, "AES uncrypted message");
        AESDecrypt = crypto/decrypt( AESKey, AESEncrypt);
        generic/print( "crypto aes", AESEncrypt, AESDecrypt );
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- crypto (RSA) ------------------------------------------------------------------------------------------------------------------------------------

        [ PublicKey1 | PrivateKey1 ] = crypto/createkey( "RSA" );
        [ PublicKey2 | PrivateKey2 ] = crypto/createkey( "RSA" );

        Encrypt1to2 = crypto/encrypt( PublicKey2, "RSA message from 1 to 2" );
        Encrypt2to1 = crypto/encrypt( PublicKey1, "RSA message from 2 to 1" );

        Decrypt1to2 = crypto/decrypt( PrivateKey2, Encrypt1to2 );
        Decrypt2to1 = crypto/decrypt( PrivateKey1, Encrypt2to1 );

        generic/print("crypto rsa 1 to 2", Encrypt1to2, Decrypt1to2 );
        generic/print("crypto rsa 2 to 1", Encrypt2to1, Decrypt2to1 );
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // ---- sequencial & parallel lambda expression --------------------------------------------------------------------------------------------------------

        (L) -> Y : generic/print(Y);
        @(L) -> Y | R : R = Y+1;

        generic/print("lambda return", R);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- sequential & parallel unification ---------------------------------------------------------------------------------------------------------------

        // unify default
        >>hallo( UN1 ) << true;
        >>foo( UN4, UN5 ) << true;
        >>foo( blub( UN6 ), hallo( UN7 ) ) << true;
        >>foo( blub(1), hallo( UN8 ) ) << true;

        // unify with expression
        >>( hallo( UN2 ), generic/typ/isstring(UN2) ) << true;
        @>>( hallo( UN3 ), generic/typ/isnumeric(UN3) && (UN3 > 200) ) << true;

        // unfiy variable (I is defined on the deconstruct call on top)
        >>( blah(UN9), I ) << true;

        // manual literal parsing & unification
        UN10 = generic/typ/parseliteral("foo(12345)");
        >>( foo(UN11), UN10 ) << true;

        generic/print("unifcation", UN1, UN2, UN3, "   ", UN4, UN5, "   ", UN6, UN7, UN8, "   ", UN9, "   ", UN10, UN11 );
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- repair & plan & goal handling -------------------------------------------------------------------------------------------------------------------

        // test-goal not exist, so use repair definition
        ?plannotexist << true;

        // test-goal exists, so no repair handling (only the functor is checked)
        ?main;

        // run plan immediatly
        PLAN = "mytest";
        !!PLAN;
        !!PLAN(5);

        // run plan within the next cycle
        !mytest;
        !mytest(4);
        !errorplan;

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- test belief calls -------------------------------------------------------------------------------------------------------------------------------

        +counter(UN8);

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- rule execution ----------------------------------------------------------------------------------------------------------------------------------

        $myfunction("fooooooo");
        $fibonacci(8, FIB);

        RULE = "fibonacci";
        $RULE(8,FIB2);

        generic/print("rule execution (fibonacci)", FIB, FIB2);
        FIB == 21;
        FIB2 == 21;

        $ackermann(3, 3, ACK);
        generic/print("rule execution (ackermann)", ACK);
        ACK == 61;

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- condition & plan passing ------------------------------------------------------------------------------------------------------------------------

        Text = Z > 100 ? "Z greater equal 100" : "Z is less 100";
        generic/print("ternary operator", Text);

        Z < 100;
        generic/print("plan passed")

        // -----------------------------------------------------------------------------------------------------------------------------------------------------

.
