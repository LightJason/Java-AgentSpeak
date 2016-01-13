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

package lightjason.agent.action.buildin.generic;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.Collection;


/**
 * action for average
 */
public final class CAverage extends IBuildinAction
{

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 1;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Collection<ITerm> p_annotation, final Collection<ITerm> p_parameter,
                                               final Collection<ITerm> p_return
    )
    {
        final Collection<ITerm> l_parameter = CCommon.replaceVariableFromContext( p_context, p_parameter );
        p_return.add( new CRawTerm<>( p_parameter.stream().filter( i -> ( i instanceof IVariable<?> ) || ( i instanceof CRawTerm<?> ) ).mapToDouble( i -> {

            if ( i instanceof IVariable<?> )
                return ( (IVariable<?>) i ).throwNotAllocated().throwValueNotAssignableTo( Double.class ).<Double>getTyped();

            if ( i instanceof CRawTerm<?> )
                return ( (CRawTerm<?>) i ).throwNotAllocated().throwValueNotAssignableTo( Double.class ).<Double>getTyped();

            // filter avoid this value
            return 0;
        } ).average() ) );

        return CBoolean.from( true );
    }
}
