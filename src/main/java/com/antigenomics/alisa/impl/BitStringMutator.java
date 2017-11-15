package com.antigenomics.alisa.impl;

import com.antigenomics.alisa.entities.BitString;
import com.antigenomics.alisa.estimator.mc.EntityMutator;
import com.antigenomics.alisa.estimator.mc.MonteCarloUtils;
import com.sun.istack.internal.NotNull;

import java.util.Arrays;

public class BitStringMutator implements EntityMutator<BitString> {
    @Override
    public BitString mutate(@NotNull final BitString entity) {
        boolean[] bitSet = Arrays.copyOf(entity.getBits(), entity.getBits().length);

        final int mutationPos = MonteCarloUtils.nextInt(bitSet.length);

        bitSet[mutationPos] = !bitSet[mutationPos];

        return new BitString(bitSet);
    }
}
