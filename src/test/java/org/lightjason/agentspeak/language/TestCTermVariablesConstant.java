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

package org.lightjason.agentspeak.language;

import com.codepoetics.protonpack.StreamUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CNoSuchElementException;
import org.lightjason.agentspeak.error.CTypeNotAssignable;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.CMutexVariable;
import org.lightjason.agentspeak.language.variable.CRelocateMutexVariable;
import org.lightjason.agentspeak.language.variable.CRelocateVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IRelocateVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Collection;
import java.util.NoSuchElementException;
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

        Assert.assertEquals( "bar", l_emptyliteral.functor() );
        Assert.assertEquals( CPath.of( "foo/bar" ), l_emptyliteral.fqnfunctor() );
        Assert.assertFalse( l_emptyliteral.hasAt() );
        Assert.assertTrue( l_emptyliteral.emptyValues() );



        final ILiteral l_valueliteral = CLiteral.of( "foo/value", CRawTerm.of( 5 ), CRawTerm.of( "hello" ) );

        Assert.assertEquals( "value", l_valueliteral.functor() );
        Assert.assertEquals( CPath.of( "foo/value" ), l_valueliteral.fqnfunctor() );
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
        Assert.assertEquals( "hello", l_stringterm.raw() );


        final double l_value = Math.random();
        final ITerm l_numberterm = CRawTerm.of( l_value );

        Assert.assertTrue( CCommon.isssignableto( l_numberterm, Number.class ) );
        Assert.assertTrue( CCommon.isssignableto( l_numberterm, Double.class ) );
        Assert.assertEquals( "number value", l_value, l_numberterm.raw(), 0 );
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

        Assert.assertEquals( "C", l_constant.functor() );
        Assert.assertTrue( l_constant.hasVariable() );
        Assert.assertTrue( CCommon.isssignableto( l_constant, Number.class ) );
        Assert.assertTrue( CCommon.isssignableto( l_constant, Double.class ) );
        Assert.assertEquals( "constant number value", l_value, l_constant.raw(), 0 );
    }

    /**
     * test constant copy
     */
    @Test
    public void constantcopy()
    {
        Assert.assertEquals(
            "value",
            new CConstant<>( "const/value", "test" ).shallowcopysuffix().functor()
        );

        Assert.assertEquals(
            "xxx/const/value",
            new CConstant<>( "const/value", "test" ).shallowcopy( CPath.of( "xxx" ) ).fqnfunctor().toString()
        );
    }

    /**
     * check any variable
     */
    @Test( expected = NoSuchElementException.class )
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
    @Test( expected = CTypeNotAssignable.class )
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
        Assert.assertEquals( "data()", new CVariable<>( "data" ).toString() );
        Assert.assertEquals( "data(value)", new CVariable<>( "data", "value" ).toString() );
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


        Assert.assertEquals( "get variable number value", l_value, l_variable.raw(), 0 );

        l_value = Math.random();
        l_variable.set( l_value );

        Assert.assertEquals( "set variable number value", l_value, l_variable.raw(), 0 );

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
            CPath.of( "prefix" ),
            new CVariable<>( "prefix/name" ).functorpath()
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

        Assert.assertEquals( "V", l_variable.functor() );
        Assert.assertTrue( l_variable.allocated() );
        Assert.assertTrue( l_variable.mutex() );
        Assert.assertTrue( l_variable.hasVariable() );
        Assert.assertTrue( CCommon.isssignableto( l_variable, Number.class ) );
        Assert.assertTrue( CCommon.isssignableto( l_variable, Double.class ) );


        Assert.assertEquals( "get mutex variable number value", l_value, l_variable.raw(), 0 );

        l_value = Math.random();
        l_variable.set( l_value );

        Assert.assertEquals( "set mutex variable number value", l_value, l_variable.raw(), 0 );

        l_variable.set( null );
        Assert.assertFalse( l_variable.allocated() );
    }

    /**
     * test variable shallow- and deep-copy
     */
    @Test
    public void variablecopy()
    {
        final IVariable<?> l_constant = new CConstant<>( "prefix/copy", new Object() );
        final IVariable<?> l_variable = new CVariable<>( "prefix/copy", new Object()  );
        final IVariable<?> l_variablemutex = new CMutexVariable<Object>( "prefix/copy", new Object() );
        final IVariable<?> l_relocatevariable = new CRelocateVariable<>( l_variable );
        final IVariable<?> l_relocatemutexvariable = new CRelocateMutexVariable<>( l_variablemutex );

        Stream.of(
            l_constant,
            l_variable,
            l_variablemutex,
            l_relocatevariable,
            l_relocatemutexvariable
        ).forEach( i ->
        {
            Assert.assertEquals( CPath.of( "copy" ), i.shallowcopysuffix().fqnfunctor() );
            Assert.assertEquals( i.<Object>raw(), i.shallowcopysuffix().raw() );

            Assert.assertEquals( i.getClass().toString(), CPath.of( "prefix/copy" ), i.shallowcopy().fqnfunctor() );
            Assert.assertEquals( i.getClass().toString(), CPath.of( "zzz/prefix/copy" ), i.shallowcopy( CPath.of( "zzz" ) ).fqnfunctor() );

            final ITerm l_deepsuffix = i.deepcopysuffix();
            Assert.assertEquals( i.getClass().toString(), CPath.of( "copy" ), l_deepsuffix.fqnfunctor() );
            Assert.assertNotEquals( i.getClass().toString(), i.raw(), l_deepsuffix.raw() );

            final ITerm l_deepcopy = i.deepcopy();
            Assert.assertEquals( i.getClass().toString(), CPath.of( "prefix/copy" ), l_deepcopy.fqnfunctor() );
            Assert.assertNotEquals( i.getClass().toString(), i.raw(), l_deepcopy.raw() );

            final ITerm l_deepcopypath = i.deepcopy( CPath.of( "yyy" ) );
            Assert.assertEquals( i.getClass().toString(), CPath.of( "yyy/prefix/copy" ), l_deepcopypath.fqnfunctor() );
            Assert.assertNotEquals( i.getClass().toString(), i.raw(), l_deepcopypath.raw() );
        } );
    }


    /**
     * test realocated variable
     */
    @Test
    public void relocatevariablerelocate()
    {
        final IVariable<String> l_variable = new CVariable<>( "RA" );
        final IVariable<String> l_variablemutex = new CMutexVariable<String>( "MRA" );
        final IRelocateVariable<String> l_relocate = new CRelocateVariable<>( l_variable );
        final IRelocateVariable<String> l_relocatemutex = new CRelocateMutexVariable<>( l_variablemutex );

        Assert.assertFalse( l_variable.mutex() );
        Assert.assertTrue( l_variablemutex.mutex() );
        Assert.assertFalse( l_relocate.mutex() );
        Assert.assertTrue( l_relocatemutex.mutex() );

        Assert.assertEquals( "RA", l_variable.functor() );
        Assert.assertEquals( "MRA", l_variablemutex.functor() );
        Assert.assertEquals( "RA", l_relocate.functor() );
        Assert.assertEquals( "MRA", l_relocatemutex.functor() );

        Assert.assertFalse( l_variable.allocated() );
        Assert.assertFalse( l_relocate.allocated() );
        Assert.assertFalse( l_relocatemutex.allocated() );

        Assert.assertTrue( l_variable.hasVariable() );
        Assert.assertTrue( l_variablemutex.hasVariable() );
        Assert.assertTrue( l_relocate.hasVariable() );
        Assert.assertTrue( l_relocatemutex.hasVariable() );


        l_relocate.set( "relocated foo" );
        Assert.assertTrue( l_relocate.allocated() );
        Assert.assertEquals( "relocated foo", l_relocate.raw() );


        l_relocate.relocate();
        Assert.assertTrue( l_variable.allocated() );
        Assert.assertEquals( "relocated foo", l_variable.raw() );
    }


    /**
     * test realocated variable
     */
    @Test
    public void relocatevariablemutex()
    {
        final IVariable<String> l_variable = new CVariable<>( "RAM" );
        final IRelocateVariable<String> l_relocate = new CRelocateMutexVariable<>( l_variable );

        Assert.assertTrue( l_relocate.mutex() );

        Assert.assertEquals( "RAM", l_variable.functor() );
        Assert.assertEquals( "RAM", l_relocate.functor() );

        Assert.assertFalse( l_variable.allocated() );
        Assert.assertFalse( l_relocate.allocated() );

        Assert.assertTrue( l_variable.hasVariable() );
        Assert.assertTrue( l_relocate.hasVariable() );


        l_relocate.set( "relocated mutex foo" );
        Assert.assertTrue( l_relocate.allocated() );
        Assert.assertEquals( "relocated mutex foo", l_relocate.raw() );


        l_relocate.relocate();
        Assert.assertTrue( l_variable.allocated() );
        Assert.assertEquals( "relocated mutex foo", l_variable.raw() );
    }

    /**
     * test raw-term unpack
     */
    @Test
    public void rawtermunpack()
    {
        Assert.assertEquals(  "rawterm", CRawTerm.of( CRawTerm.of( "rawterm" ) ).raw() );
    }

    /**
     * test raw-term-list unpack
     */
    @Test
    public void rawlistunpack()
    {
        Assert.assertArrayEquals(
            Stream.of( "rawlist", 1, 2L, 3D ).toArray(),
            CRawTermList.of(
                CRawTerm.of( "rawlist" ),
                CRawTerm.of( 1 ),
                CRawTerm.of( 2L ),
                CRawTerm.of( 3D )
            ).<Collection<?>>raw().toArray()
        );
    }

    /**
     * test empty raw term
     */
    @Test( expected = CNoSuchElementException.class )
    public void rawtermempty()
    {
        final IRawTerm<?> l_term = new CRawTerm<>();

        Assert.assertFalse( l_term.allocated() );
        Assert.assertEquals( IPath.EMPTY, l_term.fqnfunctor() );
        Assert.assertTrue( l_term.functor().isEmpty() );

        l_term.thrownotallocated();
    }

    /**
     * test empty literal
     */
    @Test
    public void emptyliteral()
    {
        Assert.assertFalse( ILiteral.EMPTY.negated() );
        Assert.assertFalse( ILiteral.EMPTY.hasAt() );
        Assert.assertFalse( ILiteral.EMPTY.hasVariable() );

        Assert.assertEquals( 0, ILiteral.EMPTY.values().count() );
        Assert.assertEquals( 0, ILiteral.EMPTY.orderedvalues().count() );

        Assert.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.raw() );
        Assert.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.allocate( IContext.EMPTYPLAN ) );
        Assert.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.bind( IContext.EMPTYPLAN ) );
    }

    /**
     * test empty variable
     */
    @Test( expected = NoSuchElementException.class )
    public void emptyvariable()
    {
        Assert.assertFalse( IVariable.EMPTY.mutex() );
        Assert.assertTrue( IVariable.EMPTY.any() );
        Assert.assertFalse( IVariable.EMPTY.allocated() );
        Assert.assertFalse( IVariable.EMPTY.hasVariable() );

        Assert.assertNull( IVariable.EMPTY.raw() );
        Assert.assertEquals( IVariable.EMPTY, IVariable.EMPTY.shallowcopysuffix() );
        Assert.assertEquals( 0, IVariable.EMPTY.structurehash() );
        Assert.assertEquals( CPath.EMPTY.toString(), IVariable.EMPTY.functor() );
        Assert.assertEquals( CPath.EMPTY, IVariable.EMPTY.fqnfunctor() );
        Assert.assertEquals( CPath.EMPTY, IVariable.EMPTY.functorpath() );

        IVariable.EMPTY.thrownotallocated();
    }

}
