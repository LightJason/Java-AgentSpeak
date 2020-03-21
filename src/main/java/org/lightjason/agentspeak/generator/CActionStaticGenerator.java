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

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * action lazy-loader static generator
 */
public final class CActionStaticGenerator extends IBaseActionGenerator
{
    /**
     * ctor
     */
    public CActionStaticGenerator()
    {
        super();
    }

    /**
     * ctor
     *
     * @param p_actions action set
     */
    public CActionStaticGenerator( @NonNull final Collection<IAction> p_actions )
    {
        this( p_actions.stream() );
    }

    /**
     * ctor
     *
     * @param p_actions action stream
     */
    public CActionStaticGenerator( @NonNull final Stream<IAction> p_actions )
    {
        super( p_actions );
    }

    @Override
    protected Optional<IAction> stream( @Nonnull final IPath p_path )
    {
        return Optional.empty();
    }

}
