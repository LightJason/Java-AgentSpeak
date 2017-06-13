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
// agent for testing math actions
// -----

// initial-goal
!test.

/**
 * base test
 */
+!test <-
    !testarithmetic;
    !testblas;
    !testrandom;
    !testlpmaximize;
    !testlpminimize;
    !testinterpolate;
    !teststatistic;
    !testshape
.


/**
 * test simple arithmetic
 */
+!testarithmetic <-
    Z = 12 * 36 ** 0.5;
    generic/print("simple expression", Z);

    R = Z == 72;
    test/result( R, "result simple arithmetic incorrect" )
.


/**
 * test blas
 */
+!testblas <-
         M = math/blas/matrix/create(2,2);
         math/blas/matrix/set(0,0, 1, M);
         math/blas/matrix/set(0,1, 2, M);
         math/blas/matrix/set(1,0, 3, M);
         math/blas/matrix/set(1,1, 4, M);

         Det = math/blas/matrix/determinant(M);
         [EVal|EVec]  = math/blas/matrix/eigen(M);
         generic/print("blas matrix", M,Det,EVal,EVec);

         test/result( success )
.


/**
 * test random
 */
+!testrandom <-
        Distribution = math/statistic/createdistribution( "normal", 20, 100 );
        RV = math/statistic/randomsample( Distribution, 8 );
        generic/print("random", RV);

        test/result( success )
.


/**
 * test LP solver minimize
 */
+!testlpminimize <-
        LP = math/linearprogram/create( 2, 2, 1, 0 );
        math/linearprogram/valueconstraint( LP, 1, 1, 0, ">=", 1 );
        math/linearprogram/valueconstraint( LP, 1, 0, 1, ">=", 1 );
        math/linearprogram/valueconstraint( LP, 0, 1, 0, ">=", 1 );

        [LPValue | LPPointCount | LPPoints] = math/linearprogram/solve( LP, "minimize", "non-negative" );
        generic/print("LP solve minimize", LPValue, LPPointCount, LPPoints);

        R = LPValue == 3.0;
        test/result( R, "LP solver minimize incorrect" )
.


/**
 * test LP solver maximize
 */
+!testlpmaximize <-
        LP = math/linearprogram/create( 0.8, 0.2, 0.7, 0.3, 0.6, 0.4, 0 );
        math/linearprogram/valueconstraint( LP, 1, 0, 1, 0, 1, 0,  "=", 23 );
        math/linearprogram/valueconstraint( LP, 0, 1, 0, 1, 0, 1,  "=", 23 );
        math/linearprogram/valueconstraint( LP, 1, 0, 0, 0, 0, 0, ">=", 10 );
        math/linearprogram/valueconstraint( LP, 0, 0, 1, 0, 0, 0, ">=", 8 );
        math/linearprogram/valueconstraint( LP, 0, 0, 0, 0, 1, 0, ">=", 5 );

        [LPValue | LPPointCount | LPPoints] = math/linearprogram/solve( LP, "maximize", "non-negative" );
        generic/print("LP solve maximize", LPValue, LPPointCount, LPPoints);

        R = LPValue == 25.800000000000004;
        test/result( R, "LP solver maximize incorrect" )
.


/**
 * test polynomial interpolation
 */
+!testinterpolate <-
        PI = math/interpolate/create("neville", [-5,1,2,8,14], [7,3,7,4,8]);
        [PIV] = math/interpolate/singleinterpolate( PI, 3 , 5, 10, -3);

        generic/print("interpolate", PIV);

        [R|_] = PIV;
        R = R == 10.057772636720006;
        test/result( R, "interpolation fails" )
.


/**
 * test statistic
 */
+!teststatistic <-
        Statistic = math/statistic/createstatistic();
        Distribution = math/statistic/createdistribution( "normal", 20, 100 );

        RV = math/statistic/randomsample( Distribution, 8 );
        L = collection/list/range(1, 20);
        math/statistic/addstatisticvalue(Statistic, RV, L);

        [SMax|SMin|SCount|SPopVariance|SQuadraticMean|SSecondMom|SStd|SSum|SSumSq|SVar|SMean]  = math/statistic/multiplestatisticvalue(Statistic, "max", "min", "count", "populationvariance", "quadraticmean", "secondmoment", "standarddeviation", "sum", "sumsquare", "variance", "mean" );
        SX = math/statistic/singlestatisticvalue("mean", Statistic);

        generic/print("statistic", SMax, SMin, SCount, SPopVariance, SQuadraticMean, SSecondMom, SStd, SSum, SSumSq, SVar, SMean );
        test/result( success )
.


/**
 * test shape
 */
+!testshape <-
    test/result( math/shape/inrectangle( 0,0, 4,5,   2,1 ), "in-shape has been failed" );
    test/result( math/shape/incircle( 2,2,1,  2,1 ), "in-circle has been failed" );
    test/result( math/shape/intriangle( 350,320,  25,375,  40,55,    160,270 ), "in-triangle has been failed" );

    generic/print("shapes (in) executed completly")
.