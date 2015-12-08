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

package lightjason.language.plan.action;

import lightjason.agent.IAgent;
import lightjason.language.ILiteral;
import lightjason.language.plan.fuzzy.CBoolean;

import java.text.MessageFormat;
import java.util.List;


/**
 * achievement goal action
 */
public final class CAchievementGoal extends IAction<ILiteral>
{
    /**
     * flag to run immediately
     */
    private final boolean m_immediately;

    /**
     * ctor
     *
     * @param p_literal literal
     * @param p_immediately immediately execution
     */
    public CAchievementGoal( final ILiteral p_literal, final boolean p_immediately )
    {
        super( p_literal );
        m_immediately = p_immediately;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}{1}", m_immediately ? "!!" : "!", m_value );
    }

    @Override
    public CBoolean execute( final IAgent p_agent, final List<?> p_parameter )
    {
        return new CBoolean( false );
    }
}
