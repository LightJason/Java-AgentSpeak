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
 * # along with this program. If not, see <http://www.gnu.org/licenses/>.               #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.execution.action;

import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.Collection;


/**
 * assignment action
 */
public class CAssignment<T> extends IBaseExecution<IVariable<T>>
{

    /**
     * ctor
     *
     * @param p_value assign variable
     */
    public CAssignment( final IVariable<T> p_value )
    {
        super( p_value );
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Collection<ITerm> p_annotation, final Collection<ITerm> p_parameter,
                                         final Collection<ITerm> p_return
    )
    {
        return CBoolean.from( true );
    }
}
