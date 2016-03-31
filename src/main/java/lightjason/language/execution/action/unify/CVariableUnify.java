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

package lightjason.language.execution.action.unify;

import lightjason.language.CCommon;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * unify a literal
 */
public final class CVariableUnify extends CDefaultUnify
{
    /**
     * unification variable with literal
     */
    private final IVariable<?> m_constraint;

    /**
     * ctor
     *
     * @param p_parallel parallel execution
     * @param p_literal variable with literal
     * @param p_constraint expression
     */
    public CVariableUnify( final boolean p_parallel, final ILiteral p_literal, final IVariable<?> p_constraint )
    {
        super( p_parallel, p_literal );
        m_constraint = p_constraint;
    }


    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}>>({1}, {2})", m_parallel ? "@" : "", m_value, m_constraint );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final Set<IVariable<?>> l_variables = p_context.getAgent().getUnifier().literalunify( m_value, CCommon.getRawValue(
                CCommon.replaceFromContext( p_context, m_constraint ) ) );
        if ( l_variables.size() != m_variablenumber )
            return CBoolean.from( false );

        CCommon.updatecontext( p_context, l_variables.stream() );
        return CBoolean.from( true );
    }

    @Override
    @SuppressWarnings( "serial" )
    public final Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{
            addAll( CVariableUnify.super.getVariables() );
            add( m_constraint.shallowcopy() );
        }};
    }
}
