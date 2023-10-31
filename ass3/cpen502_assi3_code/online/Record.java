package assi3;

public class Record {
    public State preState;
    public State currState;
    public int  preAction;
    public double Reward;

    public Record(State preState, State currState, int preAction, double Reward) {
        this.preState = preState;
        this.currState = currState;
        this.preAction = preAction;
        this.Reward = Reward;
    }
}
