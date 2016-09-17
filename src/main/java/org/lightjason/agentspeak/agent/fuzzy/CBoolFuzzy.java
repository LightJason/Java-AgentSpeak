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

package org.lightjason.agentspeak.agent.fuzzy;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.fuzzy.defuzzification.CCrisp;
import org.lightjason.agentspeak.language.execution.fuzzy.defuzzification.IDefuzzification;
import org.lightjason.agentspeak.language.execution.fuzzy.operator.IFuzzyOperator;
import org.lightjason.agentspeak.language.execution.fuzzy.operator.bool.CComplement;
import org.lightjason.agentspeak.language.execution.fuzzy.operator.bool.CIntersection;

import java.text.MessageFormat;


/**
 * boolean fuzzy element
 *
 * @tparam T agent type
 */
public final class CBoolFuzzy<T extends IAgent<?>> implements IFuzzy<Boolean, T>
{
    /**
     * fuzzy operator
     */
    private final IFuzzyOperator<Boolean> m_operator;
    /**
     * defuzzyfication
     */
    private final IDefuzzification<Boolean, T> m_defuzzyfication;

    /**
     * ctor
     */
    public CBoolFuzzy()
    {
        this( new CIntersection(), new CCrisp<>( new CComplement() ) );
    }

    /**
     * ctor
     *
     * @param p_operator fuzzy operator
     * @param p_defuzzyfication defuzzyfication
     */
    public CBoolFuzzy( final IFuzzyOperator<Boolean> p_operator, final IDefuzzification<Boolean, T> p_defuzzyfication )
    {
        m_operator = p_operator;
        m_defuzzyfication = p_defuzzyfication;
    }

    @Override
    public final IFuzzyOperator<Boolean> getResultOperator()
    {
        return m_operator;
    }

    @Override
    public final IDefuzzification<Boolean, T> getDefuzzyfication()
    {
        return m_defuzzyfication;
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "defuzzyfication: {0} / fuzzy-operator: {1}", m_defuzzyfication, m_operator );
    }
}
