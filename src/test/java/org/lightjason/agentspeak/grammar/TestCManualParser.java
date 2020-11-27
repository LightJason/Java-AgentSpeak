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

package org.lightjason.agentspeak.grammar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.List;
import java.util.stream.Collectors;


/**
 * test for manual parser
 */
public final class TestCManualParser extends IBaseTest
{
    /**
     * test literal parsing
     */
    @Test
    public void literal()
    {
        final ILiteral l_literal =  CLiteral.parse( "foo(X, 123, 'test')" );

        Assertions.assertEquals( "foo", l_literal.functor() );

        final List<ITerm> l_values = l_literal.orderedvalues().collect( Collectors.toList() );
        Assertions.assertEquals( 3, l_values.size() );

        Assertions.assertTrue( l_values.get( 0 ) instanceof IVariable<?> );
        Assertions.assertEquals( l_values.get( 0 ).<IVariable<?>>term().functor(), "X" );
        Assertions.assertEquals( l_values.get( 1 ).<Number>raw(), 123.0 );
        Assertions.assertEquals( l_values.get( 2 ).raw(), "test" );
    }

}
