/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.unify;

import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.error.CNoSuchElementException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * unifier action
 *
 * @note parallel is ignored
 */
public class CDefaultUnify extends IBaseExecution<ILiteral>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2858968840990978860L;
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
    public CDefaultUnify( final boolean p_parallel,  @Nonnull final ILiteral p_literal )
    {
        super( p_literal );
        m_parallel = p_parallel;

        // check unique variables - get variable frequency especially any variable "_"
        final Map<IVariable<?>, Integer> l_frequency = CCommon.variablefrequency( p_literal );
        if ( l_frequency.isEmpty() )
            throw new CNoSuchElementException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "novariable" ) );

        if ( l_frequency.entrySet().stream().filter( i -> !i.getKey().any() ).anyMatch( i -> i.getValue() > 1 ) )
            throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "duplicatedvariable" ) );

        // count variables
        m_variablenumber = l_frequency.size();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}>>{1}", m_parallel ? "@" : "", m_value );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        return p_context.agent().unifier().unify(
            p_context,
            m_value,
            m_variablenumber,
            IExpression.EMPTY,
            m_parallel
        );
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return CCommon.flattenrecursive( m_value.values() ).filter( i -> i instanceof IVariable<?> ).map( ITerm::term );
    }

}
