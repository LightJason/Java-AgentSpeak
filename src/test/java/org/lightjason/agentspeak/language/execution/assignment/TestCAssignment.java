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

package org.lightjason.agentspeak.language.execution.assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.passing.CPassRaw;
import org.lightjason.agentspeak.language.execution.passing.CPassVariable;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * test single assignment
 */
public final class TestCAssignment extends IBaseTest
{

    /**
     * assignment operator
     *
     * @return 4-tuple as array, first & second input values, 3rd operator, 4th result
     */
    public static Stream<Arguments> operator()
    {
        return Stream.of(

            Arguments.of( 5, 2, EAssignOperator.INCREMENT, 7.0 ),
            Arguments.of( 7, 3, EAssignOperator.DECREMENT, 4.0 ),

            Arguments.of( 11, 11, EAssignOperator.MULTIPLY, 121.0 ),
            Arguments.of( 33, 3, EAssignOperator.DIVIDE, 11.0 ),

            Arguments.of( 21, 17, EAssignOperator.MODULO, 4L ),
            Arguments.of( -1, 17, EAssignOperator.MODULO, 16L ),
            Arguments.of( -18, 17, EAssignOperator.MODULO, 1L ),

            Arguments.of( 2, 3, EAssignOperator.POWER, 8.0 ),
            Arguments.of( 9, 0.5, EAssignOperator.POWER, 3.0 ),

            Arguments.of( 2, 3, EAssignOperator.ASSIGN,  3 )

        );
    }

