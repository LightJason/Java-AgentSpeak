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
import org.lightjason.agentspeak.beliefbase.storage.CSingleOnlyStorage;
import org.lightjason.agentspeak.beliefbase.storage.CSingleStorage;
import org.lightjason.agentspeak.beliefbase.storage.IStorage;
import org.lightjason.agentspeak.beliefbase.view.CView;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.testing.IBaseTest;


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
        Assert.assertFalse( l_storage.putMultiElement( l_literal.functor(), l_literal ) );

        final IView l_view = new CView( "foobar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putSingleElement( l_view.name(), l_view ) );


        Assert.assertEquals( 0, l_storage.size() );
        Assert.assertEquals( l_storage.size(), l_storage.streamMultiElements().count() );
        Assert.assertEquals( 0, l_storage.streamMultiElements().count() );
        Assert.assertEquals( 1, l_storage.streamSingleElements().count() );
        Assert.assertTrue( l_storage.getMultiElement( l_literal.functor() ).isEmpty() );
    }

    /**
     * test single-storage-only clear & empty
     */
    @Test
    public void singlestorageonlyclearempty()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleOnlyStorage<>();
        Assert.assertTrue( l_storage.empty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assert.assertFalse( l_storage.putMultiElement( l_literal.functor(), l_literal ) );
        Assert.assertTrue( l_storage.empty() );

        final IView l_view = new CView( "bar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putSingleElement( l_view.name(), l_view ) );
        Assert.assertFalse( l_storage.empty() );

        l_storage.clear();
        Assert.assertTrue( l_storage.empty() );
    }

    /**
     * test single-storage-only remove, contains & get
     */
    @Test
    public void singlestorageonlyaccess()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleOnlyStorage<>();
        Assert.assertTrue( l_storage.empty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        Assert.assertFalse( l_storage.putMultiElement( l_literal.functor(), l_literal ) );
        Assert.assertTrue( l_storage.getMultiElement( l_literal.functor() ).isEmpty() );

        final IView l_view = new CView( "xbar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putSingleElement( l_view.name(), l_view ) );
        Assert.assertEquals( l_view, l_storage.getSingleElement( l_view.name() ) );

        Assert.assertTrue( l_storage.containsSingleElement( l_view.name() ) );
        Assert.assertFalse( l_storage.containsMultiElement( l_literal.functor() ) );

        Assert.assertTrue( l_storage.removeSingleElement( l_view.name() ) );
        Assert.assertTrue( l_storage.empty() );
        Assert.assertFalse( l_storage.removeMultiElement( l_literal.functor(), l_literal ) );
    }



    /**
     * test single-storage
     */
    @Test
    public void singlestorage()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleStorage<>();

        final ILiteral l_literal = CLiteral.of( "xxx" );
        Assert.assertTrue( l_storage.putMultiElement( l_literal.functor(), l_literal ) );

        final IView l_view = new CView( "foobar", IBeliefbase.EMPY );
        Assert.assertTrue( l_storage.putSingleElement( l_view.name(), l_view ) );


        Assert.assertEquals( 1, l_storage.size() );
        Assert.assertEquals( l_storage.size(), l_storage.streamMultiElements().count() );
        Assert.assertEquals( 1, l_storage.streamMultiElements().count() );
        Assert.assertEquals( 1, l_storage.streamSingleElements().count() );
        Assert.assertFalse( l_storage.getMultiElement( l_literal.functor() ).isEmpty() );
    }

    /**
     * test single-storage clear & empty
     */
    @Test
    public void singlestorageclearempty()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleStorage<>();
        Assert.assertTrue( l_storage.empty() );

        final ILiteral l_literal = CLiteral.of( "yyy" );
        l_storage.putMultiElement( l_literal.functor(), l_literal );
        Assert.assertFalse( l_storage.empty() );

        final IView l_view = new CView( "bar", IBeliefbase.EMPY );
        l_storage.putSingleElement( l_view.name(), l_view );
        Assert.assertFalse( l_storage.empty() );

        l_storage.clear();
        Assert.assertTrue( l_storage.empty() );
    }

    /**
     * test single-storage-only remove, contains & get
     */
    @Test
    public void singlestorageaccess()
    {
        final IStorage<ILiteral, IView> l_storage = new CSingleStorage<>();
        Assert.assertTrue( l_storage.empty() );

        final ILiteral l_literal1 = CLiteral.of( "abc" );
        Assert.assertTrue( l_storage.putMultiElement( l_literal1.functor(), l_literal1 ) );

        final ILiteral l_literal2 = CLiteral.of( "abc", CRawTerm.of( 123 ) );
        Assert.assertTrue( l_storage.putMultiElement( l_literal2.functor(), l_literal2 ) );

        Assert.assertFalse( l_storage.getMultiElement( l_literal1.functor() ).isEmpty() );

        final IView l_view = new CView( "xbar", IBeliefbase.EMPY );
        l_storage.putSingleElement( l_view.name(), l_view );
        Assert.assertEquals( l_view, l_storage.getSingleElement( l_view.name() ) );

        Assert.assertTrue( l_storage.containsSingleElement( l_view.name() ) );
        Assert.assertTrue( l_storage.containsMultiElement( l_literal1.functor() ) );

        Assert.assertTrue( l_storage.removeSingleElement( l_view.name() ) );
        Assert.assertFalse( l_storage.empty() );

        Assert.assertTrue( l_storage.containsMultiElement( l_literal1.functor() ) );
        Assert.assertEquals( l_literal2, l_storage.streamMultiElements().findFirst().get() );
        Assert.assertFalse( l_storage.removeMultiElement( l_literal1.functor(), l_literal1 ) );
        Assert.assertTrue( l_storage.removeMultiElement( l_literal2.functor(), l_literal2 ) );

        Assert.assertEquals( 0, l_storage.streamMultiElements().count() );
    }
}
