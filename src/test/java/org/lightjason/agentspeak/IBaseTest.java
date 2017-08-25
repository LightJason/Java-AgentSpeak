/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak;

import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * base test class with helpers
 */
public abstract class IBaseTest
{
    /**
     * enable printing of test-data
     */
    protected static final boolean PRINTENABLE = Files.exists( Paths.get( "agentprinting.conf" ) );


    /**
     * invoke all test manually
     */
    protected final void invoketest()
    {
        final Set<Method> l_before = this.before();

        Arrays.stream( this.getClass().getMethods() )
              .filter( i -> i.getAnnotation( Test.class ) != null )
              .filter( i -> i.getAnnotation( Ignore.class ) == null )
              .forEach( i -> this.invoke( i, l_before ) );
    }

    /**
     * invoke method and read if possible the data-provider
     *
     * @param p_method method
     * @param p_before before method
     */
    private void invoke( final Method p_method, final Set<Method> p_before )
    {
        // method uses a data-provider
        if ( p_method.getAnnotation( UseDataProvider.class ) == null )
            this.execute( p_method, p_before );
        else
        {
            final Object[] l_arguments;

            try
            {
                l_arguments = (Object[]) this.getClass().getDeclaredMethod( p_method.getAnnotation( UseDataProvider.class ).value() ).invoke( null );

            }
            catch ( final InvocationTargetException l_exception )
            {
                Assert.assertTrue( l_exception.getTargetException().toString(), false );
                return;
            }
            catch ( final IllegalAccessException | NoSuchMethodException l_exception )
            {
                Assert.assertTrue( l_exception.toString(), false );
                return;
            }

            Arrays.stream( l_arguments ).forEach( i -> this.execute( p_method, p_before, i ) );
        }
    }

    /**
     * invokes the method within the current object context
     *
     * @param p_method method
     * @param p_before before method
     * @param p_arguments optional arguments
     */
    private void execute( final Method p_method, final Set<Method> p_before, final Object... p_arguments )
    {
        try
        {
            if ( !p_before.isEmpty() )
                p_before.forEach( i ->
                {
                    try
                    {
                        i.invoke( this );
                    }
                    catch ( final IllegalAccessException | InvocationTargetException l_exception )
                    {
                        l_exception.printStackTrace();
                        Assert.assertTrue( false );
                    }
                } );

            p_method.invoke( this, p_arguments );
        }
        catch ( final AssumptionViolatedException l_exception )
        {
            // ignore catched exception
        }
        catch ( final InvocationTargetException l_exception )
        {
            if ( l_exception.getTargetException() instanceof AssumptionViolatedException )
                return;

            if ( !p_method.getAnnotation( Test.class ).expected().isInstance( l_exception.getTargetException() ) )
            {
                l_exception.getTargetException().printStackTrace();
                Assert.assertTrue( false );
            }
        }
        catch ( final IllegalAccessException l_exception )
        {
            Assert.assertTrue( l_exception.toString(), false );
        }
    }

    /**
     * reads the before annotated methods
     *
     * @return optional before method
     */
    private Set<Method> before()
    {
        return Arrays.stream( this.getClass().getMethods() )
                     .filter( i -> i.getAnnotation( Before.class ) != null )
                     .filter( i -> i.getAnnotation( Ignore.class ) == null )
                     .collect( Collectors.toSet() );
    }
}
