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



!test.

/**
 * base test
 */
+!test <-
    !testdatetime;
    !testbool;
    !teststring;
    !testunification;
    !testdeconstruct
.


/**
 * test date / time
 */
+!testdatetime <-
    [Hour | Minute | Second | Nano ] = datetime/time();
    [Year | Month | Day | DayOfWeek | DayOfYear] = datetime/date();
    Zone = datetime/zoneid();
    generic/print("date & time", Hour, Minute, Second, Nano, "--", Day, Month, Year, DayOfWeek, DayOfYear, "--", Zone);

    test/result( success )
.


/**
 * test boolean operator
 */
+!testbool <-
    BAnd = bool/and( true, false, true );
    RBAnd = ~BAnd;
    test/result( RBAnd, "bool-and has been failed" );


    BOr  = bool/or( true, false, false );
    test/result( BOr, "bool-or has been failed" );


    BXor = bool/xor( true, false, true, false );
    test/result( BXor, "bool-xor has been failed" );


    generic/print("boolean", BAnd, BOr, BXor)
.


/**
 * test string
 */
+!teststring <-
    SBase64 = string/base64encode( "Base64 encoded string" );
    RSBase64 = bool/equal( SBase64, "QmFzZTY0IGVuY29kZWQgc3RyaW5n" );
    test/result( RSBase64, "string base64 has been failed" );


    SReverse = string/reverse( "abcdefg" );
    RSReverse = bool/equal( SReverse, "gfedcba" );
    test/result( RSReverse, "string reverse has been failed" );


    SUpper = string/upper("AbCdefg");
    RSUpper = bool/equal( SUpper, "ABCDEFG" );
    test/result( RSUpper, "string upper has been failed" );


    SLower = string/lower("AbCdefg");
    RSLower = bool/equal( SLower, "abcdefg" );
    test/result( RSLower, "string lower has been failed" );


    SReplace = string/replace( "1", "-", "a1b1defg1xyz1ui" );
    RSReplace = bool/equal( SReplace, "a-b-defg-xyz-ui" );
    test/result( RSReplace, "string replace has been failed" );


    SRand = string/random( "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 20 );

    generic/print("string", SBase64, "--", SReverse, "--", SUpper, "--", SLower, "--", SReplace, "--", SRand )
.


/**
 * test unification
 */
+!testunification <-

        // unify default
        >>hallo( UN1 ) << true;
        >>foo( UN4, UN5 ) << true;
        >>foo( blub( UN6 ), hallo( UN7 ) ) << true;
        >>foo( blub(1), hallo( UN8 ) ) << true;
/*
        // unify with expression
        >>( hallo( UN2 ), generic/type/isstring(UN2) ) << true;
        @>>( hallo( UN3 ), generic/type/isnumeric(UN3) && (UN3 > 200) ) << true;

        // unify variable (I is defined on the deconstruct call on top)
        >>( blah(UN9), I ) << true;

        // manual literal parsing & unification
        UN10 = generic/type/parseliteral("foo(12345)");
        >>( foo(UN11), UN10 ) << true;
*/
//        generic/print("unification", UN1, UN2, UN3, "   ", UN4, UN5, "   ", UN6, UN7, UN8, "   ", UN9, "   ", UN10, UN11 );

        test/result( success )
.


/**
 * test the deconstruct operator
 */
+!testdeconstruct <-
        [O|P] =.. foo( blub(1), blah(3) );
        R1 = bool/equal( O, "foo" );
        test/result( R1, "first deconstruct has been failed" );

        [H|I] = P;
        [A|C] =.. H;
        [B|D] =.. I;

        R2 = bool/equal( A, "blub" );
        R3 = bool/equal( B, "blah" );
        test/result( R2, "second deconstruct has been failed" );
        test/result( R3, "third deconstruct has been failed" );

        [X|Y] = generic/type/type( C, D );
        generic/print(X, Y);

        R4 =  V1 == 1;
        R5 =  V2 == 3;
        test/result( R4, "deconstruct first value has been failed" );
        test/result( R5, "deconstruct second value has been failed" );

        generic/print("deconstruct", O,P,H,I)
.