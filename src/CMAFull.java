import Jama.CholeskyDecomposition;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import org.vu.contest.ContestEvaluation;

import java.util.*;

/**
 * Author: alenaponomareva
 * Date: 14/10/16
 * Time: 23:44
 */
public class CMAFull {

    public static Random rnd;
    public static ContestEvaluation evaluation;
    public static int evaluationLimit;
    public static double lastFitness;


    private List<Individual> population;

    private int n = 10;
    private int lambda, mu;
    private double sigma, mueff, ccov, cc, mucov, cs, damps, chin;
    Matrix m, C, weights, pc, ps;

    public CMAFull(int newLambda, int newMu) {
        lambda = newLambda;
        mu = newMu;

        sigma = 2.5;
        double max = 5.0;
        double min = -5.0;

        // constants and weights
        double[] weightsDouble = new double[mu];
        double sum = 0.0, sum2 = 0.0;
        for (int i = 0; i < mu; i++) {
//            weightsDouble[i] = 1.0 / mu;
            weightsDouble[i] = Math.log(mu + 1.0) - Math.log(i + 1.0);
            sum += weightsDouble[i];
        }
        for (int i = 0; i < mu; i++) {
            weightsDouble[i] /= sum;
            sum2 += weightsDouble[i] * weightsDouble[i];
        }
        weights = new Matrix(weightsDouble, mu);

        mueff = 1.0 / sum2;

        mucov = mueff;


        cc = 4.0 / (n + 4.0);
        cs = (mueff + 2.0) / (n + mueff + 3.0);

        ccov = mucov / Math.pow(n, 2.0);

        damps = 1.0 + 2.0 * Math.max(0.0, Math.sqrt((mueff - 1.0) / (n + 1.0)) - 1.0) + cs;

        chin = Math.sqrt(n) * (1.0 - 1.0 / (4.0 * n) + 1.0 / (21.0 * Math.pow(n, 2.0)));

        // vectors and matrices
        double[] mDouble = new double[n];
        for (int i = 0; i < n; i++) {
            mDouble[i] = max * 2.0 * rnd.nextDouble() + min;
        }
        m = new Matrix(mDouble, n);
        C = Matrix.identity(n, n);
        pc = new Matrix(new double[n], n);
        ps = new Matrix(new double[n], n);
    }

    private Matrix getNewIndividual(Matrix mean, Matrix c) {
        Matrix yi = c.times(getGaussianNoiseVector(n));
        Matrix yi_s = yi.times(sigma);
        return mean.plus(yi_s);

    }

    public int makeStep(int currentCount) {
        // 1. generate lambda individuals
        population = new ArrayList<>();

        // get B and D parts from C
        C = triu(C);
        EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecomposition(C);
        Matrix B = eigenvalueDecomposition.getV();
        Matrix D = eigenvalueDecomposition.getD();
        // get C^1/2 , C^(-1/2)
        Matrix sqrtC = B.times(vecToDiag(getSqrtElementx(diagToVec(D))));
        Matrix InvertSqrtC = B.times(vecToDiag(getInverseSqrtElementx(diagToVec(D)))).times(B.transpose());


        for (int i = 0; i < lambda; i++) {
            population.add(new Individual(getNewIndividual(m, sqrtC).transpose().getArray()[0]));
        }
        // 2. sort and cut
        currentCount = evaluateFitness(currentCount);
        sortPopulation();
        cutPopulation(mu);

        // 3. calculate new m and yw
        Matrix yw = new Matrix(new double[n], n);
        Matrix Cmu = new Matrix(new double[n][n]);
        for (int i = 0; i < mu; i++) {
            Matrix individual = new Matrix(population.get(i).getGenome(), n);
            individual = individual.minus(m).times(1.0 / sigma);

            yw = yw.plus(individual.times(weights.get(i, 0)));
            Cmu = Cmu.plus(individual.times(individual.transpose()).times(weights.get(i, 0)));
        }

        // 4. update mean
        Matrix mold = m.copy();
        m = m.plus(yw.times(sigma));


        // 5. update evolutionary paths
        Matrix ps1 = ps.times(1.0 - cs);
        Matrix ps2 = InvertSqrtC.times((m.minus(mold)).times(1.0 / sigma)).times(Math.sqrt(cs * (2.0 - cs) * mueff));

        ps = ps1.plus(ps2);
        double normPs = norm(ps);

        int hsig = normPs / Math.sqrt(1.0 - Math.pow(1.0 - cs, 2.0 * (currentCount / lambda))) < (1.5 + 1.0 / (n - 0.5)) * chin ? 1 : 0;

        Matrix pc1 = pc.times(1.0 - cc);
        Matrix pc2 = (m.minus(mold)).times(1.0 / sigma).times(Math.sqrt(cc * (2.0 - cc) * mueff) * hsig);
        pc = pc1.plus(pc2);


        // 5. update covariance
        Matrix C1 = C.times(1.0 - ccov);
        Matrix C2 = (pc.times(pc.transpose()).plus(C.times((1 - hsig) * (cc * (2.0 - cc))))).times(ccov / mucov);
        Matrix C3 = Cmu.times(ccov * (1.0 - 1.0 / mucov));
        C = C1.plus(C2).plus(C3);

        sigma = sigma * Math.exp((cs / damps) * (normPs / chin - 1.0));
//        System.out.println("sigma " + sigma);

        lastFitness = population.get(0).getFitness();
//        System.out.println(lastFitness);
        return currentCount;
    }

