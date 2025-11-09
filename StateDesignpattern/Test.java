public class Test {
    public static void main(String[] args){
        TrafficLightContext context = new TrafficLightContext();
        context.change(); // Green
        context.change(); // Yellow
        context.change(); // Red
        context.change(); // Green
    }
    
}
