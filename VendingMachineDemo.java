import java.util.HashMap;
import java.util.Map;

/**
 
VendingMachine:
Functional Requirements:
    User:
        Insert Money
        Select Product

    Machine:
        Dipense the product
        Maintain Inventory
        Return the change
        Handle Insufficent balance
        Inventory out of stock
        Machine Should generate the receipt
        Machine should accept different modes of payment

Non-Functional Requirements:
    Extensible
        - multiple payment modes
        - different product types
    Thread safety


2nd Step:
    -- Indentifying the core entities
        VendingMachine
        Inventory
            -- Map of Product -- Count
        Product
        Slot
            Product
        Coin
        Note
        Payment
        State

State
    1. IdleState
    2. HasMoneyState
    3. productSelectedState
    4. DispenseState
    5. OutOfStockState

Methods:
    insertMoney
    selectProduct
    dipenseProduct
    cancel
*/

class Product {
    private String name;
    private double cost;
}

class Slot {
    String id;
    Product product;
}

class Inventory {
    Map<String, Slot> slots = new HashMap<>();
}

enum Coin {

}

interface IState {
    void insertCoin(Coin coin);
    void selectProduct(String slotId);
    void dispenseProduct();
    void cancel();
}

class NoMoneyState implements IState {
    VendingMachine vendingMachine;

    public NoMoneyState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        // Changing state if coin is inserted
    }

    @Override
    public void selectProduct(String slotId) {
        
    }

    @Override
    public void dispenseProduct() {
        
    }

    @Override
    public void cancel() {
        
    }
    
}

class HasMoneyState implements IState {

    private VendingMachine vendingMachine;

    public HasMoneyState(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin(Coin coin) {
        // Changing state if coin is inserted
    }

    @Override
    public void selectProduct(String slotId) {
        
    }

    @Override
    public void dispenseProduct() {
        
    }

    @Override
    public void cancel() {
        
    }
}

class VendingMachine {
    private IState currentState;
    private IState hasMoneyState;
    private IState noMoneyState;

    Payment payment;
    double amount;

    public void insertCoin(Coin coin) {
        currentState.insertCoin(coin);
    }

    public void selectProduct(String slotId) {
        currentState.selectProduct(slotId);
    }
}

public class VendingMachineDemo {
    
}