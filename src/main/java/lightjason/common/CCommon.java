/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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
 * # along with this program. If not, see <http://www.gnu.org/licenses/>.               #
 * ######################################################################################
 * @endcond
 */

package lightjason.common;

import lightjason.error.CIllegalArgumentException;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


/**
 * class for any helper calls
 */
public final class CCommon
{
    /**
     * properties of the package
     */
    private static final Properties c_properties;
    /**
     * package name
     **/
    private static final String c_packageroot;
    /**
     * language resource bundle
     **/
    private static final ResourceBundle c_language = ResourceBundle.getBundle( "language", Locale.getDefault(), new UTF8Control() );

    static
    {
        String l_packageroot = "";
        c_properties = new Properties();
        ;
        try
        {
            c_properties.load( CCommon.class.getClassLoader().getResourceAsStream( "configuration.properties" ) );
            l_packageroot = c_properties.getProperty( "rootpackage" );
        }
        catch ( final IOException l_exception )
        {
        }

        c_packageroot = l_packageroot;
    }

    /**
     * private ctor - avoid instantiation
     */
    private CCommon()
    {
    }

    /**
     * returns the language bundle
     *
     * @return bundle
     */
    public static ResourceBundle getLanguageBundle()
    {
        return c_language;
    }

    /**
     * returns the property data of the package
     *
     * @return property object
     */
    public static Properties getConfiguration()
    {
        return c_properties;
    }

    /**
     * returns the root package path
     *
     * @return string package path
     */
    public static String getPackage()
    {
        return c_packageroot;
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
     * returns root path of the resource
     *
     * @return URL of file or null
     */
    public static URL getResourceURL()
    {
        return CCommon.class.getClassLoader().getResource( "" );
    }

    /**
     * returns a file from a resource e.g. Jar file
     *
     * @param p_file file
     * @return URL of file or null
     */
    public static URL getResourceURL( final String p_file ) throws URISyntaxException, MalformedURLException
    {
        return getResourceURL( new File( p_file ) );
    }

    /**
     * returns a file from a resource e.g. Jar file
     *
     * @param p_file file relative to the CMain
     * @return URL of file or null
     */
    public static URL getResourceURL( final File p_file ) throws URISyntaxException, MalformedURLException
    {
        if ( p_file.exists() )
            return p_file.toURI().normalize().toURL();
        return CCommon.class.getClassLoader().getResource( p_file.toString().replace( File.separator, "/" ) ).toURI().normalize().toURL();
    }


    /**
     * returns the language depend string on any object
     *
     * @param p_source any object
     * @param p_label label name
     * @param p_parameter parameter
     * @return translated string
     *
     * @tparam T object type
     */
    public static <T> String getLanguageString( final T p_source, final String p_label, final Object... p_parameter )
    {
        return getLanguageString( p_source.getClass(), p_label, p_parameter );
    }

    /**
     * returns a string of the resource file
     *
     * @param p_class class for static calls
     * @param p_label label name of the object
     * @param p_parameter object array with substitutions
     * @return resource string
     */
    public static String getLanguageString( final Class<?> p_class, final String p_label, final Object... p_parameter )
    {
        try
        {
            return MessageFormat.format( c_language.getString( getLanguageLabel( p_class, p_label ) ), p_parameter );
        }
        catch ( final MissingResourceException l_exception )
        {
        }

        return "";
    }

    /**
     * returns the label of a class and string to get access to the resource
     *
     * @param p_class class for static calls
     * @param p_label label name of the object
     * @return label name
     */
    public static String getLanguageLabel( final Class<?> p_class, final String p_label )
    {
        return ( p_class.getCanonicalName().toLowerCase() + "." + p_label.toLowerCase() ).replaceAll( "[^a-zA-Z0-9_\\.]+", "" ).replace(
                c_packageroot + ".", "" );
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
     * creates a map from parameters
     *
     * @param p_objects list with pairs of string and object
     * @return map with data
     */
    public static Map<String, Object> getMap( final Object... p_objects )
    {
        if ( p_objects.length % 2 != 0 )
            throw new CIllegalArgumentException( CCommon.getLanguageString( CCommon.class, "argumentsnoteven" ) );

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
     * helper method to replace variables with context variables
     *
     * @param p_context execution context
     * @param p_terms replacing term list
     * @return result term list
     */
    public static List<ITerm> replaceVariableFromContext( final IContext<?> p_context, final Collection<ITerm> p_terms )
    {
        return p_terms.stream().map( i -> replaceVariableFromContext( p_context, i ) ).collect( Collectors.toList() );
    }

    /**
     * helper method to replace variable with context variable
     * other values will be passed
     *
     * @param p_context execution context
     * @param p_term term
     * @return replaces variable object
     */
    public static ITerm replaceVariableFromContext( final IContext<?> p_context, final ITerm p_term )
    {
        if ( !( p_term instanceof IVariable ) )
            return p_term;

        final IVariable<?> l_variable = p_context.getInstanceVariables().get( p_term.getFQNFunctor() );
        if ( l_variable == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( CCommon.class, "variablenotfoundincontext", p_term.getFQNFunctor() ) );

        return l_variable;
    }

    /**
     * returns a native / raw value of a term
     *
     * @return term value or raw value
     */
    @SuppressWarnings( "unchecked" )
    public static <T, N> T getRawValue( final N p_term )
    {
        return (T) ( p_term instanceof CRawTerm<?> ? ( (CRawTerm<?>) p_term ).getValue() : p_term );
    }



    /**
     * class to read UTF-8 encoded property file
     *
     * @note Java default encoding for property files is ISO-Latin-1
     */
    private static final class UTF8Control extends ResourceBundle.Control
    {

        public final ResourceBundle newBundle( final String p_basename, final Locale p_locale, final String p_format, final ClassLoader p_loader,
                                               final boolean p_reload
        ) throws IllegalAccessException, InstantiationException, IOException
        {
            InputStream l_stream = null;
            final String l_resource = this.toResourceName( this.toBundleName( p_basename, p_locale ), "properties" );

            if ( !p_reload )
                l_stream = p_loader.getResourceAsStream( l_resource );
            else
            {

                final URL l_url = p_loader.getResource( l_resource );
                if ( l_url == null )
                    return null;

                final URLConnection l_connection = l_url.openConnection();
                if ( l_connection == null )
                    return null;

                l_connection.setUseCaches( false );
                l_stream = l_connection.getInputStream();
            }

            try
            {
                return new PropertyResourceBundle( new InputStreamReader( l_stream, "UTF-8" ) );

            }
            catch ( final Exception l_exception )
            {
            }
            finally
            {
                l_stream.close();
            }

            return null;
        }
    }

}
