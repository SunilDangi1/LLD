
public class YellowState implements TrafficLightState {

    @Override
    public void next(TrafficLightContext context) {
        System.out.println("Changing from Yellow to Red");
        context.setState(new RedState());
    }

    @Override
    public String getColor() {
        return "Yellow";
    }

}
