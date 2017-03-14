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

package org.lightjason.agentspeak.common;

import com.google.common.reflect.ClassPath;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.binding.CMethodAction;
import org.lightjason.agentspeak.action.binding.IAgentAction;
import org.lightjason.agentspeak.action.binding.IAgentActionFilter;
import org.lightjason.agentspeak.agent.IAgent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;


/**
 * class for any helper calls
 */
public final class CCommon
{
    /**
     * package name
     **/
    public static final String PACKAGEROOT = "org.lightjason.agentspeak";
    /**
     * logger
     */
    private static final Logger LOGGER = CCommon.logger( CCommon.class );
    /**
     * language resource bundle
     **/
    private static final ResourceBundle LANGUAGE = ResourceBundle.getBundle(
                                                        MessageFormat.format( "{0}.{1}", PACKAGEROOT, "language" ),
                                                        Locale.getDefault(),
                                                        new CUTF8Control()
    );
    /**
     * properties of the package
     */
    private static final ResourceBundle PROPERTIES = ResourceBundle.getBundle(
                                                        MessageFormat.format( "{0}.{1}", PACKAGEROOT, "configuration" ),
                                                        Locale.getDefault(),
                                                        new CUTF8Control()
    );


    /**
     * private ctor - avoid instantiation
     */
    private CCommon()
    {
    }

    /**
     * returns a logger instance
     *
     * @param p_class class type
     * @return logger
     */
    public static Logger logger( final Class<?> p_class )
    {
        return Logger.getLogger( p_class.getName() );
    }

    /**
     * list of usable languages
     *
     * @return list of language pattern
     */
    public static String[] languages()
    {
        return Arrays.stream( PROPERTIES.getString( "translation" ).split( "," ) ).map( i -> i.trim().toLowerCase() ).toArray( String[]::new );
    }

    /**
     * returns the language bundle
     *
     * @return bundle
     */
    public static ResourceBundle languagebundle()
    {
        return LANGUAGE;
    }

    /**
     * returns the property data of the package
     *
     * @return bundle object
     */
    public static ResourceBundle configuration()
    {
        return PROPERTIES;
    }

    // --- access to action instantiation ----------------------------------------------------------------------------------------------------------------------

    /**
     * get all classes within an Java package as action
     *
     * @param p_package full-qualified package name or empty for default package
     * @return action stream
     */
    @SuppressWarnings( "unchecked" )
    public static Stream<IAction> actionsFromPackage( final String... p_package )
    {
        return ( ( p_package == null ) || ( p_package.length == 0 )
                 ? Stream.of( MessageFormat.format( "{0}.{1}", PACKAGEROOT, "action.buildin" ) )
                 : Arrays.stream( p_package ) )
            .flatMap( j -> {
                try
                {
                    return ClassPath.from( Thread.currentThread().getContextClassLoader() )
                                    .getTopLevelClassesRecursive( j )
                                    .parallelStream()
                                    .map( ClassPath.ClassInfo::load )
                                    .filter( i -> !Modifier.isAbstract( i.getModifiers() ) )
                                    .filter( i -> !Modifier.isInterface( i.getModifiers() ) )
                                    .filter( i -> Modifier.isPublic( i.getModifiers() ) )
                                    .filter( IAction.class::isAssignableFrom )
                                    .map( i -> {
                                        try
                                        {
                                            return (IAction) i.newInstance();
                                        }
                                        catch ( final IllegalAccessException | InstantiationException l_exception )
                                        {
                                            LOGGER.warning( CCommon.languagestring( CCommon.class, "actioninstantiate", i, l_exception ) );
                                            return null;
                                        }
                                    } )

                                    // action can be instantiate
                                    .filter( Objects::nonNull )

                                    // check usable action name
                                    .filter( CCommon::actionusable );
                }
                catch ( final IOException l_exception )
                {
                    throw new UncheckedIOException( l_exception );
                }
            } );
    }


