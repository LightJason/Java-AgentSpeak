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

package lightjason.beliefbase;


import lightjason.common.CCommon;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;

import java.util.Iterator;


/**
 * default beliefbase
 *
 * @tparam T literal type
 * @todo event storing must be implement
 */
public class CBeliefBase implements IBeliefBase
{
    /**
     * storage with data
     */
    protected final IStorage<ILiteral, IMask> m_storage;

    /**
     * ctor
     *
     * @param p_storage storage
     */
    public CBeliefBase( final IStorage<ILiteral, IMask> p_storage )
    {
        if ( p_storage == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "empty" ) );
        m_storage = p_storage;
    }

    @Override
    public final int hashCode()
    {
        return m_storage.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final void add( final ILiteral p_literal )
    {
        m_storage.getMultiElements().put( p_literal.getFunctor(), p_literal );
    }

    @Override
    public final IMask add( final IMask p_mask )
    {
        m_storage.getSingleElements().put( p_mask.getName(), p_mask );
        return p_mask;
    }

    @Override
    public final void clear()
    {
        m_storage.getSingleElements().values().parallelStream().forEach( i -> i.clear() );
        m_storage.clear();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <E extends IMask> E create( final String p_name )
    {
        return (E) new CMask( p_name, this );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final <L extends IStorage<ILiteral, IMask>> L getStorage()
    {
        return (L) m_storage;
    }

    @Override
    public final boolean isEmpty()
    {
        return m_storage.isEmpty();
    }

    @Override
    public final boolean remove( final IMask p_mask )
    {
        return m_storage.getSingleElements().remove( p_mask.getName() ) != null;
    }

    @Override
    public final boolean remove( final ILiteral p_literal )
    {
        return m_storage.getMultiElements().remove( p_literal.getFunctor(), p_literal );
    }

    @Override
    public final void update()
    {
        m_storage.update();
        m_storage.getSingleElements().values().parallelStream().forEach( i -> i.update() );
    }

    @Override
    public int size()
    {
        return m_storage.getMultiElements().size() + m_storage.getSingleElements().values().parallelStream().mapToInt( i -> i.size() ).sum();
    }

    @Override
    public Iterator<ILiteral> iteratorLiteral()
    {
        return m_storage.getMultiElements().values().iterator();
    }

    @Override
    public Iterator<IMask> iteratorBeliefBaseMask()
    {
        return m_storage.getSingleElements().values().iterator();
    }

    @Override
    public boolean remove( final String p_name )
    {
        final boolean l_single = m_storage.getSingleElements().remove( p_name ) != null;
        final boolean l_multi = m_storage.getMultiElements().removeAll( p_name ) != null;
        return l_single || l_multi;
    }

}
