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

package lightjason.agentspeak.language.execution.unaryoperator;

import com.google.common.collect.ImmutableMultiset;
import lightjason.agentspeak.agent.IAgent;
import lightjason.agentspeak.language.ITerm;
import lightjason.agentspeak.language.execution.IContext;
import lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import lightjason.agentspeak.language.variable.IVariable;

import java.util.List;
import java.util.stream.Stream;


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
    public final IFuzzyValue<Boolean> execute( final IContext p_context,
                                               final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final IVariable<T> l_variable = ( (IVariable<T>) lightjason.agentspeak.language.CCommon.replaceFromContext( p_context, m_variable ) ).throwNotAllocated();

        if ( l_variable.isValueAssignableTo( Double.class ) )
            l_variable.set( (T) new Double( l_variable.get().doubleValue() + 1 ) );
        if ( l_variable.isValueAssignableTo( Long.class ) )
            l_variable.set( (T) new Long( l_variable.get().longValue() + 1 ) );
        if ( l_variable.isValueAssignableTo( Float.class ) )
            l_variable.set( (T) new Float( l_variable.get().floatValue() + 1 ) );
        if ( l_variable.isValueAssignableTo( Integer.class ) )
            l_variable.set( (T) new Integer( l_variable.get().intValue() + 1 ) );

        return CFuzzyValue.from( true );
    }

    @Override
    public final double score( final IAgent<?> p_agent )
    {
        return p_agent.getAggregation().evaluate( p_agent, ImmutableMultiset.of() );
    }

    @Override
    public final Stream<IVariable<?>> getVariables()
    {
        return Stream.of( m_variable );
    }

}
