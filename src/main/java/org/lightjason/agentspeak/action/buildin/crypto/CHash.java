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

package org.lightjason.agentspeak.action.buildin.crypto;

import com.google.common.hash.Hashing;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/**
 * hash algorithm
 *
 * @see http://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
 * @see https://github.com/google/guava/wiki/HashingExplained
 */
public final class CHash extends IBuildinAction
{

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        try
        {
            p_return.add( CRawTerm.from(
                this.hash( p_argument.get( 0 ).toAny(), CCommon.getBytes( p_argument.subList( 1, p_argument.size() ) ) )
            ) );

            return CFuzzyValue.from( true );
        }
        catch ( final UnsupportedEncodingException | NoSuchAlgorithmException l_exception )
        {
            return CFuzzyValue.from( false );
        }
    }


    /**
     * runs hashing function with difference between Google Guava hashing and Java default digest
     *
     * @param p_algorithm algorithm name
     * @param p_data byte data representation
     * @return hash value
     *
     * @throws NoSuchAlgorithmException on unknown hashing algorithm
     */
    private String hash( final String p_algorithm, final byte[] p_data ) throws NoSuchAlgorithmException
    {
        switch ( p_algorithm.trim().toLowerCase() )
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

            case "siphash-2-4":
                return Hashing.sipHash24().newHasher().putBytes( p_data ).hash().toString();

            default:
                return String.format( "%032x", new BigInteger( 1, MessageDigest.getInstance( p_algorithm ).digest( p_data ) ) );
        }
    }

}
