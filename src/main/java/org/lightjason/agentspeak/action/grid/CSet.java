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

package org.lightjason.agentspeak.action.grid;

import cern.colt.matrix.tobject.ObjectMatrix2D;
import com.codepoetics.protonpack.StreamUtils;
import com.codepoetics.protonpack.functions.TriFunction;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * sets an object within the grid.
 * The action sets an object on the given positions and
 * fails iif the object cannot be set
 */
public final class CSet extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 2540547833565366171L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CSet.class, "grid" );
    /**
     * checker function
     */
    private final TriFunction<ObjectMatrix2D, Number, Number, Boolean> m_avoid;

    /**
     * ctor
     */
    public CSet()
    {
        this( ( g, r, c ) -> false );
    }

    /**
     * ctor
     *
     * @param p_avoid function to execute action on grid parameters
     */
    public CSet( @NonNull final TriFunction<ObjectMatrix2D, Number, Number, Boolean> p_avoid )
    {
        m_avoid = p_avoid;
    }

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
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        return StreamUtils.windowed(
            l_arguments.stream().skip( 1 ),
            3,
            3
        ).flatMap( i ->
        {
            final Number l_row = i.get( 0 ).raw();
            final Number l_col = i.get( 1 ).raw();
            final ObjectMatrix2D l_grid = l_arguments.get( 0 ).raw();

            if ( !m_avoid.apply( l_grid, l_row, l_col ) )
            {
                l_grid.setQuick( l_row.intValue(), l_col.intValue(), i.get( 2 ) );
                return Stream.of();
            }

            return p_context.agent().fuzzy().membership().fail();
        } );
    }
}
