/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
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

package org.lightjason.agentspeak.language;

import com.codepoetics.protonpack.StreamUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.CMutexVariable;
import org.lightjason.agentspeak.language.variable.CRelocateMutexVariable;
import org.lightjason.agentspeak.language.variable.CRelocateVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.stream.Stream;


/**
 * test for language components
 */
public final class TestCTermVariablesConstant extends IBaseTest
{

    /**
     * test literal structure
     */
    @Test
    public void literal()
    {
        final ILiteral l_emptyliteral = CLiteral.of( "foo/bar" );

        Assert.assertEquals( l_emptyliteral.functor(), "bar" );
        Assert.assertEquals( l_emptyliteral.fqnfunctor(), CPath.of( "foo/bar" ) );
        Assert.assertFalse( l_emptyliteral.hasAt() );
        Assert.assertTrue( l_emptyliteral.emptyValues() );



        final ILiteral l_valueliteral = CLiteral.of( "foo/value", CRawTerm.of( 5 ), CRawTerm.of( "hello" ) );

        Assert.assertEquals( l_valueliteral.functor(), "value" );
        Assert.assertEquals( l_valueliteral.fqnfunctor(), CPath.of( "foo/value" ) );
        Assert.assertFalse( l_valueliteral.hasAt() );
        Assert.assertFalse( l_valueliteral.emptyValues() );

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
    public void rawterm()
    {
        final ITerm l_stringterm = CRawTerm.of( "hello" );

        Assert.assertTrue( CCommon.isssignableto( l_stringterm, String.class ) );
        Assert.assertEquals( l_stringterm.raw(), "hello" );


        final double l_value = Math.random();
        final ITerm l_numberterm = CRawTerm.of( l_value );

        Assert.assertTrue( CCommon.isssignableto( l_numberterm, Number.class ) );
        Assert.assertTrue( CCommon.isssignableto( l_numberterm, Double.class ) );
        Assert.assertEquals( "number value", l_numberterm.raw(), l_value, 0 );
    }


    /**
     * test constant access structure
     */
    @Test( expected = RuntimeException.class )
    public void constantaccess()
    {
        new CConstant<>( "CA", 5 ).set( 10 );
    }


    /**
     * test constant structure
     */
    @Test
    public void constant()
    {
        final double l_value = Math.random();
        final ITerm l_constant = new CConstant<>( "C", l_value );

        Assert.assertEquals( l_constant.functor(), "C" );
        Assert.assertTrue( l_constant.hasVariable() );
        Assert.assertTrue( CCommon.isssignableto( l_constant, Number.class ) );
        Assert.assertTrue( CCommon.isssignableto( l_constant, Double.class ) );
        Assert.assertEquals( "constant number value", l_constant.raw(), l_value, 0 );
    }

    /**
     * test constant copy
     */
    @Test
    public void constantcopy()
    {
        Assert.assertEquals(
            new CConstant<>( "const/value", "test" ).shallowcopysuffix().functor(),
            "value"
        );

        Assert.assertEquals(
            new CConstant<>( "const/value", "test" ).shallowcopy( CPath.of( "xxx" ) ).fqnfunctor().toString(),
            "xxx/const/value"
        );
    }

    /**
     * check any variable
     */
    @Test( expected = IllegalStateException.class )
    public void variableany()
    {
        final IVariable<?> l_variable = new CVariable<Object>( "_" );

        Assert.assertTrue( l_variable.any() );
        Assert.assertFalse( l_variable.allocated() );

        l_variable.thrownotallocated();
    }


    /**
     * test exception on value asiable
     */
    @Test( expected = IllegalArgumentException.class )
    public void variablevalueassignable()
    {
        new CVariable<Object>( "num", 123 ).throwvaluenotassignableto( String.class );
    }

    /**
     * variable equals
     */
    @Test
    public void variableequals()
    {
        Assert.assertEquals(
            new CVariable<Object>( "foo", "str" ),
            new CVariable<Object>( "foo", 123 )
        );

        Assert.assertEquals(
            new CVariable<>( "eq", 56  ),
            new CVariable<>( "eq", 56 )
        );
    }

    /**
     * test tostring
     */
    @Test
    public void variabletostring()
    {
        Assert.assertEquals( new CVariable<>( "data" ).toString(), "data()" );
        Assert.assertEquals( new CVariable<>( "data", "value" ).toString(), "data(value)" );
    }


    /**
     * checks the variable structure
     */
    @Test
    public void variable()
    {
        Assert.assertTrue( new CVariable<>( "_" ).any() );


        double l_value = Math.random();
        final IVariable<Number> l_variable = new CVariable<>( "V", l_value );

        Assert.assertEquals( l_variable.functor(), "V" );
        Assert.assertTrue( l_variable.allocated() );
        Assert.assertFalse( l_variable.mutex() );
        Assert.assertTrue( l_variable.hasVariable() );
        Assert.assertTrue( CCommon.isssignableto( l_variable, Number.class ) );
        Assert.assertTrue( CCommon.isssignableto( l_variable, Double.class ) );


        Assert.assertEquals( "get variable number value", l_variable.raw(), l_value, 0 );

        l_value = Math.random();
        l_variable.set( l_value );

        Assert.assertEquals( "set variable number value", l_variable.raw(), l_value, 0 );

        l_variable.set( null );
        Assert.assertFalse( l_variable.allocated() );
    }

    /**
     * test variable functor
     */
    @Test
    public void variablefunctor()
    {
        Assert.assertEquals(
            new CVariable<>( "prefix/name" ).functorpath(),
            CPath.of( "prefix" )
        );
    }

    /**
     * check the mutex variable
     */
    @Test
    public void variablemutex()
    {
        double l_value = Math.random();
        final IVariable<Number> l_variable = new CMutexVariable<>( "V", l_value );

        Assert.assertEquals( l_variable.functor(), "V" );
        Assert.assertTrue( l_variable.allocated() );
        Assert.assertTrue( l_variable.mutex() );
        Assert.assertTrue( l_variable.hasVariable() );
        Assert.assertTrue( CCommon.isssignableto( l_variable, Number.class ) );
        Assert.assertTrue( CCommon.isssignableto( l_variable, Double.class ) );


        Assert.assertEquals( "get mutex variable number value", l_variable.raw(), l_value, 0 );

        l_value = Math.random();
        l_variable.set( l_value );

        Assert.assertEquals( "set mutex variable number value", l_variable.raw(), l_value, 0 );

        l_variable.set( null );
        Assert.assertFalse( l_variable.allocated() );
    }

    /**
     * test variable shallow-copy
     */
    @Test
    public void variablecopy()
    {
        final IVariable<?> l_variable = new CVariable<>( "prefix/copy", new Object()  );

        Assert.assertEquals( l_variable.shallowcopysuffix().fqnfunctor(), CPath.of( "copy" ) );
        Assert.assertEquals( l_variable.shallowcopy( CPath.of( "xxx" ) ).fqnfunctor(), CPath.of( "xxx/prefix/copy" ) );
        Assert.assertEquals( l_variable.shallowcopysuffix().<Object>raw(), l_variable.<Object>raw() );

        final ITerm l_deep = l_variable.deepcopy( CPath.of( "foo" ) );
        Assert.assertEquals( l_deep.fqnfunctor(), CPath.of( "prefix/copy/foo" ) );
        Assert.assertNotEquals( l_deep.<Object>raw(), l_variable.<Object>raw() );

        final ITerm l_deepsuffix = l_variable.deepcopysuffix();
        Assert.assertEquals( l_deepsuffix.fqnfunctor(), CPath.of( "copy" ) );
        Assert.assertNotEquals( l_deepsuffix.<Object>raw(), l_variable.<Object>raw() );
    }


    /**
     * test realocated variable
     */
    @Test
    public void relocatevariablerelocate()
    {
        final IVariable<String> l_variable = new CVariable<>( "RA" );
        final CRelocateVariable<String> l_relocate = new CRelocateVariable<>( l_variable );

        Assert.assertFalse( l_relocate.mutex() );

        Assert.assertEquals( l_variable.functor(), "RA" );
        Assert.assertEquals( l_relocate.functor(), "RA" );

        Assert.assertFalse( l_variable.allocated() );
        Assert.assertFalse( l_relocate.allocated() );

        Assert.assertTrue( l_variable.hasVariable() );
        Assert.assertTrue( l_relocate.hasVariable() );


        l_relocate.set( "relocated foo" );
        Assert.assertTrue( l_relocate.allocated() );
        Assert.assertEquals( l_relocate.raw(), "relocated foo" );


        l_relocate.relocate();
        Assert.assertTrue( l_variable.allocated() );
        Assert.assertEquals( l_variable.raw(), "relocated foo" );
    }


    /**
     * test realocated variable
     */
    @Test
    public void relocatevariablemutex()
    {
        final IVariable<String> l_variable = new CVariable<>( "RAM" );
        final CRelocateMutexVariable<String> l_relocate = new CRelocateMutexVariable<>( l_variable );

        Assert.assertTrue( l_relocate.mutex() );

        Assert.assertEquals( l_variable.functor(), "RAM" );
        Assert.assertEquals( l_relocate.functor(), "RAM" );

        Assert.assertFalse( l_variable.allocated() );
        Assert.assertFalse( l_relocate.allocated() );

        Assert.assertTrue( l_variable.hasVariable() );
        Assert.assertTrue( l_relocate.hasVariable() );


        l_relocate.set( "relocated mutex foo" );
        Assert.assertTrue( l_relocate.allocated() );
        Assert.assertEquals( l_relocate.raw(), "relocated mutex foo" );


        l_relocate.relocate();
        Assert.assertTrue( l_variable.allocated() );
        Assert.assertEquals( l_variable.raw(), "relocated mutex foo" );
    }

}
