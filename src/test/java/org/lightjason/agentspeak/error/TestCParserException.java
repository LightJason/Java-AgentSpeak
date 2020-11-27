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
import org.lightjason.agentspeak.error.parser.CParserCharacterException;
import org.lightjason.agentspeak.error.parser.CParserInitializationError;
import org.lightjason.agentspeak.error.parser.CParserSyntaxException;


/**
 * test parser exception
 */
public final class TestCParserException
{

    /**
     * test parser-syntax exception
     */
    @Test
    public void parsersyntaxexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CParserSyntaxException();
            }
        );
    }

    /**
     * test parser-syntax exception
     */
    @Test
    public void parsersyntaxexceptionstring()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CParserSyntaxException( "syntax" );
            }
        );
    }

    /**
     * test parser-syntax exception
     */
    @Test
    public void parsersyntaxexceptionpassexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CParserSyntaxException( new RuntimeException() );
            }
        );
    }

    /**
     * test parser-syntax exception
     */
    @Test
    public void parsersyntaxexceptionstringexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CParserSyntaxException( "syntax", new RuntimeException() );
            }
        );
    }



    /**
     * test parser-character-syntax exception
     */
    @Test
    public void parsercharactersyntaxexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CParserCharacterException();
            }
        );
    }

    /**
     * test parser-character-syntax exception
     */
    @Test
    public void parsercharactersyntaxexceptionstring()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CParserCharacterException( "char" );
            }
        );
    }

    /**
     * test parser-character-syntax exception
     */
    @Test
    public void parsercharactersyntaxexceptionpassexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CParserCharacterException( new RuntimeException() );
            }
        );
    }

    /**
     * test parser-character-syntax exception
     */
    @Test
    public void parsercharactersyntaxexceptionstringexception()
    {
        Assertions.assertThrows(
            IllegalStateException.class,
            () ->
            {
                throw new CParserCharacterException( "char", new RuntimeException() );
            }
        );
    }



    /**
     * test parser-initialization exception
     */
    @Test
    public void parserinitialization()
    {
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
            {
                throw new CParserInitializationError();
            }
        );
    }

    /**
     * test parser-initialization exception
     */
    @Test
    public void parserinitializationstring()
    {
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
            {
                throw new CParserInitializationError( "init" );
            }
        );
    }

    /**
     * test parser-initialization exception
     */
    @Test
    public void parserinitializationexception()
    {
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
            {
                throw new CParserInitializationError( new RuntimeException() );
            }
        );
    }

    /**
     * test parser-initialization exception
     */
    @Test
    public void parserinitializationstringexception()
    {
        Assertions.assertThrows(
            RuntimeException.class,
            () ->
            {
                throw new CParserInitializationError( "init", new RuntimeException() );
            }
        );
    }
}
