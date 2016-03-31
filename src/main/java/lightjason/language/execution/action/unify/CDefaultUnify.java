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

package lightjason.language.execution.action.unify;

import lightjason.error.CIllegalArgumentException;
import lightjason.language.CCommon;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.action.IBaseExecution;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * unify action
 */
public class CDefaultUnify extends IBaseExecution<ILiteral>
{
    /**
     * parallel unification
     */
    protected final boolean m_parallel;
    /**
     * number of variables
     */
    protected final long m_variablenumber;

    /**
     * ctor
     *
     * @param p_parallel parallel execution
     * @param p_literal literal
     */
    public CDefaultUnify( final boolean p_parallel, final ILiteral p_literal )
    {
        super( p_literal );
        m_parallel = p_parallel;

        // check unique variables - get variable frequency especially any variable "_"
        final Map<IVariable<?>, Integer> l_frequency = CCommon.getVariableFrequency( p_literal );
        if ( l_frequency.isEmpty() )
            throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( this, "novariable" ) );

        if ( l_frequency.entrySet().stream().filter( i -> !i.getKey().isAny() ).filter( i -> i.getValue() > 1 ).findAny().isPresent() )
            throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( this, "uniquevariable" ) );

        // count variables
        m_variablenumber = l_frequency.size();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}>>{1}", m_parallel ? "@" : "", m_value );
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                         final List<ITerm> p_annotation
    )
    {
        return m_parallel
               ? p_context.getAgent().getUnifier().parallelunify( p_context, m_value, m_variablenumber )
               : p_context.getAgent().getUnifier().sequentialunify( p_context, m_value, m_variablenumber );
    }

    @Override
    @SuppressWarnings( "serial" )
    public Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{
            // create a shallow-copy of all variables within the value- and annotation-definition
            CCommon.recursiveterm( m_value.values() ).filter( i -> i instanceof IVariable<?> ).forEach( i -> add( ( (IVariable<?>) i ).shallowcopy() ) );
            CCommon.recursiveliteral( m_value.annotations() ).filter( i -> i instanceof IVariable<?> ).forEach(
                    i -> add( ( (IVariable<?>) i ).shallowcopy() ) );

        }};
    }
}
