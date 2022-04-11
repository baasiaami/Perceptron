import java.util.*;

public class Perceptron {
    private ArrayList<Double> weights;
    private double theta;
    private final double alpha;

    public Perceptron(double alpha) {
        weights = new ArrayList<>();
        theta = 0.01 + Math.random();
        this.alpha = alpha;
    }

    private void fillWeights(int dim) {
        for (int i = 0; i < dim; i++) {
            weights.add(0.01 + Math.random());
        }
    }

    public double countNet(ArrayList<Double> vector) {
        double net = 0;

        for (int i = 0; i < weights.size(); i++) {
            net += weights.get(i) * vector.get(i);
        }


        return net - theta;
    }

    public int countOutput(ArrayList<Double> vector) {
        return countNet(vector) >= 0 ? 1 : 0;
    }

    public void adjustValues(int d, int y, ArrayList<Double> x) {
        double change = alpha * (d - y);

        for (int i = 0; i < weights.size(); i++) {
            weights.set(i, weights.get(i) + change * x.get(i));
        }

        theta -= change;
    }

    public void train(HashMap<List<Double>, String> trainHashMap) {
        if (weights.size() == 0) {
            fillWeights(trainHashMap.entrySet().iterator().next().getKey().size());
        }
        // virginica - 1, versicolor - 0

        for (Map.Entry<List<Double>,String> entry : trainHashMap.entrySet()) {
            int output = countOutput((ArrayList<Double>) entry.getKey());
            if ("Iris-virginica".equals(entry.getValue())) {
                adjustValues(1, output, (ArrayList<Double>) entry.getKey());
            } else {
                adjustValues(0, output, (ArrayList<Double>) entry.getKey());
            }
        }
    }

    public double test(HashMap<List<Double>, String> testHashMap) {
        int correctAnswers = 0;

        for (Map.Entry<List<Double>,String> entry : testHashMap.entrySet()) {
            int output = countOutput((ArrayList<Double>) entry.getKey());

            if (output == 1 && entry.getValue().equals("Iris-virginica") || output == 0 && entry.getValue().equals("Iris-versicolor")){
                correctAnswers++;
            }
        }
        return ((double)correctAnswers)/testHashMap.size();
    }
}
