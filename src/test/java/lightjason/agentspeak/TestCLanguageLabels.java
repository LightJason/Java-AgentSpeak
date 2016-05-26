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
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.agentspeak;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lightjason.agentspeak.common.CCommon;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;


/**
 * test all resource strings
 */
public final class TestCLanguageLabels
{
    /**
     * search path
     **/
    private static final URI SEARCHPATH;
    /**
     * property filenames with language data
     */
    private static final Map<String, URI> LANGUAGEPROPERY = new HashMap<>();

    static
    {
        URI l_uri = null;
        try
        {
            l_uri = CCommon.concatURL( CCommon.getResourceURL(), "../../src/main/java/" ).toURI();

            LANGUAGEPROPERY.put( "en", CCommon.concatURL( CCommon.getResourceURL(), "../../src/main/resources/language.properties" ).toURI() );
            LANGUAGEPROPERY.put( "de", CCommon.concatURL( CCommon.getResourceURL(), "../../src/main/resources/language_de.properties" ).toURI() );
        }
        catch ( final Exception l_exception )
        {
            assumeTrue( MessageFormat.format( "source directory cannot be read: {0}", l_exception.getMessage() ), false );
        }

        SEARCHPATH = l_uri;
    }

    /**
     * check package translation configuration versus property items
     */
    @Test
    public void testTranslation()
    {
        assumeTrue( "no languages are defined for checking", !LANGUAGEPROPERY.isEmpty() );

        // --- read language definitions from the configuration
        final Set<String> l_translation = Collections.unmodifiableSet(
            Arrays.stream( CCommon.getConfiguration().getObject( "translation" ).toString().split( "," ) )
                  .map( i -> i.trim().toLowerCase() )
                  .collect( Collectors.toSet() )
        );


        // --- check if a test (language resource) exists for each definied language
        final Set<String> l_translationtesting = new HashSet<>( l_translation );
        l_translationtesting.removeAll( LANGUAGEPROPERY.keySet() );
        assertFalse(
            MessageFormat.format(
                "configuration defines {1,choice,1#translation|1<translations} {0} that {1,choice,1#is|1<are} not tested",
                l_translationtesting,
                l_translationtesting.size()
            ),
            !l_translationtesting.isEmpty()
        );


        // --- check unused language resource files
        final Set<String> l_translationusing = new HashSet<>( LANGUAGEPROPERY.keySet() );
        l_translationusing.removeAll( l_translation );
        assertFalse(
            MessageFormat.format(
                "{1,choice,1#translation|1<translations} {0} {1,choice,1#is|1<are} checked, which will not be used within the package configuration",
                l_translationusing,
                l_translationusing.size()
            ),
            !l_translationusing.isEmpty()
        );
    }

    /**
     * test-case all resource strings
     *
     * @throws IOException throws on io errors
     */
    @Test
    public void testResourceString() throws IOException
    {
        assumeTrue( "no languages are defined for checking", !LANGUAGEPROPERY.isEmpty() );

        final Set<String> l_ignoredlabel = new HashSet<>();

        // --- parse source and get label definition
        final Set<String> l_label = Collections.unmodifiableSet(
            Files.walk( Paths.get( SEARCHPATH ) )
                 .filter( Files::isRegularFile )
                 .filter( i -> i.toString().endsWith( ".java" ) )
                 .flatMap( i -> {
                     try
                     {
                         final CJavaVistor l_parser = new CJavaVistor();
                         l_parser.visit( JavaParser.parse( new FileInputStream( i.toFile() ) ), null );
                         return l_parser.getLabel().stream();
                     }
                     catch ( final IOException l_excpetion )
                     {
                         assertTrue( MessageFormat.format( "io error on file [{0}]: {1}", i, l_excpetion.getMessage() ), false );
                         return Stream.<String>empty();
                     }
                     catch ( final ParseException l_exception )
                     {
                         // add label build by class path to the ignore list
                         l_ignoredlabel.add(
                             i.toAbsolutePath().toString()
                              // remove path to class directory
                              .replace(
                                  FileSystems.getDefault()
                                             .provider()
                                             .getPath( SEARCHPATH )
                                             .toAbsolutePath()
                                             .toString(),
                                  ""
                              )
                              // string starts with path separator
                              .substring( 1 )
                              // remove file extension
                              .replace( ".java", "" )
                              // replace separators with dots
                              .replace( "/", "." )
                              // convert to lower-case
                              .toLowerCase()
                              // remove package-root name
                              .replace( CCommon.getPackageRoot() + ".", "" )
                         );

                         System.err.println( MessageFormat.format( "parsing error on file [{0}]:\n{1}", i, l_exception.getMessage() ) );
                         return Stream.<String>empty();
                     }
                 } )
                 .collect( Collectors.toSet() )
        );

        // --- check label towards the property definition
        if ( l_ignoredlabel.size() > 0 )
            System.err.println( MessageFormat.format( "labels that starts with {0} are ignored, because parsing errors are occurred", l_ignoredlabel ) );

        LANGUAGEPROPERY.entrySet()
                       .forEach( i -> {
                           try
                           {
                               final Properties l_property = new Properties();
                               l_property.load( new FileInputStream( new File( i.getValue() ) ) );

                               final Set<String> l_parseditems = new HashSet<>( l_label );
                               final Set<String> l_propertyitems = l_property.keySet().parallelStream().map( j -> j.toString() ).collect( Collectors.toSet() );

                               // --- check if all property items are within the parsed labels
                               l_parseditems.removeAll( l_propertyitems );
                               assertTrue(
                                   MessageFormat.format(
                                       "the following {1,choice,1#key|1<keys} in language [{0}] {1,choice,1#is|1<are} not existing within the language file:\n{2}",
                                       i.getKey(),
                                       l_parseditems.size(),
                                       StringUtils.join( l_parseditems, ", " )
                                   ),
                                   l_parseditems.isEmpty()
                               );


                               // --- check if all parsed labels within the property item and remove ignored labels
                               l_propertyitems.removeAll( l_label );
                               final Set<String> l_ignoredpropertyitems = l_propertyitems.parallelStream()
                                                                                         .filter( j -> l_ignoredlabel.parallelStream()
                                                                                                                     .map( l -> j.startsWith( l ) )
                                                                                                                     .allMatch( l -> false )
                                                                                         )
                                                                                         .collect( Collectors.toSet() );
                               assertTrue(
                                   MessageFormat.format(
                                       "the following {1,choice,1#key|1<keys} in language [{0}] {1,choice,1#is|1<are} not existing within the source code:\n{2}",
                                       i.getKey(),
                                       l_ignoredpropertyitems.size(),
                                       StringUtils.join( l_ignoredpropertyitems, ", " )
                                   ),
                                   l_ignoredpropertyitems.isEmpty()
                               );
                           }
                           catch ( final IOException l_exception )
                           {
                               assertTrue( MessageFormat.format( "io exception: {0}", l_exception.getMessage() ), false );
                           }
                       } );
    }

