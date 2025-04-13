public class Perceptron {

    double[] wagi;
    double alpha; // beta = alpha
    double prog;



    public Perceptron(int wagi_len, double alpha, double prog) {
        this.alpha = alpha;
        this.prog = prog;
        wagi = new double[wagi_len];
    }

    public Perceptron(int wagi_len) {
        this.alpha = 0.5;
        this.prog = 0.8;
        wagi = new double[wagi_len];
    }



    public int calculate(double[] input) {
        double net = 0;
        for(int i = 0; i < wagi.length; i++) {
            net += input[i] * wagi[i];
        }
        return net >= prog ? 1 : 0;

    }



    public void learn(double[] input, double correct) {
        int calculated = calculate(input);

        for(int i = 0; i < wagi.length; i++) {
            wagi[i] = wagi[i] + (correct - calculated) * alpha * input[i];
        }

        prog = prog - (correct - calculated) * alpha;

    }


}
