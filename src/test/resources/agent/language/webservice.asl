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
    !testjsonlist
.


/**
 * test webservice via json list
 **/
+!testjsonlist <-
    GH = web/rest/jsonlist( "https://api.github.com/repos/LightJason/AgentSpeak/commits", "github", "elements" );
    +webservice( GH )
.


/**
 * test webservice via json object
 **/
+!testjsonobject <-
    GO = web/rest/jsonobject( "https://maps.googleapis.com/maps/api/geocode/json?address=Clausthal-Zellerfeld", "google", "location" );
    +webservice( GO )
.


/**
 * test webservice via xml object
 **/
+!testxmlobject <-
    WP = web/rest/xmlobject( "https://en.wikipedia.org/wiki/Special:Export/AgentSpeak", "wikipedia" );
    +webservice( WP )
.


/**
 * add trigger
 **/
+webservice(X) <-
    [_|D] =.. X;
    D = collection/list/get(D, 0);
    [N] =.. D;

    test/result( bool/or( bool/equal( N, "elements" ), bool/equal( N, "schemalocation" ), bool/equal( N, "location" ) ) )
.