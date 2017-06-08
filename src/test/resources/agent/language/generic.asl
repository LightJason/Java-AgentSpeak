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


hello(123).
hello(600).
hello(666).
hello(700).
hello(777).
hello(888).
hello(900).
hello(999).
hello(1111).
hello("foo").
hello(foo(3)).
foo(blub(1),hello("test")).



!test.

/**
 * base test
 */
+!test <-
    //!testdatetime;
    //!testbool;
    //!teststring;
    !testunification
    //!testdeconstruct
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
    BAnd = ~BAnd;
    test/result( BAnd, "bool-and has been failed" );

    test/result( bool/or( true, false, false ), "bool-or has been failed" );
    test/result( bool/xor( true, false, true, false ), "bool-xor has been failed" )
.


/**
 * test string
 */
+!teststring <-
    SBase64 = string/base64encode( "Base64 encoded string" );
    test/result( bool/equal( SBase64, "QmFzZTY0IGVuY29kZWQgc3RyaW5n" ), "string base64 has been failed" );

    SReverse = string/reverse( "abcdefg" );
    test/result( bool/equal( SReverse, "gfedcba" ), "string reverse has been failed" );

    SUpper = string/upper("AbCdefg");
    test/result( bool/equal( SUpper, "ABCDEFG" ), "string upper has been failed" );

    SLower = string/lower("AbCdefg");
    test/result( bool/equal( SLower, "abcdefg" ), "string lower has been failed" );

    SReplace = string/replace( "1", "-", "a1b1defg1xyz1ui" );
    test/result( bool/equal( SReplace, "a-b-defg-xyz-ui" ), "string replace has been failed" );

    SRand = string/random( "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 20 );
    generic/print("string", SBase64, "--", SReverse, "--", SUpper, "--", SLower, "--", SReplace, "--", SRand )
.


/**
 * test unification
 */
+!testunification <-
        // unify default
        >>hello( UN1 );
        generic/print( "first unification", UN1 );
        test/result( bool/equal( UN1, 123 ), "first unification has been failed" );

        // unify subitem
        >>foo( blub(1), hello( UN2 ) );
        generic/print( "second unification", UN2 );
        test/result( bool/equal( UN2, "test" ), "second unification has been failed" );

        // unify two items
        >>foo( blub( UN3a ), hello( UN3b ) );
        generic/print( "third unification", UN3a, UN3b );
        test/result( bool/equal( UN3a, 1 ), "third unification first part has been failed" );
        test/result( bool/equal( UN3b, "test" ), "third unification second part has been failed" );

        // unify by parsing literal
        Literal = generic/type/parseliteral( "foo(12345)" );
        >>( foo(UN4), Literal );
        generic/print( "forth unification", UN4 );
        test/result( bool/equal( UN4, 12345 ), "forth unification has been failed" );

        // recursive unification
        >>foo( UN5a, UN5b );
        generic/print( "fifth unification", UN5a, UN5b );
        test/result( bool/equal( generic/type/tostring(UN5a), "blub[1]" ), "fifth unification first part has been failed" );
        test/result( bool/equal( generic/type/tostring(UN5b), "hello[test]" ), "fifth unification second part has been failed" )

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
.


/**
 * test the deconstruct operator
 */
+!testdeconstruct <-
        [O|P] =.. foo( blub(1), blah(3) );
        generic/print("first deconstruct", O, P);
        test/result( bool/equal( O, "foo" ), "first deconstruct has been failed" );

        [H|I] = P;
        [A|C] =.. H;
        [B|D] =.. I;

        generic/print("second deconstruct", H, I, A, C, B, D);

        test/result( bool/equal( A, "blub" ), "second deconstruct has been failed" );
        test/result( bool/equal( B, "blah" ), "third deconstruct has been failed" );

        test/result( bool/equal( collection/list/get(C, 0), 1 ), "deconstruct first value has been failed" );
        test/result( bool/equal( collection/list/get(D, 0), 3 ), "deconstruct second value has been failed" );

        generic/print("deconstruct executed completly")
.