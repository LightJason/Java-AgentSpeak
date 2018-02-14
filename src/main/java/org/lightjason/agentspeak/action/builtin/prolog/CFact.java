/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

import alice.tuprolog.Prolog;
import alice.tuprolog.Struct;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;


/**
 * adds a fact to prolog interpreter.
 */
public final class CFact extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4583733245650639036L;

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_argument = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        if ( l_argument.size() < 2 )
            return CFuzzyValue.from( false );

        // https://github.com/bolerio/hgdb/wiki/TuProlog
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205/tuProlog-3.2.1-mvn/src/alice/tuprolog/?at=master
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205/tuProlog-3.2.1-mvn/src/alice/tuprolog/Struct.java?at=master&fileviewer=file-view-default
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205/tuProlog-3.2.1-mvn/src/alice/tuprolog/Term.java?at=master&fileviewer=file-view-default
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205/tuProlog-3.2.1-mvn/src/alice/tuprolog/Theory.java?at=master&fileviewer=file-view-default
        // https://bitbucket.org/tuprologteam/tuprolog/src/b025eb748c235c9c340d22b6ae3678adfe93c205/tuProlog-3.2.1-mvn/src/alice/tuprolog/Var.java?at=master&fileviewer=file-view-default

        l_argument.stream()
                  .skip( 1 )
                  .map( ITerm::<Prolog>raw )
                  .forEach( i -> new Struct(  ) );


        return CFuzzyValue.from( true );
    }


}
