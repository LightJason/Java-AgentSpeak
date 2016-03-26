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

package lightjason.beliefbase;

import lightjason.common.IPath;
import lightjason.language.instantiable.plan.trigger.ITrigger;

import java.util.Set;


/**
 * interface of beliefbase definition
 */
public interface IBeliefBase extends IStructure
{

    /**
     * retruns all trigger of the beliefbase
     *
     * @param p_view mask for events
     * @return set with trigger events
     */
    Set<ITrigger<IPath>> getTrigger( final IView p_view );

    /**
     * removes single- and multi-elements
     *
     * @param p_name name
     * @return success
     */
    boolean remove( final String p_name );

}
