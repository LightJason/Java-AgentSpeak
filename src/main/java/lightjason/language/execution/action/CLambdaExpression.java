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

package lightjason.language.execution.action;

import lightjason.agent.IAgent;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;


/**
 * lambda expression definition
 *
 * @bug incomplete
 */
public final class CLambdaExpression extends IBaseExecution<IVariable<?>>
{
    /**
     * initialization expression
     */
    private final IExecution m_initialize;
    /**
     * execution body
     */
    private final IExecution m_body;
    /**
     * flag of parallel execution
     */
    private boolean m_parallel;


    /**
     * ctor
     *
     * @param p_parallel parallel execution flag
     * @param p_initialize expression
     * @param p_variable iteration variable
     * @param p_body execution body
     */
    public CLambdaExpression( final boolean p_parallel, final IExecution p_initialize, final IVariable<?> p_variable, final IExecution p_body )
    {
        super( p_variable );
        m_parallel = p_parallel;
        m_initialize = p_initialize;
        m_body = p_body;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // execute initialization
        // stream over the resulting list
        // reallocate variables
        // create new context
        // run inner execution

        return CBoolean.from( true );
    }

    @Override
    public final int hashCode()
    {
        return m_initialize.hashCode() + m_value.hashCode() + m_body.hashCode() + ( m_parallel ? 9931 : 0 );
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }


    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return 0;
    }

    @Override
    public final Set<IVariable<?>> getVariables()
    {
        return m_initialize.getVariables();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}({1}) -> {2} | {3}", m_parallel ? "@" : "", m_initialize, m_value, m_body );
    }
}
