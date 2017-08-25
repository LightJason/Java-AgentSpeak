/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.builtin;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;


/**
 * base class of build-in actions for setting name
 * by package/classname (without prefix character)
 */
public abstract class IBuiltinAction extends IBaseAction
{
    /**
     * logger
     */
    protected static final Logger LOGGER = CCommon.logger( IBuiltinAction.class );
    /**
     * serial id
     */
    private static final long serialVersionUID = 3888384147874966937L;
    /**
     * action name
     */
    private final IPath m_name;


    /**
     * ctor
     */
    protected IBuiltinAction()
    {
        this( 2 );
    }

    /**
     * ctor
     *
     * @param p_length length of package parts
     */
    protected IBuiltinAction( final int p_length )
    {
        final List<String> l_names = Arrays.asList( this.getClass().getCanonicalName().split( "\\." ) );
        l_names.set( l_names.size() - 1, l_names.get( l_names.size() - 1 ).substring( 1 ) );

        m_name = new CPath(
            IntStream.range( Math.max( 0, l_names.size() - p_length ), l_names.size() )
                     .mapToObj( l_names::get )
        ).lower();
    }

    @Nonnull
    @Override
    public final IPath name()
    {
        return m_name;
    }

}
