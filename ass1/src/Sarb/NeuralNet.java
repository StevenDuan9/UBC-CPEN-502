package Sarb;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NeuralNet implements NeuralNetInterface {

    private boolean binary = false;
    private int inputNumber = 2;
    private int hiddenNumber = 4;
    private int outputNumber = 1;
    private double[] inputNode;
    private double[] hiddenNode;
    private double[] outputNode;
    private double[][] trainInput;
    private double[][] trainOutput;
    private double[][] inputWeight;
    private double[][] hiddenWeight;
    private double[][] inputWeightDelta;
    private double[][] hiddenWeightDelta;
    private double[] outputErrorSignal;
    private double[] hiddenErrorSignal;
    private double learningRate = 0.2;
    private double momentum = 0.0;
    private double acceptedError = 0.05;
    private double totalError = 0.0;
    private double[] singleError;
    private int epoch = 0;

    public NeuralNet(boolean binary, int inputNumber, int outputNumber, int hiddenNumber, double learningRate, double momentum, double acceptedError) {
        this.binary = binary;
        this.inputNumber = inputNumber;
        this.hiddenNumber = hiddenNumber;
        this.outputNumber = outputNumber;
        this.inputNode = new double[inputNumber + 1];
        this.hiddenNode = new double[hiddenNumber + 1];
        this.outputNode = new double[outputNumber];
        this.inputWeight = new double[inputNumber + 1][hiddenNumber+1];
        this.hiddenWeight = new double[hiddenNumber + 1][outputNumber];
        this.inputWeightDelta = new double[inputNumber + 1][hiddenNumber+1];
        this.hiddenWeightDelta = new double[hiddenNumber + 1][outputNumber];
        this.outputErrorSignal = new double[outputNumber];
        this.hiddenErrorSignal = new double[hiddenNumber+1];
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.acceptedError = acceptedError;
        this.totalError = 0.0;
        this.singleError = new double[outputNumber];
        this.epoch = 0;

    }

    public void initializeSet() {
        if (this.binary = true) {
            this.trainInput = new double[][]{{0.0, 0.0}, {0.0, 1.0}, {1.0, 0.0}, {1.0, 1.0}};
            this.trainOutput = new double[][]{{0.0}, {1.0}, {1.0}, {0.0}};
        } else {
            this.trainInput = new double[][]{{-1.0, -1.0}, {-1.0, 1.0}, {1.0, -1.0}, {1.0, 1.0}};
            this.trainOutput = new double[][]{{-1.0}, {1.0}, {1.0}, {-1.0}};
        }
    }

    public void initializeWeights() {
        for (int i = 0; i < inputNumber + 1; i++) {
            for (int j = 0; j < hiddenNumber; j++) {
                //System.out.println(Math.random());
                inputWeight[i][j] = Math.random() - 0.5D;
            }
        }
        for (int i = 0; i < hiddenNumber + 1; i++) {
            for (int j = 0; j < outputNumber; j++) {
                hiddenWeight[i][j] = Math.random() - 0.5D;
            }
        }
    }


    public void initialize() {
        initializeSet();
        initializeWeights();
        inputNode[inputNumber] = 1.0;
        hiddenNode[hiddenNumber] = 1.0;
    }

    public void forwardPropagation(int setRowOrder) {
        for (int i = 0; i < trainInput[setRowOrder].length; i++) {
            inputNode[i] = trainInput[setRowOrder][i];
        }
        //inputNode[inputNumber] = 1.0;
//        for(int i = 0; i < trainInput[setRowOrder].length+1; i++){
//            System.out.println(inputNode[i]);
//        }
        for (int i = 0; i < hiddenNumber + 1; i++) {
            for (int j = 0; j < inputNumber + 1; j++) {
                hiddenNode[i] += inputNode[j] * inputWeight[j][i];
            }
            hiddenNode[i] = binary ? sigmoid(hiddenNode[i]) : customSigmoid(hiddenNode[i]);
        }
        //hiddenNode[hiddenNumber] = 1.0;
        for (int i = 0; i < outputNumber; i++) {
            for (int j = 0; j < hiddenNumber + 1; j++) {
                outputNode[i] = hiddenNode[j] * hiddenWeight[j][i];
            }
            outputNode[i] = binary ? sigmoid(outputNode[i]) : customSigmoid(outputNode[i]);
        }
    }

    public void getTotalError(int trainInputOrder) {
        for (int i = 0; i < outputNumber; i++) {
            singleError[i] = trainOutput[trainInputOrder][i] - outputNode[i];
            totalError += singleError[i] * singleError[i] / 2.0D;
        }
    }

    public void backPropagation(int setRowOrder) {
        //compute the error for the output units
        for (int i = 0; i < outputNumber; i++) {
            //outputErrorSignal[i] = 0;
            outputErrorSignal[i] = binary ? outputNode[i] * (1.0D - outputNode[i]) * (trainOutput[setRowOrder][i] - outputNode[i]) : 0.5D * (1.0D - outputNode[i] * outputNode[i]) * (trainOutput[setRowOrder][i] - outputNode[i]);
        }
        //update the weight of the hidden units
        for (int i = 0; i < outputNumber; i++) {
            for (int j = 0; j < hiddenNumber+1; j++) {
                hiddenWeightDelta[j][i] = momentum * hiddenWeightDelta[j][i] + learningRate * outputErrorSignal[i] * hiddenNode[j];
                hiddenWeight[j][i] += hiddenWeightDelta[j][i];
            }
        }
        //compute the error for the hidden units
        for (int i = 0; i < hiddenNumber +1 ; i++) {
            //hiddenErrorSignal[i] = 0;
            for (int j = 0; j < outputNumber; j++) {
                hiddenErrorSignal[i] += outputErrorSignal[j] * hiddenWeight[i][j];
            }
            hiddenErrorSignal[i] *= binary ? hiddenNode[i] * (1.0D - hiddenNode[i]) : 0.5D * (1.0D - hiddenNode[i] * hiddenNode[i]);
        }
        //update the weight of the input units
        for (int i = 0; i < hiddenNumber +1 ; i++) {
            for (int j = 0; j < inputNumber+1; j++) {
                inputWeightDelta[j][i] = momentum * inputWeightDelta[j][i] + learningRate * hiddenErrorSignal[i] * inputNode[j];
                inputWeight[j][i] += inputWeightDelta[j][i];
                //System.out.println(inputWeight[j][i]);
            }
        }
    }


    public void train() {
        epoch = 0;
        /* do{
            totalError = 0.0;
            for(int i = 0; i < trainInput.length; i++){
                forwardPropagation(i);
                getTotalError(i);
                System.out.println(totalError);
                backPropagation(i);
            }
            System.out.println("epoch:" + epoch + "total error+" + totalError);
            epoch++;
        }while(totalError > acceptedError); */

        for (int j = 0; j < 5000; j++) {
            totalError = 0.0;
            for (int i = 0; i < trainInput.length; i++) {
                forwardPropagation(i);
                getTotalError(i);
                //System.out.println("epoch: " + epoch + ", total error: " + totalError);
                backPropagation(i);
            }
            epoch++;
            System.out.println("epoch: " + epoch + ", total error: " + totalError);
        }
    }


    public double train(double[] X, double argValue) {
        return 0;
    }


    @Override
    public double sigmoid(double x) {
        return 1.0D / (1.0D + Math.exp(-x));
    }

    @Override
    public double customSigmoid(double x) {
        return 2.0D / (1.0D + Math.exp(-x)) - 1.0D;
    }

    @Override
    public void save(File argFile) {
    }

    @Override
    public void load(String argFileName) throws IOException {

    }

    @Override
    public double outputFor(double[] X) {
        return 0;
    }

    @Override
    public void zeroWeights () {
            double weights = 0.0;
    }

}
