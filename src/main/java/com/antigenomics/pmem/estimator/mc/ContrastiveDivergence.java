package com.antigenomics.pmem.estimator.mc;

import com.antigenomics.pmem.state.State;
import com.antigenomics.pmem.estimator.LogLikelihoodGradientAscent;
import com.antigenomics.pmem.hamiltonian.Hamiltonian;
import com.antigenomics.pmem.representation.ImmutableLinearSpaceObject;
import com.antigenomics.pmem.representation.MutableLinearSpaceObject;

import java.util.ArrayList;
import java.util.stream.Collector;

public final class ContrastiveDivergence<S extends State,
        R extends ImmutableLinearSpaceObject<R>,
        H extends Hamiltonian<S, R>> extends LogLikelihoodGradientAscent<S, R, H> {
    private final MonteCarloSimulator<S, R, H> simulator;

    public ContrastiveDivergence(final double learningRate, final double tol, final int maxIter,
                                 final MonteCarloSimulator<S, R, H> simulator) {
        super(learningRate, tol, maxIter);
        this.simulator = simulator;
    }

    @Override
    protected MutableLinearSpaceObject<R> computeNetGradient(final ArrayList<S> states,
                                                             final H hamiltonian,
                                                             final R parameters) {
        return states.parallelStream()
                .map(s -> simulator.simulate(s, parameters, hamiltonian))
                .map(sL -> computeMcSimulationsGradient(sL, parameters, hamiltonian))
                .collect(Collector.of(
                        () -> hamiltonian.getZeroParameters().toMutable(),
                        MutableLinearSpaceObject::plusInplace,
                        (result, newElement) -> {
                            newElement.plusInplace(result);
                            return newElement;
                        }
                ));
    }

    private MutableLinearSpaceObject<R> computeMcSimulationsGradient(final ArrayList<S> states,
                                                                     final R parameters,
                                                                     final H hamiltonian) {
        final MutableLinearSpaceObject<R> xS = hamiltonian.getZeroParameters().toMutable();

        for (S state : states) {
            xS.plusInplace(hamiltonian.computeGradient(state, parameters));
        }

        xS.multiplyInplace(1.0 / states.size());
        xS.minusInplace(hamiltonian.computeGradient(states.get(0), parameters));

        return xS;
    }

    public MonteCarloSimulator<S, R, H> getSimulator() {
        return simulator;
    }
}