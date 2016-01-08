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

package lightjason.language.execution.unaryoperator;

import com.google.common.collect.ImmutableMultiset;
import lightjason.agent.IAgent;
import lightjason.common.CCommon;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * unary increment
 */
public final class CIncrement<T extends Number> implements IOperator<T>
{
    /**
     * variable
     */
    private final IVariable<T> m_variable;

    /**
     * ctor
     *
     * @param p_variable variable
     */
    public CIncrement( final IVariable<T> p_variable )
    {
        m_variable = p_variable;
    }

    @Override
    public final String toString()
    {
        return m_variable.toString() + "++";
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context,
                                               final Collection<ITerm> p_annotation, final Collection<ITerm> p_parameter,
                                               final Collection<ITerm> p_return
    )
    {
        final IVariable<T> l_variable = (IVariable<T>) lightjason.language.CCommon.replaceVariableFromContext( p_context, m_variable );

        if ( !l_variable.isAllocated() )
            throw new IllegalArgumentException( CCommon.getLanguageString( this, "notallocated", l_variable ) );

        if ( l_variable.isValueAssignableTo( Double.class ) )
            l_variable.set( (T) new Double( l_variable.get().doubleValue() + 1 ) );
        if ( l_variable.isValueAssignableTo( Long.class ) )
            l_variable.set( (T) new Long( l_variable.get().longValue() + 1 ) );
        if ( l_variable.isValueAssignableTo( Float.class ) )
            l_variable.set( (T) new Float( l_variable.get().floatValue() + 1 ) );
        if ( l_variable.isValueAssignableTo( Integer.class ) )
            l_variable.set( (T) new Integer( l_variable.get().intValue() + 1 ) );

        return CBoolean.from( true );
    }

    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return p_aggregate.evaluate( p_agent, ImmutableMultiset.of() );
    }

    @Override
    @SuppressWarnings( "serial" )
    public final Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{
            add( m_variable.clone() );
        }};
    }

}
