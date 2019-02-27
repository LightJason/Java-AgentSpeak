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

package org.lightjason.agentspeak.action.builtin.listsettuple.list;

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * creates a list of tuples with elements of two lists.
 * Creates list of tupels of the first half arguments and the
 * second half arguments with \f$ \mathbb{X} \f$ and \f$ \mathbb{Y} \f$
 * and result \f$ \langle x_i, y_i \rangle \f$
 *
 * {@code T = .collection/list/zip( [1,3,5,7], [2,4,6,8] );}
 */
public final class CZip extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3280909344611567001L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CZip.class, "collection", "list" );

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
        final List<?> l_arguments = CCommon.flatten( p_argument ).map( ITerm::raw ).collect( Collectors.toList() );
        if ( l_arguments.size() % 2 == 1 )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "argumentsnoteven" )
            );

        final List<AbstractMap.Entry<?, ?>> l_result = StreamUtils.zip(
            l_arguments.stream().limit( l_arguments.size() / 2 ),
            l_arguments.stream().skip( l_arguments.size() / 2 ),
            AbstractMap.SimpleEntry::new
        ).collect( Collectors.toList() );

        p_return.add(
            CRawTerm.of(
                p_parallel ? Collections.synchronizedList( l_result ) : l_result
            )
        );

        return Stream.of();
    }

}
