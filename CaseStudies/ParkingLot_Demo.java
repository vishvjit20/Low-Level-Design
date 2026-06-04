package CaseStudies;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * Functional Requirements -
 * 1. Multiple entry / exit gate
 * 2. Different type of Vehicles
 * - bike, car, truck
 * 3. Multiple floors
 * 4. Each floor has multiple slots
 * 5. Slot assignment strategy
 * - near exit gate
 * - random
 * 6. Charge on hourly basis
 * 7. Ticket generation at exit
 * 8. Payment at exit
 * 
 * Non functional requirements -
 * 1. Extensible
 * 2. Thread safety
 * 
 * 
 * Entities
 * 1. EntryGate
 * 2. ExitGate
 * 3. Vehicle
 * 4. VehicleType
 * - CAR, BIKE, TRUCK
 * 5. Floor
 * 6. ParkingSlot
 * 7. Ticket
 * 8. Payment
 * 
 * 
 * parkinglot/
 * 
 * ├── model
 * │ ├── Vehicle.java
 * │ ├── VehicleType.java
 * │ ├── ParkingSlot.java
 * │ ├── Floor.java
 * │ ├── Ticket.java
 * │ ├── Payment.java
 * │ └── Receipt.java
 * │
 * ├── gate
 * │ ├── EntryGate.java
 * │ └── ExitGate.java
 * │
 * ├── strategy
 * │ ├── slot
 * │ │ ├── SlotAssignmentStrategy.java
 * │ │ ├── RandomSlotStrategy.java
 * │ │ └── NearExitStrategy.java
 * │ │
 * │ └── pricing
 * │ ├── PricingStrategy.java
 * │ └── HourlyPricingStrategy.java
 * │
 * ├── service
 * │ ├── ParkingService.java
 * │ ├── PaymentService.java
 * │ └── TicketService.java
 * │
 * ├── repository
 * │ └── TicketRepository.java
 * │
 * ├── factory
 * │ └── VehicleFactory.java
 * │
 * ├── manager
 * │ └── ParkingLotManager.java
 * │
 * └── Main.java
 */

enum VehicleType {
        CAR, TRUCK, BIKE
}

enum PaymentMode {
        UPI, CARD, CASH
}

enum PaymentStatus {
        PENDING, SUCCESS, FAILED
}

class ParkingFullException extends RuntimeException {
        public ParkingFullException(String message) {
                super(message);
        }
}

class InvalidTicketException extends RuntimeException {
        public InvalidTicketException(String message) {
                super(message);
        }
}

class Vehicle {
        private String vehicleNumber;
        private VehicleType type;

        public Vehicle(String vehicleNumber, VehicleType type) {
                this.vehicleNumber = vehicleNumber;
                this.type = type;
        }

        public String getVehicleNumber() {
                return vehicleNumber;
        }

        public VehicleType getType() {
                return type;
        }

        @Override
        public String toString() {
                return vehicleNumber + " (" + type + ")";
        }
}

class ParkingSlot {
        private String slotId;
        private int floorNumber;
        private int distanceFromExit;
        private VehicleType supportedType;

        private Vehicle parkedVehicle;
        private boolean occupied;

        private final ReentrantLock lock = new ReentrantLock();

        public ParkingSlot(String slotId,
                        int floorNumber,
                        VehicleType supportedType,
                        int distanceFromExit) {
                this.slotId = slotId;
                this.floorNumber = floorNumber;
                this.supportedType = supportedType;
                this.distanceFromExit = distanceFromExit;
        }

        public boolean parkVehicle(Vehicle vehicle) {
                lock.lock();
                try {
                        if (occupied || vehicle.getType() != supportedType) {
                                return false;
                        }

                        occupied = true;
                        parkedVehicle = vehicle;
                        return true;
                } finally {
                        lock.unlock();
                }
        }

