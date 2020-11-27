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

package org.lightjason.agentspeak.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Collections;

/**
 * test plan-bundle configuration
 */

public final class TestCPlanBundleConfiguration extends IBaseTest
{
    /**
     * test plan-bundle
     */
    @Test
    public void planbundle()
    {
        final IPlanBundleConfiguration l_bundle = new CDefaultPlanBundleConfiguration( Collections.emptySet(), Collections.emptySet(), Collections.emptySet() );
        Assertions.assertEquals( Collections.emptySet(), l_bundle.beliefs() );
        Assertions.assertEquals( Collections.emptySet(), l_bundle.plans() );
        Assertions.assertEquals( Collections.emptySet(), l_bundle.rules() );
    }

}
