package dk.itu.mario.level;

import java.io.Serializable;
import java.text.DecimalFormat;

public abstract class Vector implements Serializable, Copyable {
    

    /**
     * Get the size of the vector (the number of rows)
     * @return the number of rows
     */
    public abstract int size();
    
    /**
     * Get an element from the vecotr
     * @param i the element to get
     * @return the element
     */
    public abstract double get(int i);
    
    /**
     * Get some sub portion of the vector
     * @param ia the starting index (inclusive)
     * @param ib the ending index (exclusive)
     * @return the result
     */
    public Vector get(int ia, int ib) {
        double[] result = new double[ib - ia];
        for (int i = 0; i < result.length; i++) {
            result[i] = get(ia + i);
        }
        return new DenseVector(result);
    }
    
    /**
     * Set a portion of the vector
     * @param i the starting porition
     * @param values the values
     */
    public void set(int i, Vector values) {
        for (int row = i; row-i < values.size(); row++) {
            set(row, values.get(row-i));
        }
    }
    
    /**
     * Set an element in the vector
     * @param i the element to set
     * @param d the new value
     */
    public abstract void set(int i, double d);

	/**
	 * Make a copy of this vector
	 * @return the copy
	 */
	public DenseVector copy() {
		double[] copy = new double[size()];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = get(i);
		}
		return new DenseVector(copy);
	}
}