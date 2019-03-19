/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.bool;

import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;


/**
 * checks elements of equality.
 * The actions checks the first argument
 * to all others arguments of equality,
 * list structures won't be unflaten, but
 * elementwise compared.
 * On number arguments not the value must equal, also the type (double / integral) must be equal,
 * so keep in mind, that you use the correct number type on the argument input
 *
 * {@code [E1|E2] = .bool/equal( "this is equal", "this is equal", [123, "test"] );}
 */
public final class CEqual extends IBaseEqualiy
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2953614515361905328L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CEqual.class, "bool" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    /**
     * apply to change boolean result
     *
     * @param p_value boolean result
     * @return boolean value
     */
    protected boolean apply( final boolean p_value )
    {
        return p_value;
    }

}
