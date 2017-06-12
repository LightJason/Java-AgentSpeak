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

package org.lightjason.agentspeak.action.buildin.math.blas;

import cern.colt.matrix.DoubleMatrix1D;

import java.text.MessageFormat;


/**
 * formatter of 1D matrix
 */
public final class CFormat1D extends IFormat<DoubleMatrix1D>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5588984423759143305L;

    @Override
    protected final Class<?> getType()
    {
        return DoubleMatrix1D.class;
    }

    @Override
    protected String format( final DoubleMatrix1D p_data )
    {
        return MessageFormat.format( "[{0}]({1})", p_data.size(), FORMATTER.toString( p_data ) );
    }

}
