package FoodItemFactory.ConcreateFoodClass;

import FoodItemFactory.FoodFactory;
import FoodItemFactory.FoodItem;

public class BonusFood extends FoodItem{
    public BonusFood(int row, int col){
        super(row,col);
        this.points = 3;
    }
}
