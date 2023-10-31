package assignment_2;
import robocode.*;

import java.io.File;
import java.io.IOException;
public class State {

    private  int selfHPState;
    private  int enemyHPState;
    private  int distanceToEnemyState;
    private  int distanceToBoundaryState;
    private  int actionState;
    private boolean attackTurn = false;

    public State(int selfHPState, int enemyHPState, int distanceToEnemyState, int distanceToBoundaryState, int actionState, boolean attackTurn) {
        this.selfHPState = selfHPState;
        this.enemyHPState = enemyHPState;
        this.distanceToEnemyState = distanceToEnemyState;
        this.distanceToBoundaryState = distanceToBoundaryState;
        this.actionState = actionState;
        this.attackTurn = attackTurn;
    }

    public void setActionState(int actionState) {
        this.actionState = actionState;
    }

    public void setAttackTurn(boolean attackTurn) {
        this.attackTurn = attackTurn;
    }

    public void setSelfHPState(int selfHPState) {
        this.selfHPState = selfHPState;
    }

    public void setEnemyHPState(int enemyHPState) {
        this.enemyHPState = enemyHPState;
    }

    public void setDistanceToEnemyState(int distanceToEnemyState) {
        this.distanceToEnemyState = distanceToEnemyState;
    }

    public void setDistanceToBoundaryState(int distanceToBoundaryState) {
        this.distanceToBoundaryState = distanceToBoundaryState;
    }

    public int getSelfHPState() {
        return this.selfHPState;
    }

    public int getEnemyHPState() {
        return this.enemyHPState;
    }

    public int getDistanceToEnemyState() {
        return this.distanceToEnemyState;
    }

    public int getDistanceToBoundaryState() {
        return this.distanceToBoundaryState;
    }

    public int getActionState() {
        return this. actionState;
    }

    public boolean isAttackTurn() {
        return attackTurn;
    }
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
}
