/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.language.variable;

import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * structure for creating evaluate variables
 */
public final class CVariableEvaluate implements IVariableEvaluate
{
    /**
     * content variable with a string or literal
     */
    private final IVariable<?> m_variable;
    /**
     * optional parameter list
     */
    private final List<ITerm> m_parameter;

    /**
     * ctor
     *
     * @param p_variable variable
     */
    public CVariableEvaluate( final IVariable<?> p_variable )
    {
        this( p_variable, Collections.<ITerm>emptyList() );
    }

    /**
     * ctor
     *
     * @param p_variable variable
     * @param p_parameter optional parameter list
     */
    public CVariableEvaluate( final IVariable<?> p_variable, final List<ITerm> p_parameter )
    {
        m_variable = p_variable;
        m_parameter = Collections.unmodifiableList( p_parameter );
    }


    @Override
    public final boolean hasMutex()
    {
        return m_variable.hasMutex();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final ILiteral evaluate( final IContext p_context )
    {
        final IVariable<?> l_variable = (IVariable<?>) CCommon.replaceFromContext( p_context, m_variable );
        if ( !l_variable.isAllocated() )
            throw new CIllegalStateException();

        // if variable is a string
        if ( l_variable.isValueAssignableTo( String.class ) )
            return this.fromString( l_variable.getTyped(), p_context );

        if ( m_variable.isValueAssignableTo( ILiteral.class ) )
            return this.fromLiteral( l_variable.getTyped(), p_context );

        throw new CIllegalStateException();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Stream<IVariable<?>> getVariables()
    {
        return Stream.concat(
            Stream.of( m_variable ),
            m_parameter.parallelStream()
                       .filter( i -> i instanceof IVariable<?> )
                       .map( i -> (IVariable<?>) i )
        );
    }


    @Override
    public final int hashCode()
    {
        return m_variable.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}{1}", m_variable, m_parameter.isEmpty() ? "" : m_parameter );
    }

    @Override
    public final String getFunctor()
    {
        return m_variable.getFunctor();
    }

    @Override
    public final IPath getFunctorPath()
    {
        return m_variable.getFunctorPath();
    }

    @Override
    public final IPath getFQNFunctor()
    {
        return m_variable.getFQNFunctor();
    }

    @Override
    public final ITerm deepcopy( final IPath... p_prefix )
    {
        return m_variable.deepcopy( p_prefix );
    }

    @Override
    public final ITerm deepcopySuffix()
    {
        return m_variable.deepcopySuffix();
    }

    /**
     * creates the result literal from an input string
     *
     * @param p_value input string (literal functor)
     * @param p_context execution context
     * @return result literal
     */
    private ILiteral fromString( final String p_value, final IContext p_context )
    {
        return CLiteral.from( p_value, m_parameter ).unify( p_context );
    }

    /**
     * creates the result literal from an input literal
     *
     * @param p_literal input literal
     * @param p_context execution context
     * @return result literal
     */
    private ILiteral fromLiteral( final ILiteral p_literal, final IContext p_context )
    {
        if ( m_parameter.isEmpty() )
            return p_literal.unify( p_context );

        return new CLiteral(
            p_literal.hasAt(),
            p_literal.isNegated(),
            p_literal.getFQNFunctor(),
            m_parameter,
            Collections.<ILiteral>emptyList()
        ).unify( p_context );
    }

}
