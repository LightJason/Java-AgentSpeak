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

package lightjason.language;

import lightjason.error.CIllegalArgumentException;
import lightjason.language.execution.IContext;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * common structure for execution definition
 */
public final class CCommon
{
    /**
     * private ctor - avoid instantiation
     */
    private CCommon()
    {
    }

    /**
     * returns a native / raw value of a term
     *
     * @return term value or raw value
     */
    @SuppressWarnings( "unchecked" )
    public static <T, N> T getRawValue( final N p_term )
    {
        return (T) ( p_term instanceof CRawTerm<?> ? ( (CRawTerm<?>) p_term ).get() : p_term );
    }

    /**
     * replace variables with context variables
     *
     * @param p_context execution context
     * @param p_terms replacing term list
     * @return result term list
     */
    public static List<ITerm> replaceVariableFromContext( final IContext<?> p_context, final Collection<ITerm> p_terms )
    {
        return p_terms.stream().map( i -> replaceVariableFromContext( p_context, i ) ).collect( Collectors.toList() );
    }

    /**
     * replace variable with context variable
     * other values will be passed
     *
     * @param p_context execution context
     * @param p_term term
     * @return replaces variable object
     */
    public static ITerm replaceVariableFromContext( final IContext<?> p_context, final ITerm p_term )
    {
        if ( !( p_term instanceof IVariable<?> ) )
            return p_term;

        final IVariable<?> l_variable = p_context.getInstanceVariables().get( p_term.getFQNFunctor() );
        if ( l_variable == null )
            throw new CIllegalArgumentException( lightjason.common.CCommon
                                                         .getLanguageString(
                                                                 lightjason.common.CCommon.class, "variablenotfoundincontext", p_term.getFQNFunctor() ) );

        return l_variable;
    }
}
