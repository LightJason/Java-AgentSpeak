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

package lightjason.language.execution.fuzzy;

/**
 * result for an immutable fuzzy value
 *
 * @tparam T fuzzy type
 */
public interface IFuzzyValue<T>
{
    /**
     * returns the result
     *
     * @return value
     */
    T getValue();

    /**
     * returns the fuzziness
     *
     * @return fuzzy value in [0,1]
     */
    double getFuzzy();

    /**
     * checkes assignable of the value
     *
     * @param p_class class
     * @return assignable (on null always true)
     */
    boolean isValueAssignableTo( final Class<?>... p_class );

}
