package Sarb;

import java.io.*;

public class LUTQuestion1 {


    double error = 0;
    int epoch;
    FileWriter file;
    //TODO set number
    static double errorThreshold = 0.15;
    static double learning_rate = 0.01;
    static double momentum = 0.9;
    static int hiddenNumber = 50;

    static double [][]traininput = new double[3125][5];
    static double [][]trainouput = new double[3125][1];



    public static void main(String[] args){
        int count = 0;
        try {
            count = load();
            NeuralNet lut = new NeuralNet(false, 5, 1, hiddenNumber,  learning_rate, momentum, errorThreshold);
            lut.calculateAverage(1, traininput, trainouput, count);
            lut.writeToCSV();
        }catch(Exception e){
            //System.out.println("error1");
            e.printStackTrace();
        }

    }

    public static int load() throws IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        BufferedReader read = new BufferedReader(new FileReader(System.getProperty("user.dir")+ "/data/lut.csv"));
        String line = read.readLine();
        double min = 100;
        double max = -100;
        int count = 0;
        for(int i = 0; i < traininput.length; i++){
            //System.out.println(line);
            String split[] = line.split(",");
            //System.out.println(split[6]);
            double temp = Double.parseDouble(split[6]);

            if(temp > 0){
                traininput[count][0] = Double.parseDouble(split[0]);
                traininput[count][1] = Double.parseDouble(split[1]);
                traininput[count][2] = Double.parseDouble(split[2]);
                traininput[count][3] = Double.parseDouble(split[3]);
                traininput[count][4] = Double.parseDouble(split[4]);
                trainouput[count][0] = Double.parseDouble(split[5]);
                if(Double.parseDouble(split[5]) > max){
                    max = trainouput[count][0];
                }
                if(Double.parseDouble(split[5]) < min){
                    min = trainouput[i][0];
                }
                count++;
            }
            line = read.readLine();

        }
        for(int i = 0; i < trainouput.length; i++){
            trainouput[i][0] = (trainouput[i][0] - min) * 2 / (max-min) - 1;
        }
        return count;
    }
}
