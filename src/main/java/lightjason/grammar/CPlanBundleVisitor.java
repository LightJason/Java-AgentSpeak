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

package lightjason.grammar;

import lightjason.agent.IAction;
import lightjason.language.event.IEvent;
import lightjason.language.plan.EExecutionState;
import lightjason.language.plan.IPlan;

import java.util.List;


/**
 * class to visit each AST node of an plan-bundle
 */
public class CPlanBundleVisitor implements IPlan
{
    @Override
    public IEvent<?> getTrigger()
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
    public long getNumberOfRuns()
    {
        return 0;
    }

    @Override
    public long getNumberOfFailRuns()
    {
        return 0;
    }

    @Override
    public List<IAction> getActions()
    {
        return null;
    }
}
