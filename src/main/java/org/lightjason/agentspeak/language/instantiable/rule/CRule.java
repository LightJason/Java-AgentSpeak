/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.instantiable.rule;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.action.achievement_test.CAchievementRuleLiteral;
import org.lightjason.agentspeak.language.execution.annotation.IAnnotation;
import org.lightjason.agentspeak.language.instantiable.IBaseInstantiable;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * rule structure
 */
public final class CRule extends IBaseInstantiable implements IRule
{
    /**
     * identifier of the rule
     */
    private final ILiteral m_id;

    /**
     * ctor
     *
     * @param p_id literal with signature
     * @param p_action action list
     */
    public CRule( final ILiteral p_id, final List<IExecution> p_action )
    {
        super(
            p_action,
            Collections.<IAnnotation<?>>emptySet(),
            p_id.hashCode()
            + p_action.stream().mapToInt( Object::hashCode ).sum()
        );
        m_id = p_id;
    }

    @Override
    public final ILiteral identifier()
    {
        return m_id;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IRule replaceplaceholder( final Multimap<IPath, IRule> p_rules )
    {
        return new CRule(
            m_id,
            m_action.stream().map( i ->
                                       i instanceof CRulePlaceholder
                                       // create a full deep-copy of the literal for avoid indeterminisitic
                                       // behaviour on rule unification
                                       ? new CAchievementRuleLiteral( (ILiteral) ( (CRulePlaceholder) i ).identifier().deepcopy() )
                                       : i
            ).collect( Collectors.toList() )
        );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final Stream<IVariable<?>> variables()
    {
        return (Stream<IVariable<?>>) Stream.of(
            CCommon.recursiveterm( m_id.orderedvalues() ).filter( i -> i instanceof IVariable<?> ).map( i -> (IVariable<?>) i ),
            super.variables()
        )
                                            .reduce( Stream::concat )
                                            .orElseGet( Stream::<IVariable<?>>empty );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format(
            "{0} ({1} ==>> {2})",
            super.toString(),
            m_id,
            m_action
        );
    }

}
