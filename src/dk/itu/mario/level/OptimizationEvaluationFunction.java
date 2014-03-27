package dk.itu.mario.level;

public class OptimizationEvaluationFunction {
	  
    /**
     * The weights for the things that can be put in the sack
     */
    private double[] weights;
    
    /**
     * The volumes for the things that can be put in the sack
     */
    private double[] volumes;
    
    /**
     * The maximum volume in the knapsack
     */
    private double maxVolume;
    
    /**
     * The maximum sum of all items
     */
    private double maxVolumeSum;
    
    /**
     * Make a new knapsack evaluation function
     * @param w the set of values
     * @param v the set of volumes
     * @param maxV the maximum volumes
     * @param maxC the maximum counts
     */
    public OptimizationEvaluationFunction(double[] w, double[] v, double maxV, int[] maxC) {
        weights = w;
        volumes = v;
        maxVolume = maxV;
        for (int i = 0; i < v.length; i++) {
            maxVolumeSum += maxC[i] * v[i];
        }
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        Vector data = d.getData();
        double volume = 0;
        double value = 0;
        double[] result = new double[2];
        
        for (int i = 0; i < data.size(); i++) {
            volume += volumes[i] * data.get(i);
            value += weights[i] * data.get(i);
        }
        if (volume > maxVolume) {
            double smallNumber = 1E-10;
            result[0] = smallNumber*(maxVolumeSum - volume);
            return result[0];
        } else {
            return value;
        }   
    } 
}