        public void removeVehicle() {
                lock.lock();
                try {
                        occupied = false;
                        parkedVehicle = null;
                } finally {
                        lock.unlock();
                }
        }

        public boolean isAvailable() {
                return !occupied;
        }

        public String getSlotId() {
                return slotId;
        }

        public VehicleType getSupportedType() {
                return supportedType;
        }

        public int getDistanceFromExit() {
                return distanceFromExit;
        }

        @Override
        public String toString() {
                return "Slot{" +
                                "id='" + slotId + '\'' +
                                ", floor=" + floorNumber +
                                ", type=" + supportedType +
                                '}';
        }
}

class Floor {
        private int floorNumber;
        private List<ParkingSlot> slots;

        public Floor(int floorNumber, List<ParkingSlot> slots) {
                this.floorNumber = floorNumber;
                this.slots = slots;
        }

        public List<ParkingSlot> getSlots() {
                return slots;
        }

        public int getFloorNumber() {
                return floorNumber;
        }
}

class Ticket {
        private String ticketId;
        private Vehicle vehicle;
        private ParkingSlot slot;
        private LocalDateTime entryTime;
        private String entryGateId;

        public Ticket(String ticketId, Vehicle vehicle, ParkingSlot slot, LocalDateTime entryTime, String entryGateId) {
                this.ticketId = ticketId;
                this.vehicle = vehicle;
                this.slot = slot;
                this.entryTime = entryTime;
                this.entryGateId = entryGateId;
        }

        public String getTicketId() {
                return ticketId;
        }

        public Vehicle getVehicle() {
                return vehicle;
        }

        public ParkingSlot getSlot() {
                return slot;
        }

        public LocalDateTime getEntryTime() {
                return entryTime;
        }

        @Override
        public String toString() {
                return "Ticket{" +
                                "ticketId='" + ticketId + '\'' +
                                ", vehicle=" + vehicle +
                                ", slot=" + slot +
                                ", entryTime=" + entryTime +
                                ", gate=" + entryGateId +
                                '}';
        }
}

class Payment {

        private String paymentId;
        private double amount;
        private PaymentMode mode;
        private PaymentStatus status;

        public Payment(String paymentId, double amount, PaymentMode mode, PaymentStatus status) {
                this.paymentId = paymentId;
                this.amount = amount;
                this.mode = mode;
                this.status = status;
        }

        @Override
        public String toString() {
                return "Payment{" +
                                "amount=" + amount +
                                ", mode=" + mode +
                                ", status=" + status +
                                '}';
        }
}

class Receipt {
        private String receiptId;
        private Ticket ticket;
        private Payment payment;
        private LocalDateTime exitTime;

        public Receipt(String receiptId, Ticket ticket, Payment payment, LocalDateTime exitTime) {
                this.receiptId = receiptId;
                this.ticket = ticket;
                this.payment = payment;
                this.exitTime = exitTime;
        }

        @Override
        public String toString() {
                return "\n========== RECEIPT ==========\n" +
                                "Receipt Id : " + receiptId + "\n" +
                                "Ticket Id  : " + ticket.getTicketId() + "\n" +
                                "Vehicle    : " + ticket.getVehicle() + "\n" +
                                "Slot       : " + ticket.getSlot().getSlotId() + "\n" +
                                "Exit Time  : " + exitTime + "\n" +
                                payment +
                                "\n=============================\n";
        }
}

interface SlotAssignmentStrategy {
        ParkingSlot assignSlot(List<Floor> floors, Vehicle vehicle);
}

class NearExitStrategy implements SlotAssignmentStrategy {

        @Override
        public ParkingSlot assignSlot(List<Floor> floors, Vehicle vehicle) {
                ParkingSlot bestSlot = null;

                for (Floor floor : floors) {
                        for (ParkingSlot slot : floor.getSlots()) {
                                if (slot.isAvailable() && slot.getSupportedType() == vehicle.getType()) {
                                        if (bestSlot == null || slot.getDistanceFromExit() < bestSlot
                                                        .getDistanceFromExit()) {
                                                bestSlot = slot;
                                        }
                                }
                        }
                }
                return bestSlot;
        }
}

