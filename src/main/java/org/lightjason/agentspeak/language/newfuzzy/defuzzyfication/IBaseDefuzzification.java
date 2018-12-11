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

package org.lightjason.agentspeak.language.newfuzzy.defuzzyfication;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.newfuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;


/**
 * abstract class of defuzzification
 *
 * @tparam E fuzzy set enum type
 */
public abstract class IBaseDefuzzification<E extends Enum<?>> implements IDefuzzification<E>
{
    /**
     * default value on empty input
     */
    protected final E m_default;
    /**
     * ctor
     *
     * fuzzy class fuzzy set class
     */
    protected final Class<? extends IFuzzySet<E>> m_class;
    /**
     * function to define successful execution
     */
    private final BiFunction<E, Class<? extends IFuzzySet<E>>, Boolean> m_success;



    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_default fuzzy enum type
     */
    protected IBaseDefuzzification( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final E p_default )
    {
        this( p_class, p_default, ( i, j ) -> i.ordinal() < j.getEnumConstants().length / 2 );
    }


    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_default fuzzy enum type
     * @param p_success class of success execution
     */
    protected IBaseDefuzzification( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final E p_default,
                                    @NonNull final BiFunction<E, Class<? extends IFuzzySet<E>>, Boolean> p_success )
    {
        m_class = p_class;
        m_default = p_default;
        m_success = p_success;
    }

    /**
     * returns the enum values based on an index
     *
     * @param p_index index
     * @return enum value
     */
    protected final E index2enum( int p_index )
    {
        return m_class.getEnumConstants()[p_index].get();
    }

    @Override
    public final boolean success( @NonNull final E p_value )
    {
        return m_success.apply( p_value, m_class );
    }

    @Nonnull
    @Override
    public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return p_agent;
    }
}
