import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args)  {

        double alpha = Double.parseDouble(args[0]);

        Path trainPath = Path.of(args[1]);
        Path testPath = Path.of(args[2]);

        Perceptron perceptron = new Perceptron(alpha);

        HashMap<List<Double>, String> trainHashMap = getHashMap(trainPath);
        HashMap<List<Double>, String> testHashMap = getHashMap(testPath);

        final double Emax = 0.1;
        double E = 1;

        int count = 0;

        while(E > Emax && count <= 1000) {
            perceptron.train(trainHashMap);
            E = 1 - perceptron.test(trainHashMap);
            count++;
        }

        double accuracy = perceptron.test(testHashMap);

        System.out.println(accuracy*100 + "%");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Podaj wektor do klasyfikacji");
            String ans = scanner.next();
            List<Double> vector = Arrays.stream(ans.split(",")).map(Double::parseDouble).collect(Collectors.toList());
            normalize(vector);
            int output = perceptron.countOutput((ArrayList<Double>) vector);
            System.out.println(output == 1 ? "Iris-virginica" : "Iris-versicolor");
            System.out.println();
        }
    }

    public static HashMap<List<Double>, String> getHashMap(Path path)  {

        List<List<String>> list = null;
        try {
            list = Files.readAllLines(path).stream()
                    .map(s -> s.split(","))
                    .map(t -> Arrays.stream(t).collect(Collectors.toList()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<List<Double>, String> hashMap = new HashMap<>();

        list.stream()
                .forEach(
                        l -> hashMap.put(
                                l.subList(0, l.size() - 1).stream()
                                        .map(Double::parseDouble)
                                        .collect(Collectors.toList())
                                , l.get(l.size() - 1)
                        )
                );

        for (List<Double> vector : hashMap.keySet()) {
            normalize(vector);
        }

        return hashMap;
    }

    public static void normalize(List<Double> vector) {
        double length = 0;
        for (double d : vector) {
            length += d * d;
        }
        length = Math.sqrt(length);
        for (int i = 0; i < vector.size(); i++) {
            vector.set(i, vector.get(i)/length);
        }
    }
}