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

package lightjason.language.plan;

import lightjason.language.execution.IExecution;
import lightjason.language.execution.annotation.IAnnotation;
import lightjason.language.plan.trigger.ITrigger;

import java.util.Collection;
import java.util.List;


/**
 * interface of plan
 */
public interface IPlan extends IExecution
{

    /**
     * returns the trigger event
     *
     * @return trigger event
     */
    ITrigger<?> getTrigger();

    /**
     * returns plan state
     *
     * @return state
     */
    EState getState();

    /**
     * return unmodifieable annotation set
     *
     * @return set with annotation
     */
    Collection<IAnnotation<?>> getAnnotations();

    /**
     * returns unmodifieable list with plan actions
     *
     * @return action list;
     */
    List<IExecution> getBodyActions();


    /**
     * execution state of a plan
     */
    enum EState
    {
        SUCCESS,
        FAIL,
        SUSPEND;
    }

}