    /**
     * main method
     *
     * @param p_args arguments
     * @throws IOException in file error
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCLanguageLabels l_test = new TestCLanguageLabels();
        l_test.testTranslation();
        l_test.testResourceString();
    }

    /**
     * method to build a package path
     *
     * @param p_names list of package parts
     * @return full-qualified string
     */
    private static String getPackagePath( final String... p_names )
    {
        return StringUtils.join( p_names, ClassUtils.PACKAGE_SEPARATOR );
    }


    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * AST visitor class
     */
    private static class CJavaVistor extends VoidVisitorAdapter<Object>
    {
        /**
         * method to translate strings
         */
        private static final String TRANSLATEMETHOD = "CCommon.getLanguageString";
        /**
         * reg expression to extract label data
         */
        private static final Pattern LANGUAGE = Pattern.compile( TRANSLATEMETHOD + ".+\\)" );
        /**
         * inner class name *
         */
        private String m_innerclass = "";
        /**
         * outer class name *
         */
        private String m_outerclass = "";
        /**
         * package name *
         */
        private String m_package = "";
        /**
         * label set
         */
        private Set<String> m_label = new HashSet<>();

        /**
         * returns the translated labels
         *
         * @return set with labels
         */
        public final Set<String> getLabel()
        {
            return m_label;
        }

        @Override
        public void visit( final PackageDeclaration p_package, final Object p_arg )
        {
            m_package = p_package.getName().toStringWithoutComments();
            super.visit( p_package, p_arg );
        }

        @Override
        public void visit( final ClassOrInterfaceDeclaration p_class, final Object p_arg )
        {
            if ( m_outerclass.isEmpty() )
                m_outerclass = p_class.getName();
            else
                m_innerclass = p_class.getName();

            super.visit( p_class, p_arg );

            m_innerclass = "";
        }

        @Override
        public void visit( final EnumDeclaration p_enum, final Object p_arg )
        {
            if ( m_outerclass.isEmpty() )
                m_outerclass = p_enum.getName();
            else
                m_innerclass = p_enum.getName();

            super.visit( p_enum, p_arg );

            m_innerclass = "";
        }

        @Override
        public void visit( final MethodCallExpr p_methodcall, final Object p_arg )
        {
            final String l_label = this.getLabel( p_methodcall.toStringWithoutComments() );
            if ( !l_label.isEmpty() )
                m_label.add( l_label );

            super.visit( p_methodcall, p_arg );
        }

        /**
         * returns full qualified class name
         * (inner & outer class)
         *
         * @param p_package package name
         * @param p_outerclass outer class
         * @param p_innerclass inner class
         * @param p_label label (only firat element is used)
         * @return full-qualified class name
         */
        private static String buildlabel( final String p_package, final String p_outerclass, final String p_innerclass, final String p_label )
        {
            return MessageFormat.format(
                "{0}{1}{2}{3}{4}{5}",
                p_package, ClassUtils.PACKAGE_SEPARATOR,
                p_outerclass, p_innerclass.isEmpty() ? "" : ClassUtils.PACKAGE_SEPARATOR,
                p_innerclass,

                p_label.isEmpty()
                ? ""
                : "." + p_label
            );
        }

        /**
         * gets the class name and label name
         *
         * @param p_line input timmed line
         * @return label or empty string
         */
        private String getLabel( final String p_line )
        {
            final Matcher l_matcher = LANGUAGE.matcher( p_line );
            if ( !l_matcher.find() )
                return "";

            final String[] l_split = l_matcher.group( 0 ).split( "," );
            final String[] l_return = new String[2];

            // class name
            l_return[0] = l_split[0].replace( TRANSLATEMETHOD, "" ).replace( "(", "" ).trim();
            // label name
            l_return[1] = l_split[1].replace( ")", "" ).replace( "\"", "" ).split( ";" )[0].trim().toLowerCase();

            return (
                "this".equals( l_return[0] )
                ? buildlabel( m_package, m_outerclass, m_innerclass, l_return[1] )
                : buildlabel( m_package, l_return[0].replace( ".class", "" ).replace( m_package + ".", "" ), "", l_return[1] )
            ).trim().toLowerCase().replace( CCommon.getPackageRoot() + ".", "" );

        }

    }

}
