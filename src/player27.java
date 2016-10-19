
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Properties;

public class player27 implements ContestSubmission {
    Random rnd_;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private boolean isMultimodal;

    public player27() {
        rnd_ = new Random();
    }

    public void setSeed(long seed) {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation) {
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
        if (isMultimodal) {
            // Do sth
        } else {
            // Do sth else
        }
    }


    public void run() {
        if (!isMultimodal) {
            runWithEvolPaths();
        } else {
            runWithIPOPandBIPOP();
        }
    }


    public void runCMAFull() {
        // Run your algorithm here

        CMAFull.evaluation = evaluation_;
        CMAFull.evaluationLimit = evaluations_limit_;
        CMAFull.rnd = rnd_;
        CMAFull.lastFitness = -1000.0;

        CMAFull cma = new CMAFull(100, 50);

        int evals = 0;
        // init population
        // calculate fitness
        while (evals < evaluations_limit_) {
            evals = cma.makeStep(evals);
        }
        System.out.println(CMAFull.lastFitness);
    }

    public void runWithFullPathsAndRandomRestartsWithConvergance() {

        CMAFull.evaluation = evaluation_;
        CMAFull.evaluationLimit = evaluations_limit_;
        CMAFull.rnd = rnd_;
        int convergenceCheckInterval = 50;


        int[] lambdas = {100, 150, 200, 250};
        int[] mus = {100 / 4, 150 / 4, 200 / 4, 250 / 4};
        CMAFull[] cmas = new CMAFull[lambdas.length];
        List<Integer> hopelessParametersIndices;
        int evaluationsPerRun = evaluations_limit_ / lambdas.length;
        double badnessThreshold = 3.0;


        for (int i = 0; i < cmas.length; i++) {
            cmas[i] = new CMAFull(lambdas[i], mus[i]);
        }

        boolean enterEvolutionLoop = false;

        int globalEvals = 0;
        int runsCount = 0;
        while (globalEvals < evaluations_limit_) {
            enterEvolutionLoop = false;
            System.out.println("count #" + runsCount);
            runsCount++;

            hopelessParametersIndices = new ArrayList<>();
            // first run for each of cmas
            for (int i = 0; i < cmas.length; i++) {
                int currentEvals = 0;


                while (currentEvals < evaluationsPerRun - lambdas[i]) {
                    enterEvolutionLoop = true;
                    currentEvals = cmas[i].makeStep(currentEvals);
                    int generationsCount = cmas[i].getFitnesses().size() - 1;
                    if (generationsCount > convergenceCheckInterval) {
                        if (cmas[i].getFitnesses().get(generationsCount) < cmas[i].getFitnesses().get(generationsCount - convergenceCheckInterval)) {
                            hopelessParametersIndices.add(i);
                            globalEvals += currentEvals;
                            System.out.println("i converged with lambda: " + lambdas[i] + " mu: " + mus[i] + " fitness: " + cmas[i].getFitnesses().get(generationsCount));
                            break;
                        }
                    }
                }
                globalEvals += currentEvals;
            }

            if (hopelessParametersIndices.size() != lambdas.length) {

                // update lambdas and mus
                int newLambdas[] = new int[lambdas.length - hopelessParametersIndices.size()];
                int newMus[] = new int[lambdas.length - hopelessParametersIndices.size()];
                CMAFull newCmas[] = new CMAFull[lambdas.length - hopelessParametersIndices.size()];

                int count = 0;
                for (int i = 0; i < lambdas.length; i++) {
                    if (!hopelessParametersIndices.contains(i)) {
                        newLambdas[count] = lambdas[i];
                        newMus[count] = mus[i];
                        newCmas[count] = cmas[i];
                        count++;
                    }
                }
                lambdas = newLambdas;
                mus = newMus;
                cmas = newCmas;
            }

            evaluationsPerRun = (evaluations_limit_ - globalEvals) / lambdas.length;
            if (!enterEvolutionLoop) break;
//            System.out.println(lambda + " " + mu + " " + CMAFull.lastFitness);

        }
        for (int i = 0; i < cmas.length; i++) {
            int generationsCount = cmas[i].getFitnesses().size() - 1;
            System.out.println("lambda: " + lambdas[i] + " mu: " + mus[i] + " fitness: " + cmas[i].getFitnesses().get(generationsCount));

        }
    }

    public void runWithIPOPandBIPOP() {

        CMAFull.evaluation = evaluation_;
        CMAFull.evaluationLimit = evaluations_limit_;
        CMAFull.rnd = rnd_;

        int lambda;
        int mu;
        int evals;
        double prop = 0.35;

        // run BIPOP
        int bipopTimes2 = 5;
        int BIBOPLambdas2[] = new int[bipopTimes2];
        for (int i = 0; i < bipopTimes2; i++) {
            BIBOPLambdas2[i] = getBIPOPLambda(100, 400);
        }

        evals = 0;
        for (int i = 0; i < bipopTimes2; i++) {
            CMAFull.lastFitness = -1000.0;
            int currentEvals = 0;
            lambda = BIBOPLambdas2[i];
            mu = lambda / 7;
            CMAFull cma = new CMAFull(lambda, mu);

            while (currentEvals < (int)(evaluations_limit_ *(prop)) / bipopTimes2 - lambda) {
                currentEvals = cma.makeStep(currentEvals);
            }
            evals += currentEvals;
//            System.out.println("bipop " + lambda + " " + mu + " " + CMAFull.lastFitness);
        }

//         run BIPOP
        int bipopTimes = 15;
        int BIBOPLambdas[] = new int[bipopTimes];
        for (int i = 0; i < bipopTimes; i++) {
            BIBOPLambdas[i] = getBIPOPLambda(100, 320);
        }

        evals = 0;
        for (int i = 0; i < bipopTimes; i++) {
            CMAFull.lastFitness = -1000.0;
            int currentEvals = 0;
            lambda = BIBOPLambdas[i];
            mu = lambda / 2;
            CMAFull cma = new CMAFull(lambda, mu);

            while (currentEvals < (int)(evaluations_limit_ *(1.0-prop)) / bipopTimes - lambda) {
                currentEvals = cma.makeStep(currentEvals);
            }
            evals += currentEvals;
//            System.out.println("bipop " + lambda + " " + mu + " " + CMAFull.lastFitness);
        }
    }

