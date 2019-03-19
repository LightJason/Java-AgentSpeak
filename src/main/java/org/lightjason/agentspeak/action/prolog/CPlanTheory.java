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

package org.lightjason.agentspeak.action.prolog;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * creates theory objects with a plan literals.
 * The action create a theory object of the current
 * plan literals
 *
 * {@code T = prolog/plantheory;}
 */
public final class CPlanTheory extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 5754468981342810974L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CPlanTheory.class, "prolog" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnull
    @Override
    public final Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                 @Nonnull final List<ITerm> p_return
    )
    {
        try
        {
            p_return.add(
                CRawTerm.of(
                    new Theory(
                        p_context.agent()
                                 .plans()
                                 .keys()
                                 .stream()
                                 .map( ITrigger::literal )
                                 .distinct()
                                 .map( i -> i.toString() + "." )
                                 .collect( Collectors.joining( "\n" ) )
                        + "\n"
                    )
                )
            );

            return Stream.of();
        }
        catch ( final InvalidTheoryException l_exception )
        {
            throw new CExecutionIllegalStateException( p_context, l_exception );
        }
    }
}
