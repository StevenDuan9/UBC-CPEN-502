package assignment_2;

import assignment2.LUTInterface;
import robocode.RobocodeFileWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LUT implements LUTInterface {
    private int selfHPStateLength;
    private int enemyHPStateLength;
    private int distanceToEnemyStateLength;
    private int distanceToBoundaryStateLength;
    private int actionStateLength;
    public double [][][][][] lookUpTable;

    public LUT(int selfHPStateLength, int enemyHPStateLength, int distanceToEnemyStateLength, int distanceToBoundaryStateLength, int actionStateLength){
        this.selfHPStateLength = selfHPStateLength;
        this.enemyHPStateLength = enemyHPStateLength;
        this.distanceToEnemyStateLength = distanceToEnemyStateLength;
        this.distanceToBoundaryStateLength = distanceToBoundaryStateLength;
        this.actionStateLength = actionStateLength;
        this.lookUpTable = new double[selfHPStateLength][enemyHPStateLength][distanceToEnemyStateLength][distanceToBoundaryStateLength][actionStateLength];
        initialiseLUT();
    }

    @Override
    public void initialiseLUT(){
        for(int i = 0; i < selfHPStateLength; i++) {
            for(int j = 0; j < enemyHPStateLength; j++) {
                for(int k = 0; k < distanceToEnemyStateLength; k++) {
                    for(int l = 0; l < distanceToBoundaryStateLength; l++) {
                        for(int m = 0; m < actionStateLength; m++) {
                            lookUpTable[i][j][k][l][m] = Math.random();
                        }
                    }
                }
            }
        }
    }

    @Override
    public int indexFor(double[] X) {
        return 0;
    }

    public void setQValue(int[] index, double value){
        int i = index[0];
        int j = index[1];
        int k = index[2];
        int l = index[3];
        int m = index[4];
        lookUpTable[i][j][k][l][m] = value;
    }

    public double getQValue(int [] index){
        int i = index[0];
        int j = index[1];
        int k = index[2];
        int l = index[3];
        int m = index[4];
        return lookUpTable[i][j][k][l][m];
    }

    public int greedyMove(int selfHPState, int enemyHPState, int distanceToEnemyState, int distanceToBoundaryState){
        double QValue = -1;
        int nextMove = -1;
        for(int i = 0; i < actionStateLength; i++){
            if(lookUpTable[selfHPState][enemyHPState][distanceToEnemyState][distanceToBoundaryState][i] > QValue){
                QValue = lookUpTable[selfHPState][enemyHPState][distanceToEnemyState][distanceToBoundaryState][i];
                nextMove = i;
            }
        }
       // System.out.println("nextmove: " + nextMove);
        return nextMove;
    }

    public int exploratoryMove(){
        return (int)(Math.random() * actionStateLength);
    }


    @Override
    public double outputFor(double[] X) {
        return 0;
    }

    @Override
    public double train(double[] X, double argValue) {
        return 0;
    }


    @Override
    public void load(String argFileName) throws IOException {

    }

    @Override
    public void save(File argFile) throws IOException {
        RobocodeFileWriter fileWriter = new RobocodeFileWriter(argFile.getAbsolutePath(),true);
        for(int i = 0; i < selfHPStateLength; i++) {
            for(int j = 0; j < enemyHPStateLength; j++) {
                for(int k = 0; k < distanceToEnemyStateLength; k++) {
                    for(int l = 0; l < distanceToBoundaryStateLength; l++) {
                        for(int m = 0; m < actionStateLength; m++) {
                            String s = String.format("%d,%d,%d,%d,%d,%2f", i,j, k, l, m, lookUpTable[i][j][k][l][m]);
                            fileWriter.write(s);
                            fileWriter.write("\r\n");
                        }
                    }
                }
            }
        }
        fileWriter.close();
    }
}