    private int getBIPOPLambda(double lambdaDef, double lambdaLast) {
        double ls = lambdaDef * Math.pow(0.5 * lambdaLast / lambdaDef, Math.pow(rnd_.nextDouble(), 2));
        return (int) ls;
    }

    public void runWithIPOPandBIPOPGoodFor3() {

        CMAFull.evaluation = evaluation_;
        CMAFull.evaluationLimit = evaluations_limit_;
        CMAFull.rnd = rnd_;

        int lambdaDef = 10;
        int lambda = lambdaDef;
        int mu = lambda / 2;
        int evals = 0;

        //  IPOP
        int ipopTimes = 5;
        for (int i = 0; i < ipopTimes; i++) {
            CMAFull.lastFitness = -1000.0;
            int currentEvals = 0;

            CMAFull cma = new CMAFull(lambda, mu);
            mu = lambda;
            lambda = lambda * 2;

            while (evals + currentEvals < evaluations_limit_ / 2 - lambda / 2) {
                currentEvals = cma.makeStep(currentEvals);
                if (currentEvals >= evaluations_limit_ / 2 / ipopTimes) break;
            }
            evals += currentEvals;
//            System.out.println("ipop " + lambda / 2 + " " + mu / 2 + " " + CMAFull.lastFitness);
        }

        // run BIPOP
        int bipopTimes = 10;
        int BIBOPLambdas[] = new int[bipopTimes];
        for (int i = 0; i < bipopTimes; i++) {
            BIBOPLambdas[i] = getBIPOPLambda(lambdaDef, lambda / 2);
        }

        evals = 0;
        for (int i = 0; i < bipopTimes; i++) {
            CMAFull.lastFitness = -1000.0;
            int currentEvals = 0;
            lambda = BIBOPLambdas[i];
            mu = lambda / 2;
//            double newSigma = 2.5 * Math.pow(10.0, -2.0 * rnd_.nextDouble());
            CMAFull cma = new CMAFull(lambda, mu);

            while (evals + currentEvals < evaluations_limit_ / 2 - lambda) {
                currentEvals = cma.makeStep(currentEvals);
                if (currentEvals >= evaluations_limit_ / 2 / bipopTimes) break;
            }
            evals += currentEvals;
//            System.out.println("bipop " + lambda + " " + mu + " " + CMAFull.lastFitness);
        }
    }

    public void runWithFullPathsAndRandomRestarts() {

        CMAFull.evaluation = evaluation_;
        CMAFull.evaluationLimit = evaluations_limit_;
        CMAFull.rnd = rnd_;

        int[] lambdas = {150, 150, 150, 150};

        int evals = 0;
        int times = lambdas.length;
        for (int i = 0; i < times; i++) {
            CMAFull.lastFitness = -1000.0;
            int currentEvals = 0;
            int lambda = lambdas[i];
            int mu = (int) (lambda / 7.0);
            CMAFull cma = new CMAFull(lambda, mu);

            while (evals + currentEvals < evaluations_limit_ - lambda) {
                currentEvals = cma.makeStep(currentEvals);
                if (currentEvals >= evaluations_limit_ / times) break;
            }
            evals += currentEvals;
            System.out.println(lambda + " " + mu + " " + CMAFull.lastFitness);

        }
    }

    public void runWithEvolPathsAndRandomRestarts() {

        CMAEvolPaths.evaluation = evaluation_;
        CMAEvolPaths.evaluationLimit = evaluations_limit_;
        CMAEvolPaths.rnd = rnd_;

        int[] lambdas = {350, 400, 450, 500};

        int evals = 0;
        int times = lambdas.length;
        for (int i = 0; i < times; i++) {
            CMAEvolPaths.lastFitness = -1000.0;
            int currentEvals = 0;
            int lambda = lambdas[i];
            int mu = (int) (lambda / 4.0);
            CMAEvolPaths cma = new CMAEvolPaths(lambda, mu);

            while (evals + currentEvals < evaluations_limit_ - lambda) {
                currentEvals = cma.makeStep(currentEvals);
                if (currentEvals >= evaluations_limit_ / times) break;
            }
            evals += currentEvals;

        }
    }


    public void runWithEvolPaths() {
        // Run your algorithm here

        CMAEvolPaths.evaluation = evaluation_;
        CMAEvolPaths.evaluationLimit = evaluations_limit_;
        CMAEvolPaths.rnd = rnd_;
        CMAEvolPaths.lastFitness = -1000.0;

        CMAEvolPaths cma = new CMAEvolPaths(100, 50);

        int evals = 0;
        // init population
        // calculate fitness
        while (evals < evaluations_limit_) {
            evals = cma.makeStep(evals);
        }
//        System.out.println(CMAEvolPaths.lastFitness);
    }
}