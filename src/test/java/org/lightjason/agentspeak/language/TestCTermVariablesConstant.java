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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
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

        Assertions.assertEquals( "bar", l_emptyliteral.functor() );
        Assertions.assertEquals( CPath.of( "foo/bar" ), l_emptyliteral.fqnfunctor() );
        Assertions.assertFalse( l_emptyliteral.hasAt() );
        Assertions.assertTrue( l_emptyliteral.emptyValues() );



        final ILiteral l_valueliteral = CLiteral.of( "foo/value", CRawTerm.of( 5 ), CRawTerm.of( "hello" ) );

        Assertions.assertEquals( "value", l_valueliteral.functor() );
        Assertions.assertEquals( CPath.of( "foo/value" ), l_valueliteral.fqnfunctor() );
        Assertions.assertFalse( l_valueliteral.hasAt() );
        Assertions.assertFalse( l_valueliteral.emptyValues() );

        Assertions.assertTrue(
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

        Assertions.assertTrue( CCommon.isssignableto( l_stringterm, String.class ) );
        Assertions.assertEquals( "hello", l_stringterm.raw() );


        final double l_value = Math.random();
        final ITerm l_numberterm = CRawTerm.of( l_value );

        Assertions.assertTrue( CCommon.isssignableto( l_numberterm, Number.class ) );
        Assertions.assertTrue( CCommon.isssignableto( l_numberterm, Double.class ) );
        Assertions.assertEquals( l_value, l_numberterm.raw(), 0, "number value" );
    }


    /**
     * test constant access structure
     */
    @Test
    public void constantaccess()
    {
        Assertions.assertThrows( RuntimeException.class, () -> new CConstant<>( "CA", 5 ).set( 10 ) );
    }


    /**
     * test constant structure
     */
    @Test
    public void constant()
    {
        final double l_value = Math.random();
        final ITerm l_constant = new CConstant<>( "C", l_value );

        Assertions.assertEquals( "C", l_constant.functor() );
        Assertions.assertTrue( l_constant.hasVariable() );
        Assertions.assertTrue( CCommon.isssignableto( l_constant, Number.class ) );
        Assertions.assertTrue( CCommon.isssignableto( l_constant, Double.class ) );
        Assertions.assertEquals( l_value, l_constant.raw(), 0, "constant number value" );
    }

    /**
     * check any variable
     */
    @Test
    public void variableany()
    {
        final IVariable<?> l_variable = new CVariable<Object>( "_" );

        Assertions.assertTrue( l_variable.any() );
        Assertions.assertFalse( l_variable.allocated() );

        Assertions.assertThrows( NoSuchElementException.class, l_variable::thrownotallocated );
    }


    /**
     * test exception on value asiable
     */
    @Test
    public void variablevalueassignable()
    {
        Assertions.assertThrows(
            CTypeNotAssignable.class,
            () -> new CVariable<Object>( "num", 123 ).throwvaluenotassignableto( String.class )
        );
    }

    /**
     * variable equals
     */
    @Test
    public void variableequals()
    {
        Assertions.assertEquals(
            new CVariable<Object>( "foo", "str" ),
            new CVariable<Object>( "foo", 123 )
        );

        Assertions.assertEquals(
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
        Assertions.assertEquals( "data()", new CVariable<>( "data" ).toString() );
        Assertions.assertEquals( "data(value)", new CVariable<>( "data", "value" ).toString() );
    }


    /**
     * checks the variable structure
     */
    @Test
    public void variable()
    {
        Assertions.assertTrue( new CVariable<>( "_" ).any() );


        double l_value = Math.random();
        final IVariable<Number> l_variable = new CVariable<>( "V", l_value );

        Assertions.assertEquals( l_variable.functor(), "V" );
        Assertions.assertTrue( l_variable.allocated() );
        Assertions.assertFalse( l_variable.mutex() );
        Assertions.assertTrue( l_variable.hasVariable() );
        Assertions.assertTrue( CCommon.isssignableto( l_variable, Number.class ) );
        Assertions.assertTrue( CCommon.isssignableto( l_variable, Double.class ) );


        Assertions.assertEquals( l_value, l_variable.raw(), 0, "get variable number value" );

        l_value = Math.random();
        l_variable.set( l_value );

        Assertions.assertEquals( l_value, l_variable.raw(), 0, "set variable number value" );

        l_variable.set( null );
        Assertions.assertFalse( l_variable.allocated() );
    }

    /**
     * test variable functor
     */
    @Test
    public void variablefunctor()
    {
        Assertions.assertEquals(
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

        Assertions.assertEquals( "V", l_variable.functor() );
        Assertions.assertTrue( l_variable.allocated() );
        Assertions.assertTrue( l_variable.mutex() );
        Assertions.assertTrue( l_variable.hasVariable() );
        Assertions.assertTrue( CCommon.isssignableto( l_variable, Number.class ) );
        Assertions.assertTrue( CCommon.isssignableto( l_variable, Double.class ) );


        Assertions.assertEquals( l_value, l_variable.raw(), 0, "get mutex variable number value" );

        l_value = Math.random();
        l_variable.set( l_value );

        Assertions.assertEquals( l_value, l_variable.raw(), 0, "set mutex variable number value" );

        l_variable.set( null );
        Assertions.assertFalse( l_variable.allocated() );
    }

    /**
     * test shallow- and deep-copy
     */
    @Test
    public void copy()
    {
        final IPath l_base = CPath.of( "prefix/copy" );

        final IVariable<?> l_constant = new CConstant<>( l_base, new Object() );
        final IVariable<?> l_variable = new CVariable<>( l_base, new Object()  );
        final IVariable<?> l_variablemutex = new CMutexVariable<>( l_base, new Object() );
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
            final IVariable<?> l_copy = i.shallowcopywithoutsuffix();
            Assertions.assertEquals(
                CPath.of( "prefix" ), l_copy.fqnfunctor(),
                () -> MessageFormat.format( "shallow-copy-withoutsuffix-fqnfunctor: {0}", i.getClass() )
            );
            Assertions.assertEquals( i.<Object>raw(), l_copy.raw() );


            Assertions.assertEquals(
                CPath.of( "copy" ), i.shallowcopysuffix().fqnfunctor(),
                MessageFormat.format( "shallow-copy-suffix-fqnfunctor: {0}", i.getClass() )
            );
            Assertions.assertEquals(
                i.<Object>raw(), i.shallowcopysuffix().raw(),
                MessageFormat.format( "shallow-copy-raw: {0}", i.getClass() )
            );

            Assertions.assertEquals(
                CPath.of( "prefix/copy" ), i.shallowcopy().fqnfunctor(),
                i.getClass().toString()
            );
            Assertions.assertEquals(
                CPath.of( "zzz/prefix/copy" ), i.shallowcopy( CPath.of( "zzz" ) ).fqnfunctor(),
                MessageFormat.format( "shallow-copy-fqnfunctor: {0}", i.getClass() )
            );


            final ITerm l_deepsuffix = i.deepcopysuffix();
            Assertions.assertEquals(
                CPath.of( "copy" ), l_deepsuffix.fqnfunctor(),
                MessageFormat.format( "deep-copy-suffix-fqnfunctor: {0}", i.getClass() )
            );
            Assertions.assertNotEquals(
                i.<Object>raw(), l_deepsuffix.<Object>raw(),
                MessageFormat.format( "deep-copy-suffix-notequal: {0}", i.getClass() )
            );


            final ITerm l_deepcopy = i.deepcopy();
            Assertions.assertEquals(
                CPath.of( "prefix/copy" ), l_deepcopy.fqnfunctor(),
                MessageFormat.format( "deep-copy-fqnfunctor 1: {0}", i.getClass() )
            );
            Assertions.assertNotEquals(
                i.<Object>raw(), l_deepcopy.<Object>raw(),
                MessageFormat.format( "deep-copy-notequal 1: {0}", i.getClass() )
            );

            final ITerm l_deepcopypath = i.deepcopy( CPath.of( "yyy" ) );
            Assertions.assertEquals(
                CPath.of( "yyy/prefix/copy" ), l_deepcopypath.fqnfunctor(),
                MessageFormat.format( "deep-copy-fqnfunctor 2: {0}", i.getClass() )
            );
            Assertions.assertNotEquals(
                i.<Object>raw(), l_deepcopypath.<Object>raw(),
                MessageFormat.format( "deep-copy-notequal 2: {0}", i.getClass() )
            );
        } );


        final ILiteral l_literal = CLiteral.of( "prefix/copy" );
        Assertions.assertEquals( CPath.of( "copy" ), l_literal.shallowcopysuffix().fqnfunctor() );
        Assertions.assertEquals( l_base, l_literal.shallowcopy().fqnfunctor() );
        Assertions.assertEquals( CPath.of( "zzz/prefix/copy" ), l_literal.shallowcopy( CPath.of( "zzz" ) ).fqnfunctor() );
        Assertions.assertEquals( CPath.of( "copy" ), l_literal.deepcopysuffix().fqnfunctor() );
        Assertions.assertEquals( l_base, l_literal.deepcopy().fqnfunctor() );
        Assertions.assertEquals( CPath.of( "yyy/prefix/copy" ), l_literal.deepcopy( CPath.of( "yyy" ) ).fqnfunctor() );
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

        Assertions.assertFalse( l_variable.mutex() );
        Assertions.assertTrue( l_variablemutex.mutex() );
        Assertions.assertFalse( l_relocate.mutex() );
        Assertions.assertTrue( l_relocatemutex.mutex() );

        Assertions.assertEquals( "RA", l_variable.functor() );
        Assertions.assertEquals( "MRA", l_variablemutex.functor() );
        Assertions.assertEquals( "RA", l_relocate.functor() );
        Assertions.assertEquals( "MRA", l_relocatemutex.functor() );

        Assertions.assertFalse( l_variable.allocated() );
        Assertions.assertFalse( l_relocate.allocated() );
        Assertions.assertFalse( l_relocatemutex.allocated() );

        Assertions.assertTrue( l_variable.hasVariable() );
        Assertions.assertTrue( l_variablemutex.hasVariable() );
        Assertions.assertTrue( l_relocate.hasVariable() );
        Assertions.assertTrue( l_relocatemutex.hasVariable() );


        l_relocate.set( "relocated foo" );
        Assertions.assertTrue( l_relocate.allocated() );
        Assertions.assertEquals( "relocated foo", l_relocate.raw() );


        l_relocate.relocate();
        Assertions.assertTrue( l_variable.allocated() );
        Assertions.assertEquals( "relocated foo", l_variable.raw() );
    }


    /**
     * test realocated variable
     */
    @Test
    public void relocatevariablemutex()
    {
        final IVariable<String> l_variable = new CVariable<>( "RAM" );
        final IRelocateVariable<String> l_relocate = new CRelocateMutexVariable<>( l_variable );

        Assertions.assertTrue( l_relocate.mutex() );

        Assertions.assertEquals( "RAM", l_variable.functor() );
        Assertions.assertEquals( "RAM", l_relocate.functor() );

        Assertions.assertFalse( l_variable.allocated() );
        Assertions.assertFalse( l_relocate.allocated() );

        Assertions.assertTrue( l_variable.hasVariable() );
        Assertions.assertTrue( l_relocate.hasVariable() );


        l_relocate.set( "relocated mutex foo" );
        Assertions.assertTrue( l_relocate.allocated() );
        Assertions.assertEquals( "relocated mutex foo", l_relocate.raw() );


        l_relocate.relocate();
        Assertions.assertTrue( l_variable.allocated() );
        Assertions.assertEquals( "relocated mutex foo", l_variable.raw() );
    }

    /**
     * test raw-term unpack
     */
    @Test
    public void rawtermunpack()
    {
        Assertions.assertEquals(  "rawterm", CRawTerm.of( CRawTerm.of( "rawterm" ) ).raw() );
    }

    /**
     * test raw-term-list unpack
     */
    @Test
    public void rawlistunpack()
    {
        Assertions.assertArrayEquals(
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
    @Test
    public void rawtermempty()
    {
        final IRawTerm<?> l_term = new CRawTerm<>();

        Assertions.assertFalse( l_term.allocated() );
        Assertions.assertEquals( IPath.EMPTY, l_term.fqnfunctor() );
        Assertions.assertTrue( l_term.functor().isEmpty() );

        Assertions.assertThrows( CNoSuchElementException.class, l_term::thrownotallocated );
    }

    /**
     * test termlist-copy
     */
    @Test
    @SuppressWarnings( "unchecked" )
    public void rawtermcopy()
    {
        final IRawTerm<?> l_list = CRawTermList.of( CRawTerm.of( "list" ), CRawTerm.of( 8 ), CRawTerm.of( 12L ), CRawTerm.of( 99D ) );

        Assertions.assertTrue( l_list.allocated() );
        Assertions.assertFalse( l_list.hasVariable() );
        Assertions.assertTrue( l_list.valueassignableto( List.class ) );
        Assertions.assertArrayEquals(
            Stream.of( "list", 8, 12L, 99D ).toArray(),
            l_list.<List<?>>raw().toArray()
        );
    }

    /**
     * test empty literal
     */
    @Test
    public void emptyliteral()
    {
        Assertions.assertFalse( ILiteral.EMPTY.negated() );
        Assertions.assertFalse( ILiteral.EMPTY.hasAt() );
        Assertions.assertFalse( ILiteral.EMPTY.hasVariable() );

        Assertions.assertEquals( 0, ILiteral.EMPTY.values().count() );
        Assertions.assertEquals( 0, ILiteral.EMPTY.orderedvalues().count() );

        Assertions.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.raw() );
        Assertions.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.allocate( IContext.EMPTYPLAN ) );
        Assertions.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.bind( IContext.EMPTYPLAN ) );

        Assertions.assertTrue( ILiteral.EMPTY.emptyValues() );
        Assertions.assertNotEquals( 0, ILiteral.EMPTY.compareTo( CLiteral.of( "xxx" ) ) );

        Assertions.assertEquals( IPath.EMPTY.toString(), ILiteral.EMPTY.functor() );
        Assertions.assertEquals( IPath.EMPTY, ILiteral.EMPTY.functorpath() );
        Assertions.assertEquals( IPath.EMPTY, ILiteral.EMPTY.fqnfunctor() );

        Assertions.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.deepcopy() );
        Assertions.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.deepcopysuffix() );
        Assertions.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.shallowcopy() );
        Assertions.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.shallowcopysuffix() );
        Assertions.assertEquals( ILiteral.EMPTY, ILiteral.EMPTY.shallowcopywithoutsuffix() );
        Assertions.assertFalse( ILiteral.EMPTY.hasShallowcopywithoutsuffix() );

        Assertions.assertEquals( 0, ILiteral.EMPTY.structurehash() );
    }

    /**
     * test empty variable
     */
    @Test
    public void emptyvariable()
    {
        Assertions.assertFalse( IVariable.EMPTY.mutex() );
        Assertions.assertTrue( IVariable.EMPTY.any() );
        Assertions.assertFalse( IVariable.EMPTY.allocated() );
        Assertions.assertFalse( IVariable.EMPTY.hasVariable() );

        Assertions.assertNull( IVariable.EMPTY.raw() );
        Assertions.assertEquals( IVariable.EMPTY, IVariable.EMPTY.shallowcopysuffix() );
        Assertions.assertEquals( 0, IVariable.EMPTY.structurehash() );
        Assertions.assertEquals( CPath.EMPTY.toString(), IVariable.EMPTY.functor() );
        Assertions.assertEquals( CPath.EMPTY, IVariable.EMPTY.fqnfunctor() );
        Assertions.assertEquals( CPath.EMPTY, IVariable.EMPTY.functorpath() );

        Assertions.assertThrows( NoSuchElementException.class, IVariable.EMPTY::thrownotallocated );
    }

    /**
     * test empty term
     */
    @Test
    public void emptyterm()
    {
        Assertions.assertTrue( ITerm.EMPTYTERM.functor().isEmpty() );
        Assertions.assertEquals( IPath.EMPTY, ITerm.EMPTYTERM.functorpath() );
        Assertions.assertEquals( IPath.EMPTY, ITerm.EMPTYTERM.fqnfunctor() );
        Assertions.assertFalse( ITerm.EMPTYTERM.hasVariable() );
        Assertions.assertNull( ITerm.EMPTYTERM.raw() );
        Assertions.assertEquals( ITerm.EMPTYTERM, ITerm.EMPTYTERM.deepcopy() );
        Assertions.assertEquals( ITerm.EMPTYTERM, ITerm.EMPTYTERM.deepcopysuffix() );
        Assertions.assertEquals( 0, ITerm.EMPTYTERM.structurehash() );
    }

}
