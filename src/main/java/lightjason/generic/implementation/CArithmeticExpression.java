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

import lightjason.generic.IArithmeticExpressionElement;
import lightjason.generic.IVariable;

import java.text.MessageFormat;
import java.util.Stack;


/**
 * defines an arithmetic expression with numbers
 *
 * @see https://en.wikipedia.org/wiki/Reverse_Polish_notation
 * @see http://www.leda-tutorial.org/en/official/ch02s04s01.html
 */
public class CArithmeticExpression
{
    /**
     * stores the arithmetic expression
     **/
    private Stack<IArithmeticExpressionElement<?>> m_expression = new Stack<>();


    public void add( final EOperator p_operator, final Number p_number1, final Number p_number2 )
    {
        /*
        m_expression.push( new COperatorElement( p_operator ) );
        m_expression.push( new CNumberElement( p_number1 ) );
        m_expression.push( new CNumberElement( p_number2 ) );
        */
        m_expression.push( new CNumberElement( p_operator.evaluate( p_number1, p_number2 ) ) );
    }

    public <T extends Number> void add( final EOperator p_operator, final IVariable<T> p_variable1, final IVariable<T> p_variable2 )
    {
        m_expression.push( new COperatorElement( p_operator ) );
        m_expression.push( new CVariableElement<>( p_variable1 ) );
        m_expression.push( new CVariableElement<>( p_variable2 ) );
    }

    public <T extends Number> void add( final EOperator p_operator, final IVariable<T> p_variable, final Number p_number )
    {
        m_expression.push( new COperatorElement( p_operator ) );
        m_expression.push( new CVariableElement<>( p_variable ) );
        m_expression.push( new CNumberElement( p_number ) );
    }

    public <T extends Number> void add( final EOperator p_operator, final Number p_number, final IVariable<T> p_variable )
    {
        m_expression.push( new COperatorElement( p_operator ) );
        m_expression.push( new CNumberElement( p_number ) );
        m_expression.push( new CVariableElement<>( p_variable ) );
    }

    public <T extends Number> void add( final EOperator p_operator, final Number p_number )
    {
        m_expression.push( new COperatorElement( p_operator ) );
        m_expression.push( new CNumberElement( p_number ) );
    }

    public <T extends Number> void add( final EOperator p_operator, final IVariable<T> p_variable )
    {
        m_expression.push( new COperatorElement( p_operator ) );
        m_expression.push( new CVariableElement<>( p_variable ) );
    }

    public void add( final EOperator p_operator )
    {
        m_expression.add( new COperatorElement( p_operator ) );
    }

    public Number evaluate()
    {
        final Stack<IArithmeticExpressionElement<?>> l_stack = (Stack<IArithmeticExpressionElement<?>>) m_expression.clone();

        while ( l_stack.size() > 1 )
        {

        }

        return ( (CNumberElement) l_stack.pop() ).get();
    }


    public Number solveNumber( final IArithmeticExpressionElement<?> p_element )
    {
        if ( p_element instanceof COperatorElement )
            throw new IllegalArgumentException( "an operator element cannot be resolved" );

        if ( p_element instanceof CVariable )
            throw new IllegalArgumentException( "not yet working" );

        return (Number) p_element.get();
    }

    public EOperator solveOperator( final IArithmeticExpressionElement<?> p_element )
    {
        if ( !( p_element instanceof COperatorElement ) )
            throw new IllegalArgumentException( "a number element cannot be resolved" );

        return ( (COperatorElement) p_element ).get();
    }


    protected static class CNumberElement implements IArithmeticExpressionElement<Number>
    {
        private final Number m_value;

        public CNumberElement( final Number p_value )
        {
            m_value = p_value;
        }

        @Override
        public Number get()
        {
            return m_value;
        }
    }


    protected static class CVariableElement<T extends Number> implements IArithmeticExpressionElement<IVariable<T>>
    {
        private final IVariable<T> m_variable;

        public CVariableElement( final IVariable<T> p_variable )
        {
            m_variable = p_variable;
        }

        @Override
        public IVariable<T> get()
        {
            return m_variable;
        }
    }


    protected static class COperatorElement implements IArithmeticExpressionElement<EOperator>
    {
        private final EOperator m_operator;

        public COperatorElement( final EOperator p_operator )
        {
            m_operator = p_operator;
        }

        @Override
        public EOperator get()
        {
            return m_operator;
        }
    }


    public enum EOperator
    {
        Multiply,
        Divide,
        Plus,
        Minus,
        Pow;

        public Number evaluate( final Number p_value1, final Number p_value2 )
        {
            switch ( this )
            {
                case Plus:
                    return this.add( p_value1, p_value2 );

                case Minus:
                    return this.minus( p_value1, p_value2 );

                case Multiply:
                    return this.multiply( p_value1, p_value2 );

                case Divide:
                    return this.divide( p_value1, p_value2 );

                case Pow:
                    return this.pow( p_value1, p_value2 );

                default:
                    throw new IllegalStateException( "operator not defined" );
            }
        }


