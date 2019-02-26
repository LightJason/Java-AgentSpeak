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

package org.lightjason.agentspeak.action.builtin.math;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Stream;


/**
 * action for maximum.
 * The action calculates for all unflatten arguments
 * the maxmimum with \f$ max( x_0, x_1, \ldots, x_i ) \f$
 *
 * {@code Max = .math/max( 2, 5, 7, [3, 2] );}
 */
public final class CMax extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -7784521124090034499L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CMax.class, "math" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final OptionalDouble l_value = CCommon.flatten( p_argument )
                                              .map( ITerm::<Number>raw )
                                              .mapToDouble( Number::doubleValue )
                                              .max();

        if ( !l_value.isPresent() )
            throw new CExecutionIllegalStateException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "novaluepresent" )
            );

        p_return.add( CRawTerm.of( l_value.getAsDouble() ) );
        return Stream.of();
    }
}
