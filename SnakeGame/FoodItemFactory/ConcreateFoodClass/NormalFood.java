package FoodItemFactory.ConcreateFoodClass;

import FoodItemFactory.FoodFactory;
import FoodItemFactory.FoodItem;

public class NormalFood extends FoodItem{
    public NormalFood(int row, int col){
        super(row, col);
        this.points = 1;
    }
}
