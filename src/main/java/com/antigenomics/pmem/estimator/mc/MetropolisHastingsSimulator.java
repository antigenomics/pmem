package com.antigenomics.pmem.estimator.mc;


import com.antigenomics.pmem.state.State;
import com.antigenomics.pmem.hamiltonian.Hamiltonian;
import com.antigenomics.pmem.representation.Representation;

import java.util.ArrayList;

public final class MetropolisHastingsSimulator<S extends State,
        R extends Representation,
        H extends Hamiltonian<S, R>> implements MonteCarloSimulator<S, R, H> {
    private final int maxIter;
    private final StateMutator<S> mutator;

    public MetropolisHastingsSimulator(final StateMutator<S> mutator,
                                       final int maxIter) {
        this.maxIter = maxIter;
        this.mutator = mutator;
    }

    @Override
    public ArrayList<S> simulate(final S state,
                                 final R parameters,
                                 final H hamiltonian) {
        S currentState = state;
        double currentEnergy = hamiltonian.computeEnergy(state, parameters);
        final ArrayList<S> states = new ArrayList<>(maxIter);
        states.add(currentState);

        for (int i = 0; i < maxIter; i++) {
            final S candidateState = mutator.mutate(state);
            final double candidateEnergy = hamiltonian.computeEnergy(candidateState, parameters);

            // todo: check sign
            final double f = Math.exp(currentEnergy - candidateEnergy);

            if (f >= 1 || MonteCarloUtils.MC_RND.nextDouble() < f) {
                currentState = candidateState;
                currentEnergy = candidateEnergy;
                states.add(currentState);
            }
        }

        return states;
    }

    public int getMaxIter() {
        return maxIter;
    }
}