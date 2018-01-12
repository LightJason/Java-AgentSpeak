package org.lightjason.agentspeak.action.builtin;

import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.web.graphql.CSchema;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test graphql actions
 */
public final class TestCActionWebGraphQL extends IBaseTest
{
    /**
     * schema reading
     */
    @Test
    public final void schema()
    {
        /**
         {
         github {
         repo(name: "AgentSpeak", ownerUsername: "LightJason") {
         id
         name
         commits {
         sha
         message
         date
         }
         branches {
         name
         }
         }
         }
         }
         https://www.graphqlhub.com/playground

         http://graphql.org/swapi-graphql/
         */

        final List<ITerm> l_return = new ArrayList<>();

        new CSchema().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "https://www.graphqlhub.com/playground" )
                  .map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return
        );

        System.out.println( l_return );
    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionWebGraphQL().invoketest();
    }
}
