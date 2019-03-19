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

package org.lightjason.agentspeak.action.web.rest;

import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * action for calling a restful webservice with a JSON object list.
 * Creates a literal based on an JSON webservice data, the first argument is the URL of the webservice,
 * all other arguments are the literal elements of the returning literal, the webservice must return a JSON list and
 * the last argument will used to encapsulate the list elements
 *
 * {@code W = web/rest/jsonlist( "https://api.github.com/repos/LightJason/AgentSpeak/commits", "github", "elements" );}
 *
 * @see https://en.wikipedia.org/wiki/Representational_state_transfer
 * @see https://en.wikipedia.org/wiki/Web_service
 * @see https://en.wikipedia.org/wiki/JSON
 */
public final class CJsonList extends IBaseRest
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5630882266708277403L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CJsonList.class, "web", "rest" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        try
        {
            final List<?> l_data = IBaseRest.json(
                p_argument.get( 0 ).raw(),
                List.class
            );

            if ( p_argument.size() == 2 )
                l_data.stream()
                      .map( i -> CLiteral.of( p_argument.get( p_argument.size() - 1 ).<String>raw(), flatterm( (Map<String, ?>) i ) ) )
                      .forEach( p_return::add );
            else
                p_return.add(
                    IBaseRest.baseliteral(
                        p_argument.stream().skip( 1 ).map( ITerm::raw ),
                        l_data.stream()
                              .map( i -> CLiteral.of( p_argument.get( p_argument.size() - 1 ).<String>raw(), flatterm( (Map<String, ?>) i ) ) )
                    )
                );

            return Stream.of();
        }
        catch ( final IOException l_exception )
        {
            throw new CExecutionIllegalStateException( p_context, l_exception );
        }
    }

}
