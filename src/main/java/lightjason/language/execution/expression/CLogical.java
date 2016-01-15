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
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * binary logical expression
 */
public final class CLogical extends IBinary
{
    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side argument
     * @param p_righthandside right-hand-side
     */
    public CLogical( final EOperator p_operator, final IExpression p_lefthandside, final IExpression p_righthandside )
    {
        super( p_operator, p_lefthandside, p_righthandside );

        if ( !m_operator.isLogical() )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "incorrect", m_operator ) );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Collection<ITerm> p_annotation, final Collection<ITerm> p_parameter,
                                               final Collection<ITerm> p_return
    )
    {
        final List<ITerm> l_parameter = lightjason.language.CCommon.replaceVariableFromContext( p_context, p_parameter );
        if ( l_parameter.size() != 2 )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "argumentnumber" ) );

        final IVariable<Boolean> l_lhs = ( (IVariable<?>) l_parameter.get( 0 ) ).throwNotAllocated().throwValueNotAssignableTo( Boolean.class ).getTyped();
        final IVariable<Boolean> l_rhs = ( (IVariable<?>) l_parameter.get( 1 ) ).throwNotAllocated().throwValueNotAssignableTo( Boolean.class ).getTyped();
        switch ( m_operator )
        {

            case AND:
                p_return.add( new CRawTerm<>( l_lhs.get() && l_rhs.get() ) );
                break;

            case OR:
                p_return.add( new CRawTerm<>( l_lhs.get() || l_rhs.get() ) );
                break;

            default:
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "notimplement", m_operator ) );
        }


        return CBoolean.from( true );
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
