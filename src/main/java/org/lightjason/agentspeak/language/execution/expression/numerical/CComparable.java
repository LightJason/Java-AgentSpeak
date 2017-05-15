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

import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.expression.EOperator;
import org.lightjason.agentspeak.language.execution.expression.IBaseBinary;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.LinkedList;
import java.util.List;


/**
 * comparable binary expression
 */
public final class CComparable extends IBaseBinary
{

    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side argument
     * @param p_righthandside right-hand-side
     */
    public CComparable( final EOperator p_operator, final IExpression p_lefthandside,
                        final IExpression p_righthandside
    )
    {
        super( p_operator, p_lefthandside, p_righthandside );
        if ( !m_operator.isComparable() )
            throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "operator", m_operator ) );
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
            case EQUAL:
                p_return.add( CRawTerm.from( checkequal( l_argument.get( 0 ), l_argument.get( 1 ) ) ) );
                return CFuzzyValue.from( true );

            case NOTEQUAL:
                p_return.add( CRawTerm.from( !checkequal( l_argument.get( 0 ), l_argument.get( 1 ) ) ) );
                return CFuzzyValue.from( true );

            default:
                return CFuzzyValue.from( false );
        }
    }

    /**
     * check method with number handling
     *
     * @param p_value1 term value
     * @param p_value2 term value
     * @return equality flag
     */
    @SuppressWarnings( "unchecked" )
    private static boolean checkequal( final ITerm p_value1, final ITerm p_value2 )
    {
        final Object l_value1 = p_value1.raw();
        final Object l_value2 = p_value2.raw();

        return ( l_value1 instanceof Number ) && ( l_value2 instanceof Number )
               ? Double.valueOf( ( (Number) l_value1 ).doubleValue() ).equals( ( (Number) l_value2 ).doubleValue() )
               : l_value1.equals( l_value2 );
    }

}
