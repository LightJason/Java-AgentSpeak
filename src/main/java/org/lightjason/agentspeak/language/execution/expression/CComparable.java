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

import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;


/**
 * comparable binary expression
 * @deprecated remove
 */
@Deprecated
public final class CComparable extends IBaseBinary
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3088316270644309406L;

    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side argument
     * @param p_righthandside right-hand-side
     */
    public CComparable( @Nonnull final EBinaryOperator p_operator, @Nonnull final IExpression p_lefthandside, @Nonnull final IExpression p_righthandside )
    {
        super( p_operator, p_lefthandside, p_righthandside );
        //if ( !m_operator.isComparable() )
        throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "operator", m_operator ) );
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_argument = new LinkedList<>();
        if ( !this.executearguments( p_parallel, p_context, l_argument ) )
            return CFuzzyValue.of( false );

        switch ( m_operator )
        {
            case EQUAL:
                p_return.add( CRawTerm.of( checkequal( l_argument.get( 0 ), l_argument.get( 1 ) ) ) );
                return CFuzzyValue.of( true );

            case NOTEQUAL:
                p_return.add( CRawTerm.of( !checkequal( l_argument.get( 0 ), l_argument.get( 1 ) ) ) );
                return CFuzzyValue.of( true );

            default:
                return CFuzzyValue.of( false );
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
    private static boolean checkequal( @Nonnull final ITerm p_value1, @Nonnull final ITerm p_value2 )
    {
        final Object l_value1 = p_value1.raw();
        final Object l_value2 = p_value2.raw();

        return ( l_value1 instanceof Number ) && ( l_value2 instanceof Number )
               ? Double.valueOf( ( (Number) l_value1 ).doubleValue() ).equals( ( (Number) l_value2 ).doubleValue() )
               : l_value1.equals( l_value2 );
    }

}
