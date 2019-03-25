/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
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

package org.lightjason.agentspeak;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CCommon;

import javax.annotation.Nonnull;
import java.util.Locale;


/**
 * action name tests
 */
public final class TestCActionNames
{

    /**
     * run test
     */
    @Test
    public void actionnames()
    {
        CCommon.actionsFromPackage().parallel().forEach( TestCActionNames::checkname );
    }

    /**
     * check action name
     *
     * @param p_action action
     */
    private static void checkname( @Nonnull final IAction p_action )
    {
        Assert.assertTrue(
            p_action.getClass().getCanonicalName(),
            p_action.name().toString().contains( p_action.getClass().getSimpleName().substring( 1 ).toLowerCase( Locale.ROOT ) )
        );
    }

}
