//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Sarb;

import java.io.File;
import java.io.IOException;

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
    private double learningRate = 0.2D;
    private double momentum = 0.0D;
    private double acceptedError = 0.05D;
    private double totalError = 0.0D;
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
        this.inputWeight = new double[inputNumber + 1][hiddenNumber + 1];
        this.hiddenWeight = new double[hiddenNumber + 1][outputNumber];
        this.inputWeightDelta = new double[inputNumber + 1][hiddenNumber + 1];
        this.hiddenWeightDelta = new double[hiddenNumber + 1][outputNumber];
        this.outputErrorSignal = new double[outputNumber];
        this.hiddenErrorSignal = new double[hiddenNumber + 1];
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.acceptedError = acceptedError;
        this.totalError = 0.0D;
        this.singleError = new double[outputNumber];
        this.epoch = 0;
    }

    public void initializeSet() {
        if (this.binary) {
            this.trainInput = new double[][]{{0.0D, 0.0D}, {0.0D, 1.0D}, {1.0D, 0.0D}, {1.0D, 1.0D}};
            this.trainOutput = new double[][]{{0.0D}, {1.0D}, {1.0D}, {0.0D}};
        } else {
            this.trainInput = new double[][]{{-1.0D, -1.0D}, {-1.0D, 1.0D}, {1.0D, -1.0D}, {1.0D, 1.0D}};
            this.trainOutput = new double[][]{{-1.0D}, {1.0D}, {1.0D}, {-1.0D}};
        }

    }

    public void initializeWeights() {
        int i;
        int j;
        for(i = 0; i < this.inputNumber + 1; ++i) {
            for(j = 0; j < this.hiddenNumber; ++j) {
                this.inputWeight[i][j] = Math.random() - 0.5D;
            }
        }

        for(i = 0; i < this.hiddenNumber + 1; ++i) {
            for(j = 0; j < this.outputNumber; ++j) {
                this.hiddenWeight[i][j] = Math.random() - 0.5D;
            }
        }

    }

    public void initializeWeights1() {
        int i;
        int j;
        for(i = 0; i < this.inputNumber + 1; ++i) {
            for(j = 0; j < this.hiddenNumber; ++j) {
                this.inputWeight[i][j] = 0.2D;
            }
        }

        for(i = 0; i < this.hiddenNumber + 1; ++i) {
            for(j = 0; j < this.outputNumber; ++j) {
                this.hiddenWeight[i][j] = 0.2D;
            }
        }

    }

    public void initialize() {
        this.initializeSet();
        this.initializeWeights();
        this.inputNode[this.inputNumber] = 1.0D;
        this.hiddenNode[this.hiddenNumber] = 1.0D;
    }

    public void forwardPropagation(int setRowOrder) {
        int i;
        for(i = 0; i < this.trainInput[setRowOrder].length; ++i) {
            this.inputNode[i] = this.trainInput[setRowOrder][i];
        }

        double[] var10000;
        int j;
        for(i = 0; i < this.hiddenNumber + 1; ++i) {
            for(j = 0; j < this.inputNumber + 1; ++j) {
                var10000 = this.hiddenNode;
                var10000[i] += this.inputNode[j] * this.inputWeight[j][i];
            }

            this.hiddenNode[i] = this.binary ? this.sigmoid(this.hiddenNode[i]) : this.customSigmoid(this.hiddenNode[i]);
        }

        for(i = 0; i < this.outputNumber; ++i) {
            for(j = 0; j < this.hiddenNumber + 1; ++j) {
                var10000 = this.outputNode;
                var10000[i] += this.hiddenNode[j] * this.hiddenWeight[j][i];
            }

            this.outputNode[i] = this.binary ? this.sigmoid(this.outputNode[i]) : this.customSigmoid(this.outputNode[i]);
        }

    }

    public void getTotalError(int trainInputOrder) {
        for(int i = 0; i < this.outputNumber; ++i) {
            this.singleError[i] = this.trainOutput[trainInputOrder][i] - this.outputNode[i];
            this.totalError += Math.pow(this.singleError[i], 2.0D);
        }

    }

    public void backPropagation(int setRowOrder) {
        int i;
        for(i = 0; i < this.outputNumber; ++i) {
            this.outputErrorSignal[i] = this.binary ? this.outputNode[i] * (1.0D - this.outputNode[i]) * (this.trainOutput[setRowOrder][i] - this.outputNode[i]) : 0.5D * (1.0D - Math.pow(this.outputNode[i], 2.0D)) * (this.trainOutput[setRowOrder][i] - this.outputNode[i]);
        }

        double[] var10000;
        int j;
        for(i = 0; i < this.outputNumber; ++i) {
            for(j = 0; j < this.hiddenNumber + 1; ++j) {
                this.hiddenWeightDelta[j][i] = this.momentum * this.hiddenWeightDelta[j][i] + this.learningRate * this.outputErrorSignal[i] * this.hiddenNode[j];
                var10000 = this.hiddenWeight[j];
                var10000[i] += this.hiddenWeightDelta[j][i];
            }
        }

        for(i = 0; i < this.hiddenNumber + 1; ++i) {
            this.hiddenErrorSignal[i] = 0.0D;

            for(j = 0; j < this.outputNumber; ++j) {
                var10000 = this.hiddenErrorSignal;
                var10000[i] += this.outputErrorSignal[j] * this.hiddenWeight[i][j];
            }

            var10000 = this.hiddenErrorSignal;
            var10000[i] *= this.binary ? this.hiddenNode[i] * (1.0D - this.hiddenNode[i]) : 0.5D * (1.0D - this.hiddenNode[i] * this.hiddenNode[i]);
        }

        for(i = 0; i < this.hiddenNumber; ++i) {
            for(j = 0; j < this.inputNumber + 1; ++j) {
                this.inputWeightDelta[j][i] = this.momentum * this.inputWeightDelta[j][i] + this.learningRate * this.hiddenErrorSignal[i] * this.inputNode[j];
                var10000 = this.inputWeight[j];
                var10000[i] += this.inputWeightDelta[j][i];
            }
        }

    }

    public void train() {
        this.epoch = 0;

        do {
            this.totalError = 0.0D;

            for(int i = 0; i < this.trainInput.length; ++i) {
                this.forwardPropagation(i);
                this.getTotalError(i);
                this.backPropagation(i);
            }

            System.out.println("epoch:" + this.epoch + " total error+" + this.totalError);
            ++this.epoch;
        } while(this.totalError > this.acceptedError);

    }

    public double train(double[] X, double argValue) {
        return 0.0D;
    }

    public double sigmoid(double x) {
        return 1.0D / (1.0D + Math.exp(-x));
    }

    public double customSigmoid(double x) {
        return 2.0D / (1.0D + Math.exp(-x)) - 1.0D;
    }

    public void save(File argFile) {
    }

    public void load(String argFileName) throws IOException {
    }

    public double outputFor(double[] X) {
        return 0.0D;
    }

    public void zeroWeights() {
        double weights = 0.0D;
    }
}
