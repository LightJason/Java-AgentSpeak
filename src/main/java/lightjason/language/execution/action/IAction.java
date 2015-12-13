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

package lightjason.language.execution.action;

import lightjason.language.execution.IExecution;


/**
 * test goal action
 */
public abstract class IAction<T> implements IExecution
{
    /**
     * data
     */
    protected final T m_value;

    /**
     * ctor
     */
    protected IAction()
    {
        m_value = null;
    }

    /**
     * ctor
     *
     * @param p_value data
     */
    protected IAction( final T p_value )
    {
        m_value = p_value;
    }

    /**
     * checkes assinable of the value
     *
     * @param p_class class
     * @return assinable (on null always true)
     */
    public final boolean isValueAssignableTo( final Class<?> p_class )
    {
        return ( m_value == null ) || p_class.isAssignableFrom( m_value.getClass() );
    }

    /**
     * returns the value of the action
     *
     * @return value
     */
    public final T getValue()
    {
        return m_value;
    }

}
