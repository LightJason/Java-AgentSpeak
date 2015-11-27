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

import lightjason.agent.IAction;
import lightjason.language.ILiteral;
import lightjason.language.event.IEvent;

import java.text.MessageFormat;
import java.util.List;


/**
 * abstract plan structure
 */
public class CPlan implements IPlan
{
    /**
     * plan literal
     **/
    protected final ILiteral m_literal;
    /**
     * trigger event
     */
    protected final IEvent<?> m_triggerevent;
    /**
     * current plan state
     */
    protected EExecutionState m_currentstate = EExecutionState.Success;
    /**
     * number of runs
     */
    protected long m_runs = 0;
    /**
     * number of fail runs
     */
    protected long m_failruns = 0;
    /**
     * list with the plan body actions
     */
    protected final List<IAction> m_actions;

    /**
     * ctor
     *
     * @param p_event trigger event
     * @param p_literal head literal
     */
    public CPlan( final IEvent<?> p_event, final ILiteral p_literal )
    {
        m_literal = p_literal;
        m_triggerevent = p_event;
        m_actions = null;
    }

    @Override
    public IEvent<?> getTrigger()
    {
        return m_triggerevent;
    }

    @Override
    public final boolean isExecutable()
    {
        return false;
    }

    @Override
    public EExecutionState execute()
    {
        return null;
    }

    @Override
    public final EExecutionState getState()
    {
        return m_currentstate;
    }

    @Override
    public final long getNumberOfRuns()
    {
        return m_runs;
    }

    @Override
    public final long getNumberOfFailRuns()
    {
        return m_failruns;
    }

    @Override
    public final List<IAction> getActions()
    {
        return m_actions;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0} (trigger event : {1} / literal : {2})", super.toString(), m_triggerevent, m_literal );
    }
}
