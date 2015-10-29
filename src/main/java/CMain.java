import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class CMain
{

    public static void main( final String[] p_args )
    {
        try (
                final InputStream l_stream = new FileInputStream( p_args[0] );
        ) {
            new CJasonAgent( l_stream );
        } catch ( final IOException l_exception ) {
            l_exception.printStackTrace();
        }
    }

}