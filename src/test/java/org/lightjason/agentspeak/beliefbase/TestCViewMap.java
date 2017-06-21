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

package org.lightjason.agentspeak.beliefbase;

import com.codepoetics.protonpack.StreamUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.beliefbase.view.CViewMap;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 *test of map-view
 */
public final class TestCViewMap extends IBaseTest
{
    /**
     * map reference
     */
    private Map<String, Object> m_data;

    /**
     * initialize
     *
     * @throws IOException is thrown on parsing error
     */
    @Before
    @SuppressWarnings( "unchecked" )
    public final void initialize() throws IOException
    {
        m_data = new ObjectMapper().readValue(
            "{ \"val\" : 123, \"str\" : \"text value\", \"logic\" : true, \"obj\" : { \"name\" : \"abcdef\", \"val\" : 357 }, \"ar\" : [1, 3, 5] }",
            Map.class
        );
    }

    /**
     * test stream
     */
    @Test
    public final void stream()
    {
        Assume.assumeNotNull( m_data );

        Assert.assertTrue(
            StreamUtils.zip(
                new CViewMap( "main", m_data ).stream().limit( m_data.size() - 2 ),
                Stream.of(
                    CLiteral.from( "val", CRawTerm.from( 123L ) ),
                    CLiteral.from( "str", CRawTerm.from( "text value" ) ),
                    CLiteral.from( "logic", CRawTerm.from( true ) ),
                    CLiteral.from( "obj/name", CRawTerm.from( "abcdef" ) ),
                    CLiteral.from( "obj/val", CRawTerm.from( 357L ) )
                ),
                Object::equals
            ).allMatch( i -> i )
        );
    }

    /**
     * test contains literal
     */
    @Test
    public final void containsliteral()
    {
        Assume.assumeNotNull( m_data );
        final IView l_view = new CViewMap( "main", m_data );

        Assert.assertTrue( l_view.containsLiteral( CPath.from( "val" ) ) );
        Assert.assertTrue( l_view.containsLiteral( CPath.from( "obj/name" ) ) );
        Assert.assertFalse( l_view.containsLiteral( CPath.from( "not/exists" ) ) );
    }

    /**
     * test contains view
     */
    @Test
    public final void containsview()
    {
        Assume.assumeNotNull( m_data );
        final IView l_view = new CViewMap( "main", m_data );

        Assert.assertFalse( l_view.containsView( CPath.from( "not/exists" ) ) );
        Assert.assertTrue( l_view.containsView( CPath.from( "obj" ) ) );
    }

    /**
     * manual test
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCViewMap().invoketest();
    }
}
