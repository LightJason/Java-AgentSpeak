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

package lightjason.language.rule;

import lightjason.agent.IAgent;
import lightjason.common.IPath;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;

import java.util.List;
import java.util.Set;


/**
 * rule structure
 *
 * @bug incomplete
 */
public final class CRule implements IRule
{
    /**
     * name of the rule
     */
    protected final IPath m_name;
    /**
     * action list
     */
    protected final List<List<IExecution>> m_action;

    /**
     * ctor
     *
     * @param p_id literal with signature
     * @param p_action action list
     */
    public CRule( final ILiteral p_id, final List<List<IExecution>> p_action )
    {
        m_name = p_id.getFQNFunctor();
        m_action = p_action;
    }

    @Override
    public final IPath getName()
    {
        return m_name;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        return CBoolean.from( false );
    }

    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return 0;
    }

    @Override
    public final Set<IVariable<?>> getVariables()
    {
        return null;
    }

    @Override
    public final int hashCode()
    {
        return m_name.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return m_name.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return super.toString();
    }

}
