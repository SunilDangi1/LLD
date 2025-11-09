public class RedState implements TrafficLightState {

    @Override
    public void next(TrafficLightContext context) {
        System.out.println("Changing from Red to Green");
        context.setState(new GreenState());
    }

    @Override
    public String getColor() {
        return "Red";
    }

    
}