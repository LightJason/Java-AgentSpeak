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
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * atom expression
 */
public class CAtom implements IExpression
{
    /**
     * value
     */
    private final ITerm m_value;

    /**
     * ctor
     *
     * @param p_value any atomic value
     */
    @SuppressWarnings( "unchecked" )
    public <T> CAtom( final T p_value )
    {
        m_value = ( p_value instanceof IVariable<?> ) ? (IVariable<?>) p_value : CRawTerm.from( p_value );
    }


    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        p_return.add( CRawTerm.from( CCommon.getRawValue( CCommon.replaceFromContext( p_context, m_value ) ) ) );
        return CBoolean.from( true );
    }

    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return 0;
    }

    @Override
    public final Set<IVariable<?>> getVariables()
    {
        return m_value instanceof IVariable<?> ? new HashSet<IVariable<?>>()
        {{
            add( ( (IVariable<?>) m_value ).clone() );
        }} : Collections.<IVariable<?>>emptySet();
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return m_value.equals( p_object );
    }

    @Override
    public final String toString()
    {
        return m_value.toString();
    }
}
