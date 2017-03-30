!test.


// --- logical rules -------------------------------------------------------------------------------------------------------------------------------------------

fibonacci(X, R)
    // order of the rules are indeterministic, so for avoid indeterministic behaviour
    // add the condition, when the rule can be executed first
    :- X <= 2;  R = 1
    :- X > 2;   TA = X - 1; TB = X - 2; $fibonacci(TA,A); $fibonacci(TB,B); R = A+B
.

ackermann(N, M, R)
    :- N == 0; M > 0; R = M+1
    :- M == 0; N > 0; TN = N-1; $ackermann(TN, 1, RA); R = RA
    :- N > 0; M > 0; TN = N-1; TM = M-1; $ackermann(N, TM, RI); $ackermann(TN, RI, RO); R = RO
.

myfunction(X) :- generic/print("my logical rule", X).

// -------------------------------------------------------------------------------------------------------------------------------------------------------------



/**
 * base test
 */
+!test <-
    !testdirectcall;
    !testruledirect;
    !testrulevariable;
    !testrulemultiplearguments
.


/**
 * test direct rule call
 */
+!testdirectcall <-
    $myfunction("fooooooo");
    test/result( success )
.


/**
 * test rule call with variable argument
 */
+!testruledirect <-
    $fibonacci(8, FIB);
    R = FIB == 21.0;
    test/result( R, "rule direct call has been failed" );

    generic/print("rule execution (fibonacci)", FIB )
.


/**
 * test rule variable call
 */
+!testrulevariable <-
    RULE = "fibonacci";
    $RULE(8,FIB);
    R = FIB == 21.0;
    test/result( R, "rule variable call has been failed" );

    generic/print("rule execution (fibonacci)", FIB )
.


/**
 * test multiple rule arguments
 */
+!testrulemultiplearguments <-
    $ackermann(3, 3, ACK);
    R = ACK == 61;
    test/result( R, "rule multiple arguments has been failed" );

    generic/print("rule execution (ackermann)", ACK)
.
