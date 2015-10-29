import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;

public class CJasonAgent
{

    public CJasonAgent( final InputStream p_stream ) throws IOException
    {
        final JasonParser.AgentContext l_agent = new JasonParser( new CommonTokenStream( new JasonLexer( new ANTLRInputStream( p_stream ) ) ) ).agent();
        System.out.println( l_agent );
    }




}
