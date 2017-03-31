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