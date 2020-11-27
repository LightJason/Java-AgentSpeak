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

package org.lightjason.agentspeak.error;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.error.context.CExecutionException;
import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.execution.IContext;


/**
 * test context exception
 */
public final class TestCContextException
{
    /**
     * test execution-exception context
     */
    @Test
    public void executionexceptioncontext()
    {
        Assertions.assertEquals( IContext.EMPTYPLAN, new CExecutionException( IContext.EMPTYPLAN ).context() );
    }

    /**
     * test execution-exception
     */
    @Test
    public void executionexcpetion()
    {
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
            {
                throw new CExecutionException( IContext.EMPTYPLAN );
            }
        );
    }

    /**
     * test execution-exception
     */
    @Test
    public void executionexcpetionstring()
    {
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
            {
                throw new CExecutionException( IContext.EMPTYPLAN, "execution" );
            }
        );
    }

    /**
     * test execution-exception
     */
    @Test
    public void executionexcpetionpassexception()
    {
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
            {
                throw new CExecutionException( IContext.EMPTYPLAN, new RuntimeException() );
            }
        );
    }

    /**
     * test execution-exception
     */
    @Test
    public void executionexcpetionstringexception()
    {
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
            {
                throw new CExecutionException( IContext.EMPTYPLAN, "execution", new RuntimeException() );
            }
        );
    }



    /**
     * test illegalstate-exception context
     */
    @Test
    public void executionillegealstateexceptioncontext()
    {
        Assertions.assertEquals( IContext.EMPTYPLAN, new CExecutionIllegalStateException( IContext.EMPTYPLAN ).context() );
    }

    /**
     * test illegalstaten-exception
     */
    @Test
    public void executionillegealstateexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CExecutionIllegalStateException( IContext.EMPTYPLAN );
            }
        );
    }

    /**
     * test illegalstate-exception
     */
    @Test
    public void executionillegealstateexceptionstring()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CExecutionIllegalStateException( IContext.EMPTYPLAN, "state" );
            }
        );
    }

    /**
     * test illegalstate-exception
     */
    @Test
    public void executionillegealstateexceptionpassexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CExecutionIllegalStateException( IContext.EMPTYPLAN, new RuntimeException() );
            }
        );
    }

    /**
     * test illegalstate-exception
     */
    @Test
    public void executionillegealstateexceptionstringexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CExecutionIllegalStateException( IContext.EMPTYPLAN, "state", new RuntimeException() );
            }
        );
    }



    /**
     * test execution-exception context
     */
    @Test
    public void executionillegealargumentexceptioncontext()
    {
        Assertions.assertEquals( IContext.EMPTYPLAN, new CExecutionIllegealArgumentException( IContext.EMPTYPLAN ).context() );
    }

    /**
     * test execution-exception
     */
    @Test
    public void executionillegealargumentexception()
    {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->
            {
                throw new CExecutionIllegealArgumentException( IContext.EMPTYPLAN );
            }
        );
    }

    /**
     * test execution-exception
     */
    @Test
    public void executionillegealargumentexceptionstring()
    {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->
            {
                throw new CExecutionIllegealArgumentException( IContext.EMPTYPLAN, "argument" );
            }
        );
    }

    /**
     * test execution-exception
     */
    @Test
    public void executionillegealargumentexceptionpassexception()
    {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->
            {
                throw new CExecutionIllegealArgumentException( IContext.EMPTYPLAN, new RuntimeException() );
            }
        );
    }

    /**
     * test execution-exception
     */
    @Test
    public void executionillegealargumentexceptionstringexception()
    {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->
            {
                throw new CExecutionIllegealArgumentException( IContext.EMPTYPLAN, "argument", new RuntimeException() );
            }
        );
    }
}
