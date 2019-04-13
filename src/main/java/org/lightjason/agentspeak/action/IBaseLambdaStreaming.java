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

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;

import java.util.logging.Logger;


/**
 * abstract class for any lambda streaming operator
 *
 * @tparam T stream object type
 */
public abstract class IBaseLambdaStreaming<T> implements ILambdaStreaming<T>
{
    /**
     * logger
     */
    protected static final Logger LOGGER = CCommon.logger( IBaseLambdaStreaming.class );

    /**
     * serial id
     */
    private static final long serialVersionUID = 8873602790694877697L;

    @Override
    public final int hashCode()
    {
        return this.assignable().mapToInt( Object::hashCode ).reduce( 0, ( i, j ) -> i ^ j );
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return p_object instanceof ILambdaStreaming<?> && p_object.hashCode() == this.hashCode();
    }
}
