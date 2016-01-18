/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.language;

import com.google.common.reflect.ClassPath;
import lightjason.agent.action.IAction;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.execution.IContext;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * common structure for execution definition
 */
public final class CCommon
{
    /**
     * private ctor - avoid instantiation
     */
    private CCommon()
    {
    }

    /**
     * get all classes within an Java package as action
     *
     * @param p_package full-qualified package name
     * @return action set
     *
     * @todo can be moved to an own class
     */
    @SuppressWarnings( "unchecked" )
    public static Set<IAction> getActionsFromPackage( final String p_package ) throws IOException
    {
        return ClassPath.from( Thread.currentThread().getContextClassLoader() ).getTopLevelClassesRecursive( p_package ).parallelStream().map( i -> {

            try
            {
                final Class<?> l_class = i.load();
                if ( ( !Modifier.isAbstract( l_class.getModifiers() ) ) && ( !Modifier.isInterface( l_class.getModifiers() ) ) &&
                     ( Modifier.isPublic( l_class.getModifiers() ) ) && ( IAction.class.isAssignableFrom( l_class ) ) )
                    return (IAction) l_class.newInstance();
            }
            catch ( final IllegalAccessException | InstantiationException p_exception )
            {
            }

            return null;
        } ).filter( i -> i != null ).collect( Collectors.toSet() );
    }

    /**
     * returns a native / raw value of a term
     *
     * @param p_value any value type
     * @return term value or raw value
     */
    @SuppressWarnings( "unchecked" )
    public static <T, N> T getRawValue( final N p_value )
    {
        if ( p_value instanceof IVariable<?> )
            return ( (IVariable<?>) p_value ).getTyped();
        if ( p_value instanceof CRawTerm<?> )
            return ( (CRawTerm<?>) p_value ).getTyped();

        return (T) p_value;
    }

    /**
     * replace variables with context variables
     *
     * @param p_context execution context
     * @param p_terms replacing term list
     * @return result term list
     */
    public static List<ITerm> replaceVariableFromContext( final IContext<?> p_context, final Collection<? extends ITerm> p_terms )
    {
        return p_terms.stream().map( i -> replaceVariableFromContext( p_context, i ) ).collect( Collectors.toList() );
    }

    /**
     * replace variable with context variable
     * other values will be passed
     *
     * @param p_context execution context
     * @param p_term term
     * @return replaces variable object
     */
    public static ITerm replaceVariableFromContext( final IContext<?> p_context, final ITerm p_term )
    {
        if ( !( p_term instanceof IVariable<?> ) )
            return p_term;

        final IVariable<?> l_variable = p_context.getInstanceVariables().get( p_term.getFQNFunctor() );
        if ( l_variable == null )
            throw new CIllegalArgumentException( lightjason.common.CCommon
                                                         .getLanguageString(
                                                                 lightjason.common.CCommon.class, "variablenotfoundincontext", p_term.getFQNFunctor() ) );

        return l_variable;
    }


    /**
     * @param p_context
     * @param p_terms
     * @return
     *
     * @bug not working
     */
    public static List<ITerm> flatCollection( final IContext<?> p_context, final Collection<? extends ITerm> p_terms )
    {
        return replaceVariableFromContext( p_context, p_terms ).stream().collect( Collectors.toList() );
    }


    /**
     * returns the value of a term
     *
     * @return value
     */
    @Deprecated
    public static <T> T getTermValue( final Class<?> p_class, final ITerm p_term )
    {
        if ( p_term instanceof IVariable<?> )
            return ( (IVariable<?>) p_term ).throwNotAllocated().throwValueNotAssignableTo( p_class ).getTyped();
        if ( p_term instanceof CRawTerm<?> )
            return ( (CRawTerm<?>) p_term ).throwNotAllocated().throwValueNotAssignableTo( p_class ).getTyped();

        throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( CCommon.class, "notconvertable", p_term ) );
    }
}
