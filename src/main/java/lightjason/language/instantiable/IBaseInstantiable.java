package lightjason.language.instantiable;

import lightjason.agent.IAgent;
import lightjason.language.CCommon;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.annotation.CNumberAnnotation;
import lightjason.language.execution.annotation.IAnnotation;
import lightjason.language.variable.IVariable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;


/**
 * base structure of instantiable elements
 */
public abstract class IBaseInstantiable implements IInstantiable
{
    /**
     * action list
     */
    protected final List<IExecution> m_action;

    /**
     * map with annotation (enum value for getting annotation object)
     */
    protected final Map<IAnnotation.EType, IAnnotation<?>> m_annotation;
    /**
     * hash code
     */
    private final int m_hash;

    /**
     * ctor
     *
     * @param p_action executed actions
     * @param p_annotation annotation map
     * @param p_hash hash code
     */
    protected IBaseInstantiable( final List<IExecution> p_action, final Set<IAnnotation<?>> p_annotation, final int p_hash )
    {
        m_hash = p_hash;
        m_action = Collections.unmodifiableList( p_action );

        // set default annotations
        final Map<IAnnotation.EType, IAnnotation<?>> l_map = p_annotation.stream().collect( HashMap::new, ( m, s ) -> m.put( s.getID(), s ), Map::putAll );
        l_map.putIfAbsent( IAnnotation.EType.FUZZY, new CNumberAnnotation<>( IAnnotation.EType.FUZZY, 1.0 ) );
        m_annotation = Collections.unmodifiableMap( l_map );
    }

    @Override
    public final int hashCode()
    {
        return m_hash;
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public double score( final IAgent p_agent )
    {
        return p_agent.getAggregation().evaluate( m_action.parallelStream().mapToDouble( i -> i.score( p_agent ) ).boxed() );
    }

    @Override
    public final IContext instantiate( final IAgent p_agent, final Stream<IVariable<?>> p_variable )
    {
        return CCommon.instantiate( this, p_agent, p_variable );
    }

    @Override
    public Stream<IVariable<?>> getVariables()
    {
        return m_action.stream().flatMap( i -> i.getVariables() );
    }

}
