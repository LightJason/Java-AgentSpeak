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

package org.lightjason.agentspeak.language;

import org.lightjason.agentspeak.common.IPath;


/**
 * term interface
 */
public interface ITerm extends IDeepCopy<ITerm>
{

    /**
     * returns the functor without path
     *
     * @return functor
     */
    String functor();

    /**
     * returns the path of the functor
     *
     * @return path
     */
    IPath functorpath();

    /**
     * returns the full-qualified functor
     * with path and name
     *
     * @return fqn functor
     */
    IPath fqnfunctor();

    /**
     * checks if the literal has variables
     *
     * @return variable flag
     */
    boolean hasVariable();

    /**
     * cast to any raw value type
     *
     * @tparam raw type
     * @return any type
     */
    <T> T raw();

}
