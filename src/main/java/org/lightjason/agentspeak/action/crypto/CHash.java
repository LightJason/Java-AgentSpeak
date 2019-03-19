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

package org.lightjason.agentspeak.action.crypto;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.SerializationUtils;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;


/**
 * hash algorithm.
 * The actions creates a hash values of datasets, the first argument is the name of the hasing algorithm
 * (Adler-32, CRC-32, CRC-32C, Murmur3-32, Murmur3-128, Siphash-2-4, MD2, MD5, SHA-256, SHA-384, SHA-512),
 * for all other unflatten arguments a hash value is calculated and the action returns the hash values back
 *
 * {@code [Hash1 | Hash2 | Hash3] = .crypto/hash( "Adler-32 | CRC-32 | CRC-32C | ...", Dataset1, Dataset2, Dataset3 );}
 *
 * @see https://en.wikipedia.org/wiki/Secure_Hash_Algorithm
 * @see https://en.wikipedia.org/wiki/MD2_(cryptography)
 * @see https://en.wikipedia.org/wiki/MD5
 * @see https://en.wikipedia.org/wiki/Adler-32
 * @see https://en.wikipedia.org/wiki/Cyclic_redundancy_check
 * @see https://en.wikipedia.org/wiki/MurmurHash
 * @see https://en.wikipedia.org/wiki/SipHash
 * @see http://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
 * @see https://github.com/google/guava/wiki/HashingExplained
 */
public final class CHash extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 4638666396527392307L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CHash.class, "crypto" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        CCommon.flatten( p_argument )
               .skip( 1 )
               .map( i -> hash( p_context, p_argument.get( 0 ).raw(), serialize( p_context, i ) ) )
               .map( CRawTerm::of )
               .forEach( p_return::add );

        return Stream.of();
    }


    /**
     * serialize data
     *
     * @param p_context execution context
     * @param p_object term object
     * @return serialized bytes
     *
     * @note strings will be serialized always with utf-8 encoding, so comparing with md5sum is possible
     */
    private static byte[] serialize( @Nonnull final IContext p_context, @Nonnull final ITerm p_object )
    {
        return CCommon.isssignableto( p_object, String.class )
               ? p_object.<String>raw().getBytes( StandardCharsets.UTF_8 )
               : SerializationUtils.serialize( p_object.raw() );
    }

    /**
     * runs hashing function with difference between Google Guava hashing and Java default digest
     *
     * @param p_context execution context
     * @param p_algorithm algorithm name
     * @param p_data byte data representation
     * @return hash value
     */
    private static String hash( @Nonnull final IContext p_context, @Nonnull final String p_algorithm, @Nonnull final byte[] p_data )
    {
        switch ( p_algorithm.trim().toLowerCase( Locale.ROOT ) )
        {
            case "adler-32":
                return Hashing.adler32().newHasher().putBytes( p_data ).hash().toString();

            case "crc-32":
                return Hashing.crc32().newHasher().putBytes( p_data ).hash().toString();

            case "crc-32c":
                return Hashing.crc32c().newHasher().putBytes( p_data ).hash().toString();

            case "murmur3-32":
                return Hashing.murmur3_32().newHasher().putBytes( p_data ).hash().toString();

            case "murmur3-128":
                return Hashing.murmur3_128().newHasher().putBytes( p_data ).hash().toString();

            case "sha-384":
                return Hashing.sha384().newHasher().putBytes( p_data ).hash().toString();

            case "sha-256":
                return Hashing.sha256().newHasher().putBytes( p_data ).hash().toString();

            case "sha-512":
                return Hashing.sha512().newHasher().putBytes( p_data ).hash().toString();

            case "siphash-2-4":
                return Hashing.sipHash24().newHasher().putBytes( p_data ).hash().toString();

            default:
                try
                {
                    return BaseEncoding.base16().encode( MessageDigest.getInstance( p_algorithm ).digest( p_data ) ).toLowerCase( Locale.ROOT );
                }
                catch ( final NoSuchAlgorithmException l_exception )
                {
                    throw new CExecutionException( p_context, l_exception );
                }
        }
    }

}
