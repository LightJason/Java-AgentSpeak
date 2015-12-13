/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.plan;

import lightjason.agent.IAgent;
import lightjason.common.CCommon;
import lightjason.common.CPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;

import java.text.MessageFormat;
import java.util.Map;


/**
 * execution context
 */
public final class CExecutionContext implements IExecutionContext
{
    /**
     * agent of the running context
     */
    private final IAgent m_agent;
    /**
     * current instantiated plan
     */
    private final IPlan m_plan;
    /**
     * plan variables with their data
     */
    private final Map<CPath, Object> m_variables;
    /**
     * current running plans
     */
    private final Map<ILiteral, IPlan> m_runningplans;

    /**
     * ctor
     *
     * @param p_agent agent
     * @param p_plan instantiated plan
     * @param p_variables variables with number
     * @param p_runningplans current running plans
     */
    public CExecutionContext( final IAgent p_agent, final IPlan p_plan, final Map<CPath, Object> p_variables, final Map<ILiteral, IPlan> p_runningplans )
    {
        if ( ( p_agent == null ) || ( p_plan == null ) || ( p_variables == null ) || ( p_runningplans == null ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "notnull" ) );

        m_agent = p_agent;
        m_plan = p_plan;
        m_variables = p_variables;
        m_runningplans = p_runningplans;
    }


    @Override
    public final IAgent getAgent()
    {
        return m_agent;
    }

    @Override
    public final IPlan getPlan()
    {
        return m_plan;
    }

    @Override
    public Map<ILiteral, IPlan> getRunningPlans()
    {
        return null;
    }

    @Override
    public final Map<CPath, Object> getVariables()
    {
        return m_variables;
    }

    @Override
    public final int hashCode()
    {
        return m_agent.hashCode() + m_plan.hashCode() + m_variables.keySet().hashCode() + m_runningplans.values().hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} [{1} | {2} | {3} | {4}]", super.toString(), m_agent, m_plan, m_variables, m_runningplans.values() );
    }
}
