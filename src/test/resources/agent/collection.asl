!test.

/**
 * base test
 */
+!test <-
    !testindex;
    !testset
.


/**
 * test max/min index
 */
+!testindex <-
    Distribution = math/statistic/createdistribution( "normal", 20, 100 );
    RV = math/statistic/randomsample( Distribution, 8 );

    MinIdx = math/minindex(RV);
    MaxIdx = math/maxindex(RV);

    generic/print("min & max index", MinIdx, MaxIdx);

    test/result( success )
.


/**
 * test list operation
 */
+!testset <-
    Intersect = collection/list/intersect( [1,2,3,4,5], [3,4,5,6,7], [3,8,9,5] );
    [I1|I2]  = Intersect;
    RI = I1 == 3 && I2 == 5;
    test/result( RI, "intersection has been failed" );


    Union = collection/list/union( [1,2], [3,4], [4,5] );
    [U1|U2|U3|U4|U5|U6] = Union;
    RU = U1 == 1 && U2 == 2 && U3 == 3 && U4 == 4 && U5 == 4 && U6 == 5;
    test/result( RU, "union has been failed" );

    SD = "";
/*
    SD = collection/list/symmetricdifference( [1,2,3], [3,4] );
    [SD1|SD2|SD3] = SD;
    RSD = SD1 == 1 && SD2 == 2 && SD3 == 4;
    generic/print( SD, RSD, SD1, SD2, SD3);
    test/result( RSD, "symmetric difference has been failed" );
*/

    CP = collection/list/complement( [1,2,3,4,5], [1,2] );
    [CP1|CP2|CP3] = CP;
    RCP = CP1 == 3 && CP2 == 4 && CP3 == 5;
    test/result( RCP, "complement has been failed" );


    generic/print("intersection & union & symmetric difference & complement", Intersect, "--", Union, "--", SD, "--", CP)
.