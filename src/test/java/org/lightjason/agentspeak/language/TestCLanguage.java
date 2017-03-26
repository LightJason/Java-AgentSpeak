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

package org.lightjason.agentspeak.language;

import com.codepoetics.protonpack.StreamUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.CRelocateVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.stream.Stream;


/**
 * test for language components
 */
public final class TestCLanguage
{

    /**
     * test literal structure
     */
    @Test
    public final void literal()
    {
        final ILiteral l_emptyliteral = CLiteral.from( "foo/bar" );

        Assert.assertEquals( l_emptyliteral.functor(), "bar" );
        Assert.assertEquals( l_emptyliteral.fqnfunctor(), CPath.from( "foo/bar" ) );
        Assert.assertFalse( l_emptyliteral.hasAt() );
        Assert.assertTrue( l_emptyliteral.emptyValues() );
        Assert.assertTrue( l_emptyliteral.emptyAnnotations() );



        final ILiteral l_valueliteral = CLiteral.from( "foo/value", CRawTerm.from( 5 ), CRawTerm.from( "hello" ) );

        Assert.assertEquals( l_valueliteral.functor(), "value" );
        Assert.assertEquals( l_valueliteral.fqnfunctor(), CPath.from( "foo/value" ) );
        Assert.assertFalse( l_valueliteral.hasAt() );
        Assert.assertFalse( l_valueliteral.emptyValues() );
        Assert.assertTrue( l_valueliteral.emptyAnnotations() );

        Assert.assertTrue(
            StreamUtils.zip(
                Stream.of( 5, "hello" ),
                l_valueliteral.values(),
                ( i, j ) -> i.equals( j.raw() )
            ).allMatch( i -> i )
        );
    }


    /**
     * test raw-term structure
     */
    @Test
    public final void rawterm()
    {
        final ITerm l_stringterm = CRawTerm.from( "hello" );

        Assert.assertTrue( CCommon.rawvalueAssignableTo( l_stringterm, String.class ) );
        Assert.assertEquals( l_stringterm.raw(), "hello" );


        final double l_value = Math.random();
        final ITerm l_numberterm = CRawTerm.from( l_value );

        Assert.assertTrue( CCommon.rawvalueAssignableTo( l_numberterm, Number.class ) );
        Assert.assertTrue( CCommon.rawvalueAssignableTo( l_numberterm, Double.class ) );
        Assert.assertEquals( "number value", l_numberterm.raw(), l_value, 0 );
    }


    /**
     * test constant access structure
     */
    @Test( expected = RuntimeException.class )
    public final void constantaccess()
    {
        new CConstant<>( "CA", 5 ).set( 10 );
    }


    /**
     * test constantstructure
     */
    @Test
    public final void constant()
    {
        final double l_value = Math.random();
        final ITerm l_constant = new CConstant<>( "C", l_value );

        Assert.assertEquals( l_constant.functor(), "C" );
        Assert.assertTrue( l_constant.hasVariable() );
        Assert.assertTrue( CCommon.rawvalueAssignableTo( l_constant, Number.class ) );
        Assert.assertTrue( CCommon.rawvalueAssignableTo( l_constant, Double.class ) );
        Assert.assertEquals( "number value", l_constant.raw(), l_value, 0 );
    }


    /**
     * checks the variable structure
     */
    @Test
    public final void variable()
    {
        Assert.assertTrue( new CVariable<>( "_" ).any() );


        double l_value = Math.random();
        final IVariable<Number> l_variable = new CVariable<>( "V", l_value );

        Assert.assertEquals( l_variable.functor(), "V" );
        Assert.assertTrue( l_variable.allocated() );
        Assert.assertFalse( l_variable.mutex() );
        Assert.assertTrue( l_variable.hasVariable() );
        Assert.assertTrue( CCommon.rawvalueAssignableTo( l_variable, Number.class ) );
        Assert.assertTrue( CCommon.rawvalueAssignableTo( l_variable, Double.class ) );


        Assert.assertEquals( "number value", l_variable.raw(), l_value, 0 );

        l_value = Math.random();
        l_variable.set( l_value );

        Assert.assertEquals( "number value", l_variable.raw(), l_value, 0 );

        l_variable.set( null );
        Assert.assertFalse( l_variable.allocated() );

    }

    /**
     * test realocated variable
     */
    @Test
    public final void relocatevariable()
    {
        final IVariable<String> l_variable = new CVariable<>( "R" );
        final CRelocateVariable<String> l_relocate = new CRelocateVariable<>( l_variable );

        Assert.assertEquals( l_variable.functor(), "R" );
        Assert.assertEquals( l_relocate.functor(), "R" );

        Assert.assertFalse( l_variable.allocated() );
        Assert.assertFalse( l_relocate.allocated() );

        Assert.assertTrue( l_variable.hasVariable() );
        Assert.assertTrue( l_relocate.hasVariable() );


        l_relocate.set( "foo" );
        Assert.assertTrue( l_relocate.allocated() );
        Assert.assertEquals( l_relocate.raw(), "foo" );


        l_relocate.relocate();
        Assert.assertTrue( l_variable.allocated() );
        Assert.assertEquals( l_variable.raw(), "foo" );

    }


    /**
     * main method for testing
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        final TestCLanguage l_test = new TestCLanguage();

        l_test.literal();
        l_test.rawterm();
        l_test.constant();
        l_test.variable();
        l_test.relocatevariable();

        try
        {
            l_test.constantaccess();
        }
        catch ( final RuntimeException l_exception )
        {
            // check successfull
        }
    }
}
