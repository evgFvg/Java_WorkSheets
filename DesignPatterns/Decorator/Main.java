public class Main {
    public static void main(String[] args) {
        Car defaultHybridCar = new HybridCar();

        Car redHybridCar = new StreetArtCar(new HybridCar());  //decorator in action!
        Car blueElectricCar = new StreetArtCar(new ElectricCar()); //decorator in action!

        defaultHybridCar.paint();
        redHybridCar.paint();
        blueElectricCar.paint();
    }
}