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

package lightjason.language.instantiable.rule;

import lightjason.agent.IAgent;
import lightjason.language.CCommon;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IVariableBuilder;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;


/**
 * placeholder rule to define correct rule referencing
 *
 * @note rules are the first executable elements which are parsed,
 * so if a rule calls itself (recursive) the reference does not exists,
 * with this class a placeholder is used first and after that we replace
 * the placeholder with the correct rule object
 */
public final class CRulePlaceholder implements IRule
{
    /**
     * identifier of the rule
     */
    protected final ILiteral m_id;

    /**
     * ctor
     *
     * @param p_id rule literal
     */
    public CRulePlaceholder( final ILiteral p_id )
    {
        m_id = p_id;
    }


    @Override
    public final ILiteral getIdentifier()
    {
        return m_id;
    }

    @Override
    public final IRule replaceplaceholder( final Map<ILiteral, IRule> p_rules )
    {
        return this;
    }

    @Override
    public final IContext getContext( final IAgent p_agent, final IAggregation p_aggregation, final IVariableBuilder p_variablebuilder,
                                      final Set<IVariable<?>> p_variables
    )
    {
        return CCommon.getContext( this, p_agent, p_aggregation, p_variablebuilder, p_variables );
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                         final List<ITerm> p_annotation
    )
    {
        return CFuzzyValue.from( false );
    }

    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return 0;
    }

    @Override
    public final Stream<? extends IVariable<?>> getVariables()
    {
        return Stream.<IVariable<?>>empty();
    }

    @Override
    public final int hashCode()
    {
        return m_id.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return m_id.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format(
                "{0} ({1} ==>> ?)",
                super.toString(),
                m_id
        );
    }

}
