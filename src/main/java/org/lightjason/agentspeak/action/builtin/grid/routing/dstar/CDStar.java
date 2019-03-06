package org.lightjason.agentspeak.action.builtin.grid.routing.dstar;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import org.lightjason.agentspeak.action.builtin.grid.routing.IBaseRouting;

import java.util.stream.Stream;


public final class CDStar extends IBaseRouting
{
    public CDStar()
    {
        super( p_walkable );
    }

    @Override
    public Stream<DoubleMatrix1D> apply( final ObjectMatrix2D p_grid, final DoubleMatrix1D p_start, final DoubleMatrix1D p_end )
    {
        return Stream.of();
    }
}
