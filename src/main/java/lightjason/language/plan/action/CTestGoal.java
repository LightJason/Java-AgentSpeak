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

import lightjason.beliefbase.IBeliefBaseMask;
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.fuzzy.CBoolean;

import java.text.MessageFormat;
import java.util.Map;


/**
 * test goal action
 */
public final class CTestGoal extends IAction<ILiteral>
{

    /**
     * ctor
     *
     * @param p_literal literal
     */
    public CTestGoal( final ILiteral p_literal )
    {
        super( p_literal );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "?{0}", m_value );
    }

    @Override
    public CBoolean execute( final IBeliefBaseMask p_beliefbase, final Map<ILiteral, IPlan> p_runningplan )
    {
        return new CBoolean( p_runningplan.containsKey( m_value ) );
    }
}
