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

package lightjason.language;


import com.google.common.collect.SetMultimap;
import lightjason.common.CPath;


/**
 * literal interface
 *
 * @note closed world assumption, no negation marker needed
 */
public interface ILiteral extends ITerm
{

    /**
     * clones the literal
     *
     * @param p_prefix add a path to the functor
     * @return copy of the literal
     */
    ILiteral clone( final CPath p_prefix );

    /**
     * returns the optional annotations
     *
     * @return annotation terms
     */
    SetMultimap<CPath, ILiteral> getAnnotation();

    /**
     * returns the optional value term
     *
     * @return value terms
     */
    SetMultimap<CPath, ITerm> getValues();

    /**
     * getter of the literal for the negation
     */
    boolean isNegated();

    /**
     * returns if the literal has an @ prefix
     *
     * @return prefix is set
     */
    boolean hasAt();
}
