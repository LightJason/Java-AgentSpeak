/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.action;

import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Stream;


/**
 * default implementation of an action
 */
public abstract class IBaseAction implements IAction
{
    /**
     * logger
     */
    protected static final Logger LOGGER = org.lightjason.agentspeak.common.CCommon.logger( IAction.class );
    /**
     * serial id
     */
    private static final long serialVersionUID = -1706803039397374484L;


    @Override
    public final int hashCode()
    {
        return this.name().hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return p_object instanceof IAction && this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return this.name().toString();
    }

    /**
     * creates the action name on class
     *
     * @param p_class class
     * @param p_prefix prefix
     * @return path
     */
    protected static IPath namebyclass( @Nonnull final Class<?> p_class, @Nonnull final String... p_prefix )
    {
        return new CPath( Stream.concat( Arrays.stream( p_prefix ), Stream.of( p_class.getSimpleName().substring( 1 ).toLowerCase( Locale.ROOT ) ) ) );
    }

}
