package CarRental;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

enum VehicleType {
    BIKE, CAR
}

enum ReservationStatus {
    PENDING, IN_PROGRESS, BOOKED, COMPLETED
}

enum VehicleStatus{
    AVAILABLE, BOOKED
}

abstract class Vehicle{
    String registrationNo;
    VehicleType vehicleType;
    VehicleStatus status;

    public Vehicle(String reg, VehicleType type){
        this.registrationNo = reg;
        this.vehicleType = type;
        this.status = VehicleStatus.AVAILABLE;
    }
    
}

class VehicleFactory{
    public Vehicle CreateVehicle(String reg, VehicleType type){
        switch (type) {
            case VehicleType.BIKE:
                return new Bike(reg,type);
            case VehicleType.CAR:
                return new Car(reg,type);
            default:
                throw new IllegalArgumentException("Inavalid Vehicle type");
        }
    }
}

class Bike extends Vehicle{
    public Bike(String reg, VehicleType type){
        super(reg, type);
    }
}

class Car extends Vehicle{
    public Car(String reg, VehicleType type){
        super(reg,type);
    }
}

class User{
    String userId;
    String name;
    List<Reservation> reservation;

    public User(String userId, String name){
        this.name = name;
        this.userId = userId;
        this.reservation = new ArrayList<Reservation>() ;
    }
}

class Reservation{
    String id;
    User user;
    Date startDate;
    Date endDate;
    RentalStore store;
    ReservationStatus status;
    Vehicle vehicle;

    public Reservation(User user, Date startDate, Date endDate, RentalStore store, Vehicle vehicle){
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.store = store;
        this.status = ReservationStatus.PENDING;
        this.vehicle = vehicle;
    }
    
}

class RentalStore{
    String id;
    String location;
    String name;
    HashMap<String,Vehicle> vehicles;

    public RentalStore(String name, String location){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.location = location;
        this.vehicles = new HashMap<>();
    }

    public List<Vehicle> getavailabVehicles(){
        List<Vehicle>availaVehicles =  new ArrayList<>();
        for(Vehicle v : vehicles.values()){
            if(v.status==VehicleStatus.AVAILABLE){
                availaVehicles.add(v);
            }
        }
        return availaVehicles;
    }

    public void addVehicle(Vehicle vehicle){
        this.vehicles.put(vehicle.registrationNo, vehicle);
        System.out.println("Vehicle with Resetartion no " + vehicle.registrationNo + " is addesd in store " + this.name);
    }

}

class ReservationManager{
     Map<String, Reservation> reservations;
     int nextReservationId;

    public ReservationManager() {
        this.reservations = new HashMap<>();
        this.nextReservationId = 1;
    }

    public Reservation CreateReservation(User user, Date startDate, Date endDate,RentalStore store, Vehicle vehicle){
        Reservation reservation = new Reservation(user, startDate, endDate, store,vehicle);
        reservations.put(reservation.id, reservation);
        user.reservation.add(reservation);
        return reservation;
    }
}

class RentalSystem{
    private static RentalSystem instance;
    List<RentalStore>stores;
    List<User>user;
    VehicleFactory factory ;
    ReservationManager reservationManager;
    PaymentProcessor paymentProcessor;


    private RentalSystem(){
        this.stores = new ArrayList<>();
        this.user = new ArrayList<>();
        this.factory = new VehicleFactory();
        this.reservationManager = new ReservationManager();
        this.paymentProcessor = new PaymentProcessor();
    }

    public Reservation CreateReservation(User user, Date startDate, Date endDate, RentalStore store, Vehicle vehicle){
        System.out.println("Reservatuion done");
        return reservationManager.CreateReservation(user,startDate,endDate,store,vehicle);
    }

    public boolean processPayment(int reservationId, PaymentStrategy paymentStrategy){
        return paymentProcessor.processPayment(200.00,paymentStrategy);
    }
    public static RentalSystem getinstance(){
        if(instance == null)
        {
            instance = new RentalSystem();
        }
        return instance;
    }

    public void addStore(RentalStore store){
        stores.add(store);
        System.out.println("store added");
    }
}

interface PaymentStrategy{
    void processPayment(double amount);
}

class Cash implements PaymentStrategy{
    public void processPayment(double amount){
        System.out.println("amount processed");
    }
}

class PaymentProcessor{
    public boolean processPayment(double amount, PaymentStrategy strategy){
        strategy.processPayment(amount);
        return true;
    }
}

class CarRentalSystem{
    public static void main(String argu[]){
        System.out.println("hello");

        RentalSystem instance = RentalSystem.getinstance();

        Vehicle car1 = instance.factory.CreateVehicle("123", VehicleType.CAR);
        Vehicle Bike1 = instance.factory.CreateVehicle("456", VehicleType.BIKE);

        User user1 = new User("111 ", "Sunil");
        
        RentalStore store1 = new RentalStore("BhopalStore","Bhoapl");

        instance.addStore(store1);

        store1.addVehicle(Bike1);
        store1.addVehicle(car1);

        Reservation reservation1 = instance.CreateReservation(user1, new Date(2025,10,11), new Date(2025,12,2), store1, Bike1);

        boolean result = instance.processPayment(123, new Cash());
        if(result) System.out.println("payment Done");
        else{
            System.out.println("Payment failed");
        }
    }
}