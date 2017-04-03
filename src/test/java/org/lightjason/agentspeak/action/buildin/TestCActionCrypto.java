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
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.buildin.crypto.CHash;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test action crypto
 */
public final class TestCActionCrypto
{

    /**
     * test hashing
     */
    @Test
    public final void testhash()
    {
        StreamUtils.zip(
            Stream.of( "adler-32", "crc-32", "crc-32c", "murmur3-32", "murmur3-128", "siphash-2-4" ),
            Stream.of( new String[]{"9506c33d", "911c63b0"}, new String[]{"70550e9a", "29369833"},
                       new String[]{"fb586372", "4411bf68"}, new String[]{"6646ef59", "08b9852d"},
                       new String[]{"fda6cb9eb3bfe1f087bafbe8c697f128", "f4459439308d1248efc0532fb4cd6d79"},
                       new String[]{"67f4455bf3dc5366", "82ee572bf0a0dde4"}
            ),

            ( i, j ) -> {
                  final List<ITerm> l_return = new ArrayList<>();

                  new CHash().execute(
                                        null,
                                        false,
                                        Stream.of( CRawTerm.from( i ), CRawTerm.from( "test string" ), CRawTerm.from( 1234 ) ).collect( Collectors.toList() ),
                                        l_return,
                                        Collections.emptyList()
                  );

                  Assert.assertArrayEquals( l_return.stream().map( ITerm::<String>raw ).toArray( String[]::new ), j );
                  return "";
              } )
              .forEach( i -> {} );
    }


    /**
     * test call
     *
     * @param p_args commandline arguments
     */
    public static void main( final String[] p_args )
    {
        final TestCActionCrypto l_test = new TestCActionCrypto();

        l_test.testhash();
    }


}
