/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.common;

import java.io.File;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * class for any helper calls
 */
public final class CCommon
{

    /**
     * private ctor - avoid instantiation
     */
    private CCommon()
    {
    }

    /**
     * adds a file extension if necessary
     *
     * @param p_file file object
     * @param p_suffix suffix
     * @return file with extension
     */
    public static File addFileExtension( final File p_file, final String p_suffix )
    {
        return ( p_file.getAbsolutePath().endsWith( p_suffix ) ) ? p_file : new File( p_file + p_suffix );
    }

    /**
     * concats an URL with a path
     *
     * @param p_base base URL
     * @param p_string additional path
     * @return new URL
     *
     * @throws URISyntaxException thrown on syntax error
     * @throws MalformedURLException thrown on malformat
     */
    public static URL concatURL( final URL p_base, final String p_string ) throws MalformedURLException, URISyntaxException
    {
        return new URL( p_base.toString() + p_string ).toURI().normalize().toURL();
    }

    /**
     * converts any collection type into a typed array
     *
     * @param p_class class array
     * @param p_collection collection
     * @return typed array
     *
     * @tparam T collection / array type
     */
    public static <T> T[] convertCollectionToArray( final Class<T[]> p_class, final Collection<T> p_collection )
    {
        final T[] l_return = p_class.cast( Array.newInstance( p_class.getComponentType(), p_collection.size() ) );
        p_collection.toArray( l_return );
        return l_return;
    }

    /**
     * returns the hash of a string
     *
     * @param p_string input string
     * @param p_hash hash algorithm
     * @return hexadecimal hash value
     */
    public static String getHash( final String p_string, final String p_hash )
    {
        try
        {
            return getBytes2Hex( MessageDigest.getInstance( p_hash ).digest( p_string.getBytes() ) );
        }
        catch ( final Exception l_exception )
        {
        }

        return null;
    }

    /**
     * @param p_file input file
     * @param p_hash hash algorithm
     * @return hexadecimal hash value
     */
    public static String getHash( final File p_file, final String p_hash )
    {
        try
        {
            return getBytes2Hex( MessageDigest.getInstance( p_hash ).digest( Files.readAllBytes( Paths.get( p_file.toString() ) ) ) );
        }
        catch ( final Exception l_exception )
        {
        }

        return null;
    }


    /**
     * creates a map from parameters
     *
     * @param p_objects list with pairs of string and object
     * @return map with data
     */
    public static Map<String, Object> getMap( final Object... p_objects )
    {
        if ( p_objects.length % 2 != 0 )
            throw new IllegalArgumentException( MessageFormat.format( "number of arguments must be even", "" ) );

        String l_name = null;
        final Map<String, Object> l_return = new HashMap<>();

        for ( int i = 0; i < p_objects.length; ++i )
            if ( i % 2 == 0 )
                l_name = (String) p_objects[i];
            else
                l_return.put( l_name, p_objects[i] );


        return l_return;
    }


    /**
     * returns a string with hexadecimal bytes
     *
     * @param p_bytes input bytes
     * @return hexadecimal string
     */
    private static String getBytes2Hex( final byte[] p_bytes )
    {
        final StringBuilder l_str = new StringBuilder( 2 * p_bytes.length );
        for ( final byte l_byte : p_bytes )
            l_str.append( String.format( "%02x", l_byte & 0xff ) );

        return l_str.toString();
    }

}
