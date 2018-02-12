package org.lightjason.agentspeak.action.builtin;

import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.web.graphql.CQuery;
import org.lightjason.agentspeak.language.CLiteral;
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

    @Test
    public final void query()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CQuery().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of(
                CRawTerm.from( "https://daimon-dataaccess.herokuapp.com/graphql" ),
                CLiteral.from(
                    "sources",
                    CLiteral.from( "name" ),
                    CLiteral.from( "description" ),
                    CLiteral.from( "foo", CRawTerm.from( "xx" ) ),
                    CLiteral.from( "foo", CRawTerm.from( 3 ) )
                )
            ).collect( Collectors.toList() ),
            l_return
        );


    }


}
