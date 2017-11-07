package com.antigenomics.pmem.entities;

import com.milaboratory.core.sequence.AminoAcidSequence;
import com.sun.istack.internal.NotNull;

public class Peptide implements Entity {
    private final AminoAcidSequence sequence;

    public Peptide(@NotNull final AminoAcidSequence sequence) {
        this.sequence = sequence;
    }

    public AminoAcidSequence getSequence() {
        return sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peptide peptide = (Peptide) o;

        return sequence.equals(peptide.sequence);
    }

    @Override
    public int hashCode() {
        return sequence.hashCode();
    }
}
