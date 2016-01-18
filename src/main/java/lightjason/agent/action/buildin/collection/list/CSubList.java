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

package lightjason.agent.action.buildin.collection.list;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;


/**
 * returns a sublist within the index range [start,end)
 */
public final class CSubList extends IBuildinAction
{
    /**
     * ctor
     */
    public CSubList()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 3;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final List<ITerm> p_annotation, final List<ITerm> p_argument,
                                               final List<ITerm> p_return
    )
    {
        // first argument set reference, second key-value
        final List<ITerm> l_argument = CCommon.replaceVariableFromContext( p_context, p_argument );

        p_return.addAll(
                CCommon.<List<?>, ITerm>getRawValue( l_argument.get( 0 ) )
                        .subList(
                                CCommon.<Number, ITerm>getRawValue( l_argument.get( 1 ) ).intValue(),
                                CCommon.<Number, ITerm>getRawValue( l_argument.get( 2 ) ).intValue()
                        )
                        .stream().map( i -> CRawTerm.from( i ) ).collect( Collectors.toList() )
        );
        return CBoolean.from( true );
    }

}
