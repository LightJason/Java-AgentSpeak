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
 * checks elements of inequality.
 * The actions checks the first argument
 * to all others arguments of unequality,
 * list structures won't be unflaten, but
 * elementwise compared.
 * On number arguments not the value must equal, also
 * the type (double / integral) must be equal, so keep
 * in mind, that you use the correct number type on the argument input
 *
 * {@code [NE1|NE2] = .bool/notequal( "this is equal", "this is equal", [123, "test"] );}
 */
public final class CNotEqual extends IBaseEqualiy
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3226848482113693419L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CNotEqual.class, "bool" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Override
    protected boolean apply( final boolean p_value )
    {
        return !p_value;
    }

}
