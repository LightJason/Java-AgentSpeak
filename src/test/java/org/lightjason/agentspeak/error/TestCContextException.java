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

import org.junit.Assert;
import org.junit.Test;
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
        Assert.assertEquals( IContext.EMPTYPLAN, new CExecutionException( IContext.EMPTYPLAN ).context() );
    }

    /**
     * test execution-exception
     */
    @Test( expected = RuntimeException.class )
    public void executionexcpetion()
    {
        throw new CExecutionException( IContext.EMPTYPLAN );
    }

    /**
     * test execution-exception
     */
    @Test( expected = RuntimeException.class )
    public void executionexcpetionstring()
    {
        throw new CExecutionException( IContext.EMPTYPLAN, "execution" );
    }

    /**
     * test execution-exception
     */
    @Test( expected = RuntimeException.class )
    public void executionexcpetionpassexception()
    {
        throw new CExecutionException( IContext.EMPTYPLAN, new RuntimeException() );
    }

    /**
     * test execution-exception
     */
    @Test( expected = RuntimeException.class )
    public void executionexcpetionstringexception()
    {
        throw new CExecutionException( IContext.EMPTYPLAN, "execution", new RuntimeException() );
    }



    /**
     * test illegalstate-exception context
     */
    @Test
    public void executionillegealstateexceptioncontext()
    {
        Assert.assertEquals( IContext.EMPTYPLAN, new CExecutionIllegalStateException( IContext.EMPTYPLAN ).context() );
    }

    /**
     * test illegalstaten-exception
     */
    @Test( expected = IllegalStateException.class )
    public void executionillegealstateexception()
    {
        throw new CExecutionIllegalStateException( IContext.EMPTYPLAN );
    }

    /**
     * test illegalstate-exception
     */
    @Test( expected = IllegalStateException.class )
    public void executionillegealstateexceptionstring()
    {
        throw new CExecutionIllegalStateException( IContext.EMPTYPLAN, "state" );
    }

    /**
     * test illegalstate-exception
     */
    @Test( expected = IllegalStateException.class )
    public void executionillegealstateexceptionpassexception()
    {
        throw new CExecutionIllegalStateException( IContext.EMPTYPLAN, new RuntimeException() );
    }

    /**
     * test illegalstate-exception
     */
    @Test( expected = IllegalStateException.class )
    public void executionillegealstateexceptionstringexception()
    {
        throw new CExecutionIllegalStateException( IContext.EMPTYPLAN, "state", new RuntimeException() );
    }



    /**
     * test execution-exception context
     */
    @Test
    public void executionillegealargumentexceptioncontext()
    {
        Assert.assertEquals( IContext.EMPTYPLAN, new CExecutionIllegealArgumentException( IContext.EMPTYPLAN ).context() );
    }

    /**
     * test execution-exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void executionillegealargumentexception()
    {
        throw new CExecutionIllegealArgumentException( IContext.EMPTYPLAN );
    }

    /**
     * test execution-exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void executionillegealargumentexceptionstring()
    {
        throw new CExecutionIllegealArgumentException( IContext.EMPTYPLAN, "argument" );
    }

    /**
     * test execution-exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void executionillegealargumentexceptionpassexception()
    {
        throw new CExecutionIllegealArgumentException( IContext.EMPTYPLAN, new RuntimeException() );
    }

    /**
     * test execution-exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void executionillegealargumentexceptionstringexception()
    {
        throw new CExecutionIllegealArgumentException( IContext.EMPTYPLAN, "argument", new RuntimeException() );
    }
}
