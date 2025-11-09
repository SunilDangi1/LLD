
public class GreenState implements TrafficLightState {

    @Override
    public void next(TrafficLightContext context) {
        System.out.println("Changing from Green to Yellow");
        context.setState(new YellowState());
    }

    @Override
    public String getColor() {
        return "Green";
    }

}
