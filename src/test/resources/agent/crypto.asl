!test.

/**
 * base test
 */
+!test <-
    !testhash;
    !testdes;
    !testaes;
    !testrsa
.


/**
 * test hash functions
 */
+!testhash <-
    HashMD5 = crypto/hash( "md5", "hallo" );
    RHashMD5 = bool/equal( HashMD5, "598d4c200461b81522a3328565c25f7c" );

    test/result(RHashMD5, "MD5 hash has been failed");


    HashMurmur = crypto/hash( "murmur3-32", "hallo" );
    RHashMurmur = bool/equal( HashMurmur, "8f1afda0" );

    test/result(RHashMurmur, "Murmur hash has been failed");


    HashAdler = crypto/hash( "adler-32", "hallo" );
    RHashAdler = bool/equal( HashAdler, "11021c06" );

    test/result( RHashAdler, "Adler hash has been failed" );


    HashCrc = crypto/hash( "crc-32", "hallo" );
    RHashCrc = bool/equal( HashCrc, "d13172b9" );

    test/result( RHashCrc, "CRC hash has been failed" );


    HashSHA = crypto/hash( "sha-256", "string test1", "second data", 4, 5, 6);
    RHashSHA = bool/equal( HashSHA, "1fdb9b7d5ada89202177807ebe2d713770d4e8a23ecd7efef113f0e1e853e59" );

    test/result( RHashSHA, "SHA-256 hash has been failed" );


    generic/print( "MD5 & Murmur & Adler & CRC & SHA-256 hash", HashMD5, HashMurmur, HashAdler, HashCrc, HashSHA )
.


/**
 * test DES
 */
+!testdes <-
        DESKey = crypto/createkey( "DES" );
        DESEncrypt = crypto/encrypt( DESKey, "DES uncrypted message");
        DESDecrypt = crypto/decrypt( DESKey, DESEncrypt);

        generic/print( "crypto des", DESEncrypt, DESDecrypt );
        test/result( success )
.


/**
 * test AES
 */
+!testaes <-
    AESKey = crypto/createkey( "AES" );
    AESEncrypt = crypto/encrypt( AESKey, "AES uncrypted message");
    AESDecrypt = crypto/decrypt( AESKey, AESEncrypt);

    generic/print( "crypto aes", AESEncrypt, AESDecrypt );
    test/result( success )
.


/**
 * test RSA
 */
+!testrsa <-
    Message1to2 = "RSA message from 1 to 2";
    Message2to1 = "RSA message from 2 to 1";

    [ PublicKey1 | PrivateKey1 ] = crypto/createkey( "RSA" );
    [ PublicKey2 | PrivateKey2 ] = crypto/createkey( "RSA" );

    Encrypt1to2 = crypto/encrypt( PublicKey2, Message1to2 );
    Encrypt2to1 = crypto/encrypt( PublicKey1, Message2to1 );

    Decrypt1to2 = crypto/decrypt( PrivateKey2, Encrypt1to2 );
    Decrypt2to1 = crypto/decrypt( PrivateKey1, Encrypt2to1 );

    generic/print("crypto rsa 1 to 2", Encrypt1to2, Decrypt1to2 );
    generic/print("crypto rsa 2 to 1", Encrypt2to1, Decrypt2to1 );


    RDecrypt1to2 = bool/equal( Decrypt1to2, Message1to2 );
    test/result( RDecrypt1to2, "RSA has been failed on the first message" );

    RDecrypt2to1 = bool/equal( Decrypt2to1, Message2to1 );
    test/result( RDecrypt2to1, "RSA has been failed on the second message" )
.