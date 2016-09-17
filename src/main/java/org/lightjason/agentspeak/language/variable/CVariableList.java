/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.variable;

import java.util.Collections;
import java.util.List;


/**
 * list for variables with head-tail definition
 */
public class CVariableList
{
    private final List<IVariable<?>> m_variables;

    /**
     * ctor
     *
     * @param p_list list of variables
     */
    public CVariableList( final List<IVariable<?>> p_list )
    {
        m_variables = Collections.unmodifiableList( p_list );
    }

    /**
     * list getter
     *
     * @return unmodifiable list
     */
    public List<IVariable<?>> get()
    {
        return m_variables;
    }

}
