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

package lightjason.language;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import lightjason.common.CPath;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;


/**
 * default generic literal class for agent beliefs
 * a literal consists of a functor, an optional list of values and
 * an optional set of annotations, e.g. velocity(50)[source(self)]
 *
 * @todo add getter with recursive walk for inner elements
 */
public final class CLiteral implements ILiteral
{
    /**
     * literal annotations
     */
    protected final ImmutableMultimap<CPath, ILiteral> m_annotations;
    /**
     * literal values
     */
    protected final ImmutableMultimap<CPath, ITerm> m_values;
    /**
     * literals functor
     */
    protected final CPath m_functor;
    /**
     * negated option
     */
    protected final boolean m_negated;
    /**
     * @ prefix is set
     */
    protected final boolean m_at;


    /**
     * ctor
     *
     * @param p_functor functor
     */
    public CLiteral( final String p_functor )
    {
        this( false, false, p_functor, Collections.<ITerm>emptyList(), Collections.<ILiteral>emptyList() );
    }

    /**
     * ctor
     *
     * @param p_at @ prefix is set
     * @param p_functor functor of the literal
     * @param p_values negated flag
     * @param p_annotations initial set of annotations
     */
    public CLiteral( final boolean p_at, final String p_functor, final Collection<ITerm> p_values, final Collection<ILiteral> p_annotations )
    {
        this( p_at, false, CPath.from( p_functor ), p_values, p_annotations );
    }

    /**
     * ctor
     *
     * @param p_at @ prefix is set
     * @param p_negated negated flag
     * @param p_functor functor of the literal
     * @param p_values initial list of values
     * @param p_annotations initial set of annotations
     */
    public CLiteral( final boolean p_at, final boolean p_negated, final String p_functor, final Collection<ITerm> p_values,
                     final Collection<ILiteral> p_annotations
    )
    {
        this( p_at, p_negated, CPath.from( p_functor ), p_values, p_annotations );
    }


    /**
     * ctor
     *
     * @param p_at @ prefix is set
     * @param p_negated negated flag
     * @param p_functor functor of the literal
     * @param p_values initial list of values
     * @param p_annotations initial set of annotations
     */
    public CLiteral( final boolean p_at, final boolean p_negated, final CPath p_functor, final Collection<ITerm> p_values,
                     final Collection<ILiteral> p_annotations
    )
    {
        m_at = p_at;
        m_negated = p_negated;
        m_functor = p_functor;


        // create immutable structures
        final Multimap<CPath, ILiteral> l_annotations = HashMultimap.create();
        p_annotations.stream().forEach( i -> l_annotations.put( i.getFQNFunctor(), i ) );
        m_annotations = ImmutableSetMultimap.copyOf( l_annotations );

        final Multimap<CPath, ITerm> l_values = LinkedListMultimap.create();
        p_values.stream().forEachOrdered( i -> l_values.put( i.getFQNFunctor(), i ) );
        m_values = ImmutableListMultimap.copyOf( l_values );
    }


    @Override
    public final ILiteral clone( final CPath p_prefix )
    {
        return new CLiteral( m_at, m_negated, p_prefix.append( m_functor ).toString(), m_values.values(), m_annotations.values() );
    }

    @Override
    public final Multimap<CPath, ILiteral> getAnnotation()
    {
        return m_annotations;
    }

    @Override
    public final Multimap<CPath, ITerm> getValues()
    {
        return m_values;
    }

    @Override
    public final boolean isNegated()
    {
        return m_negated;
    }

    @Override
    public final boolean hasAt()
    {
        return m_at;
    }

    @Override
    public final String getFunctor()
    {
        return m_functor.getSuffix();
    }

    @Override
    public final CPath getFunctorPath()
    {
        return m_functor.getSubPath( 0, m_functor.size() - 1 );
    }

    @Override
    public final CPath getFQNFunctor()
    {
        return m_functor;
    }

    @Override
    public final int hashCode()
    {
        return m_functor.hashCode()
               + m_values.values().stream().mapToInt( i -> i.hashCode() ).sum()
               + m_annotations.values().stream().mapToInt( i -> i.hashCode() ).sum()
               + ( m_negated ? 17737 : 55529 )
               + ( m_at ? 2741 : 8081 );
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public ILiteral clone()
    {
        return new CLiteral( m_at, m_negated, m_functor, m_values.values(), m_annotations.values() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}{1}{1}{2}{3}", m_negated ? "~" : "", m_at ? "@" : "", m_functor, m_values.values(), m_annotations.values() );
    }

}
