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
 * logical binary expression
 */
public final class CLogicalBinary extends IBaseBinary
{

    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side argument
     * @param p_righthandside right-hand-side
     */
    public CLogicalBinary( final EOperator p_operator, final IExpression p_lefthandside, final IExpression p_righthandside )
    {
        super( p_operator, p_lefthandside, p_righthandside );
        if ( !m_operator.isLogical() )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "notlogical", m_operator ) );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // get left-hand-side argument and run fastcheck
        final List<ITerm> l_argument = new LinkedList<>();
        if ( ( !m_lefthandside.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_argument, Collections.<ITerm>emptyList() ).getValue() ) ||
             ( l_argument.isEmpty() ) )
            return CBoolean.from( false );

        if ( this.fastCheck( l_argument, p_return ) )
            return CBoolean.from( true );


        // get right-hand-side argument
        if ( !m_righthandside.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_argument, Collections.<ITerm>emptyList() ).getValue() )
            return CBoolean.from( false );
        if ( l_argument.size() != 2 )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "argumentnumber" ) );


        // calculate return of both expression results
        switch ( m_operator )
        {

            case AND:
                p_return.add( CRawTerm.from( lightjason.language.CCommon.<Boolean, ITerm>getRawValue( l_argument.get( 0 ) ) &&
                                             lightjason.language.CCommon.<Boolean, ITerm>getRawValue( l_argument.get( 1 ) ) ) );
                return CBoolean.from( true );

            case OR:
                p_return.add( CRawTerm.from( lightjason.language.CCommon.<Boolean, ITerm>getRawValue( l_argument.get( 0 ) ) ||
                                             lightjason.language.CCommon.<Boolean, ITerm>getRawValue( l_argument.get( 1 ) ) ) );
                return CBoolean.from( true );

            case XOR:
                p_return.add( CRawTerm.from( lightjason.language.CCommon.<Boolean, ITerm>getRawValue( l_argument.get( 0 ) ) ^
                                             lightjason.language.CCommon.<Boolean, ITerm>getRawValue( l_argument.get( 1 ) ) ) );
                return CBoolean.from( true );

            default:
                return CBoolean.from( false );
        }
    }

    /**
     * run a fast check on boolean operators to stop execution
     *
     * @param p_argument argument list
     * @param p_return modified return list
     * @return boolean flag if fast check result value
     */
    private boolean fastCheck( final List<ITerm> p_argument, final List<ITerm> p_return )
    {
        final boolean l_result;
        switch ( m_operator )
        {
            case AND:
                l_result = !lightjason.language.CCommon.<Boolean, ITerm>getRawValue( p_argument.get( 0 ) );
                break;

            case OR:
                l_result = lightjason.language.CCommon.<Boolean, ITerm>getRawValue( p_argument.get( 0 ) );
                break;

            default:
                l_result = false;
        }

        if ( l_result )
            p_return.addAll( p_argument );

        return l_result;
    }

}
