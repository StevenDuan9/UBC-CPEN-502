package assignment_2;

import robocode.*;

import java.io.File;
import java.io.IOException;

public class Robot extends AdvancedRobot{
    //Initialize
    //initialize current position
    public double selfPositionX = 0.0;
    public double selfPositionY = 0.0;
    public double distanceToEnemy = 0.0;
    public static double enemyBearing = 0.0;

    //initialize current HP
    public double selfHP = 100.0;
    public double enemyHP = 100.0;

    //initialize current and previous state
    private static State currState = new State(4,4,0,4,0,false);
    private static State preState = new State(4,4,0,4,0,false);

    //true means taking immediate rewards
    public static boolean immediateRewards = true;

    //true means take on-policy
    public static boolean onPolicy = true;


    //initialize parameters
    private double learningRate = 0.1;
    private double discountFactor = 0.9;
    private double epsilon = 0.0;
    private double QValue = 0.0;
    private double reward = 0.0;
    private double immediateReward = 0.5;
    private double immediatePunish = -0.1;
    private double terminalRewards = 1.0;
    private double terminalPunish = -0.2;

    //initialize statistics
    private static int totalRound = 0;
    private static int winRound = 0;
    private static double winRate = 0.0;

    public static LUT lut = new LUT(5,5,5,5,5);



    public double QValueComputation(boolean onPolicy, double reward){
        double curQValue = lut.getQValue(new int[]{currState.getSelfHPState(),currState.getEnemyHPState(), currState.getDistanceToEnemyState(), currState.getDistanceToBoundaryState(),currState.getActionState()});
        double preQValue = lut.getQValue(new int[]{preState.getSelfHPState(), preState.getEnemyHPState(), preState.getDistanceToEnemyState(), preState.getDistanceToBoundaryState(), preState.getActionState()});
        int greedyChoice = lut.greedyMove(currState.getSelfHPState(), currState.getEnemyHPState(), currState.getDistanceToEnemyState(), currState.getDistanceToBoundaryState());
        double QValueMax = lut.getQValue(new int[]{currState.getSelfHPState(),currState.getEnemyHPState(), currState.getDistanceToEnemyState(), currState.getDistanceToBoundaryState(),greedyChoice});
        if(onPolicy){
            return preQValue + learningRate * (reward + discountFactor * curQValue - preQValue);
        }else{
            return preQValue + learningRate * (reward + discountFactor * QValueMax - preQValue);
        }
    }

    public void updateQValue(double reward){
        //compute the QValue for the last defend turn
        QValue = QValueComputation(onPolicy, reward);
        //update the lookUpTable
        lut.setQValue(new int[]{preState.getSelfHPState(), preState.getEnemyHPState(), preState.getDistanceToEnemyState(), preState.getDistanceToBoundaryState(), preState.getActionState()}, QValue);
    }

    @Override
    public void run(){
        super.run();
        //System.out.printf("123456");
        //setBodyColor(Color.red);
        while(true){
            if(!currState.isAttackTurn()){
                reward = 0.0;
                turnRadarRight(60);
                continue;
            }else{

                Double random = Math.random();
                if(random <= epsilon){
                    //curActionState = lut.exploratoryMove();
                    currState.setActionState(lut.exploratoryMove());
                }else {
                    //curActionState = lut.greedyMove(getHPToState(selfHP), getHPToState(enemyHP), getDisToEnemyToState(distanceToEnemy), getDisToBoundaryToState(selfPositionX, selfPositionY));
                    currState.setActionState(lut.greedyMove(currState.getHPToState(selfHP), currState.getHPToState(enemyHP), currState.getDisToEnemyToState(distanceToEnemy), currState.getDisToBoundaryToState(selfPositionX,selfPositionY,getBattleFieldHeight(),getBattleFieldWidth())));
                }
                switch (currState.getActionState()) {
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
                updateQValue(reward);
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

        /*currState.setSelfHPState(currState.getHPToState(selfHP));
        currState.setEnemyHPState(currState.getHPToState(enemyHP));
        currState.setDistanceToEnemyState(currState.getDisToEnemyToState(distanceToEnemy));
        currState.setDistanceToBoundaryState(currState.getDisToBoundaryToState(selfPositionX,selfPositionY,getBattleFieldHeight(),getBattleFieldWidth()));
        currState.setAttackTurn(true);*/

        // System.out.println(curActionState + " " + curEnemyHPState + " " +  curDistanceToEnemyState + " " + curDistanceToBoundaryState +" " +  curActionState+ " \r\n");
        //get the current state
        currState = new State(currState.getHPToState(selfHP), currState.getHPToState(enemyHP), currState.getDisToEnemyToState(distanceToEnemy), currState.getDisToBoundaryToState(selfPositionX,selfPositionY,getBattleFieldHeight(), getBattleFieldWidth()),currState.getActionState(),true);
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
        }
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event){
        if(immediateRewards){
            reward += immediatePunish;
        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event){
        if(immediateRewards){
            reward += immediatePunish;
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent event){
        if(immediateRewards) {
            reward += immediatePunish;
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
        reward = terminalRewards;
        updateQValue(reward);
        winRound++;
        totalRound++;
        System.out.printf("round:" + totalRound + " win");
        try {
            getWinRate(totalRound);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeath(DeathEvent event){
        reward = terminalPunish;
        updateQValue(reward);
        totalRound++;
        System.out.println("round:" + totalRound + " lose");
        try {
            getWinRate(totalRound);
        } catch (IOException e) {
            e.printStackTrace();
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
        saveLUT(4000);
    }

    public void saveLUT(int targetRound)  {
        if(targetRound == totalRound){
            try {
                File file = getDataFile("lut.csv");
                System.out.println(file.getAbsolutePath());
                lut.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


























}