class RandomSlotStrategy implements SlotAssignmentStrategy {
        private final Random random = new Random();

        @Override
        public ParkingSlot assignSlot(List<Floor> floors, Vehicle vehicle) {
                List<ParkingSlot> available = new ArrayList<>();

                for (Floor floor : floors) {
                        for (ParkingSlot slot : floor.getSlots()) {
                                if (slot.isAvailable() &&
                                                slot.getSupportedType() == vehicle.getType()) {
                                        available.add(slot);
                                }
                        }
                }

                if (available.isEmpty()) {
                        return null;
                }

                return available.get(random.nextInt(available.size()));
        }
}

interface PricingStrategy {
        double calculatePrice(Ticket ticket);
}

class HourlyPricingStrategy implements PricingStrategy {
        private static final Map<VehicleType, Integer> HOURLY_RATE = Map.of(VehicleType.BIKE, 20, VehicleType.CAR, 50,
                        VehicleType.TRUCK, 100);

        @Override
        public double calculatePrice(Ticket ticket) {
                long hours = Duration.between(ticket.getEntryTime(), LocalDateTime.now()).toHours();
                hours = Math.max(hours, 1);

                return hours * HOURLY_RATE.get(ticket.getVehicle().getType());
        }

}

class ParkingLot {
        private static ParkingLot instance;
        private List<Floor> floors;
        private ConcurrentHashMap<String, Ticket> activeTickets = new ConcurrentHashMap<>();
        private SlotAssignmentStrategy slotAssignmentStrategy;
        private PricingStrategy pricingStrategy;

        private ParkingLot(List<Floor> floors, SlotAssignmentStrategy slotAssignmentStrategy,
                        PricingStrategy pricingStrategy) {
                this.floors = floors;
                this.slotAssignmentStrategy = slotAssignmentStrategy;
                this.pricingStrategy = pricingStrategy;
        }

        public static synchronized ParkingLot initialize(List<Floor> floors,
                        SlotAssignmentStrategy slotAssignmentStrategy,
                        PricingStrategy pricingStrategy) {
                if (instance == null) {
                        instance = new ParkingLot(floors, slotAssignmentStrategy, pricingStrategy);
                }
                return instance;
        }

        public static ParkingLot getInstance() {
                return instance;
        }

        public synchronized Ticket parkVehicle(Vehicle vehicle, String entryGateId) {

                ParkingSlot slot = slotAssignmentStrategy.assignSlot(floors, vehicle);

                if (slot == null || !slot.parkVehicle(vehicle)) {
                        throw new ParkingFullException("No slot available for " + vehicle.getType());
                }

                Ticket ticket = new Ticket(UUID.randomUUID().toString(), vehicle, slot, LocalDateTime.now(),
                                entryGateId);

                activeTickets.put(ticket.getTicketId(), ticket);

                System.out.println("Vehicle Parked -> " + ticket);

                return ticket;
        }

        public Receipt exitVehicle(String ticketId, PaymentMode paymentMode) {
                Ticket ticket = activeTickets.remove(ticketId);

                if (ticket == null)
                        throw new InvalidTicketException("Invalid Ticket");

                double amount = pricingStrategy.calculatePrice(ticket);

                Payment payment = new Payment(
                                UUID.randomUUID().toString(),
                                amount,
                                paymentMode,
                                PaymentStatus.SUCCESS);

                ticket.getSlot().removeVehicle();

                return new Receipt(
                                UUID.randomUUID().toString(),
                                ticket,
                                payment,
                                LocalDateTime.now());
        }

        public void changeStrategy(SlotAssignmentStrategy strategy) {
                this.slotAssignmentStrategy = strategy;
        }
}

