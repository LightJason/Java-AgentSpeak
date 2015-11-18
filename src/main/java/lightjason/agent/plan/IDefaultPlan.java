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

package lightjason.agent.plan;

/**
 * abstract plan structure
 */
public abstract class IDefaultPlan implements IPlan
{
    /**
     * name of the plan
     **/
    protected final String m_name;
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
     * ctor
     *
     * @param p_name name
     */
    protected IDefaultPlan( final String p_name )
    {
        m_name = p_name;
    }


    @Override
    public final String getName()
    {
        return m_name;
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
    public double getCost()
    {
        return 0;
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
}
