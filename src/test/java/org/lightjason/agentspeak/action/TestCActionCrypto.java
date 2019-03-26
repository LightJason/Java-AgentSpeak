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

package org.lightjason.agentspeak.action;

import com.codepoetics.protonpack.StreamUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.action.crypto.CCreateKey;
import org.lightjason.agentspeak.action.crypto.CDecrypt;
import org.lightjason.agentspeak.action.crypto.CEncrypt;
import org.lightjason.agentspeak.action.crypto.CHash;
import org.lightjason.agentspeak.action.crypto.ECryptAlgorithm;
import org.lightjason.agentspeak.error.context.CExecutionException;
import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test action crypto
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionCrypto extends IBaseTest
{

    /**
     * data provider generator of hash definition
     *
     * @return data
     */
    @DataProvider
    public static Object[] generatehash()
    {
        return StreamUtils.zip(
                    Stream.of( "adler-32", "crc-32", "crc-32c", "murmur3-32", "murmur3-128", "siphash-2-4" ),
                    Stream.of( new String[]{"7804c01a", "911c63b0"}, new String[]{"45154713", "29369833"},
                               new String[]{"387e0716", "4411bf68"}, new String[]{"306202a8", "08b9852d"},
                               new String[]{"636cc4ff5f7ed59b51f29d6d949b4709", "f4459439308d1248efc0532fb4cd6d79"},
                               new String[]{"4f27c08e5981bc5a", "82ee572bf0a0dde4"}
                    ),

                    ImmutablePair::new
        ).toArray();
    }

    /**
     * test crypt key generation
     *
     * @param p_crypt crypt definition
     */
    @Test
    @UseDataProvider( "generatecrypt" )
    public void createkey( final Triple<String, Integer, Integer> p_crypt )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreateKey().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( CRawTerm.of( p_crypt.getLeft() ) ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( p_crypt.getMiddle().intValue(), l_return.size() );
    }


    /**
     * test wrong algorithm
     *
     * @throws NoSuchAlgorithmException is thrown on key generator error
     */
    @Test( expected = CExecutionIllegealArgumentException.class )
    public void wrongalgorithm() throws NoSuchAlgorithmException
    {
        final Key l_key = KeyGenerator.getInstance( "HmacSHA1" ).generateKey();

        new CEncrypt().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( l_key ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        new CDecrypt().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( l_key ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }

    /**
     * test decrypt execute array
     *
     * @throws NoSuchAlgorithmException is thrown on key generator error
     */
    @Test( expected = CExecutionIllegalStateException.class )
    public void decryptexecutionerror() throws NoSuchAlgorithmException
    {
        final Pair<Key, Key> l_key = ECryptAlgorithm.RSA.generateKey();
        final List<ITerm> l_return = new ArrayList<>();


        new CEncrypt().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( l_key.getLeft(), "xxx" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );

        new CDecrypt().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( l_key.getLeft(), l_return.get( 0 ).<String>raw() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );
    }

    /**
     * test hashing
     *
     * @param p_hash hash value
     */
    @Test
    @UseDataProvider( "generatehash" )
    public void hash( final Pair<String, String[]> p_hash )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CHash(),
                false,
                Stream.of( CRawTerm.of( p_hash.getLeft() ), CRawTerm.of( "test string" ), CRawTerm.of( 1234 ) ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertArrayEquals( p_hash.getRight(), l_return.stream().map( ITerm::<String>raw ).toArray( String[]::new ) );
    }

    /**
     * test hash exception
     */
    @Test( expected = CExecutionException.class )
    public void hashexception()
    {
        new CHash().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( CRawTerm.of( "xxx" ), CRawTerm.of( 1234 ) ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }

    /**
     * data provider generator of crypt key definition
     *
     * @return data
     */
    @DataProvider
    public static Object[] generatecrypt()
    {
        return Stream.of(
            new ImmutableTriple<>( "des", 1, 0 ),
            new ImmutableTriple<>( "aes", 1, 0 ),
            new ImmutableTriple<>( "rsa", 2, 1 )
        ).toArray();
    }

    /**
     * test key generation on error call
     */
    @Test( expected = CExecutionIllegealArgumentException.class )
    public void createkeyError()
    {
        new CCreateKey().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( CRawTerm.of( "test" ) ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }

    /**
     * test encrypting and decrypting
     *
     * @param p_crypt tripel with input data
     */
    @Test
    @UseDataProvider( "generatecrypt" )
    public void encryptdecreypt( final Triple<String, Integer, Integer> p_crypt  )
    {
        final List<ITerm> l_returnkey = new ArrayList<>();

        new CCreateKey().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( CRawTerm.of( p_crypt.getLeft() ) ).collect( Collectors.toList() ),
            l_returnkey
        );

        Assert.assertEquals( p_crypt.getMiddle().intValue(), l_returnkey.size() );


        final List<ITerm> l_returnencrypt = new ArrayList<>();

        new CEncrypt().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_returnkey.get( 0 ), CRawTerm.of( "test string" ), CRawTerm.of( 12345 ) ).collect( Collectors.toList() ),
            l_returnencrypt
        );


        final List<ITerm> l_return = new ArrayList<>();

        new CDecrypt().execute(
            false, IContext.EMPTYPLAN,
            Stream.concat( Stream.of( l_returnkey.get( p_crypt.getRight() ) ), l_returnencrypt.stream() ).collect( Collectors.toList() ),
            l_return
        );


        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( "test string", l_return.get( 0 ).raw() );
        Assert.assertEquals( 12345, l_return.get( 1 ).<Number>raw() );
    }

}
