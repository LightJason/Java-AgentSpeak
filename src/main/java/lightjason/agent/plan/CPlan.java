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
 * plan structure
 */
public class CPlan implements IPlan
{
    /**
     * name of the plan
     **/
    private final String m_name;

    /**
     * ctor
     *
     * @param p_name name
     */
    public CPlan( final String p_name )
    {
        m_name = p_name;
    }


    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public boolean isExecutable()
    {
        return false;
    }

    @Override
    public EExecutionState execute()
    {
        return null;
    }

    @Override
    public EExecutionState getState()
    {
        return null;
    }

    @Override
    public double getCost()
    {
        return 0;
    }
}