    /**
     * returns actions by a class
     * @note class must be an inheritance of the IAgent interface
     *
     * @param p_class class list
     * @return action stream
     */
    @SuppressWarnings( "unchecked" )
    public static Stream<IAction> actionsFromAgentClass( final Class<?>... p_class )
    {
        return p_class == null || p_class.length == 0
               ? Stream.of()
               : Arrays.stream( p_class )
                       .parallel()
                       .filter( IAgent.class::isAssignableFrom )
                       .flatMap( i -> CCommon.methods( i, i ) )
                       .map( i -> {
                           try
                           {
                               return (IAction) new CMethodAction( i );
                           }
                           catch ( final IllegalAccessException l_exception )
                           {
                               LOGGER.warning( CCommon.languagestring( CCommon.class, "actioninstantiate", i, l_exception ) );
                               return null;
                           }
                       } )

                       // action can be instantiate
                       .filter( Objects::nonNull )

                       // check usable action name
                       .filter( CCommon::actionusable );
    }

    /**
     * checks if an action is usable
     *
     * @param p_action action object
     * @return boolean usable flag
     */
    private static boolean actionusable( final IAction p_action )
    {
        if ( ( p_action.name() == null ) || ( p_action.name().isEmpty() ) || ( p_action.name().get( 0 ).trim().isEmpty() ) )
        {
            LOGGER.warning( CCommon.languagestring( CCommon.class, "actionnameempty" ) );
            return false;
        }

        if ( !Character.isLetter( p_action.name().get( 0 ).charAt( 0 ) ) )
        {
            LOGGER.warning( CCommon.languagestring( CCommon.class, "actionletter", p_action ) );
            return false;
        }

        if ( !Character.isLowerCase( p_action.name().get( 0 ).charAt( 0 ) ) )
        {
            LOGGER.warning( CCommon.languagestring( CCommon.class, "actionlowercase", p_action ) );
            return false;
        }

        if ( p_action.minimalArgumentNumber() < 0 )
        {
            LOGGER.warning( CCommon.languagestring( CCommon.class, "actionargumentsnumber", p_action ) );
            return false;
        }

        return true;
    }


    /**
     * reads all methods by the action-annotations
     * for building agent-actions
     *
     * @param p_class class
     * @param p_root root class
     * @return stream of all methods with inheritance
     */
    private static Stream<Method> methods( final Class<?> p_class, final Class<?> p_root )
    {
        final Pair<Boolean, IAgentAction.EAccess> l_classannotation = CCommon.isActionClass( p_class );
        if ( !l_classannotation.getLeft() )
            return p_class.getSuperclass() == null
                   ? Stream.of()
                   : methods( p_class.getSuperclass(), p_root );

        final Predicate<Method> l_filter = IAgentAction.EAccess.WHITELIST.equals( l_classannotation.getRight() )
                                           ? i -> !CCommon.isActionFiltered( i, p_root )
                                           : i -> CCommon.isActionFiltered( i, p_root );

        return Stream.concat(
            Arrays.stream( p_class.getDeclaredMethods() )
                  .parallel()
                  .map( i -> {
                      i.setAccessible( true );
                      return i;
                  } )
                  .filter( i -> !Modifier.isAbstract( i.getModifiers() ) )
                  .filter( i -> !Modifier.isInterface( i.getModifiers() ) )
                  .filter( i -> !Modifier.isNative( i.getModifiers() ) )
                  .filter( i -> !Modifier.isStatic( i.getModifiers() ) )
                  .filter( l_filter ),
            methods( p_class.getSuperclass(), p_root )
        );
    }

    /**
     * filter of a class to use it as action
     *
     * @param p_class class for checking
     * @return boolean flag of check result
     */
    private static Pair<Boolean, IAgentAction.EAccess> isActionClass( final Class<?> p_class )
    {
        if ( !p_class.isAnnotationPresent( IAgentAction.class ) )
            return new ImmutablePair<>( false, IAgentAction.EAccess.BLACKLIST );

        final IAgentAction l_annotation = p_class.getAnnotation( IAgentAction.class );
        return new ImmutablePair<>(
                   ( l_annotation.classes().length == 0 )
                   || ( Arrays.stream( p_class.getAnnotation( IAgentAction.class ).classes() )
                              .parallel()
                              .anyMatch( p_class::equals )
                   ),
                   l_annotation.access()
               );
    }

