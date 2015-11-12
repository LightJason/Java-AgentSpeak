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

package lightjason.generic.implementation;

import lightjason.common.CPath;
import lightjason.generic.IAtom;
import lightjason.generic.IClause;
import lightjason.generic.ITerm;
import lightjason.generic.ITermCollection;

import java.util.List;
import java.util.Set;


/**
 * default generic literal class for agent beliefs
 * a literal consists of a functor, an optional list of values and
 * an optional set of annotations, e.g. speed(50)[source(self)]
 */
public class CClause<T> implements IClause
{
    /**
     * the literal annotations
     */
    protected final ITermCollection m_annotations;
    /**
     * the original agent specific literal (i.e. Jason, Goal, 2APL)
     */
    protected final T m_literal;
    /**
     * the literal values
     */
    protected final ITermCollection m_values;
    /**
     * the literals functor
     */
    protected final IAtom<String> m_functor;
    /**
     * negated option
     */
    protected final boolean m_negated;

    /**
     * ctor
     *
     * @param p_functor functor
     * @param p_literal literal
     */
    public CClause( final String p_functor, final T p_literal )
    {
        this( new CStringAtom( p_functor ), p_literal, new CTermList(), new CTermSet(), false );
    }

    /**
     * ctor
     *
     * @param p_functor functor
     * @param p_literal literal
     * @param p_negated negated flag
     */
    public CClause( final String p_functor, final T p_literal, final boolean p_negated )
    {
        this( new CStringAtom( p_functor ), p_literal, new CTermList(), new CTermSet(), p_negated );
    }

    /**
     * ctor
     *
     * @param p_functor functor of the literal
     * @param p_literal the original literal
     * @param p_values initial list of values
     * @param p_annotations initial set of annotations
     */
    public CClause( final String p_functor, final T p_literal, final List<ITerm> p_values, final Set<ITerm> p_annotations )
    {
        this( new CStringAtom( p_functor ), p_literal, new CTermList( p_values ), new CTermSet( p_annotations ), false );
    }

    /**
     * ctor
     *
     * @param p_functor functor of the literal
     * @param p_literal the original literal
     * @param p_values initial list of values
     * @param p_annotations initial set of annotations
     * @param p_negated negated flag
     */
    public CClause( final CStringAtom p_functor, final T p_literal, final ITermCollection p_values, final ITermCollection p_annotations,
            final boolean p_negated
    )
    {
        m_functor = p_functor;
        m_literal = p_literal;
        m_values = p_values;
        m_annotations = p_annotations;
        m_negated = p_negated;
    }

    @Override
    public IClause clone( final CPath p_prefix )
    {
        return new CClause<T>( new CStringAtom( p_prefix.append( m_functor.get() ).toString() ), m_literal, m_values, m_annotations, m_negated );
    }

    @Override
    public ITermCollection getAnnotation()
    {
        return m_annotations;
    }

    @Override
    public IAtom<String> getFunctor()
    {
        return m_functor;
    }


    @Override
    public ITermCollection getValues()
    {
        return m_values;
    }

    @Override
    public boolean isNegated()
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
    public String toString()
    {
        return ( m_negated ? "~" : "" ) + m_functor.toString() + m_values.toString() + m_annotations.toString();
    }

}
