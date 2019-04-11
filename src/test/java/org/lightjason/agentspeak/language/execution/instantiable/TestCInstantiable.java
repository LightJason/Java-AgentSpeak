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

package org.lightjason.agentspeak.language.execution.instantiable;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.testing.IBaseTest;


/**
 * test all instantiable structure
 */
public final class TestCInstantiable extends IBaseTest
{
    /**
     * test empty trigger
     */
    @Test
    public void emptytrigger()
    {
        Assert.assertEquals( 0, ITrigger.EMPTY.compareTo( ITrigger.EMPTY ) );
        Assert.assertEquals( -1, ITrigger.EMPTY.compareTo( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "xxx" ) ) ) );
        Assert.assertEquals( 0, ITrigger.EMPTY.structurehash() );
        Assert.assertFalse( ITrigger.EMPTY.hasShallowcopywithoutsuffix() );
        Assert.assertTrue( ITrigger.EMPTY.type().sequence().isEmpty() );
        Assert.assertEquals( ITrigger.EType.EMPTY, ITrigger.EMPTY.type() );
        Assert.assertEquals( ILiteral.EMPTY, ITrigger.EMPTY.literal() );
        Assert.assertEquals( ITrigger.EMPTY, ITrigger.EMPTY.shallowcopy() );
        Assert.assertEquals( ITrigger.EMPTY, ITrigger.EMPTY.shallowcopysuffix() );
        Assert.assertEquals( ITrigger.EMPTY, ITrigger.EMPTY.shallowcopywithoutsuffix() );
    }

}
