package ParkingLot;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {

        // 1. Initialize the Parking Lot using the Singleton's getInstance method
        ParkingLot myParkingLot = ParkingLot.getInstance("My parking", 3.00);

        // 2. Add some parking spots
        for (int i = 1; i <= 5; i++) { // 5 compact spots
            myParkingLot.addParkingSpot(new CompactSpot("C" + i));
        }
        for (int i = 1; i <= 2; i++) { // 2 large spots
            myParkingLot.addParkingSpot(new LargeSpot("L" + i));
        }
        for (int i = 1; i <= 2; i++) { // 2 motorcycle spots
            myParkingLot.addParkingSpot(new MotorcycleSpot("M" + i));
        }
        for (int i = 1; i <= 1; i++) { // 1 electric spot
            myParkingLot.addParkingSpot(new ElectricSpot("E" + i));
        }

        myParkingLot.getParkingLotStatus();

        // 3. Initialize Entry and Exit Terminals
        // Terminals still reference the single parking lot instance
        EntryTerminal entryGate1 = new EntryTerminal("Entry1", myParkingLot);
        ExitTerminal exitGate1 = new ExitTerminal("Exit1", myParkingLot);

        // 4. Simulate vehicles entering
        Map<String, String> vehicleTickets = new HashMap<>(); // To store tickets for later exit

        // Car 1 enters
        Ticket ticket1 = entryGate1.issueTicket("V-CAR-001", VehicleType.CAR);
        if (ticket1 != null) {
            vehicleTickets.put("V-CAR-001", ticket1.getTicketId());
        }

        // Motorcycle 1 enters
        Ticket ticket2 = entryGate1.issueTicket("V-MOTO-001", VehicleType.MOTORCYCLE);
        if (ticket2 != null) {
            vehicleTickets.put("V-MOTO-001", ticket2.getTicketId());
        }

        // Truck 1 enters (needs a large spot)
        Ticket ticket3 = entryGate1.issueTicket("V-TRUCK-001", VehicleType.TRUCK);
        if (ticket3 != null) {
            vehicleTickets.put("V-TRUCK-001", ticket3.getTicketId());
        }

        // Electric Car 1 enters
        Ticket ticket4 = entryGate1.issueTicket("V-EV-001", VehicleType.ELECTRIC_CAR);
        if (ticket4 != null) {
            vehicleTickets.put("V-EV-001", ticket4.getTicketId());
        }

        // Car 2 enters (fills another compact spot)
        Ticket ticket5 = entryGate1.issueTicket("V-CAR-002", VehicleType.CAR);
        if (ticket5 != null) {
            vehicleTickets.put("V-CAR-002", ticket5.getTicketId());
        }

        myParkingLot.getParkingLotStatus();
        myParkingLot.getActiveTicketsStatus();

        // Simulate some time passing (not actual time, just for calculation)
        try {
            Thread.sleep(2000); // Simulate 2 seconds passing, which will translate to some hours for fee calculation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Simulation interrupted.");
        }

        // 5. Simulate vehicles exiting
        System.out.println("\n--- Simulating Exits ---");

        // Car 1 exits
        if (vehicleTickets.containsKey("V-CAR-001")) {
            exitGate1.processExit(vehicleTickets.get("V-CAR-001"));
        }

        // Motorcycle 1 exits
        if (vehicleTickets.containsKey("V-MOTO-001")) {
            exitGate1.processExit(vehicleTickets.get("V-MOTO-001"));
        }

        // Try to exit with an invalid ticket
        exitGate1.processExit("INVALID_TICKET_ID");

        myParkingLot.getParkingLotStatus();
        myParkingLot.getActiveTicketsStatus();

        // Car 3 tries to enter when compact spots are full (if only 2 compact spots were added)
        System.out.println("\n--- Attempting to park more vehicles ---");
        Ticket ticket6 = entryGate1.issueTicket("V-CAR-003", VehicleType.CAR);
        if (ticket6 != null) {
            vehicleTickets.put("V-CAR-003", ticket6.getTicketId());
        }

        myParkingLot.getParkingLotStatus();
    }
}

