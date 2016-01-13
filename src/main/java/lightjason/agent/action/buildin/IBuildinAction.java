/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agent.action.buildin;

import lightjason.agent.action.IBaseAction;
import lightjason.common.CPath;


/**
 * base class of build-in actions for setting name
 * by package/classname (without prefix character)
 */
public abstract class IBuildinAction extends IBaseAction
{
    /**
     * action name
     */
    private final CPath m_name;

    /**
     * ctor
     */
    protected IBuildinAction()
    {
        final String[] l_name = this.getClass().getCanonicalName().split( "\\." );
        m_name = new CPath( l_name[l_name.length - 2], l_name[l_name.length - 1].substring( 1 ) ).toLower();
    }


    @Override
    public final CPath getName()
    {
        return m_name;
    }
}
