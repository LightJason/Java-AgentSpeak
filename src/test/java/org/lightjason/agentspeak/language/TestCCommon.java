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

package org.lightjason.agentspeak.language;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Arrays;


/**
 * test language common
 */
public final class TestCCommon extends IBaseTest
{
    /**
     * test compression
     */
    @Test
    public void compression()
    {
        Assert.assertTrue(
            Arrays.stream( CCommon.ECompression.values() )
                  .allMatch( i -> CCommon.ECompression.exist( i.toString() ) )
        );

        Assert.assertTrue(
            Arrays.stream( CCommon.ECompression.values() )
                  .allMatch( i -> CCommon.ECompression.of( i.toString() ).equals( i ) )
        );
    }

}
