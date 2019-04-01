/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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
// agent for testing crypto actions
// @iteration 2
// @testcount 9
// -----

// initial-goal
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
    HashMD5 = .crypto/hash( "md5", "hello agentspeak" );
    .generic/print( "MD5 Hash", HashMD5 );
    .test/result( .bool/equal( HashMD5, "55fd98e633aa50e8b0072d76121bdfed" ), "MD5 hash has been failed");


    HashMurmur = .crypto/hash( "murmur3-32", "hello agentspeak" );
    .generic/print( "Murmur Hash", HashMurmur );
    .test/result( .bool/equal( HashMurmur, "99ca3b53" ), "Murmur hash has been failed");


    HashAdler = .crypto/hash( "adler-32", "hello agentspeak" );
    .generic/print( "Adler Hash", HashAdler );
    .test/result( .bool/equal( HashAdler, "58061935" ), "Adler hash has been failed" );


    HashCrc = .crypto/hash( "crc-32", "hello agentspeak" );
    .generic/print( "CRC Hash", HashCrc );
    .test/result( .bool/equal( HashCrc, "1727aad1" ), "CRC hash has been failed" );


    HashSHA = .crypto/hash( "sha-256", "string test1" );
    .generic/print( "SHA Hash", HashSHA );
    .test/result( .bool/equal( HashSHA, "4234378fcbc448966ea91ed85de19dc9fc176719ed09fe82d1b1ee671c176ad3" ), "SHA-256 hash has been failed" )
.


/**
 * test DES
 */
+!testdes <-
    DESKey = .crypto/createkey( "DES" );
    DESEncrypt = .crypto/encrypt( DESKey, "DES uncrypted message");
    DESDecrypt = .crypto/decrypt( DESKey, DESEncrypt);

    .generic/print( "crypto des", DESEncrypt, DESDecrypt );
    .test/result( success )
.


/**
 * test AES
 */
+!testaes <-
    AESKey = .crypto/createkey( "AES" );
    AESEncrypt = .crypto/encrypt( AESKey, "AES uncrypted message");
    AESDecrypt = .crypto/decrypt( AESKey, AESEncrypt);

    .generic/print( "crypto aes", AESEncrypt, AESDecrypt );
    .test/result( success )
.


/**
 * test RSA
 */
+!testrsa <-
    Message1to2 = "RSA message from 1 to 2";
    Message2to1 = "RSA message from 2 to 1";

    [ PublicKey1 | PrivateKey1 ] = .crypto/createkey( "RSA" );
    [ PublicKey2 | PrivateKey2 ] = .crypto/createkey( "RSA" );

    Encrypt1to2 = .crypto/encrypt( PublicKey2, Message1to2 );
    Encrypt2to1 = .crypto/encrypt( PublicKey1, Message2to1 );

    Decrypt1to2 = .crypto/decrypt( PrivateKey2, Encrypt1to2 );
    Decrypt2to1 = .crypto/decrypt( PrivateKey1, Encrypt2to1 );

    .generic/print("crypto rsa 1 to 2", Encrypt1to2, Decrypt1to2 );
    .generic/print("crypto rsa 2 to 1", Encrypt2to1, Decrypt2to1 );

    .test/result( .bool/equal( Decrypt1to2, Message1to2 ), "RSA has been failed on the first message" );
    .test/result( .bool/equal( Decrypt2to1, Message2to1 ), "RSA has been failed on the second message" )
.