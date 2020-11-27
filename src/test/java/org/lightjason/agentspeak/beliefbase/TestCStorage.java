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

package org.lightjason.agentspeak.beliefbase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.beliefbase.storage.CClassStorage;
import org.lightjason.agentspeak.beliefbase.storage.CMultiStorage;
import org.lightjason.agentspeak.beliefbase.storage.CSingleOnlyStorage;
import org.lightjason.agentspeak.beliefbase.storage.CSingleStorage;
import org.lightjason.agentspeak.beliefbase.storage.IStorage;
import org.lightjason.agentspeak.beliefbase.view.CView;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nullable;
import java.util.stream.Stream;


/**
 * test storage
 */
public final class TestCStorage extends IBaseTest
{
    /**
     * test single-storage-only
     */
    @Test
    public void singlestorageonly()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleOnlyStorage<>();

        final ILiteral l_literal = CLiteral.of( "xxx" );
        Assertions.assertFalse( l_storage.putmulti( l_literal.functor(), l_literal ) );

        final IView l_view = new CView( "foobar", IBeliefbase.EMPY );
        Assertions.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );


        Assertions.assertEquals( 0, l_storage.size() );
        Assertions.assertEquals( l_storage.size(), l_storage.streammulti().count() );
        Assertions.assertEquals( 0, l_storage.streammulti().count() );
        Assertions.assertEquals( 1, l_storage.streamsingle().count() );
        Assertions.assertTrue( l_storage.multi( l_literal.functor() ).isEmpty() );
    }

    /**
     * test single-storage-only clear & empty
     */
    @Test
    public void singlestorageonlyclearempty()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleOnlyStorage<>();
        Assertions.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assertions.assertFalse( l_storage.putmulti( l_literal.functor(), l_literal ) );
        Assertions.assertTrue( l_storage.isempty() );

        final IView l_view = new CView( "bar", IBeliefbase.EMPY );
        Assertions.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );
        Assertions.assertFalse( l_storage.isempty() );

        Assertions.assertTrue( l_storage.clear().isempty() );
    }

    /**
     * test single-storage-only remove, contains & get
     */
    @Test
    public void singlestorageonlyaccess()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleOnlyStorage<>();
        Assertions.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assertions.assertFalse( l_storage.putmulti( l_literal.functor(), l_literal ) );
        Assertions.assertTrue( l_storage.multi( l_literal.functor() ).isEmpty() );

        final IView l_view = new CView( "xbar", IBeliefbase.EMPY );
        Assertions.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );
        Assertions.assertEquals( l_view, l_storage.single( l_view.name() ) );

        Assertions.assertTrue( l_storage.containssingle( l_view.name() ) );
        Assertions.assertFalse( l_storage.containsmulti( l_literal.functor() ) );

        Assertions.assertTrue( l_storage.removesingle( l_view.name() ) );
        Assertions.assertTrue( l_storage.isempty() );
        Assertions.assertFalse( l_storage.removemulti( l_literal.functor(), l_literal ) );
    }



    /**
     * test single-storage
     */
    @Test
    public void singlestorage()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleStorage<>();

        final ILiteral l_literal = CLiteral.of( "xxx" );
        Assertions.assertTrue( l_storage.putmulti( l_literal.functor(), l_literal ) );

        final IView l_view = new CView( "foobar", IBeliefbase.EMPY );
        Assertions.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );


        Assertions.assertEquals( 1, l_storage.size() );
        Assertions.assertEquals( l_storage.size(), l_storage.streammulti().count() );
        Assertions.assertEquals( 1, l_storage.streammulti().count() );
        Assertions.assertEquals( 1, l_storage.streamsingle().count() );
        Assertions.assertFalse( l_storage.multi( l_literal.functor() ).isEmpty() );
    }

    /**
     * test single-storage clear & empty
     */
    @Test
    public void singlestorageclearempty()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleStorage<>();
        Assertions.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        l_storage.putmulti( l_literal.functor(), l_literal );
        Assertions.assertFalse( l_storage.isempty() );

        final IView l_view = new CView( "bar", IBeliefbase.EMPY );
        l_storage.putsingle( l_view.name(), l_view );
        Assertions.assertFalse( l_storage.isempty() );

        Assertions.assertTrue( l_storage.clear().isempty() );
    }

    /**
     * test single-storage-only remove, contains & get
     */
    @Test
    public void singlestorageaccess()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleStorage<>();
        Assertions.assertTrue( l_storage.isempty() );

        final ILiteral l_literal1 = CLiteral.of( "abc" );
        Assertions.assertTrue( l_storage.putmulti( l_literal1.functor(), l_literal1 ) );

        final ILiteral l_literal2 = CLiteral.of( "abc", CRawTerm.of( 123 ) );
        Assertions.assertTrue( l_storage.putmulti( l_literal2.functor(), l_literal2 ) );

        Assertions.assertFalse( l_storage.multi( l_literal1.functor() ).isEmpty() );

        final IView l_view = new CView( "xbar", IBeliefbase.EMPY );
        l_storage.putsingle( l_view.name(), l_view );
        Assertions.assertEquals( l_view, l_storage.single( l_view.name() ) );

        Assertions.assertTrue( l_storage.containssingle( l_view.name() ) );
        Assertions.assertTrue( l_storage.containsmulti( l_literal1.functor() ) );

        Assertions.assertTrue( l_storage.removesingle( l_view.name() ) );
        Assertions.assertFalse( l_storage.isempty() );

        Assertions.assertTrue( l_storage.containsmulti( l_literal1.functor() ) );
        Assertions.assertEquals( l_literal2, l_storage.streammulti().findFirst().get() );
        Assertions.assertFalse( l_storage.removemulti( l_literal1.functor(), l_literal1 ) );
        Assertions.assertTrue( l_storage.removemulti( l_literal2.functor(), l_literal2 ) );

        Assertions.assertEquals( 0, l_storage.streammulti().count() );

        Assertions.assertEquals( l_view, l_storage.singleordefault( l_view.name(), l_view ) );
    }



    /**
     * test multi-storage
     */
    @Test
    public void multistorage()
    {
        final IStorage<ILiteral, IView> l_storage = new CMultiStorage<>();

        final ILiteral l_literal = CLiteral.of( "xxx" );
        Assertions.assertTrue( l_storage.putmulti( l_literal.functor(), l_literal ) );

        final IView l_view = new CView( "foobar", IBeliefbase.EMPY );
        Assertions.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );


        Assertions.assertEquals( 1, l_storage.size() );
        Assertions.assertEquals( l_storage.size(), l_storage.streammulti().count() );
        Assertions.assertEquals( 1, l_storage.streammulti().count() );
        Assertions.assertEquals( 1, l_storage.streamsingle().count() );
        Assertions.assertFalse( l_storage.multi( l_literal.functor() ).isEmpty() );
        Assertions.assertNotNull( l_storage.single( l_view.name() ) );
    }

    /**
     * test multi-storage clear & empty
     */
    @Test
    public void multistorageclearempty()
    {
        final IStorage<ILiteral, IView> l_storage = new CMultiStorage<>();
        Assertions.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assertions.assertTrue( l_storage.putmulti( l_literal.functor(), l_literal ) );
        Assertions.assertFalse( l_storage.isempty() );

        final IView l_view = new CView( "bar", IBeliefbase.EMPY );
        Assertions.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );
        Assertions.assertFalse( l_storage.isempty() );

        Assertions.assertTrue( l_storage.clear().isempty() );
    }

    /**
     * test multi-storage remove, contains & get
     */
    @Test
    public void multistorageaccess()
    {
        final IStorage<ILiteral, IView> l_storage = new CMultiStorage<>();
        Assertions.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assertions.assertTrue( l_storage.putmulti( l_literal.functor(), l_literal ) );
        Assertions.assertFalse( l_storage.multi( l_literal.functor() ).isEmpty() );

        final IView l_view = new CView( "xbar", IBeliefbase.EMPY );
        Assertions.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );
        Assertions.assertEquals( l_view, l_storage.single( l_view.name() ) );

        Assertions.assertTrue( l_storage.containssingle( l_view.name() ) );
        Assertions.assertTrue( l_storage.containsmulti( l_literal.functor() ) );

        Assertions.assertTrue( l_storage.removesingle( l_view.name() ) );
        Assertions.assertFalse( l_storage.isempty() );
        Assertions.assertTrue( l_storage.removemulti( l_literal.functor(), l_literal ) );
        Assertions.assertTrue( l_storage.isempty() );
    }


    /**
     * test class storage
     */
    @Test
    public void classstorage()
    {
        final String l_numbername = "number";
        final String l_stringname = "string";

        final CTestStorageClass l_classdata = new CTestStorageClass( 123.45, "a long text", 555 );
        final IStorage<ILiteral, IView> l_storage = new CClassStorage<>( l_classdata, i -> i.getName().replace( "m_", "" ) );

        Assertions.assertFalse( l_storage.isempty() );
        Assertions.assertEquals( 2, l_storage.size() );
        Assertions.assertArrayEquals(
            Stream.of( CLiteral.of( l_numbername, CRawTerm.of( 123.45 ) ), CLiteral.of( l_stringname, CRawTerm.of( "a long text" ) ) ).toArray(),
            l_storage.streammulti().toArray()
        );

        Assertions.assertFalse( l_storage.putmulti( l_numbername, CLiteral.of( l_numbername, CRawTerm.of( 0 ) ) ) );
        Assertions.assertTrue( l_storage.putmulti( l_stringname, CLiteral.of( l_stringname, CRawTerm.of( "shorttext" ) ) ) );
        Assertions.assertNotEquals( 0, l_classdata.number() );
        Assertions.assertEquals( "shorttext", l_classdata.string() );

        Assertions.assertTrue( l_storage.containsmulti( l_numbername ) );
        Assertions.assertFalse( l_storage.multi( l_stringname ).isEmpty() );
        Assertions.assertFalse( l_storage.containssingle( "any" ) );

        final IView l_view = new CView( "xyz", IBeliefbase.EMPY );
        Assertions.assertFalse( l_storage.putsingle( l_view.name(), l_view ) );

        Assertions.assertFalse( l_storage.removesingle( l_view.name() ) );
        Assertions.assertFalse( l_storage.removemulti( l_numbername, CLiteral.of( l_numbername, CRawTerm.of( 0 ) ) ) );

        Assertions.assertFalse( l_storage.clear().isempty() );
    }


    /**
     * test storage class
     */
    private static final class CTestStorageClass
    {
        /**
         * unchangable value
         */
        private final Number m_number;
        /**
         * changeable value
         */
        private String m_string;
        /**
         * not-shown value
         */
        private transient int m_notshown;

        /**
         * ctor
         *
         * @param p_number number
         * @param p_string string
         * @param p_notshown not-shown
         */
        CTestStorageClass( @Nullable final Number p_number, @Nullable final String p_string, final int p_notshown )
        {
            m_number = p_number;
            m_string = p_string;
            m_notshown = p_notshown;
        }

        /**
         * returns number
         *
         * @return number
         */
        public Number number()
        {
            return m_number;
        }

        /**
         * return string value
         *
         * @return string
         */
        public String string()
        {
            return m_string;
        }

        /**
         * return not-shown value
         *
         * @return not shown
         */
        public int notshown()
        {
            return m_notshown;
        }
    }

}
