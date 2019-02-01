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

package org.lightjason.agentspeak.language.fuzzy.bundle;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CNoSuchElementException;
import org.lightjason.agentspeak.language.fuzzy.defuzzyfication.CCOG;
import org.lightjason.agentspeak.language.fuzzy.membership.CCrisp;
import org.lightjason.agentspeak.language.fuzzy.membership.IFuzzyMembership;
import org.lightjason.agentspeak.language.fuzzy.set.ECrisp;

/**
 * fuzzy bundles factory
 */
public enum EFuzzyBundleFactory implements IFuzzyBundleFactory
{
    CRISP;

    @Override
    public IFuzzyBundle get()
    {
        switch ( this )
        {
            case CRISP:
                final IFuzzyMembership<ECrisp> l_membership = new CCrisp<>( ECrisp.class );
                return new CFuzzyBundle(
                        null,
                        l_membership,
                        new CCOG<ECrisp>( ECrisp.class, l_membership, ECrisp.TRUE.apply( 1 ) )
                );

            default:
                throw new CNoSuchElementException( CCommon.languagestring( this, "notfound", this ) );
        }
    }

}
