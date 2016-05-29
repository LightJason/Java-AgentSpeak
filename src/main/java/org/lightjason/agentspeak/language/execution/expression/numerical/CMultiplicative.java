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

package org.lightjason.agentspeak.language.execution.expression.numerical;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.expression.EOperator;
import org.lightjason.agentspeak.language.execution.expression.IBaseBinary;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * multiplicative binary expression
 */
public final class CMultiplicative extends IBaseBinary
{

    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side argument
     * @param p_righthandside right-hand-side
     */
    public CMultiplicative( final EOperator p_operator, final IExpression p_lefthandside,
                            final IExpression p_righthandside
    )
    {
        super( p_operator, p_lefthandside, p_righthandside );
        if ( !m_operator.isMultiplicative() )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "operator", m_operator ) );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // run left-hand- and right-hand-side argument
        final List<ITerm> l_argument = new LinkedList<>();
        if ( !m_lefthandside.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_argument, Collections.<ITerm>emptyList() ).getValue() )
            return CFuzzyValue.from( false );

        if ( !m_righthandside.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_argument, Collections.<ITerm>emptyList() ).getValue() )
            return CFuzzyValue.from( false );

        if ( l_argument.size() != 2 )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "argumentnumber" ) );


        switch ( m_operator )
        {
            case MULTIPLY:
                p_return.add( CRawTerm.from( this.multiply(
                    org.lightjason.agentspeak.language.CCommon.getRawValue( l_argument.get( 0 ) ),
                    org.lightjason.agentspeak.language.CCommon.getRawValue( l_argument.get( 1 ) )
                ) ) );
                return CFuzzyValue.from( true );

            case DIVIDE:
                p_return.add( CRawTerm.from( this.divide(
                    org.lightjason.agentspeak.language.CCommon.getRawValue( l_argument.get( 0 ) ),
                    org.lightjason.agentspeak.language.CCommon.getRawValue( l_argument.get( 1 ) )
                ) ) );
                return CFuzzyValue.from( true );

            case MODULO:
                p_return.add( CRawTerm.from( this.modulo(
                    org.lightjason.agentspeak.language.CCommon.getRawValue( l_argument.get( 0 ) ),
                    org.lightjason.agentspeak.language.CCommon.getRawValue( l_argument.get( 1 ) )
                ) ) );
                return CFuzzyValue.from( true );

            default:
                return CFuzzyValue.from( false );
        }

    }

    /**
     * runs the multiply of number types
     *
     * @param p_left left number argument
     * @param p_right right number argument
     * @return multiply value
     *
     * @tparam N any number type
     * @tparam M any number type
     */
    private <N extends Number, M extends Number> Number multiply( final N p_left, final M p_right )
    {
        return ( p_left instanceof Double ) || ( p_right instanceof Double )
               ? p_left.doubleValue() * p_right.doubleValue()
               : p_left.longValue() * p_right.longValue();
    }

    /**
     * runs the divide of number types
     *
     * @param p_left left number argument
     * @param p_right right number argument
     * @return divide value
     *
     * @tparam N any number type
     * @tparam M any number type
     */
    private <N extends Number, M extends Number> Number divide( final N p_left, final M p_right )
    {
        return ( p_left instanceof Double ) || ( p_right instanceof Double )
               ? p_left.doubleValue() / p_right.doubleValue()
               : p_left.longValue() / p_right.longValue();
    }

    /**
     * runs the modulo of number types
     *
     * @param p_left left number argument
     * @param p_right right number argument
     * @return modulo value
     *
     * @tparam N any number type
     * @tparam M any number type
     */
    private <N extends Number, M extends Number> Number modulo( final N p_left, final M p_right )
    {
        return p_left.longValue() % p_right.longValue();
    }

}
