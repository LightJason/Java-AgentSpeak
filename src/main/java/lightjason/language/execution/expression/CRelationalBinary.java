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
 * numerical relation expression
 */
public final class CRelationalBinary extends IBaseBinary
{
    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side argument
     * @param p_righthandside right-hand-side
     */
    public CRelationalBinary( final EOperator p_operator, final IExpression p_lefthandside,
                              final IExpression p_righthandside
    )
    {
        super( p_operator, p_lefthandside, p_righthandside );

        if ( !m_operator.isRelational() )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "notrelational", m_operator ) );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // run left-hand- and right-hand-side argument
        final List<ITerm> l_argument = new LinkedList<>();
        if ( !m_lefthandside.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_argument, Collections.<ITerm>emptyList() ).getValue() )
            return CBoolean.from( false );

        if ( !m_righthandside.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_argument, Collections.<ITerm>emptyList() ).getValue() )
            return CBoolean.from( false );

        if ( l_argument.size() != 2 )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "argumentnumber" ) );


        switch ( m_operator )
        {
            case GREATER:
                p_return.add( CRawTerm.from(
                        this.compare(
                                lightjason.language.CCommon.getRawValue( l_argument.get( 0 ) ),
                                lightjason.language.CCommon.getRawValue( l_argument.get( 1 ) )
                        ) > 0 )
                );
                return CBoolean.from( true );

            case GREATEREQUAL:
                p_return.add( CRawTerm.from(
                        this.compare(
                                lightjason.language.CCommon.getRawValue( l_argument.get( 0 ) ),
                                lightjason.language.CCommon.getRawValue( l_argument.get( 1 ) )
                        ) >= 0 )
                );
                return CBoolean.from( true );

            case LESS:
                p_return.add( CRawTerm.from(
                        this.compare(
                                lightjason.language.CCommon.getRawValue( l_argument.get( 0 ) ),
                                lightjason.language.CCommon.getRawValue( l_argument.get( 1 ) )
                        ) < 0 )
                );
                return CBoolean.from( true );

            case LESSEQUAL:
                p_return.add( CRawTerm.from(
                        this.compare(
                                lightjason.language.CCommon.getRawValue( l_argument.get( 0 ) ),
                                lightjason.language.CCommon.getRawValue( l_argument.get( 1 ) )
                        ) <= 0 )
                );
                return CBoolean.from( true );

            default:
                return CBoolean.from( false );
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
    private <T extends Number & Comparable> int compare( final T p_left, final T p_right )
    {
        return p_left.compareTo( p_right );
    }

}
