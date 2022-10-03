package Sarb;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NeuralNet implements NeuralNetInterface{
    boolean binary = false;
    private int inputNumber = 0;
    private int hiddenNode = 0;
    private double output = 0;
    private double [][] trainInput;
    private double [][] trainOutput;
    private double [][] inputweight;
    private double [][] outputweight;
    private double learningRate = 0;
    private double momentum = 0;
    private double acceptedError = 0;

    NeuralNet(int input, double learningRate, double momentum, int hiddenNode, double acceptedError, boolean binary){
        this.inputNumber = input;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.hiddenNode = hiddenNode;
        this.binary = binary;
        initialize();
        startProcess();
    }
    @Override
    public double outputFor(double[] X) {

        return 0;
    }

    public void startProcess(){

    }

    @Override
    public double train(double[] X, double argValue) {
        return 0;
    }

    @Override
    public void save(File argFile) {

    }

    @Override
    public void load(String argFileName) throws IOException {

    }

    @Override
    public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    @Override
    public double customSigmoid(double x) {
        return 2.0 / (1.0 +Math.exp(-x)) - 1.0;
    }

    public double randomNumber(double min, double max){
        double x = (Math.random() * ((max - min) + 1)) + min;
        return x;
    }
    public void initialize() {
        if(this.binary = true){
            this.trainInput = new double[][]{{0,0}, {0,1}, {1,0}, {1,1}};
            this.trainOutput = new double[][]{{0,1,1,0}};
        }else {
            this.trainInput = new double[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            this.trainOutput = new double[][]{{-1, 1, 1, -1}};
        }
        initializeWeights();
    }

    @Override
    public void initializeWeights() {
        inputweight = new double[inputNumber+1][hiddenNode];
        outputweight = new double[hiddenNode+1][1]; //output always 1
        for(int i = 0; i < inputNumber+1; i++){
            for(int j = 0; j < hiddenNode; i++){
                inputweight[i][j] = randomNumber(-0.5, 0.5);
            }
        }
        for(int i = 0; i < hiddenNode+1; i++){
            outputweight[i][0] = randomNumber(-0.5, 0.5);
        }
    }



    @Override
    public void zeroWeights() {
        double weights = 0.0;
    }
}
