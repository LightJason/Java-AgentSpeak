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


package lightjason.generic.implementation;

import lightjason.generic.IArithmeticOperator;
import lightjason.generic.IVariable;

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
public class CArithmeticExpression
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
    public CArithmeticExpression( final Map<String, IArithmeticOperator> p_operator )
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

    public Number evaluate()
    {
        return this.evaluate( null );
    }

    public Number evaluate( final Map<String, Number> p_solver )
    {
        // copy of data
        // @todo deep-copy
        final Stack<IArithmeticOperator> l_operator = (Stack<IArithmeticOperator>) m_operator.clone();
        final List<CNumberElement> l_elements = new LinkedList<>( m_elements );

        while ( !l_operator.isEmpty() )
        {
            final IArithmeticOperator l_executeoperator = l_operator.pop();
            final List<CNumberElement> l_arguments = l_elements.subList( 0, l_executeoperator.getNumberOfArguments() );
            final Number l_number = l_executeoperator.execution( l_arguments.parallelStream().map( i -> i.get( p_solver ) ).collect( Collectors.toList() ) );

            l_arguments.clear();
            l_elements.add( 0, new CNumberElement( l_number ) );
        }

        return l_elements.get( 0 ).get( null );
    }



    protected static class CNumberElement<T>
    {
        private final Number m_value;
        private final IVariable<T> m_variable;

        public CNumberElement( final Number p_value )
        {
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
            if ( ( p_solver != null ) && ( m_variable != null ) )
                return p_solver.get( m_variable.getName() );

            return m_value;
        }
    }

}
