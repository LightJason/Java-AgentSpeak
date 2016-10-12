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

package org.lightjason.agentspeak.consistency.metric;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.language.ITerm;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * metric based on the normalized-compression-distance
 *
 * @see https://en.wikipedia.org/wiki/Normalized_compression_distance
 */
public final class CNCD implements IMetric
{
    /**
     * compression algorithm
     */
    private final ECompression m_compression;

    /**
     * ctor
     */
    public CNCD()
    {
        this( ECompression.BZIP );
    }

    /**
     * ctor
     *
     * @param p_compression compression algorithm
     */
    public CNCD( final ECompression p_compression )
    {
        m_compression = p_compression;
    }

    @Override
    public final Double apply( final Stream<? extends ITerm> p_first, final Stream<? extends ITerm> p_second )
    {
        return this.ncd(
            p_first.map( Object::toString ).collect( Collectors.joining( "" ) ),
            p_second.map( Object::toString ).collect( Collectors.joining( "" ) )
        );
    }

    /**
     * normalized-compression-distance
     *
     * @param p_first first string
     * @param p_second second string
     * @return distance in [0,1]
     */
    private double ncd( final String p_first, final String p_second )
    {
        final double l_first = this.compress( p_first );
        final double l_second = this.compress( p_second );
        return ( this.compress( p_first + p_second ) - Math.min( l_first, l_second ) ) / Math.max( l_first, l_second );
    }

    /**
     * compression algorithm
     *
     * @param p_input input string
     * @return number of compression bytes
     * @warning counting stream returns the correct number of bytes after flushing
     */
    private double compress( final String p_input )
    {
        final DataOutputStream l_counting = new DataOutputStream( new NullOutputStream() );

        try (
            final InputStream l_input = new ByteArrayInputStream( p_input.getBytes( StandardCharsets.UTF_8 ) );
            final OutputStream l_compress = m_compression.get( l_counting )
        )
        {
            IOUtils.copy( l_input, l_compress );
        }
        catch ( final IOException l_exception )
        {
            return 0;
        }

        return l_counting.size();
    }



    /**
     * compression algorithm
     */
    public enum ECompression
    {
        BZIP,
        GZIP,
        DEFLATE,
        PACK200,
        XZ;

        /**
         * creates a compression stream
         *
         * @param p_datastream data-counting stream
         * @return compression output stream
         * @throws IOException throws on any io error
         */
        public final OutputStream get( final DataOutputStream p_datastream ) throws IOException
        {
            switch ( this )
            {
                case BZIP : return new BZip2CompressorOutputStream( p_datastream );

                case GZIP : return new GzipCompressorOutputStream( p_datastream );

                case DEFLATE : return new DeflateCompressorOutputStream( p_datastream );

                case PACK200 : return new Pack200CompressorOutputStream( p_datastream );

                case XZ : return new XZCompressorOutputStream( p_datastream );

                default :
                    throw new CIllegalStateException( CCommon.languagestring( this, "unknown", this ) );
            }
        }
    }
}
