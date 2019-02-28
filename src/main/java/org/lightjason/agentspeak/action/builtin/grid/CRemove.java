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

package org.lightjason.agentspeak.action.builtin.grid;

import cern.colt.matrix.tobject.ObjectMatrix2D;
import com.codepoetics.protonpack.StreamUtils;
import com.codepoetics.protonpack.functions.TriFunction;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * removes an object of the grid.
 * The action removes an object of the grid and
 * fails iif the object cannot remove and returns the given
 * objects
 *
 * {@code [A|B|C] = .grid/remove(Grid, 3,3, [1,1, 8,7])}
 */
public final class CRemove extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 5843417421687002640L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CRemove.class, "grid" );
    /**
     * void function
     */
    private final TriFunction<ObjectMatrix2D, Number, Number, Boolean> m_avoid;

    /**
     * ctor
     */
    public CRemove()
    {
        this( ( g, r, c ) -> false );
    }

    /**
     * ctor
     *
     * @param p_avoid function to execute action on grid parameters
     */
    public CRemove( @NonNull final TriFunction<ObjectMatrix2D, Number, Number, Boolean> p_avoid )
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
            2,
            2
        ).flatMap( i ->
        {
            final Number l_row = i.get( 0 ).raw();
            final Number l_col = i.get( 1 ).raw();
            final ObjectMatrix2D l_grid = l_arguments.get( 0 ).raw();

            if ( !m_avoid.apply( l_grid, l_row, l_col ) )
            {
                p_return.add( CRawTerm.of( l_grid.getQuick( l_row.intValue(), l_col.intValue() ) ) );
                l_grid.setQuick( l_row.intValue(), l_col.intValue(), null );
                return Stream.of();
            }

            return p_context.agent().fuzzy().membership().fail();
        } );
    }
}