    /**
     * class filter of an action to use it
     *
     * @param p_method method for checking
     * @param p_root root class
     * @return boolean flag of check result
     */
    private static boolean isActionFiltered( final Method p_method, final Class<?> p_root )
    {
        return p_method.isAnnotationPresent( IAgentActionFilter.class )
               && (
                   ( p_method.getAnnotation( IAgentActionFilter.class ).classes().length == 0 )
                   || ( Arrays.stream( p_method.getAnnotation( IAgentActionFilter.class ).classes() )
                              .parallel()
                              .anyMatch( p_root::equals )
                   )
               );
    }

    // --- resource access -------------------------------------------------------------------------------------------------------------------------------------

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
    public static URL concaturl( final URL p_base, final String p_string ) throws MalformedURLException, URISyntaxException
    {
        return new URL( p_base.toString() + p_string ).toURI().normalize().toURL();
    }

    /**
     * returns root path of the resource
     *
     * @return URL of file or null
     */
    public static URL resourceurl()
    {
        return CCommon.class.getClassLoader().getResource( "" );
    }

    /**
     * returns a file from a resource e.g. Jar file
     *
     * @param p_file file
     * @return URL of file or null
     *
     * @throws URISyntaxException thrown on syntax error
     * @throws MalformedURLException thrown on malformat
     */
    public static URL resourceurl( final String p_file ) throws URISyntaxException, MalformedURLException
    {
        return resourceurl( new File( p_file ) );
    }

    /**
     * returns a file from a resource e.g. Jar file
     *
     * @param p_file file relative to the CMain
     * @return URL of file or null
     *
     * @throws URISyntaxException is thrown on URI errors
     * @throws MalformedURLException is thrown on malformat
     */
    private static URL resourceurl( final File p_file ) throws URISyntaxException, MalformedURLException
    {
        if ( p_file.exists() )
            return p_file.toURI().normalize().toURL();
        return CCommon.class.getClassLoader().getResource( p_file.toString().replace( File.separator, "/" ) ).toURI().normalize().toURL();
    }

    // --- language operations ---------------------------------------------------------------------------------------------------------------------------------

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
    public static <T> String languagestring( final T p_source, final String p_label, final Object... p_parameter )
    {
        return languagestring( p_source.getClass(), p_label, p_parameter );
    }

    /**
     * returns a string of the resource file
     *
     * @param p_class class for static calls
     * @param p_label label name of the object
     * @param p_parameter object array with substitutions
     * @return resource string
     */
    public static String languagestring( final Class<?> p_class, final String p_label, final Object... p_parameter )
    {
        try
        {
            return MessageFormat.format( LANGUAGE.getString( languagelabel( p_class, p_label ) ), p_parameter );
        }
        catch ( final MissingResourceException l_exception )
        {
            return "";
        }
    }

    /**
     * returns the label of a class and string to get access to the resource
     *
     * @param p_class class for static calls
     * @param p_label label name of the object
     * @return label name
     */
    private static String languagelabel( final Class<?> p_class, final String p_label )
    {
        return ( p_class.getCanonicalName().toLowerCase( Locale.ROOT ) + "." + p_label.toLowerCase( Locale.ROOT ) ).replaceAll( "[^a-zA-Z0-9_\\.]+", "" ).replace(
            PACKAGEROOT + ".", "" );
    }

    // --- resource utf-8 encoding -----------------------------------------------------------------------------------------------------------------------------

    /**
     * class to read UTF-8 encoded property file
     *
     * @note Java default encoding for property files is ISO-Latin-1
     */
    private static final class CUTF8Control extends ResourceBundle.Control
    {

        public final ResourceBundle newBundle( final String p_basename, final Locale p_locale, final String p_format, final ClassLoader p_loader,
                                               final boolean p_reload
        ) throws IllegalAccessException, InstantiationException, IOException
        {
            final InputStream l_stream;
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

            final ResourceBundle l_bundle = new PropertyResourceBundle( new InputStreamReader( l_stream, "UTF-8" ) );
            l_stream.close();
            return l_bundle;
        }
    }

}
