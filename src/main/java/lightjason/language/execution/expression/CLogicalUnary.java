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

import lightjason.common.CCommon;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * logical unary expression
 */
public final class CLogicalUnary extends IBaseUnary
{

    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_expression expression
     */
    public CLogicalUnary( final EOperator p_operator, final IExpression p_expression )
    {
        super( p_operator, p_expression );
        if ( !m_operator.isLogical() )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "notlogical", m_operator ) );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final List<ITerm> p_annotation, final List<ITerm> p_argument,
                                               final List<ITerm> p_return
    )
    {
        final List<ITerm> l_argument = new LinkedList<>();
        if ( ( !m_expression.execute( p_context, Collections.<ITerm>emptyList(), Collections.<ITerm>emptyList(), l_argument ).getValue() ) ||
             ( l_argument.size() != 1 ) )
            return CBoolean.from( false );

        switch ( m_operator )
        {
            case NEGATION:
                p_return.add( CRawTerm.from(
                        !lightjason.language.CCommon.<Boolean, ITerm>getRawValue( l_argument.get( 0 ) )
                ) );
                return CBoolean.from( true );

            default:
                return CBoolean.from( false );
        }
    }

}
