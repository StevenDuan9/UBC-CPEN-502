package assi3;

public class State {

    private double selfHP;
    private double enemyHP;
    private double distanceToEnemy;
    private double distanceToBoundary;
    private int action;
    private boolean attackTurn = false;

    public State(double selfHP, double enemyHP, double distanceToEnemy, double distanceToBoundary, int action, boolean attackTurn) {
        this.selfHP = selfHP;
        this.enemyHP = enemyHP;
        this.distanceToEnemy = distanceToEnemy;
        this.distanceToBoundary = distanceToBoundary;
        this.action = action;
        this.attackTurn = attackTurn;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setAttackTurn(boolean attackTurn) {
        this.attackTurn = attackTurn;
    }

    public void setSelfHP(int selfHP) {
        this.selfHP = selfHP;
    }

    public void setEnemyHP(int enemyHP) {
        this.enemyHP = enemyHP;
    }

    public void setDistanceToEnemy(int distanceToEnemy) {
        this.distanceToEnemy = distanceToEnemy;
    }

    public void setDistanceToBoundary(int distanceToBoundary) {
        this.distanceToBoundary = distanceToBoundary;
    }

    public double getSelfHP() {
        return this.selfHP;
    }

    public double getEnemyHP() {
        return this.enemyHP;
    }

    public double getDistanceToEnemy() {
        return this.distanceToEnemy;
    }

    public double getDistanceToBoundary() {
        return this.distanceToBoundary;
    }

    public int getAction() {
        return this.action;
    }

    public boolean isAttackTurn() {
        return attackTurn;
    }





}
