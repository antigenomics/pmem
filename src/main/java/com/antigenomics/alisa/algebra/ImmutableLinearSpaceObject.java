package com.antigenomics.alisa.algebra;

import com.antigenomics.alisa.encoding.Encoding;

public interface ImmutableLinearSpaceObject<O extends ImmutableLinearSpaceObject<O>>
        extends Encoding {
    O add(final O other);

    O multiply(final double scalar);

    double norm1();

    double norm2();
}