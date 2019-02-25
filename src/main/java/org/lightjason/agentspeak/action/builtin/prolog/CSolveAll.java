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
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;
import java.util.Arrays;


/**
 * solve all given prolog queries.
 * The action solves any given prolog queries and
 * binds the beliefbase of the agent. All theory objects
 * will be aggregated together and all queries will be
 * asked and the bounded variables will be returned. The
 * action succeed if any queries can be successful executed
 * and only the bound variables will be returned
 *
 * {@code
 * [A|B] = .prolog/solveall( Theory1, ["foo(X)", Theory2, "bar(Y)"] );
 * [C|D] = .prolog/solveall( "foo(X)", "bar(Y)" );
 * }
 */
public final class CSolveAll extends IBaseSolve
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 902243292596197085L;
    /**
     * action name
     */
    private static final IPath NAME = CPath.of( "prolog/solveall" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    protected boolean issuccess( @Nonnull final SolveInfo[] p_solveinfos )
    {
        return Arrays.stream( p_solveinfos ).allMatch( SolveInfo::isSuccess );
    }

}
