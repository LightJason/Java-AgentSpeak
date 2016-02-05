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

package lightjason.agent.action.buildin.math.linearprogram;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.error.CIllegalArgumentException;
import org.apache.commons.math3.optim.linear.Relationship;


/**
 * abstract class for constraint actions
 */
public abstract class IConstraint extends IBuildinAction
{

    /**
     * ctor
     */
    protected IConstraint()
    {
        super();
    }

    /**
     * ctor
     *
     * @param p_length argument length
     */
    protected IConstraint( final int p_length )
    {
        super( p_length );
    }

    /**
     * returns the enum of a relationship by a string value
     *
     * @param p_symbol string symbol
     * @return
     */
    protected final Relationship getRelation( final String p_symbol )
    {
        switch ( p_symbol.trim().toLowerCase() )
        {
            case "less":
            case "less-equal":
            case "leq":
            case "<":
            case "<=":
                return Relationship.LEQ;

            case "greater":
            case "greater-equal":
            case "geq":
            case ">":
            case ">=":
                return Relationship.GEQ;

            case "equal":
            case "eq":
            case "=":
            case "==":
                return Relationship.EQ;

            default:
                throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( IConstraint.class, "relation", p_symbol ) );
        }
    }

}
