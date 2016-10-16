
import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

import java.util.Random;
import java.util.Properties;

public class player27 implements ContestSubmission
{
    Random rnd_;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private boolean isMultimodal;

    public player27()
    {
        rnd_ = new Random();
    }

    public void setSeed(long seed)
    {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation)
    {
        // Set evaluation problem used in the run
        evaluation_ = evaluation;

        // Get evaluation properties
        Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
        // Property keys depend on specific evaluation
        // E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

        // Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }


    public void run()
    {
        if (!isMultimodal) {
            runWithEvolPaths();
        } else {
            runWithFullPathsAndRandomRestarts();
        }
    }


    public void runCMAFull()
    {
        // Run your algorithm here

        CMAFull.evaluation = evaluation_;
        CMAFull.evaluationLimit = evaluations_limit_;
        CMAFull.rnd = rnd_;
        CMAFull.lastFitness = -1000.0;

        CMAFull cma = new CMAFull(100, 50);

        int evals = 0;
        // init population
        // calculate fitness
        while(evals < evaluations_limit_) {
            evals = cma.makeStep(evals);
        }
//        System.out.println(CMAFull.lastFitness);
    }

    public void runWithFullPathsAndRandomRestarts() {

        CMAFull.evaluation = evaluation_;
        CMAFull.evaluationLimit = evaluations_limit_;
        CMAFull.rnd = rnd_;

        int[] lambdas = {100, 200, 250, 300, 350};
        int[] mus = {50, 100, 250/4, 300/4, 350/4};

        int evals = 0;
        int times =  lambdas.length;
        for (int i = 0; i < times; i++) {
            CMAFull.lastFitness = -1000.0;
            int currentEvals = 0;
            int lambda = lambdas[i];
            int mu = mus[i];
            CMAFull cma = new CMAFull(lambda, mu);

            while (evals + currentEvals < evaluations_limit_ - lambda) {
                currentEvals = cma.makeStep(currentEvals);
                if (currentEvals >= evaluations_limit_/times) break;
            }
            evals += currentEvals;
//            System.out.println(lambda + " " + mu + " " + CMAFull.lastFitness);

        }
    }

    public void runWithEvolPathsAndRandomRestarts() {

        CMAEvolPaths.evaluation = evaluation_;
        CMAEvolPaths.evaluationLimit = evaluations_limit_;
        CMAEvolPaths.rnd = rnd_;

        int[] lambdas = {350, 400, 450, 500};

        int evals = 0;
        int times =  lambdas.length;
        for (int i = 0; i < times; i++) {
            CMAEvolPaths.lastFitness = -1000.0;
            int currentEvals = 0;
            int lambda = lambdas[i];
            int mu = (int)(lambda / 4.0);
            CMAEvolPaths cma = new CMAEvolPaths(lambda, mu);

            while (evals + currentEvals < evaluations_limit_ - lambda) {
                currentEvals = cma.makeStep(currentEvals);
                if (currentEvals >= evaluations_limit_/times) break;
            }
            evals += currentEvals;

        }
    }


    public void runWithEvolPaths()
    {
        // Run your algorithm here

        CMAEvolPaths.evaluation = evaluation_;
        CMAEvolPaths.evaluationLimit = evaluations_limit_;
        CMAEvolPaths.rnd = rnd_;
        CMAEvolPaths.lastFitness = -1000.0;

        CMAEvolPaths cma = new CMAEvolPaths(100, 50);

        int evals = 0;
        // init population
        // calculate fitness
        while(evals < evaluations_limit_) {
            evals = cma.makeStep(evals);
        }
//        System.out.println(CMAEvolPaths.lastFitness);
    }
}