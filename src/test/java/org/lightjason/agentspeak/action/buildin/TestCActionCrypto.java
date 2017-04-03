/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin;

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
import org.lightjason.agentspeak.action.buildin.crypto.CCreateKey;
import org.lightjason.agentspeak.action.buildin.crypto.CDecrypt;
import org.lightjason.agentspeak.action.buildin.crypto.CEncrypt;
import org.lightjason.agentspeak.action.buildin.crypto.CHash;
import org.lightjason.agentspeak.error.CRuntimeException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test action crypto
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionCrypto
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
                    Stream.of( new String[]{"9506c33d", "911c63b0"}, new String[]{"70550e9a", "29369833"},
                               new String[]{"fb586372", "4411bf68"}, new String[]{"6646ef59", "08b9852d"},
                               new String[]{"fda6cb9eb3bfe1f087bafbe8c697f128", "f4459439308d1248efc0532fb4cd6d79"},
                               new String[]{"67f4455bf3dc5366", "82ee572bf0a0dde4"}
                    ),

                    ImmutablePair::new
        ).toArray();
    }

    /**
     * test hashing
     *
     * @param p_hash hash value
     */
    @Test
    @UseDataProvider( "generatehash" )
    public final void testhash( final Pair<String, String[]> p_hash )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CHash().execute(
                              null,
                              false,
                              Stream.of( CRawTerm.from( p_hash.getLeft() ), CRawTerm.from( "test string" ), CRawTerm.from( 1234 ) ).collect( Collectors.toList() ),
                              l_return,
                              Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::<String>raw ).toArray( String[]::new ), p_hash.getRight() );
    }


    /**
     * test hash exception
     */
    @Test( expected = CRuntimeException.class )
    public final void testhashexception()
    {
        new CHash().execute(
            null,
            false,
            Stream.of( CRawTerm.from( "xxx" ), CRawTerm.from( 1234 ) ).collect( Collectors.toList() ),
            Collections.emptyList(),
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
     * test crypt key generation
     *
     * @param p_crypt crypt definition
     */
    @Test
    @UseDataProvider( "generatecrypt" )
    public final void testcreatekey( final Triple<String, Integer, Integer> p_crypt )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreateKey().execute(
            null,
            false,
            Stream.of( CRawTerm.from( p_crypt.getLeft() ) ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), p_crypt.getMiddle().intValue() );
    }


    /**
     * test key generation on error call
     */
    @Test
    public final void testcreatekeyError()
    {
        Assert.assertFalse(

            new CCreateKey().execute(
                null,
                false,
                Stream.of( CRawTerm.from( "test" ) ).collect( Collectors.toList() ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }



    /**
     * test encrypting & decrypting
     */
    @Test
    @UseDataProvider( "generatecrypt" )
    public final void testencryptdecreypt( final Triple<String, Integer, Integer> p_crypt  )
    {
        final List<ITerm> l_returnkey = new ArrayList<>();

        new CCreateKey().execute(
            null,
            false,
            Stream.of( CRawTerm.from( p_crypt.getLeft() ) ).collect( Collectors.toList() ),
            l_returnkey,
            Collections.emptyList()
        );

        Assert.assertEquals( l_returnkey.size(), p_crypt.getMiddle().intValue() );


        final List<ITerm> l_returnencrypt = new ArrayList<>();

        new CEncrypt().execute(
            null,
            false,
            Stream.of( l_returnkey.get( 0 ), CRawTerm.from( "test string" ), CRawTerm.from( 12345 ) ).collect( Collectors.toList() ),
            l_returnencrypt,
            Collections.emptyList()
        );


        final List<ITerm> l_return = new ArrayList<>();

        new CDecrypt().execute(
            null,
            false,
            Stream.concat( Stream.of( l_returnkey.get( p_crypt.getRight() ) ), l_returnencrypt.stream() ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertEquals( l_return.get( 0 ).raw(), "test string" );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 12345 );
    }




    /**
     * test call
     *
     * @param p_args commandline arguments
     */
    @SuppressWarnings( "unchecked" )
    public static void main( final String[] p_args )
    {
        Arrays.stream( TestCActionCrypto.generatehash() )
              .map( i -> (Pair<String, String[]>) i )
              .forEach( i -> new TestCActionCrypto().testhash( i ) );

        try
        {
            new TestCActionCrypto().testhashexception();
        }
        catch ( final CRuntimeException l_exception )
        {

        }


        new TestCActionCrypto().testcreatekeyError();

        Arrays.stream( TestCActionCrypto.generatecrypt() )
              .map( i -> (Triple<String, Integer, Integer>) i )
              .forEach( i -> new TestCActionCrypto().testcreatekey( i ) );

        Arrays.stream( TestCActionCrypto.generatecrypt() )
              .map( i -> (Triple<String, Integer, Integer>) i )
              .forEach( i -> new TestCActionCrypto().testencryptdecreypt( i ) );
    }


}
