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
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.membership.IFuzzyMembership;
import org.lightjason.agentspeak.language.newfuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.OptionalDouble;


/**
 * abstract class of defuzzification base structure
 *
 * @tparam E fuzzy set enum type
 */
public abstract class IBaseDefuzzification<E extends Enum<?>> implements IDefuzzification
{
    /**
     * default fuzzy value for numeric value
     */
    protected final IFuzzyValue<E> m_default;
    /**
     * fuzzy membership function
     */
    protected final IFuzzyMembership<E> m_membership;
    /**
     * fuzzy class fuzzy set class
     */
    protected final Class<? extends IFuzzySet<E>> m_class;



    /**
     * ctor
     *  @param p_class fuzzy set class
     * @param p_membership membership function
     * @param p_default default fuzzy value
     */
    protected IBaseDefuzzification( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final IFuzzyMembership<E> p_membership,
                                    @NonNull final IFuzzyValue<E> p_default
    )
    {
        m_class = p_class;
        m_default = p_default;
        m_membership = p_membership;
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

    /**
     * returns the maximum of all memberships
     *
     * @return optional membership maximum value
     */
    protected final OptionalDouble maximum()
    {
        return Arrays.stream( m_class.getEnumConstants() )
                                   .mapToDouble( i -> m_membership.range( this.index2enum( i.get().ordinal() ) )
                                                                  .mapToDouble( Number::doubleValue )
                                                                  .max()
                                                                  .orElse( 0 ) )
                                   .max();
    }

    @Nonnull
    @Override
    public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return p_agent;
    }
}
