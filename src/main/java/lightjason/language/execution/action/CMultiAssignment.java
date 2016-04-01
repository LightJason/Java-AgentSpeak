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


package lightjason.language.execution.action;

import lightjason.language.CCommon;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * assignment action of a multi-variable list
 */
public final class CMultiAssignment<M extends IExecution> extends IBaseExecution<List<IVariable<?>>>
{
    /**
     * right-hand argument
     */
    private final M m_righthand;

    /**
     * ctor
     *
     * @param p_lefthand left-hand variable list
     * @param p_righthand right-hand argument
     */
    public CMultiAssignment( final List<IVariable<?>> p_lefthand, final M p_righthand )
    {
        super( p_lefthand );
        m_righthand = p_righthand;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_result = new LinkedList<>();
        if ( ( !m_righthand.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_result, Collections.<ITerm>emptyList() ).getValue() ) ||
             ( l_result.isEmpty() ) )
            return CBoolean.from( false );


        // position matching on list index
        final List<ITerm> l_flatresult = CCommon.flatList( l_result );
        final List<ITerm> l_assign = CCommon.replaceFromContext( p_context, m_value );


        IntStream.range( 0, Math.min( l_assign.size(), l_flatresult.size() ) ).boxed().forEach(
                i -> ( (IVariable<?>) l_assign.get( i ) ).set( CCommon.getRawValue( l_flatresult.get( i ) ) )
        );


        // tail matching
        if ( l_assign.size() < l_flatresult.size() )
            ( (IVariable<?>) l_assign.get( l_assign.size() - 1 ) ).set(
                    CCommon.getRawValue( l_flatresult.subList( l_assign.size() - 1, l_flatresult.size() ) )
            );

        return CBoolean.from( true );
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode() + m_righthand.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} = {1}", m_value, m_righthand );
    }

    @Override
    @SuppressWarnings( "serial" )
    public final Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{
            addAll( m_value.stream().map( i -> i.shallowcopy() ).collect( Collectors.toSet() ) );
            addAll( m_righthand.getVariables() );
        }};
    }
}
