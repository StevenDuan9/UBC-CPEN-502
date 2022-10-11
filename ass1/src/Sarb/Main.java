//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Sarb;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        NeuralNet test1 = new NeuralNet(true, 2, 1, 4, 0.2D, 0.0D, 0.05D);
        new NeuralNet(false, 2, 1, 4, 0.2D, 0.0D, 0.05D);
        new NeuralNet(true, 2, 1, 4, 0.2D, 0.9D, 0.05D);
        test1.initialize();
        test1.train();
    }
}
