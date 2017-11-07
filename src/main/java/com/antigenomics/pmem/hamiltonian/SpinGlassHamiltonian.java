package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.representation.LinearSpaceObject;
import com.antigenomics.pmem.representation.algebra.BilinearComposition;
import com.antigenomics.pmem.representation.algebra.VectorSpace;
import com.antigenomics.pmem.state.State;

public interface SpingGlassHamiltonian<S extends State,
        V extends VectorSpace<V, M>,
        M extends BilinearComposition<V, M>,
        R extends LinearSpaceObject<M>>
        extends MatrixHamiltonian<S, M, R> {
}