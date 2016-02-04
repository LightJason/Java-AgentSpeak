hallo(123).
second(true).
~lub("hallo").

+!drive
    : @>>(foo(X), X > 2) <- true
    : >>foo(X) && X > 2  <- generic/print("hallo")
    <-
        // --- manual individual & internal variables ----------------------------------------------------------------------------------------------------------

        generic/print("constants", Score, Cycle, MyConstInt, MyConstString);

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- collections -------------------------------------------------------------------------------------------------------------------------------------

        L = collection/list/range(1, 20);
        [ A|B|C| _ |D|E|F|G ] = L;
        generic/print("list elements", A,B,C,D,E,F,G);
        generic/print("full list", L);
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



        // --- hash --------------------------------------------------------------------------------------------------------------------------------------------

        HashMD5 = crypto/hash( "md5", "hallo" );
        HashSHA = crypto/hash( "sha-256", "string test1", "second data", 4, 5, 6);
        generic/print("MD5 & SHA-256 hash", HashMD5, HashSHA);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // ---- crypto (AES & DES) -----------------------------------------------------------------------------------------------------------------------------

        DESKey = crypto/createkey( "DES" );
        DESEncrypt = crypto/encrypt( "DES", DESKey, "DES uncrypted message");
        DESDecrypt = crypto/decrypt( "DES", DESKey, DESEncrypt);
        generic/print( "crypto des", DESEncrypt, DESDecrypt );

        AESKey = crypto/createkey( "AES" );
        AESEncrypt = crypto/encrypt( "AES", AESKey, "AES uncrypted message");
        AESDecrypt = crypto/decrypt( "AES", AESKey, AESEncrypt);
        generic/print( "crypto aes", AESEncrypt, AESDecrypt );
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- crypto (RSA) ------------------------------------------------------------------------------------------------------------------------------------

        [ PublicKey1 | PrivateKey1 ] = crypto/createkey( "RSA" );
        [ PublicKey2 | PrivateKey2 ] = crypto/createkey( "RSA" );

        Encrypt1to2 = crypto/encrypt( "RSA", PublicKey2, "RSA message from 1 to 2" );
        Encrypt2to1 = crypto/encrypt( "RSA", PublicKey1, "RSA message from 2 to 1" );

        Decrypt1to2 = crypto/decrypt( "RSA", PrivateKey2, Encrypt1to2 );
        Decrypt2to1 = crypto/decrypt( "RSA", PrivateKey1, Encrypt2to1 );

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

        //>>foo(U);
        //@>>foo(U);

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- repair handling ---------------------------------------------------------------------------------------------------------------------------------

        //?foo;
        ?foo << true;

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- condition & plan passing ------------------------------------------------------------------------------------------------------------------------

        Text = Z > 100.0 ? "Z greater equal 100" : "Z is less 100";
        generic/print("ternary operator", Text);

        Z < 100.0;
        generic/print("plan passed")

        // -----------------------------------------------------------------------------------------------------------------------------------------------------

        .
