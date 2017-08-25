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

package org.lightjason.agentspeak.action.binding;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action for binding a method
 * @note action uses agent for object binding
 */
public final class CMethodAction extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -507236338411690842L;
    /**
     * name of the action
     */
    private final IPath m_name;
    /**
     * number of arguments
     */
    private final int m_arguments;
    /**
     * method reference
     */
    private transient Method m_method;
    /**
     * method handle
     */
    private transient MethodHandle m_methodhandle;


    /**
     * ctor
     *
     * @param p_method method reference
     * @throws IllegalAccessException on method access error
     */
    public CMethodAction( @Nonnull final Method p_method ) throws IllegalAccessException
    {
        m_method = p_method;
        m_arguments = m_method.getParameterCount();
        m_name = CPath.from(
            m_method.isAnnotationPresent( IAgentActionName.class ) && !m_method.getAnnotation( IAgentActionName.class ).name().isEmpty()
            ? m_method.getAnnotation( IAgentActionName.class ).name().toLowerCase( Locale.ROOT )
            : m_method.getName().toLowerCase( Locale.ROOT )
        );
        m_methodhandle = MethodHandles.lookup().unreflect( m_method );
    }

    /**
     * serialize call
     *
     * @param p_stream object stream
     */
    private void writeObject( final ObjectOutputStream p_stream ) throws IOException
    {
        p_stream.defaultWriteObject();

        // serialize method handle
        p_stream.writeObject( m_method.getDeclaringClass() );
        p_stream.writeUTF( m_method.getName() );
        p_stream.writeObject( m_method.getParameterTypes() );
    }

    /**
     * deserializable call
     *
     * @param p_stream object stream
     * @throws IOException is thrown on io error
     * @throws ClassNotFoundException is thrown on deserialization error
     * @throws NoSuchMethodException is thrown on method deserialization
     * @throws IllegalAccessException is thrown on creating method handle
     */
    @SuppressWarnings( "unchecked" )
    private void readObject( final ObjectInputStream p_stream ) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException
    {
        p_stream.defaultReadObject();

        // deserialize method handle
        m_method = ( (Class<?>) p_stream.readObject() ).getMethod( p_stream.readUTF(), (Class<?>[])p_stream.readObject() );
        m_methodhandle = MethodHandles.lookup().unreflect( m_method );
    }

    @Nonnull
    @Override
    public final IPath name()
    {
        return m_name;
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return m_arguments;
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        try
        {
            return m_arguments == 0

                ? CMethodAction.returnvalues(
                m_methodhandle.invoke( p_context.agent() ),
                p_return
                )

                : CMethodAction.returnvalues(
                    m_methodhandle.invokeWithArguments(
                        Stream.concat(
                            Stream.of( p_context.agent() ),
                            p_argument.stream().map( ITerm::raw )
                        ).collect( Collectors.toList() )
                    ),
                    p_return
                );
        }
        catch ( final Throwable l_throwable )
        {
            LOGGER.warning( MessageFormat.format( "binding method [{0}] throws error [{1}] in agent: ", m_name, l_throwable, p_context.agent() ) );
            return CFuzzyValue.from( false );
        }
    }

    /**
     * creates the returns values of the execution
     *
     * @param p_result return object of the invoke call
     * @param p_return return argument list
     * @return execution return
     */
    @Nonnull
    private static IFuzzyValue<Boolean> returnvalues( @Nullable final Object p_result, @Nonnull final List<ITerm> p_return )
    {
        // void result of the execution
        if ( ( p_result == null ) || ( void.class.equals( p_result.getClass() ) ) )
            return CFuzzyValue.from( true );

        // otherwise object is returned
        p_return.add( CRawTerm.from( p_result ) );
        return CFuzzyValue.from( true );
    }
}