        private Number add( final Number p_value1, final Number p_value2 )
        {
            if ( ( p_value1 instanceof Double ) || ( p_value2 instanceof Double ) )
                return new Double( p_value1.doubleValue() + p_value2.doubleValue() );

            if ( ( p_value1 instanceof Long ) || ( p_value2 instanceof Long ) )
                return new Double( p_value1.longValue() + p_value2.longValue() );

            if ( ( p_value1 instanceof Float ) || ( p_value2 instanceof Float ) )
                return new Double( p_value1.floatValue() + p_value2.floatValue() );

            if ( ( p_value1 instanceof Integer ) || ( p_value2 instanceof Integer ) )
                return new Double( p_value1.intValue() + p_value2.intValue() );

            if ( ( p_value1 instanceof Short ) || ( p_value2 instanceof Short ) )
                return new Double( p_value1.shortValue() + p_value2.shortValue() );

            if ( ( p_value1 instanceof Byte ) || ( p_value2 instanceof Byte ) )
                return new Double( p_value1.byteValue() + p_value2.byteValue() );

            throw new IllegalArgumentException(
                    MessageFormat.format( "Operation + is not defined on [{0}] and [{1}]", p_value1.getClass(), p_value2.getClass() ) );
        }

        private Number minus( final Number p_value1, final Number p_value2 )
        {
            if ( ( p_value1 instanceof Double ) || ( p_value2 instanceof Double ) )
                return new Double( p_value1.doubleValue() - p_value2.doubleValue() );

            if ( ( p_value1 instanceof Long ) || ( p_value2 instanceof Long ) )
                return new Double( p_value1.longValue() - p_value2.longValue() );

            if ( ( p_value1 instanceof Float ) || ( p_value2 instanceof Float ) )
                return new Double( p_value1.floatValue() - p_value2.floatValue() );

            if ( ( p_value1 instanceof Integer ) || ( p_value2 instanceof Integer ) )
                return new Double( p_value1.intValue() - p_value2.intValue() );

            if ( ( p_value1 instanceof Short ) || ( p_value2 instanceof Short ) )
                return new Double( p_value1.shortValue() - p_value2.shortValue() );

            if ( ( p_value1 instanceof Byte ) || ( p_value2 instanceof Byte ) )
                return new Double( p_value1.byteValue() - p_value2.byteValue() );

            throw new IllegalArgumentException(
                    MessageFormat.format( "Operation - is not defined on [{0}] and [{1}]", p_value1.getClass(), p_value2.getClass() ) );
        }

        private Number multiply( final Number p_value1, final Number p_value2 )
        {
            if ( ( p_value1 instanceof Double ) || ( p_value2 instanceof Double ) )
                return new Double( p_value1.doubleValue() * p_value2.doubleValue() );

            if ( ( p_value1 instanceof Long ) || ( p_value2 instanceof Long ) )
                return new Double( p_value1.longValue() * p_value2.longValue() );

            if ( ( p_value1 instanceof Float ) || ( p_value2 instanceof Float ) )
                return new Double( p_value1.floatValue() * p_value2.floatValue() );

            if ( ( p_value1 instanceof Integer ) || ( p_value2 instanceof Integer ) )
                return new Double( p_value1.intValue() * p_value2.intValue() );

            if ( ( p_value1 instanceof Short ) || ( p_value2 instanceof Short ) )
                return new Double( p_value1.shortValue() * p_value2.shortValue() );

            if ( ( p_value1 instanceof Byte ) || ( p_value2 instanceof Byte ) )
                return new Double( p_value1.byteValue() * p_value2.byteValue() );

            throw new IllegalArgumentException(
                    MessageFormat.format( "Operation * is not defined on [{0}] and [{1}]", p_value1.getClass(), p_value2.getClass() ) );
        }

        private Number divide( final Number p_value1, final Number p_value2 )
        {
            if ( ( p_value1 instanceof Double ) || ( p_value2 instanceof Double ) )
                return new Double( p_value1.doubleValue() / p_value2.doubleValue() );

            if ( ( p_value1 instanceof Long ) || ( p_value2 instanceof Long ) )
                return new Double( p_value1.longValue() / p_value2.longValue() );

            if ( ( p_value1 instanceof Float ) || ( p_value2 instanceof Float ) )
                return new Double( p_value1.floatValue() / p_value2.floatValue() );

            if ( ( p_value1 instanceof Integer ) || ( p_value2 instanceof Integer ) )
                return new Double( p_value1.intValue() / p_value2.intValue() );

            if ( ( p_value1 instanceof Short ) || ( p_value2 instanceof Short ) )
                return new Double( p_value1.shortValue() / p_value2.shortValue() );

            if ( ( p_value1 instanceof Byte ) || ( p_value2 instanceof Byte ) )
                return new Double( p_value1.byteValue() / p_value2.byteValue() );

            throw new IllegalArgumentException(
                    MessageFormat.format( "Operation / is not defined on [{0}] and [{1}]", p_value1.getClass(), p_value2.getClass() ) );
        }

        private Number pow( final Number p_value1, final Number p_value2 )
        {
            return new Double( Math.pow( p_value1.doubleValue(), p_value2.doubleValue() ) );
        }
    }

}
