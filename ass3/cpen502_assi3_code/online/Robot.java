package assi3;

import assignment_2.LUT;
import robocode.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Robot extends AdvancedRobot{
    //Initialize
    //initialize current position
    public double selfPositionX = 0.0;
    public double selfPositionY = 0.0;
    public double distanceToEnemy = 0.0;
    public double distanceToBoundary = 0.0;
    public static double enemyBearing = 0.0;

    //initialize current HP
    public double selfHP = 100.0;
    public double enemyHP = 100.0;

    //initialize current and previous state
    private State currState = new State(100.0,100,100.0,200.0,0,false);
    private State preState = new State(100.0,100,100.0,200.0,0,false);

    //true means taking immediate rewards
    public static boolean immediateRewards = true;

    //true means take on-policy
    public static boolean onPolicy = true;


    //initialize parameters
    private double alpha = 0.1;
    private double discountFactor = 0.9;
    private double epsilon = 0.0;
    private double QValue = 0.0;
    private double reward = 0.0;
    private double totalReward = 0.0;
    private double immediateReward = 0.5;
    private double immediatePunish = -0.1;
    private double terminalRewards = 1.0;
    private double terminalPunish = -0.2;


    //initialize NN parameters
    static int numInputs = 5;
    static int numHidden = 50;
    static double learningRate = 0.01;
    static double momentum = 0.9;
    static public NeuralNet nn = new NeuralNet(false,numInputs, 1, numHidden, learningRate,momentum);

    //initialize statistics
    private static int totalRound = 0;
    private static int winRound = 0;
    private static double winRate = 0.0;
    private static int vectorSize = 1;
    private static double[] rewardRecord = new double[20000];
    private static ReplayMemory<Record> replayMemory = new ReplayMemory<>(vectorSize);

    //public static assi3.LUT lut = new LUT(5,5,5,5,5);



    public double QValueComputation(State currState, State preState, double reward){
        double curQValue = nn.outputFor(new double[]{currState.getSelfHP(),currState.getEnemyHP(), currState.getDistanceToEnemy(), currState.getDistanceToBoundary(),currState.getAction()});
        double preQValue = nn.outputFor(new double[]{preState.getSelfHP(),preState.getEnemyHP(), preState.getDistanceToEnemy(), preState.getDistanceToBoundary(),preState.getAction()});
        int greedyChoice = greedyMove(currState.getSelfHP(), currState.getEnemyHP(), currState.getDistanceToEnemy(), currState.getDistanceToBoundary());
        double QValueMax = nn.outputFor(new double[]{currState.getSelfHP(),currState.getEnemyHP(), currState.getDistanceToEnemy(), currState.getDistanceToBoundary(),greedyChoice});
        if(onPolicy){
            return preQValue + alpha * (reward + discountFactor * curQValue - preQValue);
        }else{
            return preQValue + alpha * (reward + discountFactor * QValueMax - preQValue);
}
    }
    public static int j =0;
    public void updateQValue(){
        double[] index = new double[]{preState.getSelfHP(), preState.getEnemyHP(), preState.getDistanceToEnemy(), preState.getDistanceToBoundary()};
        replayMemory.add(new Record(preState, currState, preState.getAction(), reward));
        Object[] vector = replayMemory.sample(Math.min(replayMemory.sizeOf(), vectorSize));
        for(Object o: vector){
            int i =0;
            Record rec = (Record) o;
            nn.train(index, QValueComputation(rec.currState, rec.preState, rec.Reward));
            //System.out.println("outer: " + j + " inner: " + i );
            i++;
        }
        j++;
    }

    @Override
    public void run(){
        super.run();
        if (getRoundNum() == 0) {
            nn.initializeWeights();
        }
        //System.out.printf("123456");
        //setBodyColor(Color.red);
        while(true){
            if(!currState.isAttackTurn()){
                reward = 0.0;
                turnRadarRight(60);
                continue;
            }else{
                epsilon = totalRound < 2000 ? 0.5 : 0.0;
                Double random = Math.random();
                if(random <= epsilon){
                    //curActionState = lut.exploratoryMove();
                    currState.setAction(exploratoryMove());
                }else {
                    //curActionState = lut.greedyMove(getHPToState(selfHP), getHPToState(enemyHP), getDisToEnemyToState(distanceToEnemy), getDisToBoundaryToState(selfPositionX, selfPositionY));
                    currState.setAction(greedyMove(selfHP, enemyHP, distanceToEnemy, distanceToBoundary));

                }
                switch (currState.getAction()) {
                    case 0: {
                        super.turnGunRight(getHeading() - getGunHeading() + enemyBearing);
                        fire(3);
                        break;
                    }

                    case 1: {
                        ahead(100);
                        break;
                    }

                    case 2: {
                        back(100);
                        break;
                    }

                    case 3: {
                        turnLeft(30);
                        break;
                    }

                    case 4: {
                        turnRight(30);
                        break;
                    }
                }
                updateQValue();
                currState.setAttackTurn(false);
            }
        }
    }

    public void getCurData(ScannedRobotEvent event){
        //get current data
        this.enemyBearing = event.getBearing();
        this.selfPositionX = getX();
        this.selfPositionY = getY();
        this.selfHP = getEnergy();
        this.enemyHP = event.getEnergy();
        this.distanceToEnemy = event.getDistance();
        this.distanceToBoundary = Math.min(selfPositionX, Math.min(selfPositionY, Math.min(getBattleFieldWidth() - selfPositionX, getBattleFieldHeight() - selfPositionY)));
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event){
        super.onScannedRobot(event);
        getCurData(event);
        //turn the current state to the previous
        /*preSelfHPState = curSelfHPState;
        preEnemyHPState = curEnemyHPState;
        preDistanceToEnemyState = curDistanceToEnemyState;
        preDistanceToBoundaryState = curDistanceToBoundaryState;
        preActionState = curActionState;*/
        preState = currState;

        /*currState.setSelfHP(getHPToState(selfHP));
        currState.setEnemyHP(getHPToState(enemyHP));
        currState.setDistanceToEnemy(getDisToEnemyToState(distanceToEnemy));
        currState.setDistanceToBoundary(getDisToBoundaryToState(selfPositionX,selfPositionY,getBattleFieldHeight(),getBattleFieldWidth()));
        currState.setAttackTurn(true);*/

        // System.out.println(curActionState + " " + curEnemyHPState + " " +  curDistanceToEnemyState + " " + curDistanceToBoundaryState +" " +  curActionState+ " \r\n");
        //get the current state
        currState = new assi3.State(getHPToState(selfHP), getHPToState(enemyHP), getDisToEnemyToState(distanceToEnemy), getDisToBoundaryToState(selfPositionX,selfPositionY,getBattleFieldHeight(), getBattleFieldWidth()),currState.getAction(),true);
        /*curEnemyHPState = getHPToState(enemyHP);
        curDistanceToEnemyState = getDisToEnemyToState(distanceToEnemy);
        curDistanceToBoundaryState = getDisToBoundaryToState(selfPositionX, selfPositionY);
       // System.out.println(curActionState + " " + curEnemyHPState + " " +  curDistanceToEnemyState + " " + curDistanceToBoundaryState +" " +  curActionState+ " \r\n");
      //  System.out.println(lut.lookUpTable[1][1][1][1][1]);
        //end of defending turn, switch to attack turn
        attackTurn = true;*/
    }

    @Override
    public void onBulletHit(BulletHitEvent event){
        if(immediateRewards){
            reward += immediateReward;
            totalReward +=reward;
        }
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event){
        if(immediateRewards){
            reward += immediatePunish;
            totalReward += reward;
        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event){
        if(immediateRewards){
            reward += immediatePunish;
            totalReward += reward;
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent event){
        if(immediateRewards) {
            reward += immediatePunish;
            totalReward += reward;
        }
        setBack(200);
        setTurnRight(60);
        execute();
    }

    @Override
    public void onHitWall(HitWallEvent event){
        if(immediateRewards){
            reward += immediatePunish;
        }
        setBack(200);
        setTurnRight(60);
        execute();
    }

    @Override
    public void onWin(WinEvent event){
        reward += terminalRewards;
        totalReward += reward;
        updateQValue();
        winRound++;
        System.out.printf("round:" + totalRound + " win");
        try {
            getWinRate(totalRound);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeath(DeathEvent event){
        reward += terminalPunish;
        totalReward += reward;
        updateQValue();
        System.out.println("round:" + totalRound + " lose");
        try {
            getWinRate(totalRound);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRoundEnded(RoundEndedEvent e) {
        rewardRecord[totalRound] = totalReward;
        totalReward = 0; // reset accum reward for next round
        totalRound++;
    }

    @Override
    public void onBattleEnded(BattleEndedEvent e) {
        try {
            /*File file = getDataFile("q5a.csv");
            System.out.println(file.getAbsolutePath());
            nn.writeToCSVRobot(file);*/
            System.out.println("---------------Battle End-------------------");
            getReward(rewardRecord);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getWinRate(int totalRound) throws IOException {
        if((totalRound % 100 == 0) && (totalRound != 0)){
            //System.out.println("12345");
            winRate = (double) winRound / 100.0;
            File file = getDataFile("winRate.csv");
            String string = String.format("%d, %2f", totalRound, winRate);
            RobocodeFileWriter fileWriter = new RobocodeFileWriter(file.getAbsolutePath(),true);
            fileWriter.write(string);
            fileWriter.write("\r\n");
            fileWriter.close();

            //File folderDst1 = getDataFile(fileToSaveName);
            //log.writeToFile(file, winPercentage, round);

            winRound = 0;
        }
    }
    public void getReward(double[] rewardRecord) {
        try {
            File file = getDataFile("rewardRecord.csv");
            RobocodeFileWriter fileWriter = new RobocodeFileWriter(file.getAbsolutePath(),true);
            for (int i = 0; i < totalRound; i++) {
                String string = String.format("%d, %f",i+1, rewardRecord[i]);
                System.out.println(string);
                fileWriter.write(string);
                fileWriter.write("\r\n");
            }
            fileWriter.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    /*public void saveLUT(int targetRound)  {
        if(targetRound == totalRound){
            try {
                File file = getDataFile("lut.csv");
                System.out.println(file.getAbsolutePath());
                lut.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public int getHPToState(double hp){
        if(hp <= 20){
            return 0;
        }else if(hp <= 40){
            return 1;
        }else if(hp <= 60){
            return 2;
        }else if(hp <= 80){
            return 3;
        }else{
            return 4;
        }
    }

    public int getDisToEnemyToState(double distanceToEnemy){
        if(distanceToEnemy <= 100){
            return 0;
        }else if(distanceToEnemy <= 300){
            return 1;
        }else if(distanceToEnemy <= 500){
            return 2;
        }else if(distanceToEnemy <= 700){
            return 3;
        }else{
            return 4;
        }
    }

    public int getDisToBoundaryToState(double selfPositionX, double selfPositionY, double height, double width){
        double x = width - selfPositionX;
        double y = height - selfPositionY;
        if(selfPositionX <= 20 || selfPositionY <= 20 || x <= 20 || y <= 20){
            return 0;
        }else if(selfPositionX <= 40 || selfPositionY <= 40 || x <= 40 || y <= 40){
            return 1;
        }else if(selfPositionX <= 60 || selfPositionY <= 60 || x <= 60 || y <= 60){
            return 2;
        }else if(selfPositionX <= 80 || selfPositionY <= 80 || x <= 80 || y <= 80){
            return 3;
        }else {
            return 4;
        }
    }

    public int greedyMove(double selfHP, double enemyHP, double distanceToEnemy, double distanceToBoundary){
        double maxQValue = -100;
        int nextMove = -1;
        double[] index = new double[]{getHPToState(selfHP), getHPToState(enemyHP), getDisToEnemyToState(distanceToEnemy), getDisToBoundaryToState(selfPositionX,selfPositionY, getBattleFieldHeight(), getBattleFieldWidth()), 0};
        for(int i = 0; i < 5; i++){
            index[4] = i;
            if(nn.outputFor(index) > maxQValue){
                maxQValue = nn.outputFor(index);
                nextMove = i;
            }
        }
        // System.out.println("nextmove: " + nextMove);
        return nextMove;
    }

    public int exploratoryMove(){
        return (int)(Math.random() * 5);
    }
}
