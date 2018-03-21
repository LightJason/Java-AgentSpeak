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

package org.lightjason.agentspeak.language.execution.expression;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * binary expression
 * @deprecated remove
 */
@Deprecated
public abstract class IBaseBinary implements IBinaryExpression
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1630029294068548691L;
    /**
     * expression operator
     */
    protected final EBinaryOperator m_operator;
    /**
     * left-hand-side argument
     */
    protected final IExpression m_lefthandside;
    /**
     * right-hand-side argument
     */
    protected final IExpression m_righthandside;



    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side argument
     * @param p_righthandside right-hand-side
     */
    protected IBaseBinary( @Nonnull final EBinaryOperator p_operator, @Nonnull final IExpression p_lefthandside, @Nonnull final IExpression p_righthandside )
    {
        if ( !p_operator.isBinary() )
            throw new CIllegalArgumentException( CCommon.languagestring( IBaseBinary.class, "operator", p_operator ) );

        m_operator = p_operator;
        m_lefthandside = p_lefthandside;
        m_righthandside = p_righthandside;
    }

    /**
     * execute expression arguments
     *
     * @param p_parallel parallel execution
     * @param p_context execution context
     * @param p_argument returning arguments of the expressions
     * @return boolean of successfully execution
     * @throws IllegalArgumentException is thrown if more than two arguments are returned
     */
    protected final boolean executearguments( final boolean p_parallel, @Nonnull final IContext p_context,
                                              @Nonnull final List<ITerm> p_argument
    ) throws IllegalArgumentException
    {
        // run left-hand- and right-hand-side argument
        if ( ( !m_lefthandside.execute( p_parallel, p_context, Collections.<ITerm>emptyList(), p_argument ).value() )
             || ( p_argument.isEmpty() ) )
            return false;

        if ( ( !m_righthandside.execute( p_parallel, p_context, Collections.<ITerm>emptyList(), p_argument ).value() )
             || ( p_argument.isEmpty() ) )
            return false;

        if ( p_argument.size() != 2 )
            throw new CIllegalArgumentException( CCommon.languagestring( IBaseBinary.class, "argumentnumber" ) );

        return true;
    }


    @Nonnull
    @Override
    public final IExpression leftHandSide()
    {
        return m_lefthandside;
    }

    @Nonnull
    @Override
    public final IExpression rightHandSide()
    {
        return m_righthandside;
    }

    @Nonnull
    @Override
    public final EBinaryOperator operator()
    {
        return m_operator;
    }

    @Override
    public final int hashCode()
    {
        return m_lefthandside.hashCode() ^ m_righthandside.hashCode() ^ m_operator.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object instanceof IExpression ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} {1} {2}", m_lefthandside, m_operator, m_righthandside );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.concat( m_lefthandside.variables(), m_righthandside.variables() );
    }
}
