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


package lightjason.language.arithmetic;

import lightjason.language.IVariable;
import lightjason.language.arithmetic.operator.IArithmeticOperator;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;


/**
 * defines an arithmetic expression with numbers
 *
 * @see https://en.wikipedia.org/wiki/Reverse_Polish_notation
 * @see https://en.wikipedia.org/wiki/Shunting-yard_algorithm
 */
public class CExpression
{
    /**
     * stores the operator of the arithmetic expression
     **/
    private final Stack<IArithmeticOperator> m_operator = new Stack<>();
    /**
     * output queue
     */
    private final List<CNumberElement> m_elements = new LinkedList<>();
    /**
     * operator definition
     */
    private final Map<String, IArithmeticOperator> m_operatordefinition;

    /**
     * ctor
     *
     * @param p_operator map with operator set
     */
    public CExpression( final Map<String, IArithmeticOperator> p_operator )
    {
        m_operatordefinition = p_operator;
    }

    public void add( final String p_operator, final Number p_number1, final Number p_number2 )
    {
        if ( !m_operatordefinition.containsKey( p_operator ) )
            throw new IllegalArgumentException( MessageFormat.format( "operator [{0}] not found", p_operator ) );

        m_operator.add( m_operatordefinition.get( p_operator ) );
        m_elements.add( new CNumberElement( p_number1 ) );
        m_elements.add( new CNumberElement( p_number2 ) );
    }

    public <T extends Number> void add( final String p_operator, final IVariable<T> p_variable1, final IVariable<T> p_variable2 )
    {
        if ( !m_operatordefinition.containsKey( p_operator ) )
            throw new IllegalArgumentException( MessageFormat.format( "operator [{0}] not found", p_operator ) );

        m_operator.add( m_operatordefinition.get( p_operator ) );
        m_elements.add( new CNumberElement( p_variable1 ) );
        m_elements.add( new CNumberElement( p_variable2 ) );
    }

    public <T extends Number> void add( final String p_operator, final IVariable<T> p_variable, final Number p_number )
    {
        if ( !m_operatordefinition.containsKey( p_operator ) )
            throw new IllegalArgumentException( MessageFormat.format( "operator [{0}] not found", p_operator ) );

        m_operator.add( m_operatordefinition.get( p_operator ) );
        m_elements.add( new CNumberElement( p_variable ) );
        m_elements.add( new CNumberElement( p_number ) );
    }

    public <T extends Number> void add( final String p_operator, final Number p_number, final IVariable<T> p_variable )
    {
        if ( !m_operatordefinition.containsKey( p_operator ) )
            throw new IllegalArgumentException( MessageFormat.format( "operator [{0}] not found", p_operator ) );

        m_operator.add( m_operatordefinition.get( p_operator ) );
        m_elements.add( new CNumberElement( p_number ) );
        m_elements.add( new CNumberElement( p_variable ) );
    }

    public <T extends Number> void add( final String p_operator, final Number p_number )
    {
        if ( !m_operatordefinition.containsKey( p_operator ) )
            throw new IllegalArgumentException( MessageFormat.format( "operator [{0}] not found", p_operator ) );

        m_operator.add( m_operatordefinition.get( p_operator ) );
        m_elements.add( new CNumberElement( p_number ) );
    }

    public <T extends Number> void add( final String p_operator, final IVariable<T> p_variable )
    {
        if ( !m_operatordefinition.containsKey( p_operator ) )
            throw new IllegalArgumentException( MessageFormat.format( "operator [{0}] not found", p_operator ) );

        m_operator.add( m_operatordefinition.get( p_operator ) );
        m_elements.add( new CNumberElement( p_variable ) );
    }

    public void add( final String p_operator )
    {
        if ( !m_operatordefinition.containsKey( p_operator ) )
            throw new IllegalArgumentException( MessageFormat.format( "operator [{0}] not found", p_operator ) );

        m_operator.add( m_operatordefinition.get( p_operator ) );
    }

    public void add( final Number p_value )
    {
        m_elements.add( new CNumberElement( p_value ) );
    }

    public <T extends Number> void add( final IVariable<T> p_variable )
    {
        m_elements.add( new CNumberElement( p_variable ) );
    }

    /**
     * evaluates the expression without variable substituation
     *
     * @return
     */
    public Number evaluate()
    {
        return this.evaluate( null );
    }

    /**
     * evaluates expression
     *
     * @param p_solver map with variable substituation
     * @return number
     */
    public Number evaluate( final Map<String, Number> p_solver )
    {
        // copy of data
        final Stack<IArithmeticOperator> l_operator = (Stack<IArithmeticOperator>) m_operator.clone();
        final List<CNumberElement> l_elements = new LinkedList<>( m_elements );

        while ( !l_operator.isEmpty() )
        {
            final IArithmeticOperator l_currentoperator = l_operator.pop();
            if ( l_elements.size() < l_currentoperator.getNumberOfArguments() )
                throw new IllegalStateException(
                        MessageFormat.format( "operator [{0}] need [{1}] arguments, but the expression stack holds only [{2}] arguments", l_currentoperator
                                .getToken(), l_currentoperator.getNumberOfArguments(), l_elements.size() ) );

            final List<CNumberElement> l_arguments = l_elements.subList( 0, l_currentoperator.getNumberOfArguments() );
            final Number l_number = l_currentoperator.execution( l_arguments.parallelStream().map( i -> i.get( p_solver ) ).collect( Collectors.toList() ) );

            l_arguments.clear();
            l_elements.add( 0, new CNumberElement( l_number ) );
        }

        if ( l_elements.size() != 1 )
            throw new IllegalStateException( MessageFormat.format( "expression cannot be evaluated", "" ) );

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

        public CNumberElement( final Number p_value )
        {
            if ( p_value == null )
                throw new IllegalArgumentException( MessageFormat.format( "number need not to be null", "" ) );

            m_value = p_value;
            m_variable = null;
        }

        public CNumberElement( final IVariable<T> p_variable )
        {
            m_value = null;
            m_variable = p_variable;
        }

        public Number get( final Map<String, Number> p_solver )
        {
            Number l_return = m_value;
            if ( ( p_solver != null ) && ( m_variable != null ) )
            {
                l_return = p_solver.get( m_variable.getName() );
                if ( l_return == null )
                    throw new IllegalStateException( MessageFormat.format( "number for variable [{0}] cannot be resolved", m_variable.getName() ) );
            }

            return l_return;
        }
    }

}
