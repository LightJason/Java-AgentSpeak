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

import java.text.MessageFormat;


/**
 * default variable definition
 *
 * @note variable set is not thread-safe
 */
public class CVariable<T> implements IVariable<T>
{
    /**
     * variable name
     */
    protected final String m_name;
    /**
     * boolean flag, that defines an variable which matchs always
     */
    protected final boolean m_any;
    /**
     * value of the variable
     */
    protected T m_value;

    /**
     * ctor
     *
     * @param p_name name
     */
    public CVariable( final String p_name )
    {
        this( p_name, null );
    }

    /**
     * ctor
     *
     * @param p_name name
     * @param p_value value
     */
    public CVariable( final String p_name, final T p_value )
    {
        m_any = p_name.equals( "_" ) || ( p_name == null ) || p_name.isEmpty();
        m_name = m_any ? "_" : p_name;
        m_value = p_value;
    }

    @Override
    public final String getName()
    {
        return m_name;
    }

    @Override
    public IVariable<T> set( final T p_value )
    {
        if ( !m_any )
            m_value = p_value;
        return this;
    }

    @Override
    public T get()
    {
        return m_value;
    }

    @Override
    public boolean isAllocated()
    {
        return m_value != null;
    }

    @Override
    public boolean isValueAssignableTo( final Class<?> p_class )
    {
        return m_value == null ? true : p_class.isAssignableFrom( m_value.getClass() );
    }

    @Override
    public int hashCode()
    {
        return m_name.hashCode() + ( ( m_value != null ) ? m_value.hashCode() : 0 );
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new CVariable<T>( m_name, m_value );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})", m_name, m_value == null ? "" : m_value );
    }
}
