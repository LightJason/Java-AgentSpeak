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

import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.stream.Stream;


/**
 * external action interface
 */
public interface IAction extends IExecution
{
    /**
     * serial id
     */
    long serialVersionUID = -6374480398439703170L;

    /**
     * returns the name with path of the action
     *
     * @return path (literal functor)
     */
    @Nonnull
    IPath name();

    /**
     * minimum number of arguments
     *
     * @return number of minimal arguments
     */
    @Nonnegative
    default int minimalArgumentNumber()
    {
        return 0;
    }

    @Nonnull
    @Override
    default Stream<IVariable<?>> variables()
    {
        return Stream.empty();
    }
}
