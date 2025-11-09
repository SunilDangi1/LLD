public class Redstate implements TrafficLightState{

    @Override
    public void next(TrafficLightContext context){
        System.out.println("state changing from Red to Green");
        context.setState(new GreentState());
    }

}