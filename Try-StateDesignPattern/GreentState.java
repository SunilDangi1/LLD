public class GreentState implements TrafficLightState{

    @Override
    public void next(TrafficLightContext ct){
        System.out.println("state changing from Green to Yellow");
        ct.setState(new YellowState());
    }
    
}