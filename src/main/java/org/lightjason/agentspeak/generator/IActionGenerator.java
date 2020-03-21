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

package org.lightjason.agentspeak.generator;

import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CNoSuchElementException;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * action lazy-loader class
 */
public interface IActionGenerator extends Function<IPath, IAction>
{
    /**
     * empty generator
     */
    IActionGenerator EMPTY = new IActionGenerator()
    {


        @Override
        public boolean contains( @Nonnull final IPath p_path )
        {
            return false;
        }

        @Override
        public IActionGenerator add( @Nonnull final IActionGenerator... p_generator )
        {
            return this;
        }

        @Override
        public IActionGenerator remove( @Nonnull final IActionGenerator... p_generator )
        {
            return this;
        }

        @Override
        public IActionGenerator add( @Nonnull final Stream<IActionGenerator> p_generator )
        {
            return this;
        }

        @Override
        public IActionGenerator remove( @Nonnull final Stream<IActionGenerator> p_generator )
        {
            return this;
        }

        @Override
        public IAction apply( @Nonnull final IPath p_path )
        {
            throw new CNoSuchElementException( CCommon.languagestring( IActionGenerator.class, "notfound", p_path ) );
        }
    };

    /**
     * checks if an action exists within this generator
     *
     * @param p_path action name
     * @return existance flag
     */
    boolean contains( @Nonnull final IPath p_path );

    /**
     * adds a temporary other generators
     *
     * @param p_generator generators
     * @return self-reference
     */
    IActionGenerator add( @Nonnull IActionGenerator... p_generator );

    /**
     * removes a temporary other generators
     *
     * @param p_generator generators
     * @return self-reference
     */
    IActionGenerator remove( @Nonnull IActionGenerator... p_generator );

    /**
     * adds a temporary other generators
     *
     * @param p_generator stream of generators
     * @return self-reference
     */
    IActionGenerator add( @Nonnull Stream<IActionGenerator> p_generator );

    /**
     * removes a temporary other generators
     *
     * @param p_generator stream of generators
     * @return self-reference
     */
    IActionGenerator remove( @Nonnull Stream<IActionGenerator> p_generator );
}
