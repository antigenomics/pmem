package com.antigenomics.pmem.hamiltonian;

import com.antigenomics.pmem.encoding.Encoder;
import com.antigenomics.pmem.entities.Entity;
import com.antigenomics.pmem.representation.LinearSpaceObjectArray;
import com.antigenomics.pmem.representation.algebra.BilinearComposition;
import com.antigenomics.pmem.representation.algebra.VectorSpace;
import com.antigenomics.pmem.state.TwoLayerState;
import com.sun.istack.internal.NotNull;

import java.util.Arrays;

public final class TwoLayerSpinGlassHamiltonian<E1 extends Entity, E2 extends Entity,
        V extends VectorSpace<V, M>,
        M extends BilinearComposition<V, M>>
        implements SpinGlassHamiltonian<TwoLayerState<E1, E2>, V, M> {
    private final Encoder<E1, V> firstEncoder;
    private final Encoder<E2, V> secondEncoder;
    private final LinearSpaceObjectArray<M> zeroParameters;

    public TwoLayerSpinGlassHamiltonian(@NotNull final Encoder<E1, V> firstEncoder,
                                        @NotNull final Encoder<E2, V> secondEncoder) {
        this.firstEncoder = firstEncoder;
        this.secondEncoder = secondEncoder;
        final V z1 = firstEncoder.getZero(), z2 = secondEncoder.getZero();
        this.zeroParameters = new LinearSpaceObjectArray<>(
                Arrays.asList(
                        z1.expand(),
                        z2.expand(),
                        z1.outerProduct(z2)
                )
        );
    }

    public Encoder<E1, V> getFirstEncoder() {
        return firstEncoder;
    }

    public Encoder<E2, V> getSecondEncoder() {
        return secondEncoder;
    }

    @Override
    public double computeEnergy(@NotNull final TwoLayerState<E1, E2> state,
                                @NotNull final LinearSpaceObjectArray<M> parameters) {
        final V s1 = firstEncoder.encode(state.getFirstValue());
        final V s2 = firstEncoder.encode(state.getFirstValue());
        final M J1 = parameters.get(0), J2 = parameters.get(1), J12 = parameters.get(3);
        return J1.bilinearForm(s1, s1) +
                J2.bilinearForm(s2, s2) +
                J12.bilinearForm(s1, s2);
    }

    @Override
    public LinearSpaceObjectArray<M> computeGradient(@NotNull final TwoLayerState<E1, E2> state,
                                                     @NotNull final LinearSpaceObjectArray<M> parameters) {
        final V s1 = firstEncoder.encode(state.getFirstValue());
        final V s2 = firstEncoder.encode(state.getFirstValue());
        return new LinearSpaceObjectArray<>(
                Arrays.asList(
                        s1.expand(),
                        s2.expand(),
                        s1.outerProduct(s2)
                )
        );
    }

    @Override
    public LinearSpaceObjectArray<M> getZeroParameters() {
        return zeroParameters;
    }
}