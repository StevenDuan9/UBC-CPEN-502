package Sarb;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        NeuralNet xor1 = new NeuralNet(2, 0.2, 0, 4, 0.05, true);

        NeuralNet bi = new NeuralNet(2, 0.2, 0, 4, 0.05, false);


    }

}
