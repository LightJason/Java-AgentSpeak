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

package lightjason.agent.action.buildin.math.blas;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * creates a matrix
 */
public class CCreateMatrix extends IBuildinAction
{
    @Override
    public int getMinimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext<?> p_context, final List<ITerm> p_annotation, final List<ITerm> p_argument,
                                         final List<ITerm> p_return
    )
    {
        // first argument is row-size, second colum-size
        // optional third argument is matrix type (default dense-matrix)
        //final EMatrixType l_type = (p_argument.size() > 2) ? EMatrixType.valueOf( ((CRawTerm<?>) p_argument.get )


        //p_return.add( new CRawTerm<>(  ) );

        return CBoolean.from( true );
    }


    private enum EMatrixType
    {
        SPARSE,
        DENSE;
    }
}
