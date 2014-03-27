package dk.itu.mario.level;

import java.io.Serializable;

public class Instance implements Serializable, Copyable {

    /**
     * The label for this instance
     */
    private Instance label;
    
    /**
     * The vector storing the data
     */
    private Vector data;
    
    /**
     * The weight of the instance
     */
    private double weight;
    /**
     * Make a new instance from the given data
     * @param data the data itself
     * @param label the label
     * @param weight the weight
     * @param dataSet the data set
     */
    public Instance(Vector data, Instance label, double weight) {
        this.data = data;
        this.label = label;
        this.weight = weight;
    }
    
    /**
     * Make a new instance from the given data
     * @param data the data itself
     * @param label the label
     */
    public Instance(Vector data, Instance label) {
        this.data = data;
        this.label = label;
        this.weight = 1.0;
    }
    
    /**
     * Make a new instance using the given vector
     * @param v the vector of data
     */
    public Instance(Vector v) {
        data = v;
        weight = 1.0;
    }
    
    /**
     * Make a new instance
     * @param ds the data
     */
    public Instance(double[] ds) {
        data = new DenseVector(ds);
        weight = 1.0;
    }
    
    /**
     * Make a new instance with the given value
     * @param val the value
     */
    public Instance(double val) {
        data = new DenseVector(1);
        data.set(0, val);
        weight = 1.0;
    }
    
    /**
     * Make a new instance with the given value
     * @param val the value
     */
    public Instance(int val) {
        data = new DenseVector(1);
        data.set(0, val);
        weight = 1.0;
    }
    
    /**
     * Make a new discrete input ouptu instance
     * @param i the input
     * @param o the output
     */
    public Instance(int i, int o) {
        this(i);
        label = new Instance(o);
    }

    /**
     * Make a new double input discrete output
     * @param ds the input
     * @param i the output
     */
    public Instance(double[] ds, int i) {
        this(ds);
        label = new Instance(i);
    }

    /**
     * Make a new input output instance
    * @param ds the data
    * @param b the label
    */
    public Instance(double[] ds, boolean b) {
        this(ds);
        label = new Instance(b);
    }

    
    
    /**
     * Make a new instance with the given boolean value
     * @param val the value
     */
    public Instance(boolean val) {
        this(val ? 1 : 0);
    }
    
    /**
     * Get the size of the instance
     * @return the size
     */
    public int size() {
        return data.size();
    }
   
    /**
     * Get the data vector
     * @return the data
     */
    public Vector getData() {
        return data;
    }

    /**
     * Set the data vector
     * @param vector the data vector
     */
    public void setData(Vector vector) {
        data = vector;
    }

    /**
     * Set the label for this instance
     * @param instance the label
     */
    public void setLabel(Instance instance) {
        label = instance;
    }

    /**
     * Set the weight for the instance
     * @param d the new weight
     */
    public void setWeight(double d) {
        weight = d;
    }
    
    /**
     * Make a new instance
     * @return the copy
     */
    public Copyable copy() {
        if (label != null) {
            return new Instance((Vector) data.copy(), (Instance) label.copy(), weight);
        } else {
            return new Instance((Vector) data.copy(), null, weight);
        }
    }
}
