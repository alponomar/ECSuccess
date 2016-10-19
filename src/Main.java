import functions.*;


public class Main {

    public static void main(String[] args) {
        int seeds[] = {1, 10, 23, 581, 75, 69, 333, 9, 4, 11, 324, 0, 21};
//
        player27 player27 = new player27();
        player27.setSeed(10);
        player27.setEvaluation(new RastriginEvaluation());
        player27.run();

//        double count = 0.0;
//        int countNotGood = 0;
//        for (int i = 0; i < seeds.length; i++) {
//            player27 = new player27();
//            player27.setSeed(seeds[i]);
//            player27.setEvaluation(new AckleyEvaluation());
//            System.out.println(seeds[i] + " aucley ");
//            player27.runWithIPOPandBIPOP();
//            System.out.println();
//            count += CMAFull.lastFitness;
//            if (CMAFull.lastFitness < 5.0) {
//                countNotGood ++;
//            }
//
//            player27 = new player27();
//            player27.setEvaluation(new RastriginEvaluation());
//            System.out.println(seeds[i] + " rastrig " );
//            player27.runWithIPOPandBIPOP();
//            System.out.println();
//            count += CMAFull.lastFitness;
//            if (CMAFull.lastFitness < 5.0) {
//                countNotGood ++;
//            }
//
//            player27 = new player27();
//            player27.setEvaluation(new RastriginEvaluationShifted());
//            System.out.println(seeds[i] + " rastrig shifted" );
//            player27.runWithIPOPandBIPOP();
//            System.out.println();
//            count += CMAFull.lastFitness;
//            if (CMAFull.lastFitness < 5.0) {
//                countNotGood ++;
//            }
//
//
//            player27.setEvaluation(new SphereEvaluation());
//            System.out.println(seeds[i] + " sphere ");
//            player27.runWithIPOPandBIPOP();
//            System.out.println();
//            count += CMAFull.lastFitness;
//            if (CMAFull.lastFitness < 5.0) {
//                countNotGood ++;
//            }
//
//
//            player27 = new player27();
//            player27.setEvaluation(new FletcherPowellEvaluation());
//            System.out.println(seeds[i] + " fletch " );
//            player27.runWithIPOPandBIPOP();
//            System.out.println();
//            count += CMAFull.lastFitness;
//            if (CMAFull.lastFitness < 5.0) {
//                countNotGood ++;
//            }
//
//            player27 = new player27();
//            player27.setEvaluation(new GriewankEvaluation());
//            System.out.println(seeds[i] + " griewank " );
//            player27.runWithIPOPandBIPOP();
//            System.out.println();
//            count += CMAFull.lastFitness;
//            if (CMAFull.lastFitness < 5.0) {
//                countNotGood ++;
//            }
//
//
//            System.out.println();
//
//        }
//        System.out.println(count);
//        System.out.println(countNotGood);
    }

}
