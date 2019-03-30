/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.error;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.common.CCommon;

import javax.annotation.Nonnull;
import java.util.logging.Logger;


/**
 * enum-constant-not-present exception
 */
public final class CEnumConstantNotPresentException extends EnumConstantNotPresentException implements IException
{
    /**
     * logger
     */
    private static final Logger LOGGER = CCommon.logger( CEnumConstantNotPresentException.class );
    /**
     * serial uid
     */
    private static final long serialVersionUID = -2627223365911300936L;

    /**
     * ctor
     *
     * @param p_enum enum type
     * @param p_missing missing enum type
     */
    public CEnumConstantNotPresentException( @Nonnull final Class<? extends Enum<?>> p_enum, @NonNull final String p_missing )
    {
        super( p_enum, p_missing );
        LOGGER.warning( this.getMessage() );
    }
}
