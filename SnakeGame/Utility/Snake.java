package Utility;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Snake {
    private Deque<Pair>body;
    private Map<Pair,Boolean> positionMap;
    public Snake() {
        this.body = new LinkedList<>();
        this.positionMap = new HashMap<>();
        Pair intialPos = new Pair(0,0);
        this.body.offerFirst(intialPos);
        this.positionMap.put(intialPos, true);
    }
    public Deque<Pair> getBody() {
        return body;
    }
    public void setBody(Deque<Pair> body) {
        this.body = body;
    }
    public Map<Pair, Boolean> getPositionMap() {
        return positionMap;
    }
    public void setPositionMap(Map<Pair, Boolean> positionMap) {
        this.positionMap = positionMap;
    }
    
    
}
