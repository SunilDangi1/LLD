public class TrafficLightContext {
    private TrafficLightState state;

    public TrafficLightContext(){
        this.state = new RedState();
    }

    public void setState(TrafficLightState state) {
        this.state = state;
    }

    public void change() {
        state.next(this);
    }
    
}
