/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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
// agent for testing generic structures and actions
// -----


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


// initial-goal
!test.

/**
 * base test
 */
+!test <-
    !testunification;
    !testunifyany
.


/**
 * test unification
 */
+!testunification <-
/*
        // unify default
        >>hello( UN1 );
        .generic/print( "first unification", UN1 );
        .test/result( .bool/equal( UN1, 123 ), "first unification has been failed" );

        // unify subitem
        >>foo( blub(1), hello( UN2 ) );
        .generic/print( "second unification", UN2 );
        .test/result( .bool/equal( UN2, "test" ), "second unification has been failed" );

        // unify two items
        >>foo( blub( UN3a ), hello( UN3b ) );
        .generic/print( "third unification", UN3a, UN3b );
        .test/result( .bool/equal( UN3a, 1 ), "third unification first part has been failed" );
        .test/result( .bool/equal( UN3b, "test" ), "third unification second part has been failed" );

        // unify by parsing literal
        Literal = foo(12345);
        >>( foo(UN4), Literal );
        .generic/print( "forth unification", UN4 );
        .test/result( .bool/equal( UN4, 12345 ), "forth unification has been failed" );

        // recursive unification
        >>foo( UN5a, UN5b );
        .generic/print( "fifth unification", UN5a, UN5b );
        .test/result( .bool/equal( .generic/type/tostring(UN5a), "blub[1.0]" ), "fifth unification first part has been failed" );
        .test/result( .bool/equal( .generic/type/tostring(UN5b), "hello[test]" ), "fifth unification second part has been failed" );

        // unify with expression
        >>( hello( UN6 ), .generic/type/isstring(UN6) );
        .generic/print( "sixth unification", UN6 );
        .test/result( .bool/equal( UN6, "foo" ), "sixth unification has been failed" );
*/
        // parallel unfiy with expression
        @>>( hello( UN7 ), .generic/type/isnumeric(UN7) && UN7 > 1000 );
        .generic/print( "seventh unification", UN7 );
        .test/result( .bool/equal( UN7, 1111 ), "seventh unification has been failed" );
/*
        // failing unify
        >>notexist( UN8 ) << true;
        .generic/type/isnull( UN8 );
        .generic/print( "eighth unification successful" );
        .test/result( success, "eighth unification has been failed" );
*/
        .generic/print("unification executed completly")
.


/**
 * test for unification and non-existing
 */
+!testunifyany
    : >>hello(X) <-
        .generic/print( "first unifyany:", X );
        .test/result( .bool/equal( X, 123 ), "first unifyany failed" )
        .generic/print("unify any first executed completly")

    : ~>>foobar( _ ) <-
        .generic/print( "second unifyany" );
        .test/result( success );
        .generic/print("unify any second executed completly")
.
