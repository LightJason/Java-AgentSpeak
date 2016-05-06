package lightjason.language.execution.action.achievement_test;

import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.action.IBaseExecution;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.instantiable.rule.IRule;
import lightjason.language.variable.IRelocateVariable;
import lightjason.language.variable.IVariable;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;


/**
 * abstract class for execute a logical-rule
 */
abstract class IAchievementRule<T extends ITerm> extends IBaseExecution<T>
{
    /**
     * ctor
     *
     * @param p_type value of the achievment-goal
     */
    IAchievementRule( final T p_type )
    {
        super( p_type );
    }

    /**
     * execute rule from context
     *
     * @param p_context execution context
     * @param p_value execution literal
     * @param p_parallel parallel execution
     * @return boolean result
     */
    protected static IFuzzyValue<Boolean> execute( final IContext p_context, final ILiteral p_value, final boolean p_parallel )
    {
        // read current rules, if not exists execution fails
        final Collection<IRule> l_rules = p_context.getAgent().getRules().get( p_value.getFQNFunctor() );
        if ( l_rules == null )
            return CFuzzyValue.from( false );

        // first step is the unification of the caller literal, so variables will be set from the current execution context
        final ILiteral l_unified = p_value.unify( p_context );

        // second step execute backtracking rules sequential / parallel
        return (
                p_parallel
                ? l_rules.parallelStream()
                : l_rules.stream()
        ).map( i -> {

            // instantiate variables by unification of the rule literal
            final Set<IVariable<?>> l_variables = p_context.getAgent().getUnifier().literal( i.getIdentifier(), l_unified );

            // execute rule
            final IFuzzyValue<Boolean> l_return = i.execute(
                    i.instantiate( p_context.getAgent(), l_variables.stream() ),
                    false,
                    Collections.<ITerm>emptyList(),
                    Collections.<ITerm>emptyList(),
                    Collections.<ITerm>emptyList()
            );

            // create rule result with fuzzy- and defuzzificated value and instantiate variable set
            return new ImmutableTriple<>( p_context.getAgent().getFuzzy().getDefuzzyfication().defuzzify( l_return ), l_return, l_variables );

        } )

         // find successfully ended rule
         .filter( i -> i.getLeft() )
         .findFirst()

         // realocate rule instantiated variables back to execution context
         .map( i -> {

             i.getRight().parallelStream()
              .filter( j -> j instanceof IRelocateVariable )
              .forEach( j -> ( (IRelocateVariable) j ).relocate() );

             return i.getMiddle();

         } )

         // otherwise rule fails (default behaviour)
         .orElse( CFuzzyValue.from( false ) );
    }

}
