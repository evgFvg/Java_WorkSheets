abstract class CarDecorator implements Car {
    protected Car decoratedCar;
    public CarDecorator(Car car){
        decoratedCar = car;
    }
    public void paint(){
        decoratedCar.paint();
    }
}