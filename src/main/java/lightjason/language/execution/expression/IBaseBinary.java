/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.language.execution.expression;

import lightjason.agent.IAgent;
import lightjason.common.CCommon;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.IVariable;
import lightjason.language.score.IAggregation;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;


/**
 * binary expression
 */
public abstract class IBaseBinary implements IBinaryExpression
{
    /**
     * expression operator
     */
    protected final EOperator m_operator;
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
    protected IBaseBinary( final EOperator p_operator, final IExpression p_lefthandside, final IExpression p_righthandside )
    {
        if ( !p_operator.isBinary() )
            throw new CIllegalArgumentException( CCommon.getLanguageString( IBaseBinary.class, "operator", p_operator ) );

        m_operator = p_operator;
        m_lefthandside = p_lefthandside;
        m_righthandside = p_righthandside;
    }

    @Override
    public final IExpression getLeftHandSide()
    {
        return m_lefthandside;
    }

    @Override
    public final IExpression getRightHandSide()
    {
        return m_righthandside;
    }

    @Override
    public final EOperator getOperator()
    {
        return m_operator;
    }

    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return 0;
    }

    @Override
    public final int hashCode()
    {
        return m_lefthandside.hashCode() + m_righthandside.hashCode() + m_operator.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} {1} {2}", m_lefthandside, m_operator, m_righthandside );
    }

    @Override
    public final Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{
            addAll( m_lefthandside.getVariables() );
            addAll( m_righthandside.getVariables() );
        }};
    }
}
