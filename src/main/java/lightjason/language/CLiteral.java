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
import java.util.LinkedList;
import java.util.List;


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
     * negation symbol
     */
    protected static final String NEGATION = "~";
    /**
     * at symbol
     */
    protected static final String AT = "@";
    /**
     * literal annotations
     */
    protected final ImmutableMultimap<CPath, ILiteral> m_annotations;
    /**
     * literal values
     */
    protected final ImmutableMultimap<CPath, ITerm> m_values;
    /**
     * literal values as list
     */
    protected final List<ITerm> m_orderedvalues;
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
        m_functor = p_functor.normalize();


        // create immutable structures
        final Multimap<CPath, ILiteral> l_annotations = HashMultimap.create();
        p_annotations.stream().forEach( i -> l_annotations.put( i.getFQNFunctor(), i ) );
        m_annotations = ImmutableSetMultimap.copyOf( l_annotations );

        final Multimap<CPath, ITerm> l_values = LinkedListMultimap.create();
        p_values.stream().forEachOrdered( i -> l_values.put( i.getFQNFunctor(), i ) );
        m_values = ImmutableListMultimap.copyOf( l_values );

        m_orderedvalues = Collections.unmodifiableList( new LinkedList<>( p_values ) );
    }

    /**
     * factory
     *
     * @param p_functor functor string
     * @return literal
     */
    public static ILiteral from( final String p_functor )
    {
        return from( p_functor, Collections.emptySet(), Collections.emptySet() );
    }

    /**
     * factory
     *
     * @param p_functor functor string
     * @param p_values value literals
     * @return literal
     */
    public static ILiteral from( final String p_functor, final Collection<ITerm> p_values )
    {
        return from( p_functor, p_values, Collections.emptySet() );
    }

    /**
     * factory
     *
     * @param p_functor functor string
     * @param p_values value literals
     * @param p_annotations annotation literals
     * @return literal
     */
    public static ILiteral from( final String p_functor, final Collection<ITerm> p_values, final Collection<ILiteral> p_annotations )
    {
        return new CLiteral(
                p_functor.contains( AT ), p_functor.contains( NEGATION ), CPath.from( p_functor.replace( AT, "" ).replace( NEGATION, "" ) ), p_values,
                p_annotations
        );
    }

    @Override
    public final ILiteral clone( final CPath p_prefix )
    {
        return new CLiteral( m_at, m_negated, p_prefix.append( m_functor ), m_values.values(), m_annotations.values() );
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
    public final List<ITerm> getOrderedValues()
    {
        return m_orderedvalues;
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
               + m_orderedvalues.stream().mapToInt( i -> i.hashCode() ).sum()
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
    public ILiteral cloneWithoutPath()
    {
        return new CLiteral( m_at, m_negated, CPath.from( m_functor.getSuffix() ), m_values.values(), m_annotations.values() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}{1}{1}{2}{3}", m_negated ? NEGATION : "", m_at ? AT : "", m_functor, m_orderedvalues, m_annotations.values() );
    }

}
