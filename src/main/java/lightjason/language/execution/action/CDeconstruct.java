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
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * deconstruct assignment
 */
public final class CDeconstruct<M extends ITerm> extends IBaseExecution<List<IVariable<?>>>
{
    /**
     * right-hand argument (literal)
     */
    private final M m_righthand;

    /**
     * ctor
     *
     * @param p_lefthand left-hand variable list
     * @param p_righthand right-hand argument
     */
    public CDeconstruct( final List<IVariable<?>> p_lefthand, final M p_righthand )
    {
        super( p_lefthand );
        m_righthand = p_righthand;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        this.set( CCommon.replaceFromContext( p_context, m_value ), CCommon.getRawValue( m_righthand ) );
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
        return MessageFormat.format( "{0} =.. {1}", m_value, m_righthand );
    }

    @Override
    public final Set<IVariable<?>> getVariables()
    {
        return m_value.stream().map( i -> i.shallowcopy() ).collect( Collectors.toSet() );
    }

    /**
     * sets the values
     *
     * @param p_assignment variable list
     * @param p_term term
     */
    @SuppressWarnings( "unchecked" )
    private void set( final List<ITerm> p_assignment, final ILiteral p_term )
    {
        if ( p_assignment.size() >= 1 )
            ( (IVariable<Object>) p_assignment.get( 0 ) ).set( p_term.getFQNFunctor().toString() );
        if ( p_assignment.size() >= 2 )
            ( (IVariable<Object>) p_assignment.get( 1 ) ).set( Collections.synchronizedList( p_term.values().collect( Collectors.toList() ) ) );
        if ( p_assignment.size() >= 3 )
            ( (IVariable<Object>) p_assignment.get( 2 ) ).set(
                    Collections.synchronizedList( new LinkedList<>( p_term.annotations().collect( Collectors.toList() ) ) ) );
    }
}
