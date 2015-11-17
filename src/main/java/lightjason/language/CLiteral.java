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

import lightjason.common.CPath;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;


/**
 * default generic literal class for agent beliefs
 * a literal consists of a functor, an optional list of values and
 * an optional set of annotations, e.g. speed(50)[source(self)]
 */
public final class CLiteral implements ILiteral
{
    /**
     * the literal annotations
     */
    protected final ITermCollection m_annotations;
    /**
     * the literal values
     */
    protected final ITermCollection m_values;
    /**
     * the literals functor
     */
    protected final String m_functor;
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
        this( p_literal.getFunctor(), p_negated, new CTermList( p_literal.getValues() ), new CTermSet( p_literal.getAnnotation() ) );
    }

    /**
     * ctor
     *
     * @param p_functor functor
     */
    public CLiteral( final String p_functor )
    {
        this( p_functor, false, new CTermList(), new CTermSet() );
    }

    /**
     * ctor
     *
     * @param p_functor functor
     * @param p_negated negated flag
     */
    public CLiteral( final String p_functor, final boolean p_negated )
    {
        this( p_functor, p_negated, new CTermList(), new CTermSet() );
    }

    /**
     * ctor
     *
     * @param p_functor functor of the literal
     * @param p_values initial list of values
     */
    public CLiteral( final String p_functor, final List<ITerm> p_values )
    {
        this( p_functor, false, new CTermList( p_values ), new CTermSet() );
    }

    /**
     * ctor
     *
     * @param p_functor functor of the literal
     * @param p_values initial list of values
     * @param p_annotations initial set of annotations
     */
    public CLiteral( final String p_functor, final List<ITerm> p_values, final Set<ITerm> p_annotations )
    {
        this( p_functor, false, new CTermList( p_values ), new CTermSet( p_annotations ) );
    }

    /**
     * ctor
     *
     * @param p_functor functor of the literal
     * @param p_negated negated flag
     * @param p_values initial list of values
     * @param p_annotations initial set of annotations
     */
    public CLiteral( final String p_functor, final boolean p_negated, final ITermCollection p_values, final ITermCollection p_annotations
    )
    {
        m_functor = p_functor;
        m_values = p_values;
        m_annotations = p_annotations;
        m_negated = p_negated;
    }

    @Override
    public final ILiteral clone( final CPath p_prefix )
    {
        return new CLiteral( p_prefix.append( m_functor ).toString(), m_negated, m_values, m_annotations );
    }

    @Override
    public final ITermCollection getAnnotation()
    {
        return m_annotations;
    }

    @Override
    public final String getFunctor()
    {
        return m_functor;
    }

    @Override
    public final ITermCollection getValues()
    {
        return m_values;
    }

    @Override
    public final boolean isNegated()
    {
        return m_negated;
    }

    @Override
    public final int hashCode()
    {
        return 41 * m_functor.hashCode() +
               43 * m_values.hashCode() +
               59 * m_annotations.hashCode() +
               ( m_negated ? 11 : 17 );
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}{1}{2}{3}", m_negated ? "~" : "", m_functor, m_values, m_annotations );
    }

}
