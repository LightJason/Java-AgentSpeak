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

package org.lightjason.agentspeak.language.execution.passing;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * test passing
 */
public final class TestCPassing extends IBaseTest
{

    /**
     * test raw data
     */
    @Test
    public void passraw()
    {
        final List<ITerm> l_return = new ArrayList<>();

        execute( new CPassRaw<>( "foo" ), false, Collections.emptyList(), l_return );
        execute( new CPassRaw<>( false ), false, Collections.emptyList(), l_return );
        execute( new CPassRaw<>( 12 ), false, Collections.emptyList(), l_return );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertEquals( "foo", l_return.get( 0 ).raw() );
        Assert.assertEquals( false, l_return.get( 1 ).raw() );
        Assert.assertEquals( 12, l_return.get( 2 ).<Number>raw() );
    }

    /**
     * test variable
     */
    @Test
    public void passvariable()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final IVariable<?> l_string = new CVariable<>( "foo" ).set( "hello" );
        final IVariable<?> l_number = new CVariable<>( "bar" ).set( 5 );

        execute(
            new CPassVariable( l_string ),
            false,
            Collections.emptyList(),
            l_return,
            l_string
        );

        execute(
            new CPassVariable( l_number ),
            false,
            Collections.emptyList(),
            l_return,
            l_number
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( "hello", l_return.get( 0 ).raw() );
        Assert.assertEquals( 5, l_return.get( 1 ).<Number>raw() );
    }



}
