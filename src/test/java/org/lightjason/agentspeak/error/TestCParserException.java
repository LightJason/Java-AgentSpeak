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

import org.junit.Test;
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
    @Test( expected = IllegalStateException.class )
    public void parsersyntaxexception()
    {
        throw new CParserSyntaxException();
    }

    /**
     * test parser-syntax exception
     */
    @Test( expected = IllegalStateException.class )
    public void parsersyntaxexceptionstring()
    {
        throw new CParserSyntaxException( "syntax" );
    }

    /**
     * test parser-syntax exception
     */
    @Test( expected = IllegalStateException.class )
    public void parsersyntaxexceptionpassexception()
    {
        throw new CParserSyntaxException( new RuntimeException() );
    }

    /**
     * test parser-syntax exception
     */
    @Test( expected = IllegalStateException.class )
    public void parsersyntaxexceptionstringexception()
    {
        throw new CParserSyntaxException( "syntax", new RuntimeException() );
    }



    /**
     * test parser-character-syntax exception
     */
    @Test( expected = IllegalStateException.class )
    public void parsercharactersyntaxexception()
    {
        throw new CParserCharacterException();
    }

    /**
     * test parser-character-syntax exception
     */
    @Test( expected = IllegalStateException.class )
    public void parsercharactersyntaxexceptionstring()
    {
        throw new CParserCharacterException( "char" );
    }

    /**
     * test parser-character-syntax exception
     */
    @Test( expected = IllegalStateException.class )
    public void parsercharactersyntaxexceptionpassexception()
    {
        throw new CParserCharacterException( new RuntimeException() );
    }

    /**
     * test parser-character-syntax exception
     */
    @Test( expected = IllegalStateException.class )
    public void parsercharactersyntaxexceptionstringexception()
    {
        throw new CParserCharacterException( "char", new RuntimeException() );
    }



    /**
     * test parser-initialization exception
     */
    @Test( expected = RuntimeException.class )
    public void parserinitialization()
    {
        throw new CParserInitializationError();
    }

    /**
     * test parser-initialization exception
     */
    @Test( expected = RuntimeException.class )
    public void parserinitializationstring()
    {
        throw new CParserInitializationError( "init" );
    }

    /**
     * test parser-initialization exception
     */
    @Test( expected = RuntimeException.class )
    public void parserinitializationexception()
    {
        throw new CParserInitializationError( new RuntimeException() );
    }

    /**
     * test parser-initialization exception
     */
    @Test( expected = RuntimeException.class )
    public void parserinitializationstringexception()
    {
        throw new CParserInitializationError( "init", new RuntimeException() );
    }
}
