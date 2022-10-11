//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Sarb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        NeuralNet test1 = new NeuralNet(true, 2, 1, 4, 0.2D, 0.0D, 0.05D);
        NeuralNet test2 = new NeuralNet(false, 2, 1, 4, 0.2D, 0.0D, 0.05D);
        NeuralNet test3 = new NeuralNet(true, 2, 1, 4, 0.2D, 0.9D, 0.05D);

        test3.calculateAverage(100);
//        test1.initialize();
//        test1.train();
        try{
            test3.writeToCSV();
        } catch(Exception e){
            System.out.println("Can not write to file");
        }

    }
}



