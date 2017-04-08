package org.lightjason.agentspeak.action.buildin;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.buildin.agent.CPlanList;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.annotation.IAnnotation;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.IBaseInstantiable;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.score.IAggregation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.LogManager;
import java.util.stream.LongStream;


/**
 * test agent action
 */
public final class TestCActionAgent extends IBaseTest
{
    /**
     * agent context
     */
    private IContext m_context;



    static
    {
        LogManager.getLogManager().reset();
    }


    /**
     * initialize
     */
    @Before
    public void initialize() throws Exception
    {
        m_context = new CContext(
            new CGenerator( new ByteArrayInputStream( "".getBytes( StandardCharsets.UTF_8 ) ), Collections.emptySet(), IAggregation.EMPTY ).generatesingle(),
            new CEmptyPlan( CTrigger.from( ITrigger.EType.ADDGOAL, CLiteral.from( "contextplan" ) ) ),
            Collections.emptyList()
        );
    }


    /**
     * test cycle
     */
    @Test
    public final void cycle()
    {
        Assert.assertEquals( m_context.agent().cycle(), 0 );
        LongStream.range( 1, new Random().nextInt( 200 ) + 1 ).forEach( i -> Assert.assertEquals( this.next().agent().cycle(), i ) );
    }


    /**
     * test plan list
     */
    @Test
    public final void planlist()
    {
        final ITrigger l_trigger = CTrigger.from( ITrigger.EType.ADDGOAL, CLiteral.from( "testplan" ) );
        final IPlan l_plan = new CEmptyPlan( l_trigger );
        final List<ITerm> l_return = new ArrayList<>();

        new CPlanList().execute(
            m_context,
            false,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertEquals( l_return.get( 0 ).<List<?>>raw().size(), 0 );


        m_context.agent().plans().put( l_plan.getTrigger(), new ImmutableTriple<>( l_plan, new AtomicLong(), new AtomicLong() ) );

        new CPlanList().execute(
            m_context,
            false,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof List<?> );
        Assert.assertEquals( l_return.get( 1 ).<List<?>>raw().size(), 1 );
        Assert.assertTrue( l_return.get( 1 ).<List<?>>raw().get( 0 ) instanceof AbstractMap.Entry<?, ?> );
        Assert.assertEquals( l_return.get( 1 ).<List<AbstractMap.Entry<String, ILiteral>>>raw().get( 0 ).getKey(), l_trigger.getType().sequence() );
        Assert.assertEquals( l_return.get( 1 ).<List<AbstractMap.Entry<String, ILiteral>>>raw().get( 0 ).getValue(), l_trigger.getLiteral() );
    }


    /**
     * execute agent cycle
     *
     * @return execution context
     */
    private IContext next()
    {
        try
        {
            m_context.agent().call();
        }
        catch ( final Exception l_exception )
        {
            Assert.assertTrue( l_exception.getMessage(), false );
        }

        return m_context;
    }

    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionAgent().invoketest();
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * agent class
     */
    private static final class CAgent extends IBaseAgent<CAgent>
    {

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CAgent( final IAgentConfiguration<CAgent> p_configuration )
        {
            super( p_configuration );
        }
    }

    /**
     * agent generator
     */
    private static final class CGenerator extends IBaseAgentGenerator<CAgent>
    {

        CGenerator( final InputStream p_stream, final Set<IAction> p_actions,
                    final IAggregation p_aggregation ) throws Exception
        {
            super( p_stream, p_actions, p_aggregation );
        }

        @Override
        public final CAgent generatesingle( final Object... p_data )
        {
            return new CAgent( m_configuration );
        }
    }


    /**
     * empty plan
     */
    private static class CEmptyPlan extends IBaseInstantiable implements IPlan
    {
        /**
         * trigger
         */
        private final ITrigger m_trigger;

        /**
         * ctor
         *
         * @param p_trigger trigger
         */
        CEmptyPlan( final ITrigger p_trigger )
        {
            super( Collections.emptyList(), Collections.emptySet(), 0 );
            m_trigger = p_trigger;
        }

        @Override
        public final ITrigger getTrigger()
        {
            return m_trigger;
        }

        @Override
        public final Collection<IAnnotation<?>> getAnnotations()
        {
            return m_annotation.values();
        }

        @Override
        public final List<IExecution> getBodyActions()
        {
            return Collections.emptyList();
        }

        @Override
        public final IFuzzyValue<Boolean> condition( final IContext p_context )
        {
            return CFuzzyValue.from( true );
        }
    }

}
