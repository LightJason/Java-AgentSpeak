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

package org.lightjason.agentspeak.language.execution.expression.numerical;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.expression.EOperator;
import org.lightjason.agentspeak.language.execution.expression.IBaseBinary;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.LinkedList;
import java.util.List;


/**
 * numerical relation expression
 */
public final class CRelational extends IBaseBinary
{
    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side argument
     * @param p_righthandside right-hand-side
     */
    public CRelational( final EOperator p_operator, final IExpression p_lefthandside,
                        final IExpression p_righthandside
    )
    {
        super( p_operator, p_lefthandside, p_righthandside );

        if ( !m_operator.isRelational() )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "operator", m_operator ) );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        final List<ITerm> l_argument = new LinkedList<>();
        if ( !this.executearguments( p_context, p_parallel, l_argument ) )
            return CFuzzyValue.from( false );


        switch ( m_operator )
        {
            case GREATER:
                p_return.add( CRawTerm.from(
                    this.compare(
                        CRelational.map( l_argument.get( 0 ).raw() ),
                        CRelational.map( l_argument.get( 1 ).raw() )
                    ) > 0 )
                );
                return CFuzzyValue.from( true );

            case GREATEREQUAL:
                p_return.add( CRawTerm.from(
                    this.compare(
                        CRelational.map( l_argument.get( 0 ).raw() ),
                        CRelational.map( l_argument.get( 1 ).raw() )
                    ) >= 0 )
                );
                return CFuzzyValue.from( true );

            case LESS:
                p_return.add( CRawTerm.from(
                    this.compare(
                        CRelational.map( l_argument.get( 0 ).raw() ),
                        CRelational.map( l_argument.get( 1 ).raw() )
                    ) < 0 )
                );
                return CFuzzyValue.from( true );

            case LESSEQUAL:
                p_return.add( CRawTerm.from(
                    this.compare(
                        CRelational.map( l_argument.get( 0 ).raw() ),
                        CRelational.map( l_argument.get( 1 ).raw() )
                    ) <= 0 )
                );
                return CFuzzyValue.from( true );

            default:
                return CFuzzyValue.from( false );
        }
    }

    /**
     * compare method for any number type
     *
     * @param p_left left argument
     * @param p_right right argument
     * @return default compare values [-1,1]
     *
     * @tparam T number type
     */
    private <T extends Number & Comparable<T>> int compare( final T p_left, final T p_right )
    {
        return ( p_left instanceof Double ) || ( p_right instanceof Double )
               ? Double.compare( p_left.doubleValue(), p_right.doubleValue() )
               : p_left.compareTo( p_right );
    }


    /**
     * type mapping method
     *
     * @param p_value value
     * @tparam N return type
     * @tparam M value type
     * @return casted value
     */
    @SuppressWarnings( "unchecked" )
    private static <N, M> N map( final M p_value )
    {
        return (N) p_value;
    }

}
