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


import lightjason.generic.IBeliefBase;
import lightjason.generic.IBeliefBaseMask;
import lightjason.generic.IClause;
import lightjason.generic.IStorage;

import java.text.MessageFormat;
import java.util.Iterator;


/**
 * default beliefbase
 *
 * @tparam T literal type
 */
public class CBeliefBase<T> implements IBeliefBase
{
    /**
     * storage with data
     */
    protected final IStorage<IClause, IBeliefBaseMask> m_storage;


    /**
     * ctor - creates an root beliefbase
     */
    public CBeliefBase()
    {
        this( new CBeliefStorage<>() );
    }

    /**
     * ctor
     *
     * @param p_storage storage
     */
    public CBeliefBase( final IStorage<IClause, IBeliefBaseMask> p_storage )
    {
        if ( p_storage == null )
            throw new IllegalArgumentException( MessageFormat.format( "storage need not to be empty", "" ) );
        m_storage = p_storage;
    }

    @Override
    public final int hashCode()
    {
        return 23 * m_storage.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final void add( final IClause p_literal )
    {
        m_storage.addMultiElement( p_literal.getFunctor().get(), p_literal );
    }

    @Override
    public final IBeliefBaseMask add( final IBeliefBaseMask p_mask )
    {
        m_storage.addSingleElement( p_mask.getName(), p_mask );
        return p_mask;
    }

    @Override
    public final void clear()
    {
        for ( final Iterator<IBeliefBaseMask> l_iterator = m_storage.iteratorSingleElement(); l_iterator.hasNext(); )
            l_iterator.next().clear();
        m_storage.clear();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <E extends IBeliefBaseMask> E createMask( final String p_name )
    {
        return (E) new CMask( p_name, this );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final <L extends IStorage<IClause, IBeliefBaseMask>> L getStorage()
    {
        return (L) m_storage;
    }

    @Override
    public final boolean isEmpty()
    {
        return m_storage.isEmpty();
    }

    @Override
    public final boolean remove( final IBeliefBaseMask p_mask )
    {
        return m_storage.removeSingleElement( p_mask.getName() );
    }

    @Override
    public final boolean remove( final IClause p_literal )
    {
        return m_storage.removeMultiElement( p_literal.getFunctor().get(), p_literal );
    }

    @Override
    public boolean remove( final String p_name )
    {
        return m_storage.remove( p_name );
    }

    @Override
    public final void update()
    {
        m_storage.update();

        // iterate over all masks and call update (cascading)
        for ( final Iterator<IBeliefBaseMask> l_iterator = m_storage.iteratorSingleElement(); l_iterator.hasNext(); )
            l_iterator.next().update();
    }

    @Override
    public int sizeMask()
    {
        int l_sum = m_storage.sizeSingleElement();
        for ( final Iterator<IBeliefBaseMask> l_iterator = m_storage.iteratorSingleElement(); l_iterator.hasNext(); )
            l_sum += l_iterator.next().sizeMask();

        return l_sum;
    }

    @Override
    public int sizeLiteral()
    {
        int l_sum = m_storage.sizeMultiElement();
        for ( final Iterator<IBeliefBaseMask> l_iterator = m_storage.iteratorSingleElement(); l_iterator.hasNext(); )
            l_sum += l_iterator.next().sizeLiteral();

        return l_sum;
    }

    @Override
    public int size()
    {
        return this.sizeMask() + this.sizeLiteral();
    }

    @Override
    public Iterator<IClause> iteratorLiteral()
    {
        return m_storage.iteratorMultiElement();
    }

    @Override
    public Iterator<IBeliefBaseMask> iteratorBeliefBaseMask()
    {
        return m_storage.iteratorSingleElement();
    }

}
