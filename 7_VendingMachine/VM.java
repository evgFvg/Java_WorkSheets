import java.util.ArrayList;

public class VM {
    private double balance = 0;
    private Monitor monitor = null;
    private ArrayList<Product> products = null;
    private State currState;
    private Product currProduct = null;

    public VM(Monitor monitor, ArrayList<Product> products) {
        this.products = products;
        this.monitor = monitor;
        currState = State.OFF;
    }

    public double getBalance() {
        return balance;
    }

    void setBalance(double balance) {
        this.balance = balance;
    }

    State getCurrState() {
        return currState;
    }

    void setCurrState(State currState) {
        this.currState = currState;
    }

    Monitor getMonitor() {
        return monitor;
    }

    Product getCurrProduct() {
        return currProduct;
    }

    void setCurrProduct(Product currProduct) {
        this.currProduct = currProduct;
    }

    private ArrayList<Product> getProducts() {
        return products;
    }

    public void insertMoney(CoinI coins) {
        if (getCurrState() != State.OFF) {
            setBalance(getBalance() + coins.getCurrencyInShekel(1));
            this.getCurrState().insertMoney(this);
        }
    }

    public void chooseItem(String product_name) {
        if (getCurrState() != State.OFF) {
            for (Product p : getProducts()) {
                if (p.toString().equals(product_name)) {
                    setCurrProduct(p);
                    getCurrState().chooseProduct(this);
                    break;
                }
            }
            if (null == this.getCurrProduct()) {
                getMonitor().print("No such item as " + product_name);
            }
        }
    }

    public void on() {

        getCurrState().on(this);
    }

    public void off() {

        getCurrState().off(this);
    }

    public void cancel() {

        getCurrState().cancel(this);
    }

    void getChangeAndSetState(State newState) {
        getMonitor().print("Take your " + getBalance() + " shekels back");
        setBalance(0);
        setCurrState(newState);
        setCurrProduct(null);
    }

    boolean isEnoughMoney(double itemPrice) {
        return (0 <= Double.compare(getBalance(), itemPrice));
    }

    void rejectHandler(String msg, State newState) {
        getChangeAndSetState(newState);
        getMonitor().print(msg);
    }

    void sellHandler() {
        double itemPrice = getCurrProduct().getPrice();
        if (isEnoughMoney(itemPrice)) {
            getMonitor().print("Enjoy your " + getCurrProduct().getName());
            setBalance(getBalance() - getCurrProduct().getPrice());
            getChangeAndSetState(State.IDLE);
        } else {
            getMonitor().print("No enough money. Please add " + (itemPrice - getBalance()));
            setCurrState(State.WAIT);
        }
    }
}
