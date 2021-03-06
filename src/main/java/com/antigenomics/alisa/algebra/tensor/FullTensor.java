package com.antigenomics.alisa.algebra.tensor;

import com.antigenomics.alisa.algebra.LinearAlgebraUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.antigenomics.alisa.algebra.LinearAlgebraUtils.getFullTensorIndex;

public final class FullTensor extends Tensor {
    protected FullTensor(double[] elements,
                         int numberOfRows, int numberOfColumns,
                         int numberOfCategoryRows, int numberOfCategoryColumns) {
        this(elements, numberOfRows, numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns, false);
    }

    public FullTensor(double[] elements,
                      int numberOfRows, int numberOfColumns,
                      int numberOfCategoryRows, int numberOfCategoryColumns,
                      boolean safe) {
        super(elements, numberOfRows, numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns,
                false, false, safe);
    }

    protected FullTensor(double[][][][] elements) {
        super(new double[elements.length * elements[0].length * elements[0][0].length * elements[0][0][0].length],
                elements.length, elements[0].length, elements[0][0].length, elements[0][0][0].length,
                false, false, false);
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            double[][][] r1 = elements[i];
            for (int j = 0; j < numberOfColumns; j++) {
                double[][] c1 = r1[i];
                for (int a = 0; a < numberOfCategoryRows; a++) {
                    double[] a1 = c1[i];
                    for (int b = 0; b < numberOfColumns; b++) {
                        this.elements[k++] = a1[b];
                    }
                }
            }
        }
    }

    public FullTensor(Iterable<IndexedTensorValue> elements,
                      int numberOfRows, int numberOfColumns,
                      int numberOfCategoryRows, int numberOfCategoryColumns) {
        this(new double[numberOfRows * numberOfColumns * numberOfCategoryRows * numberOfCategoryColumns],
                numberOfRows, numberOfColumns,
                numberOfCategoryRows, numberOfCategoryColumns);
        for (IndexedTensorValue e : elements) {
            int fullMatrixIndex = getFullTensorIndex(e.getRowIndex(), e.getColumnIndex(),
                    e.getFirstCategoryIndex(), e.getSecondCategoryIndex(),
                    numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns);
            this.elements[fullMatrixIndex] = e.getDoubleValue();
        }
    }

    @Override
    protected int getLinearIndex(int rowIndex, int columnIndex, int rowCategory, int columnCategory) {
        return LinearAlgebraUtils.getFullTensorIndex(rowIndex, columnIndex, rowCategory, columnCategory,
                numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns);
    }

    @Override
    protected Tensor withElements(double[] elements) {
        return new FullTensor(elements,
                numberOfRows, numberOfColumns, numberOfCategoryRows, numberOfCategoryColumns);
    }

    @Override
    public int getEffectiveSize() {
        int effectiveSize = 0;
        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                for (int a = 0; a < numberOfCategoryRows; a++) {
                    for (int b = 0; b < numberOfColumns; b++) {
                        if (elements[k++] != 0) {
                            effectiveSize++;
                        }
                    }
                }
            }
        }
        return effectiveSize;
    }

    @Override
    public Iterator<IndexedTensorValue> iterator() {
        List<IndexedTensorValue> values = new LinkedList<>();

        int k = 0;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                for (int a = 0; a < numberOfCategoryRows; a++) {
                    for (int b = 0; b < numberOfCategoryColumns; b++) {
                        values.add(new IndexedTensorValue(i, j, a, b,
                                elements[k++]));
                    }
                }
            }
        }
        return values.iterator();
    }
}
