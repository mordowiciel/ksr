import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<ClassificationSubject> trainingSet = new ArrayList<>();

        String label1 = "1";
        List<Double> vector1 = new ArrayList<>();
        vector1.add(1.0);
        vector1.add(1.0);
        ClassificationSubject subject1 = new ClassificationSubject(label1, vector1);

        String label2 = "2";
        List<Double> vector2 = new ArrayList<>();
        vector2.add(1.3);
        vector2.add(1.3);
        ClassificationSubject subject2 = new ClassificationSubject(label2, vector2);

        String label3 = "2";
        List<Double> vector3 = new ArrayList<>();
        vector3.add(2.0);
        vector3.add(2.0);
        ClassificationSubject subject3 = new ClassificationSubject(label3, vector3);

        String testLabel = null;
        List<Double> testVector = new ArrayList<>();
        testVector.add(0.5);
        testVector.add(0.5);
        ClassificationSubject testSubject = new ClassificationSubject(testLabel, testVector);

        trainingSet.add(subject1);
        trainingSet.add(subject2);
        trainingSet.add(subject3);

        KNNClassifier knnClassifier = new KNNClassifier(3, trainingSet, new EuclideanMetric());
        String label = knnClassifier.classifyObject(testSubject);
        System.out.println(label);
    }
}
