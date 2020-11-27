/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.execution.unify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * test unify execution
 */
public final class TestCUnify extends IBaseTest
{
    /**
     * test literal unify
     *
     * @throws IOException on stream error
     */
    @Test
    public void literalunify() throws IOException
    {
        final ILiteral l_source = CLiteral.parse( "foobar(X)" );
        final ILiteral l_target = CLiteral.parse( "foobar(Y)" );
        final IVariable<?> l_xvariable = new CVariable<>( "X", 321 );
        final IVariable<?> l_yvariable = new CVariable<>( "Y", 111 );
        final IContext l_context = new CLocalContext( new CAgentGenerator().generatesingle(), l_xvariable, l_yvariable );

        Assertions.assertEquals( 2, l_context.instancevariables().size() );
        Assertions.assertEquals( Integer.valueOf( 321 ), l_xvariable.raw() );
        Assertions.assertEquals( Integer.valueOf( 111 ), l_yvariable.raw() );

        Assertions.assertTrue(
            execute(
                new CLiteralUnify( false, l_source, l_target ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_context
            )
        );


        Assertions.assertEquals( Integer.valueOf( 321 ), l_xvariable.raw() );
        Assertions.assertEquals( Integer.valueOf( 321 ), l_yvariable.raw() );
        Assertions.assertEquals( l_xvariable, l_context.instancevariables().get( l_xvariable.fqnfunctor() ) );
        Assertions.assertEquals( l_yvariable, l_context.instancevariables().get( l_yvariable.fqnfunctor() ) );
        Assertions.assertFalse( l_target.values().findFirst().get().<IVariable<?>>term().allocated() );
        Assertions.assertFalse( l_source.values().findFirst().get().<IVariable<?>>term().allocated() );
    }

    /**
     * test literal unify fail
     */
    @Test
    public void failunify()
    {
        Assertions.assertFalse(
            execute(
                new CLiteralUnify( false, CLiteral.parse( "xxx(123)" ), CLiteral.parse( "yyy(Y)" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

    /**
     * test unify tostring
     */
    @Test
    public void unifystring()
    {
        Assertions.assertEquals(
            ">>(zzz[Z()], abc[123.0])",
            new CLiteralUnify( false, CLiteral.parse( "abc(123)" ), CLiteral.parse( "zzz(Z)" ) ).toString()
        );
    }

    /**
     * test literal variables
     */
    @Test
    public void variables()
    {
        final List<IVariable<?>> l_variables = new CLiteralUnify(
            false,
            CLiteral.parse( "foobar(X)" ),
            CLiteral.parse( "foobar(Y)" )
        ).variables().collect( Collectors.toList() );

        Assertions.assertEquals( 2, l_variables.size() );
        Assertions.assertEquals( CPath.of( "X" ), l_variables.get( 0 ).fqnfunctor() );
        Assertions.assertEquals( CPath.of( "Y" ), l_variables.get( 1 ).fqnfunctor() );
        Assertions.assertFalse( l_variables.get( 0 ).allocated() );
        Assertions.assertFalse( l_variables.get( 1 ).allocated() );
    }
}
