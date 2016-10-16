/**
 * Author: alenaponomareva
 * Date: 14/10/16
 * Time: 23:43
 */
public class Individual {
    private double[] genome;
    private double fitness;

    public Individual(double[] genome) {
        this.genome = genome;
        this.fitness = -10000.0;
    }

    public double[] getGenome() {
        return genome;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
