package InventoryManagmentSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale.Category;

enum ProductCategory{
    FURNITURE, ELETRONICS
}

class Product{
    String id;
    ProductCategory category;
    String name;
    int quantity;
    int thresold;

    public Product(String id, ProductCategory category, String name, int quantity, int thresold){
        this.id = id;
        this.thresold = thresold;
        this.category = category;
        this.quantity = quantity;
        this.name = name;
    }

}

class ElectronicsProduct extends Product {
    String Brand;
    public ElectronicsProduct (String id, ProductCategory category, String name, int quantity, int thresold){
        super(name, category, name, thresold, thresold);
        this.Brand = "none";
    }
    
}

class FurnitureProduct extends Product {
    String Brand;
    public FurnitureProduct (String id, ProductCategory category, String name, int quantity, int thresold){
        super(name, category, name, thresold, thresold);
        this.Brand = "none";
    }
    
}

class ProductFactory{
    public Product creatProduct(String id, ProductCategory type, String name, int quantity, int thresold){
        switch (type) {
            case ProductCategory.ELETRONICS:
                return new ElectronicsProduct(id,type, name, quantity, thresold);
            case ProductCategory.FURNITURE:
                return new FurnitureProduct(id,type, name, quantity, thresold);
            default:
                throw new IllegalArgumentException("invalid Category");
        }
        
    }
}

class WareHouse{
    String id;
    String location;
    HashMap<String, Product> products;

    public WareHouse(String id, String location){
        this.id =  id;
        this.location = location;
        this.products = new HashMap<>();
    }

     public void addProduct(Product product){
        products.put(product.id, product);
        System.out.println("Product added quantity:  " + product.quantity);
    }

     public void removeProduct(Product product){
        if(products.get(product.id)!=null)
            products.remove(product.id);
        else
        {
            System.out.println("no product found for removal from wareHouse");
        }

    }

}

class InvertoryManager{
    private static InvertoryManager instance;
    private ProductFactory factory;
    ArrayList<WareHouse>wareHouses;
    private InvertoryManager(){
        this.factory =  new ProductFactory();
        this.wareHouses =  new ArrayList<>();
    }
    public static InvertoryManager getinstance(){
        if(instance==null){
            instance = new InvertoryManager();
        }
        return instance;
    }
    public void addWarehoue(WareHouse wareHouse){
            this.wareHouses.add(wareHouse);
    }
    public void reomoveWarehoue(WareHouse wareHouse){
            this.wareHouses.remove(wareHouse);
    }
}

public class InventoryManagmentSytem {
    public static void main(String[] args) {
        InvertoryManager invertoryManager = InvertoryManager.getinstance();

        WareHouse w1 = new WareHouse("123", "Bhopal");
        WareHouse w2 = new WareHouse("111", "Hyd");

        ProductFactory factory = new ProductFactory();
        Product laptop =  factory.creatProduct("1", ProductCategory.ELETRONICS, "Asus", 11, 5);
        Product sofa  =  factory.creatProduct("2", ProductCategory.FURNITURE, "Dewas", 100 , 5);

        w1.addProduct(sofa);
        w1.addProduct(laptop);

    }
}
