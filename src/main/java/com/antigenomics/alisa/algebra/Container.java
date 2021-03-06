package com.antigenomics.alisa.algebra;

import com.antigenomics.alisa.TypedCloneable;

import java.io.Serializable;

/**
 * A container of indexed values. Can be either accessed by indices or used as an iterable.
 * The efficiency of fetching an indexed value depends on the implementation (sparse/dense).
 *
 * @param <V> element type
 * @param <C> container type
 */
public interface Container<V extends IndexedValue<V>, C extends Container<V, C>>
        extends Iterable<V>, TypedCloneable<C>, Serializable {
    /**
     * Checks if this container is backed by a sparse indexed value storage.
     *
     * @return true if sparse storage is used.
     */
    boolean isSparse();

    /**
     * Fetches the value stored at a given index combination.
     *
     * @param indices indices of a container element
     * @return double value
     */
    double getAt(int... indices);

    /**
     * Gets the effective size of the container, i.e.
     * the number of non-zero elements.
     *
     * @return number of non-zero indexed values.
     */
    int getEffectiveSize();

    /**
     * Gets a sparse storage-backed container of the same type.
     * This method always performs a deep copy.
     *
     * @return a sparse container.
     */
    C asSparse();

    /**
     * Gets a dense storage-backed container of the same type.
     * This method always performs a deep copy.
     *
     * @return a dense container.
     */
    C asDense();
}
