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

!test.

/**
 * base test
 */
+!test <-
    !testjsonlist;
    !testjsonobject;
    !testxmlobject
.


+!testjsonlist <-
    GH = rest/jsonlist( "https://api.github.com/repos/LightJason/AgentSpeak/commits", "github", "elements" );
    +webservice( GH )
.



+!testjsonobject <-
    GO = rest/jsonobject( "https://maps.googleapis.com/maps/api/geocode/json?address=Clausthal-Zellerfeld", "google", "location" );
    +webservice( GO )
.


+!testxmlobject <-
    WP = rest/xmlobject( "https://en.wikipedia.org/wiki/Special:Export/AgentSpeak", "wikipedia" );
    +webservice( WP )
.


+webservice(X) <-
    generic/print(X);
    test/result( success )
.