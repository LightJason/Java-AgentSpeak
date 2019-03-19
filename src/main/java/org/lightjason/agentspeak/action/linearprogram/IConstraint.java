/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.linearprogram;

import org.apache.commons.math3.optim.linear.Relationship;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.error.CEnumConstantNotPresentException;

import javax.annotation.Nonnull;
import java.util.Locale;


/**
 * abstract class for constraint actions
 */
public abstract class IConstraint extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1078912058401537801L;

    /**
     * returns the enum of a relationship by a string value
     *
     * @param p_symbol string symbol
     * @return relationship
     */
    @Nonnull
    protected final Relationship getRelation( @Nonnull final String p_symbol )
    {
        switch ( p_symbol.trim().toLowerCase( Locale.ROOT ) )
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
                throw new CEnumConstantNotPresentException( Relationship.class, p_symbol );
        }
    }

}
