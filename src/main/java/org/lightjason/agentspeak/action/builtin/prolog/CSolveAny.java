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

package org.lightjason.agentspeak.action.builtin.prolog;

import alice.tuprolog.SolveInfo;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;
import java.util.Arrays;


/**
 * solve any given prolog queries.
 * The action solves all given prolog queries and
 * binds the beliefbase of the agent. All theory objects
 * will be aggregated together and all queries will be
 * asked and the bounded variables will be returned. The
 * action succeed if all queries can be successful executed.
 *
 * {@code
 * [A|B] = .prolog/solveany( Theory1, ["foo(X)", Theory2, "bar(Y)"] );
 * [C|D] = .prolog/solveany( "foo(X)", "bar(Y)" );
 * }
 */
public final class CSolveAny extends IBaseSolve
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7170142285182677094L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CSolveAny.class, "prolog" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Override
    protected boolean issuccess( @Nonnull final SolveInfo[] p_solveinfos )
    {
        return Arrays.stream( p_solveinfos ).anyMatch( SolveInfo::isSuccess );
    }
}
