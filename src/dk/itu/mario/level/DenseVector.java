package dk.itu.mario.level;

public class DenseVector extends Vector{
    

	//The data
    private double[] data;
    
    /**
     * Make a new dense vector
     * @param data the data
     */
    public DenseVector(double[] data) {
        this.data = data;
    }
    
    /**
     * Make a new dense vector of the given size
     * @param size the size to make it
     */
    public DenseVector(int size) {
        data = new double[size];
    }

    /**
     * @see linalg.Vector#size()
     */
    public int size() {
        return data.length;
    }

    /**
     * @see linalg.Vector#get(int)
     */
    public double get(int i) {
        return data[i];
    }
    
    /**
     * @see linalg.Vector#set(int, double)
     */
    public void set(int i, double value) {
        data[i] = value;
    }
}
