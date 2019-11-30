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

package org.lightjason.agentspeak.common;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.binding.CMethodAction;
import org.lightjason.agentspeak.action.binding.IAgentAction;
import org.lightjason.agentspeak.action.binding.IAgentActionFilter;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
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
     * package separator
     */
    private static final String PACKAGESEPARATOR = ".";
    /**
     * language resource bundle
     **/
    private static final ResourceBundle LANGUAGE = ResourceBundle.getBundle(
        MessageFormat.format( "{0}{1}{2}", PACKAGEROOT, PACKAGESEPARATOR, "language" ),
        Locale.getDefault()
    );
    /**
     * properties of the package
     */
    private static final ResourceBundle PROPERTIES = ResourceBundle.getBundle(
        MessageFormat.format( "{0}{1}{2}", PACKAGEROOT, PACKAGESEPARATOR, "configuration" ),
        Locale.getDefault()
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
    @Nonnull
    public static Logger logger( final Class<?> p_class )
    {
        return Logger.getLogger( p_class.getName() );
    }

    /**
     * list of usable languages
     *
     * @return list of language pattern
     */
    @Nonnull
    public static String[] languages()
    {
        return Arrays.stream( PROPERTIES.getString( "translation" ).split( "," ) ).map( i -> i.trim().toLowerCase( Locale.ROOT ) ).toArray( String[]::new );
    }

    /**
     * returns the language bundle
     *
     * @return bundle
     */
    @Nonnull
    public static ResourceBundle languagebundle()
    {
        return LANGUAGE;
    }

    /**
     * returns the property data of the package
     *
     * @return bundle object
     */
    @Nonnull
    public static ResourceBundle configuration()
    {
        return PROPERTIES;
    }


    // --- access to lambda-streaming instantiation ------------------------------------------------------------------------------------------------------------

    /**
     * read lambda-streaming class of package
     *
     * @param p_package full-qualified package name or empty for default package
     * @return lambda-streaming stream
     */
    public static Stream<ILambdaStreaming<?>> lambdastreamingFromPackage( @Nullable final String... p_package )
    {
        return CCommon.classfrompackage( ILambdaStreaming.class, packagenames( p_package ) );
    }

    /**
     * read lambda-streaming class of package
     *
     * @param p_package stream with full-qualified package name
     * @return lambda-streaming stream
     */
    public static Stream<ILambdaStreaming<?>> lambdastreamingFromPackage( @Nonnull final Stream<String> p_package )
    {
        return CCommon.classfrompackage( ILambdaStreaming.class, p_package );
    }


    // --- access to action instantiation ----------------------------------------------------------------------------------------------------------------------

    /**
     * get all classes within an Java package as action
     *
     * @param p_package full-qualified package name or empty for default package
     * @return action stream
     */
    @Nonnull
    public static Stream<IAction> actionsFromPackage( @Nullable final String... p_package )
    {
        return CCommon.<IAction>classfrompackage( IAction.class, packagenames( p_package ) ).filter( CCommon::actionusable );
    }

    /**
     * get all classes within an Java package as action
     *
     * @param p_package stream with full-qualified package name
     * @return action stream
     */
    @Nonnull
    public static Stream<IAction> actionsFromPackage( @Nonnull final Stream<String> p_package )
    {
        return CCommon.<IAction>classfrompackage( IAction.class, p_package ).filter( CCommon::actionusable );
    }

    /**
     * returns actions by a class
     *
     * @param p_class class list
     * @return action stream
     *
     * @note class must be an inheritance of the IAgent interface
     */
    @Nonnull
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    public static Stream<IAction> actionsFromAgentClass( @Nonnull final Class<? extends IAgent<?>>... p_class )
    {
        return p_class.length == 0 ? Stream.empty() : actionsFromAgentClass( Arrays.stream( p_class ) );
    }

    /**
     * returns actions by a class
     *
     * @param p_class class list
     * @return action stream
     *
     * @note class must be an inheritance of the IAgent interface
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static Stream<IAction> actionsFromAgentClass( @Nonnull final Stream<Class<? extends IAgent<?>>> p_class )
    {
        return p_class.parallel()
                      .flatMap( CCommon::methods )
                      .map( i ->
                      {
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
     * build package name structure
     *
     * @param p_package package names
     * @return package stream or defualt package name
     */
    private static Stream<String> packagenames( @Nullable final String... p_package )
    {
        return Objects.isNull( p_package ) || p_package.length == 0
               ? Stream.of( MessageFormat.format( "{0}.{1}", PACKAGEROOT, "action" ) )
               : Arrays.stream( p_package );
    }

    /**
     * checks if an action is usable
     *
     * @param p_action action object
     * @return boolean usable flag
     */
    private static boolean actionusable( final IAction p_action )
    {
        if ( p_action.name().empty() || p_action.name().get( 0 ).trim().isEmpty() )
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

        return true;
    }


    /**
     * reads all methods by the action-annotations
     * for building agent-actions
     *
     * @param p_class class
     * @return stream of all methods with inheritance
     */
    @Nonnull
    private static Stream<Method> methods( final Class<?> p_class )
    {
        final Pair<Boolean, IAgentAction.EAccess> l_classannotation = CCommon.isActionClass( p_class );
        if ( !l_classannotation.getLeft() )
            return Objects.isNull( p_class.getSuperclass() )
                   ? Stream.empty()
                   : methods( p_class.getSuperclass() );

        final Predicate<Method> l_filter = IAgentAction.EAccess.WHITELIST.equals( l_classannotation.getRight() )
                                           ? i -> !CCommon.isActionFiltered( i, p_class )
                                           : i -> CCommon.isActionFiltered( i, p_class );

        return Stream.concat(
            Arrays.stream( p_class.getDeclaredMethods() )
                  .parallel()
                  .peek( i -> i.setAccessible( true ) )
                  .filter( i -> !Modifier.isAbstract( i.getModifiers() ) )
                  .filter( i -> !Modifier.isInterface( i.getModifiers() ) )
                  .filter( i -> !Modifier.isNative( i.getModifiers() ) )
                  .filter( i -> !Modifier.isStatic( i.getModifiers() ) )
                  .filter( l_filter ),
            methods( p_class.getSuperclass() )
        );
    }

    /**
     * filter of a class to use it as action
     *
     * @param p_class class for checking
     * @return boolean flag of check result
     */
    @Nonnull
    private static Pair<Boolean, IAgentAction.EAccess> isActionClass( final Class<?> p_class )
    {
        if ( !p_class.isAnnotationPresent( IAgentAction.class ) )
            return new ImmutablePair<>( false, IAgentAction.EAccess.BLACKLIST );

        final IAgentAction l_annotation = p_class.getAnnotation( IAgentAction.class );
        return new ImmutablePair<>(
            l_annotation.classes().length == 0
            || Arrays.stream( p_class.getAnnotation( IAgentAction.class ).classes() )
                     .parallel()
                     .anyMatch( p_class::equals ),
            l_annotation.access()
        );
    }

    /**
     * class filter of an action to use it
     *
     * @param p_method method for checking
     * @param p_class class
     * @return boolean flag of check result
     */
    private static boolean isActionFiltered( final Method p_method, final Class<?> p_class )
    {
        return p_method.isAnnotationPresent( IAgentActionFilter.class )
               && (
                   p_method.getAnnotation( IAgentActionFilter.class ).classes().length == 0
                   || Arrays.stream( p_method.getAnnotation( IAgentActionFilter.class ).classes() )
                            .parallel()
                            .anyMatch( p_class::equals )
               );
    }


    // --- resource access -------------------------------------------------------------------------------------------------------------------------------------

    /**
     * read classes of package
     *
     * @param p_class class
     * @param p_package full-qualified package name or empty for default package
     * @return object stream
     *
     * @tparam T class type
     */
    @SuppressWarnings( "unchecked" )
    private static <T> Stream<T> classfrompackage( @Nonnull final Class<?> p_class, @Nonnull final Stream<String> p_package )
    {
        return p_package.flatMap( j -> new Reflections( j ).getSubTypesOf( p_class )
                                                           .parallelStream()
                                                           .filter( i -> !Modifier.isAbstract( i.getModifiers() ) )
                                                           .filter( i -> !Modifier.isInterface( i.getModifiers() ) )
                                                           .filter( i -> Modifier.isPublic( i.getModifiers() ) )
                                                           .map( i ->
                                                           {
                                                               try
                                                               {
                                                                   return (T) i.getConstructor().newInstance();
                                                               }
                                                               catch ( final NoSuchMethodException | InvocationTargetException
                                                                   | IllegalAccessException | InstantiationException l_exception )
                                                               {
                                                                   LOGGER.warning( CCommon.languagestring( CCommon.class, "classinstantiateerror", i, l_exception ) );
                                                                   return null;
                                                               }
                                                           } )
                                                           .filter( Objects::nonNull ) );
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
    @Nonnull
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
    @Nonnull
    public static String languagestring( final Class<?> p_class, final String p_label, final Object... p_parameter )
    {
        try
        {
            return MessageFormat.format( LANGUAGE.getString( languagelabel( p_class, p_label ) ), p_parameter );
        }
        catch ( final MissingResourceException l_exception )
        {
            return Objects.isNull( p_parameter )
                   ? MessageFormat.format( "{0} - {1}", p_class, p_label )
                   : MessageFormat.format( "{0} - {1}: {2}", p_class, p_label, Arrays.toString( p_parameter ) );
        }
    }

    /**
     * returns the label of a class and string to get access to the resource
     *
     * @param p_class class for static calls
     * @param p_label label name of the object
     * @return label name
     */
    @Nonnull
    private static String languagelabel( final Class<?> p_class, final String p_label )
    {
        return ( p_class.getCanonicalName().toLowerCase( Locale.ROOT ) + "." + p_label.toLowerCase( Locale.ROOT ) )
            .replaceAll( "[^a-zA-Z0-9_.]+", "" )
            .replace( PACKAGEROOT + ".", "" );
    }

}
