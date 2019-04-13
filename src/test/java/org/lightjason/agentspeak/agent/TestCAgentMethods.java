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

package org.lightjason.agentspeak.agent;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.testing.IBaseTest;


/**
 * test agent method calls
 */
public final class TestCAgentMethods extends IBaseTest
{
    /**
     * agent
     */
    private IAgent<?> m_agent;

    /**
     * initialize
     *
     * @throws Exception agent exception
     */
    @Before
    public void initialize() throws Exception
    {
        m_agent = new CAgentGenerator().generatesingle();
    }

    /**
     * test agent tostring
     */
    @Test
    public void agenttostring()
    {
        Assume.assumeNotNull( m_agent );

        Assert.assertTrue( m_agent.toString().startsWith( "org.lightjason.agentspeak.testing.IBaseTest$CAgent@" ) );
        Assert.assertTrue( m_agent.toString().endsWith( " ( Trigger: [] / Running Plans: [] / Beliefbase: beliefbase (org.lightjason.agentspeak.beliefbase.view.CView@4ae24171): [] )" ) );
    }

    /**
     * test trigger variable error
     */
    @Test( expected = IllegalArgumentException.class )
    public void triggervariableerror()
    {
        Assume.assumeNotNull( m_agent );

        m_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.parse( "foo(X)" ) ) );
    }
}
