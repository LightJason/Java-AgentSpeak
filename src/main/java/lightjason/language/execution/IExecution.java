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

package lightjason.language.execution;

import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.execution.fuzzy.CBoolean;

import java.util.Collection;


/**
 * internal execution interface
 */
public interface IExecution
{

    /**
     * defines a plan-body operation
     *
     * @param p_context current execution context
     * @param p_annotation annotation
     * @param p_parameter parameter of the action
     * @param p_return return values
     * @return fuzzy boolean
     */
    public CBoolean execute( final IContext<?> p_context, final Collection<ILiteral> p_annotation, final Collection<ITerm> p_parameter,
            final Collection<ITerm> p_return
    );

}
