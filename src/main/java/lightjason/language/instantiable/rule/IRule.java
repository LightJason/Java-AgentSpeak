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

package lightjason.language.instantiable.rule;

import lightjason.language.ILiteral;
import lightjason.language.instantiable.IInstantiable;
import lightjason.language.instantiable.plan.IPlan;
import lightjason.language.instantiable.plan.trigger.ITrigger;

import java.util.Map;


/**
 * interface of logical rule
 */
public interface IRule extends IInstantiable
{

    /**
     * returns the identifier of the rule
     *
     * @return literal
     */
    ILiteral getIdentifier();

    /**
     * replaces all placeholder objects and reinstantiate object
     *
     * @param p_plans full instantiated plans
     * @param p_rules full instantiated rules
     * @return new object instance without placeholders
     */
    IInstantiable replaceplaceholder( final Map<ITrigger, IPlan> p_plans, final Map<ILiteral, IRule> p_rules );

}
