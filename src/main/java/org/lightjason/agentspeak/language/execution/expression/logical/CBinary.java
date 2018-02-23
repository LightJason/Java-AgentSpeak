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

package org.lightjason.agentspeak.language.execution.expression.logical;

import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.expression.EOperator;
import org.lightjason.agentspeak.language.execution.expression.IBaseBinary;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;


/**
 * logical binary expression
 */
public final class CBinary extends IBaseBinary
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 397466749260293866L;

    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side argument
     * @param p_righthandside right-hand-side
     */
    public CBinary( @Nonnull final EOperator p_operator, @Nonnull final IExpression p_lefthandside, @Nonnull final IExpression p_righthandside )
    {
        super( p_operator, p_lefthandside, p_righthandside );
        if ( !m_operator.isLogical() )
            throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "operator", m_operator ) );
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_argument = new LinkedList<>();
        if ( !this.executearguments( p_parallel, p_context, l_argument ) )
            return CFuzzyValue.from( false );

        // calculate return of both expression results
        switch ( m_operator )
        {

            case AND:
                p_return.add( CRawTerm.from( l_argument.get( 0 ).<Boolean>raw() && l_argument.get( 1 ).<Boolean>raw() ) );
                return CFuzzyValue.from( true );

            case OR:
                p_return.add( CRawTerm.from( l_argument.get( 0 ).<Boolean>raw() || l_argument.get( 1 ).<Boolean>raw() ) );
                return CFuzzyValue.from( true );

            case XOR:
                p_return.add( CRawTerm.from( l_argument.get( 0 ).<Boolean>raw() ^ l_argument.get( 1 ).<Boolean>raw() ) );
                return CFuzzyValue.from( true );

            default:
                return CFuzzyValue.from( false );
        }
    }

}
