/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */


package lightjason.language.plan.expression.arithmetic;

import lightjason.common.CCommon;
import lightjason.error.CIllegalArgumentException;
import lightjason.error.CIllegalStateException;
import lightjason.language.IVariable;
import lightjason.language.plan.expression.IExpression;
import lightjason.language.plan.expression.arithmetic.operator.CAbs;
import lightjason.language.plan.expression.arithmetic.operator.CCeil;
import lightjason.language.plan.expression.arithmetic.operator.CCos;
import lightjason.language.plan.expression.arithmetic.operator.CDivide;
import lightjason.language.plan.expression.arithmetic.operator.CExp;
import lightjason.language.plan.expression.arithmetic.operator.CFloor;
import lightjason.language.plan.expression.arithmetic.operator.CMinus;
import lightjason.language.plan.expression.arithmetic.operator.CModulo;
import lightjason.language.plan.expression.arithmetic.operator.CMultiply;
import lightjason.language.plan.expression.arithmetic.operator.CPlus;
import lightjason.language.plan.expression.arithmetic.operator.CPow;
import lightjason.language.plan.expression.arithmetic.operator.CSignum;
import lightjason.language.plan.expression.arithmetic.operator.CSin;
import lightjason.language.plan.expression.arithmetic.operator.CSqrt;
import lightjason.language.plan.expression.arithmetic.operator.CTan;
import lightjason.language.plan.expression.arithmetic.operator.IArithmeticOperator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;


/**
 * defines an arithmetic expression with numbers
 *
 * @see https://en.wikipedia.org/wiki/Reverse_Polish_notation
 * @see https://en.wikipedia.org/wiki/Shunting-yard_algorithm
 */
@SuppressWarnings( "serial" )
public final class CExpression implements IExpression
{
    /**
     * map with default operators
     **/
    public static final Map<String, IArithmeticOperator> DEFAULTOPERATOR = Collections.unmodifiableMap( new HashMap<String, IArithmeticOperator>()
    {{
        for ( final IArithmeticOperator l_operator : new IArithmeticOperator[]{
                new CAbs(), new CCeil(), new CCos(), new CDivide(), new CExp(),
                new CFloor(), new CMinus(), new CModulo(), new CMultiply(),
                new CPlus(), new CPow(), new CSignum(), new CSin(), new CSqrt(),
                new CTan()
        } )
            put( l_operator.getToken(), l_operator );
    }} );

    /**
     * stores the operator of the arithmetic expression
     **/
    private final Stack<IArithmeticOperator> m_operator = new Stack<>();
    /**
     * output queue
     */
    private final List<CNumberElement<?>> m_elements = new LinkedList<>();
    /**
     * operator definition
     */
    private final Map<String, IArithmeticOperator> m_operatordefinition;
    /**
     * stores a set of all variables
     */
    private final Set<IVariable<? extends Number>> m_variables = new HashSet<>();

    /**
     * ctor
     */
    public CExpression()
    {
        m_operatordefinition = DEFAULTOPERATOR;
    }

    /**
     * ctor
     *
     * @param p_operator map with operator set
     */
    public CExpression( final Map<String, IArithmeticOperator> p_operator )
    {
        m_operatordefinition = p_operator;
    }

    /**
     * adds operators
     *
     * @param p_operator operators
     * @return expression
     */
    public final CExpression push( final String... p_operator )
    {
        for ( final String l_item : p_operator )
        {
            final IArithmeticOperator l_operator = m_operatordefinition.get( l_item );
            if ( l_operator == null )
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "operatornotfound", l_item ) );

            m_operator.add( l_operator );
        }

        return this;
    }

    /**
     * adds numbers
     *
     * @param p_number numbers
     * @return expression
     */
    public final CExpression push( final Number... p_number )
    {
        for ( final Number l_item : p_number )
            m_elements.add( new CNumberElement<>( l_item ) );

        return this;
    }

    /**
     * adds variables
     *
     * @param p_variable variables
     * @return expression
     *
     * @tparam T number type
     */
    public final <T extends Number> CExpression push( final IVariable<T>... p_variable )
    {
        for ( final IVariable<T> l_item : p_variable )
        {
            m_elements.add( new CNumberElement<>( l_item ) );
            m_variables.add( l_item );
        }

        return this;
    }

    /**
     * returns a set of all variables of the expression
     *
     * @return set with variables
     */
    public final Set<IVariable<? extends Number>> getVariables()
    {
        return m_variables;
    }

    /**
     * evaluates the expression without variable substituation
     *
     * @return
     */
    public final Number evaluate()
    {
        return this.evaluate( null );
    }

    /**
     * evaluates expression
     *
     * @param p_solver map with variable substituation
     * @return number
     */
    @SuppressWarnings( "unchecked" )
    public final Number evaluate( final Map<String, Number> p_solver )
    {
        // copy of data
        final Stack<IArithmeticOperator> l_operator = (Stack<IArithmeticOperator>) m_operator.clone();
        final List<CNumberElement<?>> l_elements = new LinkedList<>( m_elements );

        while ( !l_operator.isEmpty() )
        {
            final IArithmeticOperator l_currentoperator = l_operator.pop();

            if ( l_elements.size() < l_currentoperator.getNumberOfArguments() )
                throw new CIllegalStateException(
                        CCommon.getLanguageString( this, "argumentnumbertosmall", l_currentoperator
                                .getToken(), l_currentoperator.getNumberOfArguments(), l_elements.size() ) );


            final List<CNumberElement<?>> l_arguments = l_elements.subList( 0, l_currentoperator.getNumberOfArguments() );
            final Number l_number = l_currentoperator.execution( l_arguments.parallelStream().map( i -> i.get( p_solver ) ).collect( Collectors.toList() ) );


            l_arguments.clear();
            l_elements.add( 0, new CNumberElement<>( l_number ) );
        }

        if ( l_elements.size() != 1 )
            throw new CIllegalStateException( CCommon.getLanguageString( this, "notevaluated" ) );

        return l_elements.get( 0 ).get( null );
    }


    /**
     * number element
     *
     * @tparam T number type
     */
    protected static class CNumberElement<T extends Number>
    {
        /**
         * number value
         */
        private final Number m_value;
        /**
         * variable value
         */
        private final IVariable<T> m_variable;

        /**
         * ctor
         *
         * @param p_value add a number
         */
        public CNumberElement( final Number p_value )
        {
            if ( p_value == null )
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "numbernotnull" ) );

            m_value = p_value;
            m_variable = null;
        }

        /**
         * ctor
         *
         * @param p_variable add a variable
         */
        public CNumberElement( final IVariable<T> p_variable )
        {
            m_value = null;
            m_variable = p_variable;
        }

        /**
         * returns the number
         *
         * @param p_solver solver map with variabl -> number mapping
         * @return number
         */
        public final Number get( final Map<String, Number> p_solver )
        {
            Number l_return = m_value;
            if ( ( p_solver != null ) && ( m_variable != null ) )
            {
                l_return = p_solver.get( m_variable.getName() );
                if ( l_return == null )
                    throw new CIllegalStateException( CCommon.getLanguageString( this, "variablenotresolve", m_variable.getName() ) );
            }

            return l_return;
        }
    }

}