// --- Enums/Constants (representing fixed values) ---

/**
 * Defines the types of parking spots available.
 */
enum ParkingSpotType {
    COMPACT,
    LARGE,
    MOTORCYCLE,
    ELECTRIC // Added for demonstration
}

/**
 * Defines the types of vehicles that can park.
 */
enum VehicleType {
    CAR,
    TRUCK,
    MOTORCYCLE,
    ELECTRIC_CAR
}

/**
 * Defines the status of a parking spot.
 */
enum ParkingStatus {
    OCCUPIED,
    FREE
}

/**
 * Defines the status of a payment.
 */
enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED
}


class ParkingLot {
    private static ParkingLot instance;
    private String name;
    private double hourlyRate;
    private Map<ParkingSpotType, List<ParkingSpot>>parkingSpots;
    private Map<String, Ticket> activeTickets;
    private Map<String, ParkingSpot> spotMap;
   
    private ParkingLot(String name, double hourlyRate) {
        this.name = name;
        this.hourlyRate = hourlyRate;
         this.parkingSpots = new HashMap<>();
        for (ParkingSpotType type : ParkingSpotType.values()) {
            this.parkingSpots.put(type, new ArrayList<>());
        }
        this.activeTickets = new HashMap<>();
        this.spotMap = new HashMap<>();

        System.out.println(String.format("Parking Lot '%s' initialized with hourly rate: $%.2f", this.name, this.hourlyRate));
    
    }

    public static ParkingLot getInstance(String name, double hourlyRate){
        if(instance==null){
            instance =  new ParkingLot(name, hourlyRate);
        }
        return instance;
    }
    public void addParkingSpot(ParkingSpot spot) {
        this.parkingSpots.get(spot.getSpotType()).add(spot);
        this.spotMap.put(spot.getSpotId(), spot);
        System.out.println(String.format("Added %s spot: %s", spot.getSpotType(), spot.getSpotId()));
    }

     public ParkingSpot findAndAssignSpot(String vehicleId, VehicleType vehicleType) {
        // Define a preference order for spot types
        Map<VehicleType, List<ParkingSpotType>> spotPreference = new HashMap<>();
        spotPreference.put(VehicleType.MOTORCYCLE, Arrays.asList(ParkingSpotType.MOTORCYCLE, ParkingSpotType.COMPACT, ParkingSpotType.LARGE));
        spotPreference.put(VehicleType.CAR, Arrays.asList(ParkingSpotType.COMPACT, ParkingSpotType.LARGE));
        spotPreference.put(VehicleType.ELECTRIC_CAR, Arrays.asList(ParkingSpotType.ELECTRIC, ParkingSpotType.COMPACT, ParkingSpotType.LARGE));
        spotPreference.put(VehicleType.TRUCK, Arrays.asList(ParkingSpotType.LARGE));

        List<ParkingSpotType> preferredTypes = spotPreference.get(vehicleType);
        if (preferredTypes != null) {
            for (ParkingSpotType preferredType : preferredTypes) {
                List<ParkingSpot> spotsOfType = this.parkingSpots.get(preferredType);
                if (spotsOfType != null) {
                    for (ParkingSpot spot : spotsOfType) {
                        if (spot.isFree()) {
                            spot.occupy(vehicleId);
                            return spot;
                        }
                    }
                }
            }
        }
        return null; // No suitable spot found
    }
    public void freeSpot(String spotId) {
        ParkingSpot spot = this.spotMap.get(spotId);
        if (spot != null) {
            spot.free();
        } else {
            System.out.println(String.format("Error: Spot %s not found.", spotId));
        }
    }
     public void getParkingLotStatus() {
        System.out.println("\n--- Parking Lot Status ---");
        for (Map.Entry<ParkingSpotType, List<ParkingSpot>> entry : parkingSpots.entrySet()) {
            ParkingSpotType spotType = entry.getKey();
            List<ParkingSpot> spots = entry.getValue();
            long freeCount = spots.stream().filter(ParkingSpot::isFree).count();
            long occupiedCount = spots.size() - freeCount;
            System.out.println(String.format("%s Spots: Total %d, Free %d, Occupied %d",
                    spotType, spots.size(), freeCount, occupiedCount));
        }
        System.out.println("--------------------------");
    }
   public void getActiveTicketsStatus() {
        System.out.println("\n--- Active Tickets ---");
        if (activeTickets.isEmpty()) {
            System.out.println("No active tickets.");
            return;
        }
        for (Ticket ticket : activeTickets.values()) {
            System.out.println(ticket);
        }
        System.out.println("----------------------");
    }

