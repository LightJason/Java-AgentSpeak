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

package org.lightjason.agentspeak.language.execution.instantiable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.EAnnotation;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.IAnnotation;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;


/**
 * test annotations
 */
public final class TestCAnnotation extends IBaseTest
{
    /**
     * test hashcode & equals
     */
    @Test
    public void hascodeequals()
    {
        Assertions.assertEquals(
            EAnnotation.ATOMIC.apply( null, null ),
            EAnnotation.ATOMIC.apply( null, null )
        );

        Assertions.assertNotEquals(
            EAnnotation.ATOMIC.apply( null, null ),
            EAnnotation.PARALLEL.apply( null, null )
        );

        Assertions.assertNotEquals(
            EAnnotation.EMPTY.apply( null, null ),
            EAnnotation.ATOMIC.apply( null, null )
        );
    }

    /**
     * test empty annotation
     */
    @Test
    public void empty()
    {
        Assertions.assertNull( IAnnotation.EMPTY.value() );
        Assertions.assertNull( IAnnotation.EMPTY.throwvaluenotassignableto( Object.class ) );
        Assertions.assertTrue( IAnnotation.EMPTY.valueassignableto( String.class ) );
        Assertions.assertTrue( IAnnotation.EMPTY.valueassignableto( Number.class ) );
        Assertions.assertTrue( IAnnotation.EMPTY.valueassignableto( Object.class ) );
    }

    /**
     * test annotation tostring
     */
    @Test
    public void annotationtostring()
    {
        Assertions.assertEquals( "", EAnnotation.EMPTY.apply( null, null ).toString() );
        Assertions.assertEquals( "@atomic", EAnnotation.ATOMIC.apply( null, null ).toString() );
        Assertions.assertEquals( "@parallel", EAnnotation.PARALLEL.apply( null, null ).toString() );
        Assertions.assertEquals( "@tag(mytag)", EAnnotation.TAG.apply( "mytag", null ).toString() );
        Assertions.assertEquals( "@description(mydescription)", EAnnotation.DESCRIPTION.apply( "mydescription", null ).toString() );
    }

    /**
     * test annotation variables
     */
    @Test
    public void variables()
    {
        Assertions.assertEquals( 0, EAnnotation.EMPTY.apply( null, null ).variables().count() );
        Assertions.assertEquals( 0, EAnnotation.ATOMIC.apply( null, null ).variables().count() );
        Assertions.assertEquals( 0, EAnnotation.PARALLEL.apply( null, null ).variables().count() );
        Assertions.assertEquals( 0, EAnnotation.TAG.apply( null, null ).variables().count() );
        Assertions.assertEquals( 0, EAnnotation.DESCRIPTION.apply( null, null ).variables().count() );
    }

    /**
     * test constant annotation
     */
    @Test
    public void constant()
    {
        Assertions.assertTrue( EAnnotation.CONSTANT.apply( "CN", 1 ).valueassignableto( Number.class ) );
        Assertions.assertFalse( EAnnotation.CONSTANT.apply( "CS", "eins" ).valueassignableto( Number.class ) );
        Assertions.assertEquals( Integer.valueOf( 2 ), EAnnotation.CONSTANT.apply( "CN", 2 ).value() );
        Assertions.assertEquals( "@constant(FOO, bar)", EAnnotation.CONSTANT.apply( "FOO", "bar" ).toString() );

        final IVariable<Object> l_const = EAnnotation.CONSTANT.apply( "CN", "xy" ).variables().findFirst().get().term();
        Assertions.assertEquals( CPath.of( "CN" ), l_const.fqnfunctor() );

        Assertions.assertThrows( IllegalStateException.class, () -> l_const.set( "zz" ) );
    }

}
