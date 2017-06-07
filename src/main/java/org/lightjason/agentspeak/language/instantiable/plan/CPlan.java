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

package org.lightjason.agentspeak.language.instantiable.plan;

import org.apache.commons.lang3.StringUtils;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.instantiable.plan.annotation.IAnnotation;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.IBaseInstantiable;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


/**
 * plan structure
 */
public final class CPlan extends IBaseInstantiable implements IPlan
{
    /**
     * trigger event
     */
    private final ITrigger m_triggerevent;
    /**
     * execution condition / expression
     */
    private final IExpression m_condition;


    /**
     * ctor
     *
     * @param p_event trigger event
     * @param p_body plan body
     * @param p_annotation annotations
     */
    public CPlan( final ITrigger p_event, final List<IExecution> p_body, final Set<IAnnotation<?>> p_annotation )
    {
        this( p_event, null, p_body, p_annotation );
    }

    /**
     * ctor
     *
     * @param p_event trigger event
     * @param p_condition execution condition
     * @param p_body plan body
     * @param p_annotation annotations
     */
    public CPlan( final ITrigger p_event, final IExpression p_condition, final List<IExecution> p_body, final Set<IAnnotation<?>> p_annotation
    )
    {
        super(
            p_body,
            p_annotation,
            p_event.hashCode()
            + ( p_condition == null ? 0 : p_condition.hashCode() )
            + p_body.stream().mapToInt( Object::hashCode ).sum()
            + p_annotation.stream().mapToInt( Object::hashCode ).sum()
        );


        m_triggerevent = p_event;
        m_condition = p_condition;
    }

    @Override
    public final ITrigger trigger()
    {
        return m_triggerevent;
    }

    @Override
    public final Collection<IAnnotation<?>> annotations()
    {
        return m_annotation.values();
    }

    @Override
    public final List<IExecution> body()
    {
        return m_action;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return )
    {
        final IFuzzyValue<Boolean> l_result = super.execute( p_context, p_parallel, p_argument, p_return );

        // create delete-goal trigger
        if ( !p_context.agent().fuzzy().getDefuzzyfication().defuzzify( l_result ) )
            p_context.agent().trigger( CTrigger.from( ITrigger.EType.DELETEGOAL, m_triggerevent.literal().unify( p_context ) ) );

        return l_result;
    }

    @Override
    public final IFuzzyValue<Boolean> condition( final IContext p_context )
    {
        if ( m_condition == null )
            return CFuzzyValue.from( true );

        final List<ITerm> l_return = new LinkedList<>();
        return CFuzzyValue.from(
            m_condition.execute( p_context, false, Collections.emptyList(), l_return ).value()
            && ( l_return.size() == 1 )
            ? l_return.get( 0 ).<Boolean>raw()
            : false
        );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format(
            "{0} ({1} | {2}{3} ==>> {4})",
            super.toString(),
            m_annotation.values(),
            m_triggerevent,
            m_condition == null ? "" : MessageFormat.format( " |- {0}", m_condition ),
            StringUtils.join( m_action, "; " )
        );
    }

    @Override
    public final Stream<IVariable<?>> variables()
    {
        return CCommon.streamconcat(
            m_condition != null ? m_condition.variables() : Stream.empty(),
            super.variables(),
            m_annotation.values().stream().flatMap( IAnnotation::variables ),
            CCommon.recursiveterm( m_triggerevent.literal().orderedvalues() ).filter( i -> i instanceof IVariable<?> ).map( i -> (IVariable<?>) i )
        );
    }

}
