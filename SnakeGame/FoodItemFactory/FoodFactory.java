package FoodItemFactory;

import FoodItemFactory.ConcreateFoodClass.BonusFood;
import FoodItemFactory.ConcreateFoodClass.NormalFood;

public class FoodFactory {
    public static FoodItem createFood(int[] position, String type){
        if("Normal".equals(type))
            return new NormalFood(position[0],position[1]);
        return new BonusFood(position[0],position[1]);
    }
}