    private void print(String add, Matrix m) {
        System.out.println(add);
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                System.out.print(m.get(i, j) + " ");
            }
            System.out.println();
        }
    }

    public void sortPopulation() {
        Collections.sort(population, new Comparator<Individual>() {
            @Override
            public int compare(Individual p1, Individual p2) {
                if (p1.getFitness() < p2.getFitness()) {
                    return 1;
                } else if (p1.getFitness() > p2.getFitness()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    public void cutPopulation(int nKeep) {
        population = population.subList(0, nKeep);
    }

    public int evaluateFitness(final int evaluationCount) {
        int currentCount = evaluationCount;

        for (Individual e : population) {
            if (evaluationCount < evaluationLimit) {
                Object oFitness = evaluation.evaluate(e.getGenome());
                if (oFitness == null) break;
                e.setFitness((double) oFitness);
                ++currentCount;
            }
        }
        return currentCount;
    }


    private Matrix getGaussianNoiseVector(int size) {
        double noise[] = new double[size];
        for (int i = 0; i < size; i++) {
            noise[i] = rnd.nextGaussian();
        }
        return new Matrix(noise, size);
    }

    private Matrix diagToVec(Matrix a) {
        Matrix Dflat = new Matrix(new double[a.getRowDimension()], a.getRowDimension());
        for (int i = 0; i < a.getColumnDimension(); i++) {
            Dflat.set(i, 0, a.get(i, i));
        }
        return Dflat;
    }


    private Matrix triu(Matrix a) {
        for (int i = 0; i < a.getColumnDimension(); i++) {
            for (int j = i + 1; j < a.getRowDimension(); j++) {
                a.set(j, i, a.get(i, j));
            }
        }
        return a;
    }

    private Matrix vecToDiag(Matrix a) {
        Matrix matrix = new Matrix(new double[a.getRowDimension()][a.getRowDimension()]);
        for (int i = 0; i < a.getRowDimension(); i++) {
            matrix.set(i, i, a.get(i, 0));
        }
        return matrix;
    }

    private double norm(Matrix a) {
        return Math.sqrt((a.transpose().times(a)).getArray()[0][0]);
    }

    private Matrix getSqrtMatrix(Matrix c) {

        c = triu(c);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(c);
        Matrix a = choleskyDecomposition.getL();


        return a;
    }

    private Matrix getSqrtElementx(Matrix c) {

        for (int i = 0; i < c.getRowDimension(); i++) {
            c.set(i, 0, Math.sqrt(c.get(i, 0)));
        }
        return c;
    }

    private Matrix getInverseSqrtElementx(Matrix c) {

        for (int i = 0; i < c.getRowDimension(); i++) {
            c.set(i, 0, 1.0 / Math.sqrt(c.get(i, 0)));
        }
        return c;
    }

    private Matrix getBD(Matrix c) {
        c = triu(c);
        EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecomposition(c);
        Matrix B = eigenvalueDecomposition.getV();
        Matrix D = eigenvalueDecomposition.getD();
        return B.times(vecToDiag(getSqrtElementx(diagToVec(D))));

    }

    private Matrix getInverseSqrtMatrix(Matrix c) {

        EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecomposition(c);
        Matrix B = eigenvalueDecomposition.getV();
        Matrix D = eigenvalueDecomposition.getD();


        Matrix dflat = diagToVec(D);
        for (int i = 0; i < dflat.getRowDimension(); i++) {
            dflat.set(i, 0, 1.0 / Math.sqrt(dflat.get(i, 0)));
        }


        return B.times(vecToDiag(dflat).times(B.transpose()));
    }
}
