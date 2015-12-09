/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import lightjason.common.CPath;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;


/**
 * default generic literal class for agent beliefs
 * a literal consists of a functor, an optional list of values and
 * an optional set of annotations, e.g. velocity(50)[source(self)]
 */
public final class CLiteral implements ILiteral
{
    /**
     * literal annotations
     */
    protected final SetMultimap<CPath, ILiteral> m_annotations = HashMultimap.create();
    /**
     * literal values
     */
    protected final SetMultimap<CPath, ITerm> m_values = LinkedHashMultimap.create();
    /**
     * literals functor
     */
    protected final CPath m_functor;
    /**
     * negated option
     */
    protected final boolean m_negated;

    /**
     * ctor
     *
     * @param p_literal literal
     * @param p_negated strong negation
     */
    public CLiteral( final CLiteral p_literal, final boolean p_negated )
    {
        this( p_literal.getFQNFunctor(), p_negated, p_literal.getValues().values(), p_literal.getAnnotation().values() );
    }

    /**
     * ctor
     *
     * @param p_functor functor
     */
    public CLiteral( final String p_functor )
    {
        this( p_functor, false, Collections.<ITerm>emptyList(), Collections.<ILiteral>emptyList() );
    }

    /**
     * ctor
     *
     * @param p_functor functor
     * @param p_negated negated flag
     */
    public CLiteral( final String p_functor, final boolean p_negated )
    {
        this( p_functor, p_negated, Collections.<ITerm>emptyList(), Collections.<ILiteral>emptyList() );
    }

    /**
     * ctor
     *
     * @param p_functor functor of the literal
     * @param p_values initial list of values
     */
    public CLiteral( final String p_functor, final Collection<ITerm> p_values )
    {
        this( p_functor, false, p_values, Collections.<ILiteral>emptyList() );
    }

    /**
     * ctor
     *
     * @param p_functor functor of the literal
     * @param p_values initial list of values
     * @param p_annotations initial set of annotations
     */
    public CLiteral( final String p_functor, final Collection<ITerm> p_values, final Collection<ILiteral> p_annotations )
    {
        this( p_functor, false, p_values, p_annotations );
    }

    /**
     * ctor
     *
     * @param p_functor functor of the literal
     * @param p_negated negated flag
     * @param p_values initial list of values
     * @param p_annotations initial set of annotations
     */
    public CLiteral( final String p_functor, final boolean p_negated, final Collection<ITerm> p_values, final Collection<ILiteral> p_annotations
    )
    {
        this( CPath.createSplitPath( CPath.DEFAULTSEPERATOR, p_functor ), p_negated, p_values, p_annotations );
    }

    /**
     * ctor
     *
     * @param p_functor functor of the literal
     * @param p_negated negated flag
     * @param p_values initial list of values
     * @param p_annotations initial set of annotations
     */
    protected CLiteral( final CPath p_functor, final boolean p_negated, final Collection<ITerm> p_values, final Collection<ILiteral> p_annotations )
    {
        m_functor = p_functor;
        p_values.stream().forEachOrdered( i -> m_values.put( i.getFQNFunctor(), i ) );
        p_annotations.stream().forEach( i -> m_annotations.put( i.getFQNFunctor(), i ) );
        m_negated = p_negated;
    }


    @Override
    public final ILiteral clone( final CPath p_prefix )
    {
        return new CLiteral( p_prefix.append( m_functor ).toString(), m_negated, m_values.values(), m_annotations.values() );
    }

    @Override
    public final SetMultimap<CPath, ILiteral> getAnnotation()
    {
        return m_annotations;
    }

    @Override
    public final SetMultimap<CPath, ITerm> getValues()
    {
        return m_values;
    }

    @Override
    public final boolean isNegated()
    {
        return m_negated;
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
        return m_functor.hashCode() +
               m_values.values().stream().mapToInt( i -> i.hashCode() ).sum() +
               m_annotations.values().stream().mapToInt( i -> i.hashCode() ).sum() +
               ( m_negated ? 17737 : 55529 );
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}{1}{2}{3}", m_negated ? "~" : "", m_functor, m_values.values(), m_annotations.values() );
    }

}
