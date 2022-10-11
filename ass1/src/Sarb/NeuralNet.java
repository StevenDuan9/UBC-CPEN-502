package Sarb;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NeuralNet implements NeuralNetInterface {

    private boolean binary;
    private int inputNumber;
    private int hiddenNumber;
    private int outputNumber;
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
<<<<<<< HEAD
    private double learningRate;
    private double momentum;
    private double acceptedError;
    private double totalError = 0.0;
=======
    private double learningRate = 0.2D;
    private double momentum = 0.0D;
    private double acceptedError = 0.05D;
    private double totalError = 0.0D;
>>>>>>> 66594f497f55e10888b8b65c32919e959e98d90c
    private double[] singleError;
    private int epoch = 0;

    public NeuralNet(boolean bin, int inputNumber, int outputNumber, int hiddenNumber, double learningRate, double momentum, double acceptedError) {
        this.binary = bin;
        this.inputNumber = inputNumber;
        this.hiddenNumber = hiddenNumber;
        this.outputNumber = outputNumber;
        this.inputNode = new double[inputNumber + 1];
        this.hiddenNode = new double[hiddenNumber + 1];
        this.outputNode = new double[outputNumber];
        this.inputWeight = new double[inputNumber + 1][hiddenNumber]; //might need fix
        this.hiddenWeight = new double[hiddenNumber + 1][outputNumber];
        this.inputWeightDelta = new double[inputNumber + 1][hiddenNumber];
        this.hiddenWeightDelta = new double[hiddenNumber + 1][outputNumber];
        this.outputErrorSignal = new double[outputNumber];
        this.hiddenErrorSignal = new double[hiddenNumber];
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.acceptedError = acceptedError;
        this.totalError = 0.0;
        this.singleError = new double[outputNumber];
        this.epoch = 0;

    }

    public void initializeSet() {
        if (this.binary == true) {
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
    public void initializeWeights1() {
        int i;
        int j;
        for(i = 0; i < this.inputNumber+ 1; ++i) {
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
        initializeSet();
        initializeWeights();
        inputNode[inputNumber] = 1.0;
        hiddenNode[hiddenNumber] = 1.0;
    }

    public void forwardPropagation(int setRowOrder) {
        for (int i = 0; i < trainInput[setRowOrder].length; i++) {
            inputNode[i] = trainInput[setRowOrder][i];
        }
//        for(int i = 0; i < inputNode.length; i++){
//            System.out.println("InputNode: " + i + " : " + inputNode[i]);   //checkprint
//        }

        //inputNode[inputNumber] = 1.0;
//        for(int i = 0; i < trainInput[setRowOrder].length+1; i++){
//            System.out.println(inputNode[i]);
//        }
        for (int i = 0; i < this.hiddenNumber; i++) {
            for (int j = 0; j < this.inputNumber + 1; j++) {
                this.hiddenNode[i] += this.inputNode[j] * this.inputWeight[j][i];
            }
            this.hiddenNode[i] = binary ? sigmoid(hiddenNode[i]) : customSigmoid(hiddenNode[i]);
        }
        //hiddenNode[hiddenNumber] = 1.0;
<<<<<<< HEAD
        for (int i = 0; i < this.outputNumber; i++) {
            for (int j = 0; j < this.hiddenNumber + 1; j++) {
                this.outputNode[i] += this.hiddenNode[j] * this.hiddenWeight[j][i];
=======
        for (int i = 0; i < outputNumber; i++) {
            for (int j = 0; j < hiddenNumber + 1; j++) {

                outputNode[i] += hiddenNode[j] * hiddenWeight[j][i];

>>>>>>> 66594f497f55e10888b8b65c32919e959e98d90c
            }
            outputNode[i] = binary ? sigmoid(outputNode[i]) : customSigmoid(outputNode[i]);
        }

//        for(int i = 0; i < this.hiddenNumber; i++){
//            System.out.println(setRowOrder + ": " + this.hiddenNode[i]);
//        }
    }

    public void getTotalError(int trainInputOrder) {
<<<<<<< HEAD
        for (int i = 0; i < this.outputNumber; i++) {
            this.singleError[i] = this.trainOutput[trainInputOrder][i] - this.outputNode[i];
            //this.totalError += (this.singleError[i] * this.singleError[i]) / 2.0;
            double temp = Math.pow(this.singleError[i], 2.0);
            this.totalError += temp / 2.0;
           // System.out.println(this.totalError);
=======
        for (int i = 0; i < outputNumber; i++) {
            singleError[i] = trainOutput[trainInputOrder][i] - outputNode[i];
            //totalError += singleError[i] * singleError[i] ;
            this.totalError += Math.pow(this.singleError[i], 2.0D);
>>>>>>> 66594f497f55e10888b8b65c32919e959e98d90c
        }
        //this.totalError =  this.totalError / 2.0;
        //System.out.println(this.totalError);

//        for(int i = 0; i < this.outputNumber; i++){
//            System.out.println(trainInputOrder + ": " + this.singleError[i]);
//        }
    }

    public void backPropagation(int setRowOrder) {
        //compute the error for the output units
        for (int i = 0; i < outputNumber; i++) {
            //outputErrorSignal[i] = 0;
<<<<<<< HEAD
            this.outputErrorSignal[i] = binary ? this.outputNode[i] * (1.0 - this.outputNode[i]) * (this.trainOutput[setRowOrder][i] - outputNode[i]) : 0.5D * (1.0D - outputNode[i] * outputNode[i]) * (trainOutput[setRowOrder][i] - outputNode[i]);
        }
        //update the weight of the hidden units
        for (int i = 0; i < outputNumber; i++) {
            for (int j = 0; j < hiddenNumber+1; j++) {
                this.hiddenWeightDelta[j][i] = this.momentum * this.hiddenWeightDelta[j][i] + this.learningRate * this.outputErrorSignal[i] * this.hiddenNode[j];
                this.hiddenWeight[j][i] += this.hiddenWeightDelta[j][i];
            }
        }
        //compute the error for the hidden units
        for (int i = 0; i < this.hiddenNumber  ; i++) {
            this.hiddenErrorSignal[i] = 0;
            for (int j = 0; j < this.outputNumber; j++) {
                this.hiddenErrorSignal[i] += this.outputErrorSignal[j] * this.hiddenWeight[i][j];
=======
            outputErrorSignal[i] = binary ? outputNode[i] * (1.0D - outputNode[i]) * (trainOutput[setRowOrder][i] - outputNode[i]) : 0.5D * (1.0D - Math.pow(outputNode[i], 2.0D)) * (trainOutput[setRowOrder][i] - outputNode[i]);
        }
        //update the weight of the hidden units
        for (int i = 0; i < outputNumber; i++) {

            for (int j = 0; j < hiddenNumber + 1; j++) {
                hiddenWeightDelta[j][i] = momentum * hiddenWeightDelta[j][i] + learningRate * outputErrorSignal[i] * hiddenNode[j];
                hiddenWeight[j][i] += hiddenWeightDelta[j][i];
            }
        }
        //compute the error for the hidden units

        for (int i = 0; i < hiddenNumber +1 ; i++) {
            hiddenErrorSignal[i] = 0;

            for (int j = 0; j < outputNumber; j++) {
                hiddenErrorSignal[i] += outputErrorSignal[j] * hiddenWeight[i][j];
>>>>>>> 66594f497f55e10888b8b65c32919e959e98d90c
            }
            this.hiddenErrorSignal[i] *= binary ? this.hiddenNode[i] * (1.0 - this.hiddenNode[i]) : 0.5D * (1.0D - hiddenNode[i] * hiddenNode[i]);
        }
        //update the weight of the input units
<<<<<<< HEAD
        for (int i = 0; i < hiddenNumber; i++) {
=======

        for (int i = 0; i < hiddenNumber; i++) {

>>>>>>> 66594f497f55e10888b8b65c32919e959e98d90c
            for (int j = 0; j < inputNumber+1; j++) {
                this.inputWeightDelta[j][i] = this.momentum * this.inputWeightDelta[j][i] + this.learningRate * this.hiddenErrorSignal[i] * this.inputNode[j];
                inputWeight[j][i] += inputWeightDelta[j][i];
                //System.out.println(inputWeight[j][i]);
            }
        }

    }


    public void train() {
<<<<<<< HEAD
        this.epoch = 0;
        /* do{
=======
        epoch = 0;

        //do{

         do{

>>>>>>> 66594f497f55e10888b8b65c32919e959e98d90c
            totalError = 0.0;
            for(int i = 0; i < trainInput.length; i++){
                forwardPropagation(i);
                getTotalError(i);
                //System.out.println(totalError);
                backPropagation(i);
            }
            System.out.println("epoch:" + epoch + " " + "total error+" + totalError);
            epoch++;

<<<<<<< HEAD
        for (int j = 0; j < 2500; j++) {
            this.totalError = 0.0;
            for (int i = 0; i < trainInput.length; i++) { //this.trainInput = new double[][]{{0.0, 0.0}, {0.0, 1.0}, {1.0, 0.0}, {1.0, 1.0}};
                forwardPropagation(i);
                getTotalError(i);
                //System.out.println("epoch: " + epoch + ", total error: " + totalError);
                backPropagation(i);

                //this.totalError /= 2.0;
            }
            epoch++;
            System.out.println("epoch: " + epoch + ", total error: " + this.totalError);
        }
=======
        }while(totalError > acceptedError);


//        for (int j = 0; j < 5000; j++) {
//            totalError = 0.0;
//            for (int i = 0; i < trainInput.length; i++) {
//                forwardPropagation(i);
//                getTotalError(i);
//                //System.out.println("epoch: " + epoch + ", total error: " + totalError);
//                backPropagation(i);
//            }
//            epoch++;
//            System.out.println("epoch: " + epoch + ", total error: " + totalError);
//        }

>>>>>>> 66594f497f55e10888b8b65c32919e959e98d90c
    }


    public double train(double[] X, double argValue) {
        return 0;
    }


    @Override
    public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
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
