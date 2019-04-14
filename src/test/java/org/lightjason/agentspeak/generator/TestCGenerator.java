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

package org.lightjason.agentspeak.generator;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.io.IOException;
import java.util.NoSuchElementException;


/**
 * generator test
 */
public final class TestCGenerator extends IBaseTest
{

    /**
     * test agent generator multiple
     *
     * @throws IOException on stream error
     */
    @Test
    public void agentgeneratormultiple() throws IOException
    {
        Assert.assertEquals( 5, new CAgentGenerator().generatemultiple( 5 ).count() );
    }

    /**
     * test static action generator fail
     */
    @Test( expected = NoSuchElementException.class )
    public void staticactiongeneratorfail()
    {
        new CActionStaticGenerator().apply( CPath.of( "foo" ) );
    }
}
