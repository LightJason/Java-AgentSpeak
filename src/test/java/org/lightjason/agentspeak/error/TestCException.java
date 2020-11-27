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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * test exception
 */
public final class TestCException
{

    /**
     * test no-such-element exception
     */
    @Test
    public void nosuchelementexception()
    {
        Assertions.assertThrows(
            CNoSuchElementException.class,
            () ->
            {
                throw new CNoSuchElementException();
            }
        );
    }

    /**
     * test enum-not-present exception
     */
    @Test
    public void enumnotpresentstring()
    {
        Assertions.assertThrows(
            CEnumConstantNotPresentException.class,
            () ->
            {
                throw new CEnumConstantNotPresentException( ETestEnum.class, "MISSING" );
            }
        );
    }

    /**
     * test illegal-argument exception
     */
    @Test
    public void illegalargument()
    {
        Assertions.assertThrows(
            CIllegalArgumentException.class,
            () ->
            {
                throw new CIllegalArgumentException();
            }
        );
    }

    /**
     * test illegal-argument exception
     */
    @Test
    public void illegalargumentstring()
    {
        Assertions.assertThrows(
            CIllegalArgumentException.class,
            () ->
            {
                throw new CIllegalArgumentException( "illegealargument" );
            }
        );
    }

    /**
     * test illegal-argument exception
     */
    @Test
    public void illegalargumentstringexception()
    {
        Assertions.assertThrows(
            CIllegalArgumentException.class,
            () ->
            {
                throw new CIllegalArgumentException( "illegealargument", new RuntimeException() );
            }
        );
    }

    /**
     * test illegal-argument exception
     */
    @Test
    public void illegalargumentexception()
    {
        Assertions.assertThrows(
            CIllegalArgumentException.class,
            () ->
            {
                throw new CIllegalArgumentException( new RuntimeException() );
            }
        );
    }

    /**
     * test unmodifyable-exception
     */
    @Test
    public void unmodifyableexception()
    {
        Assertions.assertThrows(
            CUnmodifiableException.class,
            () ->
            {
                throw new CUnmodifiableException();
            }
        );
    }

    /**
     * test unmodifyable-exception
     */
    @Test
    public void unmodifyableexceptionstringexception()
    {
        Assertions.assertThrows(
            CUnmodifiableException.class,
            () ->
            {
                throw new CUnmodifiableException( "unmodifyable", new RuntimeException() );
            }
        );
    }

    /**
     * test unmodifyable-exception
     */
    @Test
    public void unmodifyableexceptionpassexception()
    {
        Assertions.assertThrows(
            CUnmodifiableException.class,
            () ->
            {
                throw new CUnmodifiableException( new RuntimeException() );
            }
        );
    }

    /**
     * test illegal-state-exception
     */
    @Test
    public void illegalstateexception()
    {
        Assertions.assertThrows(
            CIllegalStateException.class,
            () ->
            {
                throw new CIllegalStateException();
            }
        );
    }

    /**
     * test illegal-state-exception
     */
    @Test
    public void illegalstateexceptionstring()
    {
        Assertions.assertThrows(
            CIllegalStateException.class,
            () ->
            {
                throw new CIllegalStateException( "illegal-state" );
            }
        );
    }

    /**
     * test illegal-state-exception
     */
    @Test
    public void illegalstateexceptionpassexception()
    {
        Assertions.assertThrows(
            CIllegalStateException.class,
            () ->
            {
                throw new CIllegalStateException( new RuntimeException() );
            }
        );
    }


    /**
     * test illegal-state-exception
     */
    @Test
    public void illegalstateexceptionstringexception()
    {
        Assertions.assertThrows(
            CIllegalStateException.class,
            () ->
            {
                throw new CIllegalStateException( "illegal-state", new RuntimeException() );
            }
        );
    }

    /**
     * test enum
     */
    private enum ETestEnum
    {
        FOO;
    }
}
