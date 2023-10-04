import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VMTest {

    private static ArrayList<Product> store = null;
    private static Monitor monitor = null;
    private static VM vm = null;
    private static CoinI dollar = null;
    private static CoinI euro = null;
    private static CoinI shekel = null;

    @BeforeAll
    static void setup() {
        store = new ArrayList<>();
        monitor = new Monitor();

        dollar = CoinImp.DOLLAR;
        euro = CoinImp.EURO;
        shekel = CoinImp.SHEKEL;

        Product snikers = new Product() {
            @Override
            public String getName() {
                return "Snikers";
            }

            @Override
            public double getPrice() {
                return 3.0;
            }
            @Override
            public String toString() {
                return  getName();
            }
        };
        Product cola = new Product() {
            @Override
            public String getName() {
                return "Cola";
            }

            @Override
            public double getPrice() {
                return 10.0;
            }
            @Override
            public String toString() {
                return  getName();
            }
        };
        Product pepsi = new Product() {
            @Override
            public String getName() {
                return "Pepsi";
            }

            @Override
            public double getPrice() {
                return 8.0;
            }
            @Override
            public String toString() {
                return  getName();
            }
        };

        store.add(snikers);
        store.add(cola);
        store.add(pepsi);

    }

    @BeforeEach
    void init() {
        vm = new VM(monitor, store);
    }

    @Test
    void onOff() {
        assertSame(vm.getCurrState(), State.OFF);
        assertNull(vm.getCurrProduct());
        vm.on();
        assertSame(vm.getCurrState(), State.IDLE);
        assertNull(vm.getCurrProduct());
        vm.off();
        assertSame(vm.getCurrState(), State.OFF);
    }

    @Test
    void insertMoney() {
        vm.insertMoney(dollar);
        assertSame(vm.getCurrState(), State.OFF);
        assertEquals(0, vm.getBalance());
        vm.on();
        vm.insertMoney(dollar);
        assertSame(vm.getCurrState(), State.IDLE);
        assertEquals(vm.getBalance(), 3.5);
        vm.insertMoney(euro);
        assertEquals(vm.getBalance(), 3.5 + 4);
        vm.insertMoney(shekel);
        assertEquals(vm.getBalance(), 3.5 + 4 + 1);
        vm.off();
        assertEquals(vm.getBalance(), 0);
        assertSame(vm.getCurrState(), State.OFF);
    }

    @Test
    void cancel() {
        vm.on();
        for (int i = 0; i < 4; ++i) {
            vm.insertMoney(euro);
        }
        assertEquals(vm.getBalance(), 16.0);
        vm.cancel();
        assertEquals(vm.getBalance(), 0);
        assertSame(vm.getCurrState(), State.IDLE);
        vm.off();
    }


    @Test
    void chooseItem() {
        vm.on();
        vm.insertMoney(euro);
        vm.insertMoney(euro);
        vm.chooseItem("Cola"); //price 10
        assertSame(vm.getCurrState(), State.WAIT);
        assertTrue(0 == Double.compare(vm.getBalance(), 8.0));
        vm.insertMoney(euro);
        assertTrue(0 == Double.compare(vm.getBalance(), 0.0));
        vm.insertMoney(euro);
        vm.insertMoney(euro);
        vm.chooseItem("Twix"); //not exist
        assertTrue(0 == Double.compare(vm.getBalance(), 8.0));
        assertSame(vm.getCurrState(), State.IDLE);
        assertNull(vm.getCurrProduct());
        vm.off();
    }

}

