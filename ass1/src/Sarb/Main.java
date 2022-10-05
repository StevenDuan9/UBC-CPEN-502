package Sarb;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

       // NeuralNet binary = new NeuralNet(true);
        NeuralNet test1 = new NeuralNet(true, 2, 1, 4, 0.2D, 0.0D,0.05D);
        NeuralNet test2 = new NeuralNet(false, 2, 1, 4, 0.2D, 0.0D,0.05D);
        NeuralNet test3 = new NeuralNet(true, 2, 1, 4, 0.2D, 0.9D,0.05D);
        test3.initialize();
        test3.train();


    }

}
