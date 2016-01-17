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

package lightjason.language.execution.expression;

import lightjason.agent.IAgent;
import lightjason.error.CIllegalStateException;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * atom element within an expression
 */
public class CAtom<T extends ITerm> implements IExecution
{
    private final T m_value;



    public <N> CAtom( final N p_value )
    {
        //m_value = new CRawTerm<>( p_value );
        m_value = null;
    }



    @Override
    public IFuzzyValue<Boolean> execute( final IContext<?> p_context, final List<ITerm> p_annotation, final List<ITerm> p_argument,
                                         final List<ITerm> p_return
    )
    {
        //CCommon.replaceVariableFromContext( p_context, m_value )

        return CBoolean.from( true );
    }

    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return 0;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final Set<IVariable<?>> getVariables()
    {
        return this.getVariables( m_value );
    }

    private final Set<IVariable<?>> getVariables( final IVariable<?> p_value )
    {
        return new HashSet<IVariable<?>>()
        {{
            add( p_value );
        }};
    }

    private final Set<IVariable<?>> getVariables( final T p_value )
    {
        return Collections.<IVariable<?>>emptySet();
    }

    private final ITerm getValue( final IVariable<?> p_value )
    {
        return new CRawTerm<>( p_value.throwNotAllocated().throwValueNotAssignableTo( Number.class, Boolean.class ).getTyped() );
    }

    private final ITerm getValue( final Double p_value )
    {
        return new CRawTerm<>( p_value );
    }

    private final ITerm getValue( final Long p_value )
    {
        return new CRawTerm<>( p_value );
    }

    private final ITerm getValue( final Integer p_value )
    {
        return new CRawTerm<>( p_value );
    }

    private final ITerm getValue( final Boolean p_value )
    {
        return new CRawTerm<>( p_value );
    }

    private final ITerm getValue( final T p_value )
    {
        throw new CIllegalStateException( lightjason.common.CCommon.getLanguageString( this, "valueincorrect", m_value ) );
    }
}
