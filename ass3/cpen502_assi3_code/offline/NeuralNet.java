package Sarb;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private double learningRate = 0.2D;
    private double momentum = 0.0D;
    private double acceptedError = 0.05D;
    private double totalError = 0.0D;
    private double[] singleError;
    private int epoch = 0;
    private ArrayList<String> ls1;
    private ArrayList<String> ls2;
    private boolean runOnlyOnce = false;

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
        ls1 = new ArrayList<>();
        ls2 = new ArrayList<>();
        ls1.add("Epoch");
        if(binary){
            ls2.add("Error Vs Epoch (Binary Representation)");
        }else{
            ls2.add("Error Vs Epoch (Bipolar Representation)");
        }


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

    public void initializeSetGiven(double[][] input, double[][]output) {
            this.trainInput = input;
            this.trainOutput = output;
           // this.trainInput = new double[][]{{0.0, 0.0}, {0.0, 1.0}, {1.0, 0.0}, {1.0, 1.0}};
            //this.trainOutput = new double[][]{{0.0}, {1.0}, {1.0}, {0.0}};
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


    public void initialize(double[][]input, double[][]output) {
        //initializeSet();
        initializeSetGiven(input, output);
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

                outputNode[i] += hiddenNode[j] * hiddenWeight[j][i];

            }
            outputNode[i] = binary ? sigmoid(outputNode[i]) : customSigmoid(outputNode[i]);
        }
    }

    public void getTotalError(int trainInputOrder) {
        for (int i = 0; i < outputNumber; i++) {
            singleError[i] = trainOutput[trainInputOrder][i] - outputNode[i];
            //totalError += singleError[i] * singleError[i] ;
            this.totalError += Math.pow(this.singleError[i], 2.0D);
        }
    }

    public void backPropagation(int setRowOrder) {
        //compute the error for the output units
        for (int i = 0; i < outputNumber; i++) {
            //outputErrorSignal[i] = 0;
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
            }
            hiddenErrorSignal[i] *= binary ? hiddenNode[i] * (1.0D - hiddenNode[i]) : 0.5D * (1.0D - hiddenNode[i] * hiddenNode[i]);
        }
        //update the weight of the input units

        for (int i = 0; i < hiddenNumber; i++) {

            for (int j = 0; j < inputNumber+1; j++) {
                inputWeightDelta[j][i] = momentum * inputWeightDelta[j][i] + learningRate * hiddenErrorSignal[i] * inputNode[j];
                inputWeight[j][i] += inputWeightDelta[j][i];
                //System.out.println(inputWeight[j][i]);
            }
        }

    }
    public void writeToCSV() throws Exception {

        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data.csv", false), StandardCharsets.ISO_8859_1))){
            for(int i=0; i < this.ls1.size(); i++) {
                writer.write(this.ls1.get(i));
                writer.write("," + ls2.get(i) + System.lineSeparator());
            }
        }
    }



    //    public int trainAverage() {
//        epoch = 0;
//         do{
//
//            totalError = 0.0;
//            for(int i = 0; i < trainInput.length; i++){
//                forwardPropagation(i);
//                getTotalError(i);
//                //System.out.println(totalError);
//                backPropagation(i);
//            }
//            //System.out.println("epoch:" + epoch + " " + "total error+" + totalError);
//            epoch++;
//
//        }while(totalError > acceptedError);
//         return epoch;
//    }
    public void train() {
        this.epoch = 0;
        do{

            totalError = 0.0;
            for(int i = 0; i < trainInput.length; i++){
                forwardPropagation(i);
                getTotalError(i);
                //System.out.println(totalError);
                backPropagation(i);
            }
            totalError /= 2;

            if(!runOnlyOnce){
                System.out.println("epoch:" + this.epoch + " " + "total error " + totalError);
                ls1.add(Integer.toString(this.epoch));
                ls2.add(Double.toString(this.totalError));
            }
            this.epoch++;

        }while(totalError > acceptedError);
    }

    public void train(int count){
        double RMS = 0;
        this.epoch = 0;
        double error = 0;
        do{
            epoch++;
            totalError = 0;
            error = 0;
            for(int i = 0; i < count; i++){
                forwardPropagation(i);
                //getTotalError(i);
                backPropagation(i);
                //System.out.println(trainOutput[i][0] + ", " + outputNode[0]);
                error += Math.pow((trainOutput[i][0] - outputNode[0]), 2);

            }

            RMS = Math.sqrt(error/count);
            if(!runOnlyOnce){
                if(epoch % 100 == 0){
                    System.out.println("epoch:" + this.epoch + " " + "rms " + RMS);
                }
                //System.out.println("epoch:" + this.epoch + " " + "total error " + totalError);
                ls1.add(Integer.toString(this.epoch));
                ls2.add(Double.toString(RMS));
            }

        }while(RMS > acceptedError);

    }

    /*public void saveTotalErrorForEpochToFile(){
        FileWriter filewriter = new FileWriter("D:/CPEN-502/")
    }*/
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



    public void calculateAverage(int time, double[][]input, double[][]output, int count){
        int sum = 0;
        for(int i = 0; i < time; i++){
            //TODO changed the initalize

            initialize(input, output);
            //System.out.println(count);
            train(count);
            sum += this.epoch;
            runOnlyOnce = true;
        }
        System.out.println("Average Epoch to converge when total error is less than 0.05: " + sum/time);
    }


//    public void getAverage( int times){
//        int epo = 0;
//        initializeSet();
//        for(int i = 0; i < times; i++ ){
//            initializeWeights();
//            train();
//            epo += this.epoch;
//            System.out.println(this.epoch);
//        }
//        double average = epo / times;
//        System.out.println("average epoch:" + average );
//    }
}
