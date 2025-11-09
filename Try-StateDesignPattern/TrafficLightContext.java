public class TrafficLightContext {
    private TrafficLightState state;
    public TrafficLightContext(){
        this.state = new RedState();
    }
    public void setState(TrafficLightState st){
        this.state = st;
    } 
    public void change(){
        state.next(this);
    }
    
}
