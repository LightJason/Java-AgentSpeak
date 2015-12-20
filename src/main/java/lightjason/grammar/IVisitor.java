package lightjason.grammar;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import lightjason.agent.action.IAction;
import lightjason.common.CPath;
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 */
public abstract class IVisitor implements PlanBundleVisitor<Object>, AgentVisitor<Object>
{

    /**
     * initial goal
     */
    protected ILiteral m_InitialGoal;
    /**
     * set with initial beliefs
     */
    protected final Set<ILiteral> m_InitialBeliefs = new HashSet<>();
    /**
     * map with plans
     */
    protected final SetMultimap<ITrigger<?>, IPlan> m_plans = HashMultimap.create();
    /**
     * map with action definition
     */
    protected final Map<CPath, IAction> m_actions;

    /**
     * ctor
     *
     * @param p_actions set with actions
     */
    protected IVisitor( final Set<IAction> p_actions )
    {
        m_actions = p_actions.stream().collect( Collectors.toMap( IAction::getName, i -> i ) );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * returns initial beliefs
     *
     * @return set with initial beliefs
     */
    public final Set<ILiteral> getInitialBeliefs()
    {
        return m_InitialBeliefs;
    }

    /**
     * returns initial goal
     *
     * @return literal
     */
    public final ILiteral getInitialGoal()
    {
        return m_InitialGoal;
    }

    /**
     * returns all plans
     *
     * @return set with triggers and plans
     */
    public final SetMultimap<ITrigger<?>, IPlan> getPlans()
    {
        return m_plans;
    }

    /**
     * returns all rules
     *
     * @return rules
     */
    public final Map<String, Object> getRules()
    {
        return null;
    }

}
