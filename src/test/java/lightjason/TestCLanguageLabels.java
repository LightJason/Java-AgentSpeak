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

package lightjason;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lightjason.common.CCommon;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * test all resource strings
 *
 * @todo add ignore class definitions
 */
@SuppressWarnings( "serial" )
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
    /**
     * list of all project packages for searching classes
     *
     * @note is needed on finding class-calls within other classes e.g. class a.b.c uses y.x.class
     */
    private static final Set<String> SEARCHPACKAGES = new HashSet<String>()
    {{
        for ( String l_package : new String[]{
                getPackagePath( "agent", "beliefbase", "common", "error", "language", "parser" )
        } )
            add( getPackagePath( CCommon.getPackage(), l_package ) );

    }};
    /**
     * skip list to ignore files
     */
    private static final Set<String> SKIPFILES = new HashSet<String>()
    {{
        add( "lightjason/grammar/CASTErrorListener.java" );
    }};
    /**
     * set with all labels *
     */
    private final Set<String> m_labels = new HashSet<>();

    static
    {
        URI l_uri = null;
        try
        {
            l_uri = CCommon.concatURL( CCommon.getResourceURL(), "../../src/main/java/" ).toURI();

            LANGUAGEPROPERY.put( "en", CCommon.concatURL( CCommon.getResourceURL(), "../../src/main/resources/language.properties" ).toURI() );
            LANGUAGEPROPERY.put( "de", CCommon.concatURL( CCommon.getResourceURL(), "../../src/main/resources/language_de.properties" ).toURI() );
        }
        catch ( final Exception p_exception )
        {
        }

        SEARCHPATH = l_uri;
    }

    /**
     * test-case all resource strings
     *
     * @bug disable unused label checking
     */
    @Test
    public void testResourceString() throws IOException
    {
        // --- check source -> label definition
        Files.walk( Paths.get( SEARCHPATH ) )
             .filter( Files::isRegularFile )
             .forEach( i -> {
                 if ( ( !i.toString().endsWith( ".java" ) ) || ( SKIPFILES.contains( SEARCHPATH.relativize( i.toUri() ).normalize().toString() ) ) )
                     return;

                 try
                 {
                     new CJavaVistor().visit( JavaParser.parse( new FileInputStream( i.toFile() ) ), null );
                 }
                 catch ( final ParseException | IOException p_exception )
                 {
                     //fail( MessageFormat.format( "{0}: {1}", i, p_exception.getMessage() ) );
                 }
             } );


        // --- check label -> property definition
        Arrays.stream( CCommon.getConfiguration().getProperty( "translation" ).split( "," ) )
              .forEach( i -> {
                  try
                  {
                      final Properties l_property = new Properties();
                      l_property.load( new FileInputStream( new File( LANGUAGEPROPERY.get( i ) ) ) );

                      final Set<String> l_labels = l_property.keySet().parallelStream().map( j -> j.toString() ).collect( Collectors.toSet() );
                      l_labels.removeAll( m_labels );

                      //assertTrue(
                      //        String.format( "the following keys in language [%s] are unused: %s", i, StringUtils.join( l_labels, ", " ) ), l_labels.isEmpty()
                      //);
                  }
                  catch ( final IOException l_exception )
                  {
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
        new TestCLanguageLabels().testResourceString();
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
        }

        @Override
        public void visit( final EnumDeclaration p_enum, final Object p_arg )
        {
            if ( m_outerclass.isEmpty() )
                m_outerclass = p_enum.getName();
            else
                m_innerclass = p_enum.getName();

            super.visit( p_enum, p_arg );
        }

        @Override
        public void visit( final MethodCallExpr p_methodcall, final Object p_arg )
        {
            final String l_label = this.getLabel( p_methodcall.toStringWithoutComments() );
            if ( !l_label.isEmpty() )
                System.out.println( "###>>> " + l_label );

            //if ( l_label != null )
            //    this.checkLabel( l_label[0], l_label[1] );
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
                    p_outerclass, p_innerclass.isEmpty() ? "" : ClassUtils.INNER_CLASS_SEPARATOR,
                    p_innerclass,

                    p_label.isEmpty()
                    ? ""
                    : "." + p_label
            );
        }


        /**
         * checks all languages
         *
         * @param p_classname full qualified class name
         * @param p_label label name
         *
        private void checkLabel( final String p_classname, final String p_label )
        {
            // construct class object
            final Class<?> l_class;
            try
            {
                l_class = this.getClass( p_classname );
            }
            catch ( final ClassNotFoundException p_exception )
            {
                fail( String.format( "class [%s] not found", p_classname ) );
                return;
            }

            /*
            // check resource - build a set with all strings because the fallback is always english and
            // if a translation does not exists the string must be included in english
            final Set<String> l_used = new HashSet<>();
            for ( final String l_language : CCommon.getConfiguration().getProperty( "translation" ).split( "," ) )
                try
                {
                    final String l_translation = CCommon.getLanguageString( l_language, l_class, p_label );
                    assertFalse(
                            String.format(
                                    "label [%s] in language [%s] within class [%s] not found", CCommon.getLanguageLabel( l_class, p_label ), l_language,
                                    p_classname
                            ), ( l_translation == null ) || ( l_translation.isEmpty() || (l_used.contains( l_translation )) )
                    );
                    l_used.add( l_translation );
                }
                catch ( final IllegalStateException p_exception )
                {
                    return;
                }

            m_labels.add( CCommon.getLanguageLabel( l_class, p_label ) );
        }

        /**
         * tries to instantiate a class
         *
         * @param p_name class name
         * @return class object
         *
         * @throws ClassNotFoundException thrown if class is not found
         *
        private Class<?> getClass( final String p_name ) throws ClassNotFoundException
        {
            // --- first load try ---
            try
            {
                // try to load class with default behaviour
                return Class.forName( p_name );
            }
            catch ( final ClassNotFoundException l_fornameexception )
            {
                // --- second load try ---
                try
                {
                    // try to load class depended on the current classloader
                    return this.getClass().getClassLoader().loadClass( p_name );
                }
                catch ( final ClassNotFoundException l_loaderexception )
                {
                    // --- third load try ---
                    // create a name without package
                    final String[] l_part = StringUtils.split( p_name, ClassUtils.PACKAGE_SEPARATOR );
                    final String l_name = l_part[l_part.length - 1];

                    // try to load class within the defined search pathes
                    for ( final String l_package : SEARCHPACKAGES )
                        try
                        {
                            return this.getClass().getClassLoader().loadClass( getPackagePath( l_package, l_name ) );
                        }
                        catch ( final ClassNotFoundException l_iteratingexception )
                        {
                        }
                    throw new ClassNotFoundException();
                }
            }
        }
         */

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

            return "this".equals( l_return[0] )
                   ? buildlabel( m_package, m_outerclass, m_innerclass, l_return[1] )
                   : buildlabel( m_package, l_return[0].replace( m_package + ".", "" ), "", l_return[1] );

        }

    }

}
