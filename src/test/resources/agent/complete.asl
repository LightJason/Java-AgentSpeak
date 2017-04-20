/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */


// -----
// agent for testing all action functionality
// -----


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
    // order of the rules are indeterministic, so for avoid indeterministic behaviour
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

+beliefadd(X) <- generic/print("adds the 'beliefadd' with value [", X, "] in Cycle [", Cycle, "]"); -beliefadd(X).
-beliefadd(X) <- generic/print("removes the 'beliefadd' with value [", X, "] in Cycle [", Cycle, "]").

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

-!myexternal <- generic/print("external trigger in cycle [", Cycle, "]").


+!main

    : >>( hallo(X), generic/type/isstring(X) ) <-
            generic/print("---", "first plan", "---", "unification variables", X)

    : >>( hallo(X), generic/type/isnumeric(X) && X > 1000 )  <-
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
        LSize = collection/size( L );

        generic/print("list elements", A,B,C,D,E,F,G, L);
        generic/print("intersection & union & symmetric difference & complement", Intersect, "--", Union, "--", SD, "--", CP, "--", LSize);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- literal accessing -------------------------------------------------------------------------------------------------------------------------------

        [O|P] =.. foo( blub(1), blah(3) );
        [H|I] = P;
        generic/print("deconstruct", O,P,H,I);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- simple arithmetic -------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
        Z = 10 * 4 ** 0.5;
        generic/print("simple expression", Z);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- string ------------------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
        SBase64 = string/base64encode( "Base64 encoded string" );
        SReverse = string/reverse( "abcdefg" );
        SUpper = string/upper("AbCdefg");
        SLower = string/lower("AbCdefg");
        SReplace = string/replace( "a1b1defg1xyz1ui", "1", "-" );
        SRand = string/random( "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 20 );
        generic/print("string", SBase64, "--", SReverse, "--", SUpper, "--", SLower, "--", SRand, "--", SReplace);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- blas arithmetic ---------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
        M = math/blas/matrix/create(2,2);
        math/blas/matrix/set(0,0, 1, M);
        math/blas/matrix/set(0,1, 2, M);
        math/blas/matrix/set(1,0, 3, M);
        math/blas/matrix/set(1,1, 4, M);
        Det = math/blas/matrix/determinant(M);
        [EVal|EVec]  = math/blas/matrix/eigen(M);
        generic/print("matrix", M,Det,EVal,EVec);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- random ------------------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
        Distribution = math/statistic/createdistribution( "normal", 20, 100 );
        RV = math/statistic/randomsample( Distribution, 8 );
        generic/print("random", RV);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- date / time -------------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
        [Hour | Minute | Second | Nano ] = datetime/time();
        [Year | Month | Day | DayOfWeek | DayOfYear] = datetime/date();
        Zone = datetime/zoneid();
        generic/print("date & time", Hour, Minute, Second, Nano, "--", Day, Month, Year, DayOfWeek, DayOfYear, "--", Zone);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- restful api (push to beliefbase) ----------------------------------------------------------------------------------------------------------------
//XXXXXXX
        GH = rest/jsonlist( "https://api.github.com/repos/LightJason/AgentSpeak/commits", "github", "elements" );
        +webservice( GH );

        GO = rest/jsonobject( "https://maps.googleapis.com/maps/api/geocode/json?address=Clausthal-Zellerfeld", "google", "location" );
        +webservice( GO );

        WP = rest/xmlobject( "https://en.wikipedia.org/wiki/Special:Export/AgentSpeak", "wikipedia" );
        +webservice( WP );

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- math functions ----------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
        MinIdx = math/minindex(RV);
        MaxIdx = math/maxindex(RV);
        InRect = math/shape/inrectangle( 0,0, 4,5,   2,1 );
        InCircle = math/shape/incircle( 2,2,1,  2,1 );
        InTriangle = math/shape/intriangle( 350,320,  25,375,  40,55,    160,270 );

        generic/print("min & max index", MinIdx, MaxIdx);
        generic/print("shapes (in)", "", "rectangle", InRect, "circle", InCircle, "triangle", InTriangle);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- statistics --------------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
        Statistic = math/statistic/createstatistic();
        math/statistic/addstatisticvalue(Statistic, RV, L);

        [SMax|SMin|SCount|SPopVariance|SQuadraticMean|SSecondMom|SStd|SSum|SSumSq|SVar|SMean]  = math/statistic/multiplestatisticvalue(Statistic, "max", "min", "count", "populationvariance", "quadraticmean", "secondmoment", "standarddeviation", "sum", "sumsquare", "variance", "mean" );
        SX = math/statistic/singlestatisticvalue("mean", Statistic);

        generic/print("statistic", SMax, SMin, SCount, SPopVariance, SQuadraticMean, SSecondMom, SStd, SSum, SSumSq, SVar, SMean );
        generic/print();


        FValue = collection/list/create(1, 3, 1, 1);
        Fitness1 = math/statistic/linearselection( ["a", "b", "c", "d"], FValue );
        Fitness2 = math/statistic/exponentialselection( ["e", "f", "g", "h"], [1,1,3,1], 3 );
        generic/print( "fitness proportionate selection", Fitness1, Fitness2 );
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- LP solver ---------------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
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
//XXXXXXX
        PI = math/interpolate/create("neville", [-5,1,2,8,14], [7,3,7,4,8]);
        [PIV] = math/interpolate/singleinterpolate( PI, 3 , 5, 10, -3);

        generic/print("interpolate", PIV);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- boolean operators -------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
        BAnd = bool/and( true, false, true );
        BOr  = bool/or( true, false, false );
        BXor = bool/xor( true, false, true, false );

        generic/print("boolean", BAnd, BOr, BXor);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- hash --------------------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
        HashMD5 = crypto/hash( "md5", "hallo" );
        HashMurmur = crypto/hash( "murmur3-32", "hallo" );
        HashAdler = crypto/hash( "adler-32", "hallo" );
        HashCrc = crypto/hash( "crc-32", "hallo" );
        HashSHA = crypto/hash( "sha-256", "string test1", "second data", 4, 5, 6);
        generic/print("MD5 & SHA-256 & Murmur & Adler & CRC hash", HashMD5, HashSHA, HashMurmur, HashAdler, HashCrc);
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // ---- crypto (AES & DES) -----------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
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
//XXXXXXX
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



        // ---- sequential & parallel lambda expression --------------------------------------------------------------------------------------------------------

        (L) -> Y : generic/print(Y);

        BL = agent/belieflist( "hallo" );
        (BL) -> Y : generic/print(Y);

        @(L) -> Y | R : R = Y+1;
        generic/print("lambda return", R);
        generic/print();

        PL = agent/planlist();
        (PL) -> Y : generic/print(Y);

        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- sequential & parallel unification ---------------------------------------------------------------------------------------------------------------

        // unify default
        >>hallo( UN1 ) << true;
        >>foo( UN4, UN5 ) << true;
        >>foo( blub( UN6 ), hallo( UN7 ) ) << true;
        >>foo( blub(1), hallo( UN8 ) ) << true;

        // unify with expression
        >>( hallo( UN2 ), generic/type/isstring(UN2) ) << true;
        @>>( hallo( UN3 ), generic/type/isnumeric(UN3) && (UN3 > 200) ) << true;

        // unify variable (I is defined on the deconstruct call on top)
        >>( blah(UN9), I ) << true;

        // manual literal parsing & unification
        UN10 = generic/type/parseliteral("foo(12345)");
        >>( foo(UN11), UN10 ) << true;

        generic/print("unification", UN1, UN2, UN3, "   ", UN4, UN5, "   ", UN6, UN7, UN8, "   ", UN9, "   ", UN10, UN11 );
        generic/print();

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- repair & plan & goal handling -------------------------------------------------------------------------------------------------------------------

        // test-goal not exist, so use repair definition
        ?plannotexist << true;

        // test-goal exists, so no repair handling (only the functor is checked)
        ?main;

        // run plan immediately
        PLAN = "mytest";
        !!PLAN;
        !!PLAN(5);

        // run plan within the next cycle
        !mytest;
        !mytest(4);
        !errorplan;

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- test belief calls -------------------------------------------------------------------------------------------------------------------------------

        +beliefadd(UN8);

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- rule execution ----------------------------------------------------------------------------------------------------------------------------------
//XXXXXXX
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

        FIBX = -1;
        $fibonacci(8, FIBX);
        generic/print("rule execution (fibonacci) in-place modification", FIBX );
        FIBX == 21;

        // -----------------------------------------------------------------------------------------------------------------------------------------------------



        // --- condition & plan passing ------------------------------------------------------------------------------------------------------------------------

        Text = Z > 100 ? "Z greater equal 100" : "Z is less 100";
        generic/print("ternary operator", Text);

        Z < 100;
        generic/print("plan passed")

        // -----------------------------------------------------------------------------------------------------------------------------------------------------

.
