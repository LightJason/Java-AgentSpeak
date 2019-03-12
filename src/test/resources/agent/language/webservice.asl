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
// agent for testing webservices actions
// -----

// initial-goal
!test.

/**
 * base test
 */
+!test <-
    !testxmlobject;
    !testjsonobject;
    !testjsonlist;
    !testgraphqlliteral;
    !testgraphqlnative
.


/**
 * test webservice via json list
 **/
+!testjsonlist <-
    GH = .web/rest/jsonlist( "https://api.github.com/repos/LightJason/AgentSpeak/commits", "github", "elements" );
    +rest-service( GH )
.


/**
 * test webservice via json object
 **/
+!testjsonobject <-
    GO = .web/rest/jsonobject( "https://maps.googleapis.com/maps/api/geocode/json?address=Clausthal-Zellerfeld", "google", "location" );
    +rest-service( GO )
.


/**
 * test webservice via xml object
 **/
+!testxmlobject <-
    WP = .web/rest/xmlobject( "https://en.wikipedia.org/wiki/Special:Export/AgentSpeak", "wikipedia" );
    +rest-service( WP )
.

/**
 * test graphql service with literals
 * @see http://apis.guru/graphql-apis/
 **/
+!testgraphqlliteral <-
    GQ = .web/graphql/queryliteral( "https://countries.trevorblades.com/", country( code("DE"), code, phone, name, curreny ) , "graphql" );
    +graphql-fake-literal(GQ)
.

/**
 * test graphql service with native string query
 **/
+!testgraphqlnative <-
    GQ = .web/graphql/querynative( "https://countries.trevorblades.com/", '{ country(code: "DE") { code, phone, name, currency } }', "graphql" );
    +graphql-fake-native(GQ)
.

/**
 * check goal for webservice calls
 **/
+rest-service(X) <-
    [_|D] =.. X;
    D = .collection/list/get(D, 0);
    [N] =.. D;

    .test/result( .bool/or( .bool/equal( N, "elements" ), .bool/equal( N, "schemaLocation" ), .bool/equal( N, "location" ) ) )
.

/**
 * check goal for graphql-fake literal calls
 **/
+graphql-fake-literal(X) <-
    [_|D] =.. X;
    D = .collection/list/get(D, 0);
    [N|M] =.. D;
    M = .collection/list/get(M, 0);
    [L|_] =.. M;

    .test/result( .bool/and( .bool/equal( N, "data" ), .bool/equal( L, "country" ) ) )
.

/**
 * check goal for graphql-fake native calls
 **/
+graphql-fake-native(X) <-
    generic/print(X);
    [_|D] =.. X;
    D = .collection/list/get(D, 0);
    [N|M] =.. D;
    M = .collection/list/get(M, 0);
    [L|_] =.. M;

    .test/result( .bool/and( .bool/equal( N, "data" ), .bool/equal( L, "country" ) ) )
.