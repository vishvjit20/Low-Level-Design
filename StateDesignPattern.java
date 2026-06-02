
interface ATMState {
    void insertCard();
    void ejectCard();
    void withdrawCash();
}

class NoCardState implements ATMState {

    private ATMMachine atm;

    NoCardState(ATMMachine atm) {
        this.atm = atm;
    }


    @Override
    public void insertCard() {
        System.out.println("Card Inserted");
        atm.setState(atm.getHasCardState());
    }

    @Override
    public void ejectCard() {
        System.out.println("No card to eject");
    }

    @Override
    public void withdrawCash() {
        System.out.println("Insert card first");
    }
    
}

class HasCardState implements ATMState {

    private ATMMachine atm;

    HasCardState(ATMMachine atm) {
        this.atm = atm;
    }

    @Override
    public void insertCard() {
        System.out.println("Card already inserted");
    }

    @Override
    public void ejectCard() {
        System.out.println("Card ejected");
        atm.setState(atm.getNoCardState());
    }

    @Override
    public void withdrawCash() {
        System.out.println("Cash withdrawn...");
        atm.setState(atm.getNoCardState());
    }
    
}

class ATMMachine {
    private ATMState noCardState;
    private ATMState hasCardState;
    private ATMState currentState;


    public ATMMachine () {
        noCardState = new NoCardState(this);
        hasCardState = new HasCardState(this);
        currentState = noCardState;
    }

    public void setState(ATMState state) {
        this.currentState = state;
    }

    public ATMState getNoCardState() {
        return noCardState;
    }

    public ATMState getHasCardState() {
        return hasCardState;
    }

    public void insertCard() {
        currentState.insertCard();
    }
    
    public void ejectCard() {
        currentState.ejectCard();
    }

    public void withdrawCash() {
        currentState.withdrawCash();
    }
}

public class StateDesignPattern {
    public static void main(String[] args) {
        ATMMachine atmMachine = new ATMMachine();
        atmMachine.withdrawCash();
        atmMachine.insertCard();
        atmMachine.withdrawCash();
        atmMachine.ejectCard();
    }
}
