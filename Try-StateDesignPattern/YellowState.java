public class YellowState implements TrafficLightState{

    @Override
    public void next(TrafficLightContext ct){
        System.out.println("state changing from yellow to Red");
        ct.setState(new RedState());
    }
    
}