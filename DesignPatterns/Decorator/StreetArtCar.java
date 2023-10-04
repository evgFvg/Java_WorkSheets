//One of possible Decorator implementations
public class StreetArtCar extends CarDecorator {
    public StreetArtCar(Car car) {
        super (car);
    }
    @Override
    public void paint(){
        decoratedCar.paint();
        setTheme(decoratedCar);
    }
    private void setTheme(Car car){
        // ...
    }

}