    // Getters for ParkingLot properties
    public double getHourlyRate() {
        return hourlyRate;
    }

    public Map<String, Ticket> getActiveTickets() {
        return activeTickets;
    }
}

abstract class Terminal{
    protected String terminalId;
    protected ParkingLot parkingLot;

    public Terminal(String terminalId, ParkingLot parkingLot){
        this.terminalId = terminalId;
        this.parkingLot =parkingLot;
    }
}

class EntryTerminal extends Terminal {
    public EntryTerminal(String terminalId, ParkingLot parkingLot){
        super(terminalId, parkingLot);
    }
    public Ticket issueTicket(String vehicleId, VehicleType vehicleType) {
        System.out.println(String.format("\nEntry Terminal %s: Vehicle %s (%s) attempting to enter.", terminalId, vehicleId, vehicleType));
        ParkingSpot spot = parkingLot.findAndAssignSpot(vehicleId, vehicleType);
        if (spot != null) {
            Ticket ticket = new Ticket(vehicleId, spot.getSpotId());
            parkingLot.getActiveTickets().put(ticket.getTicketId(), ticket);
            System.out.println(String.format("Ticket issued: %s for spot %s.", ticket.getTicketId(), spot.getSpotId()));
            return ticket;
        } else {
            System.out.println("No suitable parking spot found. Parking lot might be full or no spot for this vehicle type.");
            return null;
        }
    }
}

class ExitTerminal extends Terminal{
    public ExitTerminal(String terminalId, ParkingLot parkingLot)
    {
        super(terminalId, parkingLot);
    }

    public boolean processExit(String ticketId) {
        System.out.println(String.format("\nExit Terminal %s: Processing exit for ticket %s.", terminalId, ticketId));
        Ticket ticket = parkingLot.getActiveTickets().get(ticketId);

        if (ticket == null) {
            System.out.println(String.format("Error: Ticket %s not found or already processed.", ticketId));
            return false;
        }

        if (ticket.getPaymentStatus() == PaymentStatus.COMPLETED) {
            System.out.println(String.format("Ticket %s already paid. Proceeding with exit.", ticketId));
        } else {
            ticket.setExitTime();
            double amount = ticket.calculateAmount(parkingLot.getHourlyRate());
            System.out.println(String.format("Amount due for ticket %s: $%.2f", ticketId, amount));

            // Simulate payment (e.g., using a Strategy pattern here in a real system)
            if (simulatePayment(amount)) {
                ticket.markPaid();
            } else {
                System.out.println(String.format("Payment failed for ticket %s. Vehicle cannot exit.", ticketId));
                return false;
            }
        }

        // Free the parking spot
        String spotId = ticket.getSpotId();
        parkingLot.freeSpot(spotId);

        // Remove ticket from active tickets
        parkingLot.getActiveTickets().remove(ticketId);
        System.out.println(String.format("Vehicle with ticket %s has exited.", ticketId));
        return true;
    }

    /**
     * A placeholder for actual payment processing logic.
     * In a real system, this would involve payment gateways.
     * For now, it always succeeds.
     * @param amount The amount to be paid.
     * @return true if payment is successful, false otherwise.
     */
    private boolean simulatePayment(double amount) {
        System.out.println(String.format("Simulating payment of $%.2f...", amount));
        // In a real system, this would be more complex, e.g.,
        // calling a payment gateway API.
        return true;
    }

}

