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

hallo(123).
hallo(666).
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