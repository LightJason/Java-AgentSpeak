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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
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
     * name of the action
     */
    private final IPath m_name;
    /**
     * number of arguments
     */
    private final int m_arguments;
    /**
     * method handle
     */
    private final MethodHandle m_method;


    /**
     * ctor
     *
     * @param p_method method reference
     * @throws IllegalAccessException on method access error
     */
    public CMethodAction( final Method p_method ) throws IllegalAccessException
    {
        m_arguments = p_method.getParameterCount();
        m_name = CPath.from(
            p_method.isAnnotationPresent( IAgentActionName.class ) && !p_method.getAnnotation( IAgentActionName.class ).name().isEmpty()
            ? p_method.getAnnotation( IAgentActionName.class ).name().toLowerCase( Locale.ROOT )
            : p_method.getName().toLowerCase( Locale.ROOT )
        );
        m_method = MethodHandles.lookup().unreflect( p_method );
    }


    @Override
    public final IPath name()
    {
        return m_name;
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return m_arguments;
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        try
        {
            return m_arguments == 0

                ? CMethodAction.returnvalues(
                    m_method.invoke( p_context.agent() ),
                    p_return
                )

                : CMethodAction.returnvalues(
                    m_method.invokeWithArguments(
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
    private static IFuzzyValue<Boolean> returnvalues( final Object p_result, final List<ITerm> p_return )
    {
        // void result of the execution
        if ( ( p_result == null ) || ( void.class.equals( p_result.getClass() ) ) )
            return CFuzzyValue.from( true );

        // otherwise object is returned
        p_return.add( CRawTerm.from( p_result ) );
        return CFuzzyValue.from( true );
    }
}
