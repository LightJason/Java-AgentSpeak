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

import org.junit.Assert;
import org.junit.Test;
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
        Assert.assertFalse( l_storage.putmulti( l_literal.functor(), l_literal ) );

        final IView l_view = new CView( "foobar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );


        Assert.assertEquals( 0, l_storage.size() );
        Assert.assertEquals( l_storage.size(), l_storage.streammulti().count() );
        Assert.assertEquals( 0, l_storage.streammulti().count() );
        Assert.assertEquals( 1, l_storage.streamsingle().count() );
        Assert.assertTrue( l_storage.multi( l_literal.functor() ).isEmpty() );
    }

    /**
     * test single-storage-only clear & empty
     */
    @Test
    public void singlestorageonlyclearempty()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleOnlyStorage<>();
        Assert.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assert.assertFalse( l_storage.putmulti( l_literal.functor(), l_literal ) );
        Assert.assertTrue( l_storage.isempty() );

        final IView l_view = new CView( "bar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );
        Assert.assertFalse( l_storage.isempty() );

        Assert.assertTrue( l_storage.clear().isempty() );
    }

    /**
     * test single-storage-only remove, contains & get
     */
    @Test
    public void singlestorageonlyaccess()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleOnlyStorage<>();
        Assert.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assert.assertFalse( l_storage.putmulti( l_literal.functor(), l_literal ) );
        Assert.assertTrue( l_storage.multi( l_literal.functor() ).isEmpty() );

        final IView l_view = new CView( "xbar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );
        Assert.assertEquals( l_view, l_storage.single( l_view.name() ) );

        Assert.assertTrue( l_storage.containssingle( l_view.name() ) );
        Assert.assertFalse( l_storage.containsmulti( l_literal.functor() ) );

        Assert.assertTrue( l_storage.removesingle( l_view.name() ) );
        Assert.assertTrue( l_storage.isempty() );
        Assert.assertFalse( l_storage.removemulti( l_literal.functor(), l_literal ) );
    }



    /**
     * test single-storage
     */
    @Test
    public void singlestorage()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleStorage<>();

        final ILiteral l_literal = CLiteral.of( "xxx" );
        Assert.assertTrue( l_storage.putmulti( l_literal.functor(), l_literal ) );

        final IView l_view = new CView( "foobar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );


        Assert.assertEquals( 1, l_storage.size() );
        Assert.assertEquals( l_storage.size(), l_storage.streammulti().count() );
        Assert.assertEquals( 1, l_storage.streammulti().count() );
        Assert.assertEquals( 1, l_storage.streamsingle().count() );
        Assert.assertFalse( l_storage.multi( l_literal.functor() ).isEmpty() );
    }

    /**
     * test single-storage clear & empty
     */
    @Test
    public void singlestorageclearempty()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleStorage<>();
        Assert.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        l_storage.putmulti( l_literal.functor(), l_literal );
        Assert.assertFalse( l_storage.isempty() );

        final IView l_view = new CView( "bar", IBeliefbase.EMPY );
        l_storage.putsingle( l_view.name(), l_view );
        Assert.assertFalse( l_storage.isempty() );

        Assert.assertTrue( l_storage.clear().isempty() );
    }

    /**
     * test single-storage-only remove, contains & get
     */
    @Test
    public void singlestorageaccess()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleStorage<>();
        Assert.assertTrue( l_storage.isempty() );

        final ILiteral l_literal1 = CLiteral.of( "abc" );
        Assert.assertTrue( l_storage.putmulti( l_literal1.functor(), l_literal1 ) );

        final ILiteral l_literal2 = CLiteral.of( "abc", CRawTerm.of( 123 ) );
        Assert.assertTrue( l_storage.putmulti( l_literal2.functor(), l_literal2 ) );

        Assert.assertFalse( l_storage.multi( l_literal1.functor() ).isEmpty() );

        final IView l_view = new CView( "xbar", IBeliefbase.EMPY );
        l_storage.putsingle( l_view.name(), l_view );
        Assert.assertEquals( l_view, l_storage.single( l_view.name() ) );

        Assert.assertTrue( l_storage.containssingle( l_view.name() ) );
        Assert.assertTrue( l_storage.containsmulti( l_literal1.functor() ) );

        Assert.assertTrue( l_storage.removesingle( l_view.name() ) );
        Assert.assertFalse( l_storage.isempty() );

        Assert.assertTrue( l_storage.containsmulti( l_literal1.functor() ) );
        Assert.assertEquals( l_literal2, l_storage.streammulti().findFirst().get() );
        Assert.assertFalse( l_storage.removemulti( l_literal1.functor(), l_literal1 ) );
        Assert.assertTrue( l_storage.removemulti( l_literal2.functor(), l_literal2 ) );

        Assert.assertEquals( 0, l_storage.streammulti().count() );

        Assert.assertEquals( l_view, l_storage.singleordefault( l_view.name(), l_view ) );
    }



    /**
     * test multi-storage
     */
    @Test
    public void multistorage()
    {
        final IStorage<ILiteral, IView> l_storage = new CMultiStorage<>();

        final ILiteral l_literal = CLiteral.of( "xxx" );
        Assert.assertTrue( l_storage.putmulti( l_literal.functor(), l_literal ) );

        final IView l_view = new CView( "foobar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );


        Assert.assertEquals( 1, l_storage.size() );
        Assert.assertEquals( l_storage.size(), l_storage.streammulti().count() );
        Assert.assertEquals( 1, l_storage.streammulti().count() );
        Assert.assertEquals( 1, l_storage.streamsingle().count() );
        Assert.assertFalse( l_storage.multi( l_literal.functor() ).isEmpty() );
        Assert.assertNotNull( l_storage.single( l_view.name() ) );
    }

    /**
     * test multi-storage clear & empty
     */
    @Test
    public void multistorageclearempty()
    {
        final IStorage<ILiteral, IView> l_storage = new CMultiStorage<>();
        Assert.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assert.assertTrue( l_storage.putmulti( l_literal.functor(), l_literal ) );
        Assert.assertFalse( l_storage.isempty() );

        final IView l_view = new CView( "bar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );
        Assert.assertFalse( l_storage.isempty() );

        Assert.assertTrue( l_storage.clear().isempty() );
    }

    /**
     * test multi-storage remove, contains & get
     */
    @Test
    public void multistorageaccess()
    {
        final IStorage<ILiteral, IView> l_storage = new CMultiStorage<>();
        Assert.assertTrue( l_storage.isempty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assert.assertTrue( l_storage.putmulti( l_literal.functor(), l_literal ) );
        Assert.assertFalse( l_storage.multi( l_literal.functor() ).isEmpty() );

        final IView l_view = new CView( "xbar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putsingle( l_view.name(), l_view ) );
        Assert.assertEquals( l_view, l_storage.single( l_view.name() ) );

        Assert.assertTrue( l_storage.containssingle( l_view.name() ) );
        Assert.assertTrue( l_storage.containsmulti( l_literal.functor() ) );

        Assert.assertTrue( l_storage.removesingle( l_view.name() ) );
        Assert.assertFalse( l_storage.isempty() );
        Assert.assertTrue( l_storage.removemulti( l_literal.functor(), l_literal ) );
        Assert.assertTrue( l_storage.isempty() );
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

        Assert.assertFalse( l_storage.isempty() );
        Assert.assertEquals( 2, l_storage.size() );
        Assert.assertArrayEquals(
            Stream.of( CLiteral.of( l_numbername, CRawTerm.of( 123.45 ) ), CLiteral.of( l_stringname, CRawTerm.of( "a long text" ) ) ).toArray(),
            l_storage.streammulti().toArray()
        );

        Assert.assertFalse( l_storage.putmulti( l_numbername, CLiteral.of( l_numbername, CRawTerm.of( 0 ) ) ) );
        Assert.assertTrue( l_storage.putmulti( l_stringname, CLiteral.of( l_stringname, CRawTerm.of( "shorttext" ) ) ) );
        Assert.assertNotEquals( 0, l_classdata.number() );
        Assert.assertEquals( "shorttext", l_classdata.string() );

        Assert.assertTrue( l_storage.containsmulti( l_numbername ) );
        Assert.assertFalse( l_storage.multi( l_stringname ).isEmpty() );
        Assert.assertFalse( l_storage.containssingle( "any" ) );

        final IView l_view = new CView( "xyz", IBeliefbase.EMPY );
        Assert.assertFalse( l_storage.putsingle( l_view.name(), l_view ) );

        Assert.assertFalse( l_storage.removesingle( l_view.name() ) );
        Assert.assertFalse( l_storage.removemulti( l_numbername, CLiteral.of( l_numbername, CRawTerm.of( 0 ) ) ) );

        Assert.assertFalse( l_storage.clear().isempty() );
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
