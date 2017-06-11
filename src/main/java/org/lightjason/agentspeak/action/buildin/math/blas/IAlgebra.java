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

import cern.colt.matrix.linalg.Algebra;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;


/**
 * blas algebra operations e.g. inverse, determinate
 */
public abstract class IAlgebra extends IBuildinAction
{

    /**
     * algebra reference
     */
    public static final Algebra ALGEBRA = Algebra.DEFAULT;
    /**
     * serial id
     */
    private static final long serialVersionUID = 8980414250195042661L;

    /**
     * ctor
     */
    protected IAlgebra()
    {
        super();
    }

    /**
     * ctor
     *
     * @param p_length length of package parts
     */
    protected IAlgebra( final int p_length )
    {
        super( p_length );
    }

}
