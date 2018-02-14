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

package org.lightjason.agentspeak.action.builtin;

import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.web.graphql.CQuery;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test graphql actions
 */
public final class TestCActionWebGraphQL extends IBaseTest
{

    /**
     * run graphql query test
     */
    @Test
    public final void query()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CQuery().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of(
                CRawTerm.from( "http://daimon-dataaccess.herokuapp.com/graphql" ),
                CLiteral.from(
                    "source",
                    CLiteral.from(
                        "name",
                        CRawTerm.from( "helcom" )
                    ),
                    CLiteral.from(
                        "child",
                        CLiteral.from(
                            "name", CRawTerm.from( "all" )
                        ),
                        CLiteral.from(
                            "querywith",
                            CLiteral.from( "value", CRawTerm.from( Stream.of( "AIS/IMO_2008" ).collect( Collectors.toList() ) ) )
                        )
                    )
                )
            ).collect( Collectors.toList() ),
            l_return
        );


    }


}
