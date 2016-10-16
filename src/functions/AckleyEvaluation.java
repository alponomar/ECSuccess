package functions;

import org.vu.contest.ContestEvaluation;

import java.util.Properties;

// This is an example evalation. It is based on the Rastrigin. It is a maximization problem with a maximum of 10 for 
//  	vector a_.
// The Rastrigin function itself is for minimization with minimum of 0.
// Base performance is calculated as the distance of the expected fitness of a random search (with the same amount of available
//	evaluations) on the Rastrigin function to the function minimum, thus Base = E[f_best_random] - ftarget. Fitness is scaled
//	according to this base, thus Fitness = 10 - 10*(f-fbest)/Base
public class AckleyEvaluation implements ContestEvaluation
{
	// Evaluations budget
	private final static int EVALS_LIMIT_ = 100000;
	// The base performance. It is derived by doing  random search on the F&P function (see function method) with the same
	//  amount of evaluations
	private final static double BASE_ = 36.6939274039372;
	// The minimum of the sphere function
	private final static double ftarget_=0;

	// Best fitness so far
	private double best_;
	// Evaluations used so far
	private int evaluations_;

	// Properties of the evaluation
	private String multimodal_ = "true";
	private String regular_ = "true";
	private String separable_ = "false";
	private String evals_ = Integer.toString(EVALS_LIMIT_);

	private static double ALPHA_ = 10;

	public AckleyEvaluation()
	{
		best_ = 0;
		evaluations_ = 0;	
	}

	// The Rastrigin function. The global minumum is 0.
	private double function(double[] x)
	{	
		int dim = 10;
		
		double sumSquared = 0;
		double sum2pi = 0;
		for(int i=0; i<dim; i++) {
//			x[i] = x[i] - 3.0;
			sumSquared += (x[i] - 3.0)*(x[i] - 3.0);
			sum2pi += Math.cos(2*Math.PI*(x[i] - 3.0));
		}
		return -20.0 * Math.exp(-0.2 * Math.sqrt(1.0 / dim) * sumSquared) -  Math.exp(1.0 / dim * sum2pi) + 20.0 +Math.exp(1.0);
	}
	
	@Override
	public Object evaluate(Object result) 
	{
		// Check argument
		if(!(result instanceof double[])) throw new IllegalArgumentException();
		double ind[] = (double[]) result;
		if(ind.length!=10) throw new IllegalArgumentException();
		
		if(evaluations_>EVALS_LIMIT_) return null;
		
		// Transform function value (F&P is minimization).
		// Normalize using the base performance
		double f = 10 - 10*( (function(ind)-ftarget_) / BASE_ ) ;
		if(f>best_) best_ = f;
		evaluations_++;
		
		return new Double(f);
	}

	@Override
	public Object getData(Object arg0) 
	{
		return null;
	}

	@Override
	public double getFinalResult() 
	{
		return best_;
	}

	@Override
	public Properties getProperties() 
	{
		Properties props = new Properties();
		props.put("Multimodal", multimodal_);
		props.put("Regular", regular_);
		props.put("Separable", separable_);
		props.put("Evaluations", evals_);
		return props;
	}
}
