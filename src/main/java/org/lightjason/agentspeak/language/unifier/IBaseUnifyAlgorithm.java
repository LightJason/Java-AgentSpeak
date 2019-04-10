/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.unifier;

import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.CRelocateMutexVariable;
import org.lightjason.agentspeak.language.variable.CRelocateVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Set;


/**
 * abstract unify algorithm with base algorithm
 */
public abstract class IBaseUnifyAlgorithm implements IUnifyAlgorithm
{

    /**
     * check if both terms are variables
     *
     * @param p_variables output variables
     * @param p_source source term
     * @param p_target target term
     * @return execution boolean
     */
    protected static boolean bothvariables( @Nonnull final Set<IVariable<?>> p_variables,
                                            @Nonnull final ITerm p_source, @Nonnull final ITerm p_target )
    {
        // if s and t are variable create a realocated variable for backtracking
        if ( !( p_target instanceof IVariable<?> && p_source instanceof IVariable<?> ) )
            return false;

        p_variables.add(
            p_target.<IVariable<?>>term().mutex()
            ? new CRelocateMutexVariable<>( p_target.fqnfunctor(), p_source.term() )
            : new CRelocateVariable<>( p_target.fqnfunctor(), p_source.term() )
        );

        return true;
    }


    /**
     * check if the target variable will be set
     *
     * @param p_variables output variables
     * @param p_source source term
     * @param p_target target term
     * @return execution boolean
     */
    protected static boolean variables( @Nonnull final Set<IVariable<?>> p_variables,
                                        @Nonnull final ITerm p_source, @Nonnull final ITerm p_target )
    {
        if ( !( p_target instanceof IVariable<?> ) )
            return false;

        p_variables.add( p_target.<IVariable<Object>>term().set( p_source ) );
        return true;
    }

}