class EntryGate {
        private String gateId;

        public EntryGate(String gateId) {
                this.gateId = gateId;
        }

        public Ticket generateTicket(Vehicle vehicle) {
                return ParkingLot.getInstance().parkVehicle(vehicle, gateId);
        }
}

class ExitGate {
        private String gateId;

        public ExitGate(String gateId) {
                this.gateId = gateId;
        }

        public Receipt checkout(String ticketId, PaymentMode paymentMode) {
                System.out.println("Exit from gate -> " + gateId);

                return ParkingLot.getInstance().exitVehicle(ticketId, paymentMode);
        }
}

public class ParkingLot_Demo {
        public static void main(String[] args) throws Exception {

                List<Floor> floors = new ArrayList<>();
                for (int floor = 1; floor <= 2; floor++) {
                        List<ParkingSlot> slots = new ArrayList<>();

                        for (int i = 1; i <= 5; i++) {
                                slots.add(new ParkingSlot("B-" + floor + "-" + i, floor, VehicleType.BIKE, i));
                        }

                        for (int i = 1; i <= 5; i++) {
                                slots.add(new ParkingSlot("C-" + floor + "-" + i, floor, VehicleType.CAR, i));
                        }

                        for (int i = 1; i <= 3; i++) {
                                slots.add(new ParkingSlot("T-" + floor + "-" + i, floor, VehicleType.TRUCK, i));
                        }
                        floors.add(new Floor(floor, slots));
                }

                ParkingLot.initialize(floors, new NearExitStrategy(), new HourlyPricingStrategy());

                // ===============================
                // MULTIPLE ENTRY GATES
                // ===============================

                EntryGate entryGate1 = new EntryGate("ENTRY-1");
                EntryGate entryGate2 = new EntryGate("ENTRY-2");
                EntryGate entryGate3 = new EntryGate("ENTRY-3");

                // ===============================
                // MULTIPLE EXIT GATES
                // ===============================

                ExitGate exitGate1 = new ExitGate("EXIT-1");
                ExitGate exitGate2 = new ExitGate("EXIT-2");
                ExecutorService executor = Executors.newFixedThreadPool(10);

                List<Future<Ticket>> futures = new ArrayList<>();

                // ENTRY-1 vehicles
                for (int i = 1; i <= 4; i++) {

                        int num = i;

                        futures.add(executor.submit(() -> entryGate1.generateTicket(
                                        new Vehicle(
                                                        "CAR-E1-" + num,
                                                        VehicleType.CAR))));
                }

                // ENTRY-2 vehicles
                for (int i = 1; i <= 4; i++) {

                        int num = i;

                        futures.add(executor.submit(() -> entryGate2
                                        .generateTicket(new Vehicle("BIKE-E2-" + num, VehicleType.BIKE))));
                }

                // ENTRY-3 vehicles
                for (int i = 1; i <= 2; i++) {
                        int num = i;
                        futures.add(executor.submit(() -> entryGate3.generateTicket(
                                        new Vehicle("TRUCK-E3-" + num, VehicleType.TRUCK))));
                }

                List<Ticket> tickets = new ArrayList<>();

                for (Future<Ticket> future : futures) {
                        tickets.add(future.get());
                }

                System.out.println(
                                "\n========= ALL VEHICLES PARKED =========");

                Thread.sleep(2000);

                // ===============================
                // MULTIPLE EXIT GATE SIMULATION
                // ===============================

                Future<Receipt> exit1 = executor.submit(() -> exitGate1.checkout(
                                tickets.get(0)
                                                .getTicketId(),
                                PaymentMode.UPI));

                Future<Receipt> exit2 = executor.submit(() -> exitGate2.checkout(
                                tickets.get(1)
                                                .getTicketId(),
                                PaymentMode.CARD));

                System.out.println(exit1.get());
                System.out.println(exit2.get());

                executor.shutdown();
        }
}