    /**
     * test assignment operator with variables
     *
     * @param p_lhs left-hand-side argument
     * @param p_rhs right-hand-side argument
     * @param p_operator operator
     * @param p_result result
     */
    @ParameterizedTest
    @MethodSource( "operator" )
    public void assignvariable( @Nonnull final Object p_lhs, @Nonnull final Object p_rhs, @Nonnull final EAssignOperator p_operator,
                                @Nonnull final Object p_result )
    {
        final IVariable<Object> l_lhs = new CVariable<>( "Lhs" ).set( p_lhs );
        final IVariable<Object> l_rhs = new CVariable<>( "Rhs" ).set( p_rhs );

        Assertions.assertTrue(
            execute(
                new CSingleAssignment(
                    p_operator, l_lhs,
                    new CPassVariable( l_rhs )
                ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_lhs,
                l_rhs
            )
        );

        Assertions.assertEquals( p_result, l_lhs.raw() );
        Assertions.assertEquals( p_rhs, l_rhs.raw() );
    }

    /**
     * test assignment operator with native data
     *
     * @param p_lhs left-hand-side argument
     * @param p_rhs right-hand-side argument
     * @param p_operator operator
     * @param p_result result
     */
    @ParameterizedTest
    @MethodSource( "operator" )
    public void assignraw( @Nonnull final Object p_lhs, @Nonnull final Object p_rhs, @Nonnull final EAssignOperator p_operator,
                           @Nonnull final Object p_result )
    {
        final IVariable<Object> l_lhs = new CVariable<>( "Lhs" ).set( p_lhs );

        Assertions.assertTrue(
            execute(
                new CSingleAssignment(
                    p_operator, l_lhs,
                    new CPassRaw<>( p_rhs )
                ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_lhs
            )
        );

        Assertions.assertEquals( p_result, l_lhs.raw() );
    }

    /**
     * test deconstruct static argument
     */
    @Test
    public void staticargument()
    {
        final IVariable<?> l_outer = new CVariable<>( "Outer" );
        final IVariable<?> l_inner = new CVariable<>( "Inner" );

        Assertions.assertTrue(
            execute(
                new CDeconstruct( Stream.of( l_outer, l_inner ), CLiteral.of( "foobar", CRawTerm.of( 5 ), CRawTerm.of( "test" ) ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_outer,
                l_inner
            )
        );

        Assertions.assertEquals( "foobar", l_outer.raw() );
        Assertions.assertTrue( l_inner.raw() instanceof List<?> );
        Assertions.assertEquals( 2, l_inner.<List<?>>raw().size() );
        Assertions.assertArrayEquals( Stream.of( 5, "test" ).toArray(), l_inner.<List<?>>raw().toArray() );
    }

    /**
     * test deconstruct variable argument
     */
    @Test
    public void variableargument()
    {
        final IVariable<?> l_outer = new CVariable<>( "Outer" );
        final IVariable<?> l_inner = new CVariable<>( "Inner" );
        final IVariable<Object> l_argument = new CVariable<>( "Arg" );

        l_argument.set( CLiteral.of( "foo", CRawTerm.of( "bar" ), CRawTerm.of( 7 ) ) );

        Assertions.assertTrue(
            execute(
                new CDeconstruct( Stream.of( l_outer, l_inner ), l_argument ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_outer,
                l_inner,
                l_argument
            )
        );

        Assertions.assertEquals( "foo", l_outer.raw() );
        Assertions.assertTrue( l_inner.raw() instanceof List<?> );
        Assertions.assertEquals( 2, l_inner.<List<?>>raw().size() );
        Assertions.assertArrayEquals( Stream.of( "bar", 7 ).toArray(), l_inner.<List<?>>raw().toArray() );
    }

    /**
     * test assignment exception
     */
    @Test
    public void singleassignmenterror()
    {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> execute(
                new CSingleAssignment(
                    EAssignOperator.ASSIGN,
                    new CVariable<>( "X" ),
                    IExecution.EMPTY
                ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

    /**
     * test multi-assignment error result
     */
    public void multiassignmenterror()
    {
        Assertions.assertFalse(
            execute(
                new CMultiAssignment(
                    Stream.empty(),
                    new IExecution()
                    {
                        @Nonnull
                        @Override
                        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
                        )
                        {
                            return p_context.agent().fuzzy().membership().fail();
                        }

                        @Nonnull
                        @Override
                        public Stream<IVariable<?>> variables()
                        {
                            return Stream.empty();
                        }
                    }
                ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

    /**
     * test multi-assignment exception
     */
    @Test
    public void multiassigmentexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> execute(
                new CMultiAssignment(
                    Stream.empty(),
                    IExecution.EMPTY
                ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

    /**
     * test multi-assignment tail-matching
     */
    @Test
    public void multiassignmenttail()
    {
        final IVariable<?> l_xvariable = new CVariable<>( "X" );
        final IVariable<?> l_yvariable = new CVariable<>( "Y" );

        Assertions.assertTrue(
            execute(
                new CMultiAssignment(
                    Stream.of( l_xvariable, l_yvariable ),
                    new IExecution()
                    {
                        @Nonnull
                        @Override
                        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                               @Nonnull final List<ITerm> p_return
                        )
                        {
                            Stream.of( 1, 2, 3 ).map( CRawTerm::of ).forEach( p_return::add );
                            return Stream.empty();
                        }

                        @Nonnull
                        @Override
                        public Stream<IVariable<?>> variables()
                        {
                            return Stream.empty();
                        }
                    }
                ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_xvariable,
                l_yvariable
            )
        );

        Assertions.assertEquals( Integer.valueOf( 1 ), l_xvariable.raw() );
        Assertions.assertArrayEquals( Stream.of( 2, 3 ).toArray(), l_yvariable.<Collection<?>>raw().toArray() );
    }

    /**
     * test multi-assignment single assignment
     */
    @Test
    public void multiassignmentsingle()
    {
        final IVariable<?> l_variable = new CVariable<>( "Z" );

        Assertions.assertTrue(
            execute(
                new CMultiAssignment(
                    Stream.of( l_variable ),
                    new IExecution()
                    {
                        @Nonnull
                        @Override
                        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                               @Nonnull final List<ITerm> p_return
                        )
                        {
                            Stream.of( 1, 2, 3 ).map( CRawTerm::of ).forEach( p_return::add );
                            return Stream.empty();
                        }

                        @Nonnull
                        @Override
                        public Stream<IVariable<?>> variables()
                        {
                            return Stream.empty();
                        }
                    }
                ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_variable
            )
        );

        Assertions.assertArrayEquals( Stream.of( 1, 2, 3 ).toArray(), l_variable.<Collection<?>>raw().toArray() );
    }

    /**
     * test multi-assignment less variables
     */
    @Test
    public void multiassignmentless()
    {
        final IVariable<?> l_xvariable = new CVariable<>( "X" );
        final IVariable<?> l_yvariable = new CVariable<>( "Y", "xyz" );

        Assertions.assertTrue(
            execute(
                new CMultiAssignment(
                    Stream.of( l_xvariable, l_yvariable ),
                    new IExecution()
                    {
                        @Nonnull
                        @Override
                        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                               @Nonnull final List<ITerm> p_return
                        )
                        {
                            Stream.of( 1 ).map( CRawTerm::of ).forEach( p_return::add );
                            return Stream.empty();
                        }

                        @Nonnull
                        @Override
                        public Stream<IVariable<?>> variables()
                        {
                            return Stream.empty();
                        }
                    }
                ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_xvariable,
                l_yvariable
            )
        );

        Assertions.assertEquals( Integer.valueOf( 1 ), l_xvariable.raw() );
        Assertions.assertEquals( "xyz", l_yvariable.raw() );
    }

    /**
     * test hashcode and equals
     */
    @Test
    public void hashcodeeqauls()
    {
        Assertions.assertEquals(
            new CSingleAssignment( EAssignOperator.ASSIGN, IVariable.EMPTY, IExecution.EMPTY ),
            new CSingleAssignment( EAssignOperator.ASSIGN, IVariable.EMPTY, IExecution.EMPTY )
        );
        Assertions.assertNotEquals(
            new CSingleAssignment( EAssignOperator.ASSIGN, IVariable.EMPTY, IExecution.EMPTY ),
            new CSingleAssignment( EAssignOperator.INCREMENT, IVariable.EMPTY, IExecution.EMPTY )
        );


        Assertions.assertEquals(
            new CMultiAssignment( Stream.empty(), IExecution.EMPTY ),
            new CMultiAssignment( Stream.empty(), IExecution.EMPTY )
        );
        Assertions.assertNotEquals(
            new CMultiAssignment( Stream.empty(), IExecution.EMPTY ),
            new CMultiAssignment( Stream.of( new CVariable<>( "V" ) ), IExecution.EMPTY )
        );


        Assertions.assertEquals(
            new CDeconstruct( Stream.empty(), ITerm.EMPTYTERM ),
            new CDeconstruct( Stream.empty(), ITerm.EMPTYTERM )
        );
        Assertions.assertNotEquals(
            new CDeconstruct( Stream.empty(), ITerm.EMPTYTERM ),
            new CDeconstruct( Stream.empty(), IVariable.EMPTY )
        );
    }
}