class Ticket{
    private String ticketId;
    private String vehicleId;
    private String spotId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double amountDue;
    private PaymentStatus paymentStatus;

    public Ticket(String vehicleId, String spotId) {
        this.ticketId = UUID.randomUUID().toString(); // Unique ticket ID
        this.vehicleId = vehicleId;
        this.spotId = spotId;
        this.entryTime = LocalDateTime.now();
        this.exitTime = null;
        this.amountDue = 0.0;
        this.paymentStatus = PaymentStatus.PENDING;
    }

     public void setExitTime() {
        this.exitTime = LocalDateTime.now();
    }

    /**
     * Calculates the parking fee based on duration and hourly rate.
     * @param hourlyRate The hourly rate for parking.
     * @return The calculated amount due.
     */
    public double calculateAmount(double hourlyRate) {
        if (this.entryTime != null && this.exitTime != null) {
            Duration duration = Duration.between(this.entryTime, this.exitTime);
            double durationHours = Math.max(1, (double) duration.getSeconds() / 3600.0); // Minimum 1 hour charge
            this.amountDue = Math.round(durationHours * hourlyRate * 100.0) / 100.0; // Round to 2 decimal places
        } else {
            this.amountDue = 0.0;
        }
        return this.amountDue;
    }

    /**
     * Marks the ticket as paid.
     */
    public void markPaid() {
        this.paymentStatus = PaymentStatus.COMPLETED;
        System.out.println(String.format("Ticket %s marked as paid. Amount: $%.2f", this.ticketId, this.amountDue));
    }

    // Getters for Ticket properties
    public String getTicketId() {
        return ticketId;
    }

    public String getSpotId() {
        return spotId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public String toString() {
        return String.format("Ticket ID: %s, Vehicle: %s, Spot: %s, Entry: %s, Exit: %s, Amount Due: $%.2f, Status: %s",
                ticketId, vehicleId, spotId, entryTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                (exitTime != null ? exitTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A"),
                amountDue, paymentStatus);
    }
}
abstract class ParkingSpot{
    private String spotId;
    private ParkingSpotType spotType;
    private ParkingStatus status;
    private String vehicleId;

    public ParkingSpot(String spotId, ParkingSpotType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.status = ParkingStatus.FREE;
        this.vehicleId = null;
    }
     /**
     * Checks if the parking spot is free.
     * @return true if the spot is free, false otherwise.
     */
    public boolean isFree() {
        return this.status == ParkingStatus.FREE;
    }

    /**
     * Marks the spot as occupied by a vehicle.
     * @param vehicleId The ID of the vehicle occupying the spot.
     */
    public void occupy(String vehicleId) {
        if (isFree()) {
            this.status = ParkingStatus.OCCUPIED;
            this.vehicleId = vehicleId;
            System.out.println(String.format("Spot %s (%s) occupied by vehicle %s.", this.spotId, this.spotType, this.vehicleId));
        } else {
            System.out.println(String.format("Spot %s is already occupied.", this.spotId));
        }
    }

    /**
     * Frees up the parking spot.
     */
    public void free() {
        if (this.status == ParkingStatus.OCCUPIED) {
            System.out.println(String.format("Spot %s (%s) freed from vehicle %s.", this.spotId, this.spotType, this.vehicleId));
            this.status = ParkingStatus.FREE;
            this.vehicleId = null;
        } else {
            System.out.println(String.format("Spot %s is already free.", this.spotId));
        }
    }

    public String getSpotId() {
        return spotId;
    }

    public ParkingSpotType getSpotType() {
        return spotType;
    }

    @Override
    public String toString() {
        return String.format("Spot ID: %s, Type: %s, Status: %s, Vehicle: %s",
                spotId, spotType, status, (vehicleId != null ? vehicleId : "None"));
    }
}