import java.util.ArrayList;
import java.util.List;

public class Elevator {
    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_DOWN = -1;
    private static final int DIRECTION_NONE = 0;

    private int elevatorId;
    private int currentFloor;
    private int capacity;
    private List<Passengers> passengers;
    private int direction;

    public Elevator(int elevatorId, int capacity) {
        this.elevatorId = elevatorId;
        this.currentFloor = 1; // Start at the first floor
        this.capacity = capacity;
        this.passengers = new ArrayList<>();
        this.direction = DIRECTION_NONE; // Initial direction (can be UP, DOWN, or NONE)
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Passengers> getPassengers() {
        return passengers;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isFull() {
        return passengers.size() >= capacity;
    }

    public boolean isEmpty() {
        return passengers.isEmpty();
    }

    public void moveToFloor(int targetFloor) {
        // Move the elevator to the specified target floor
        if (targetFloor > currentFloor) {
            direction = DIRECTION_UP;
        } else if (targetFloor < currentFloor) {
            direction = DIRECTION_DOWN;
        } else {
            direction = DIRECTION_NONE; // No direction change if already on the target floor
        }
        currentFloor = targetFloor;
    }

    public void loadPassenger(Passengers passenger) {
        // Load a passenger into the elevator
        passengers.add(passenger);
    }

    public void unloadPassengers(int floor) {
        // Unload passengers whose destination is the specified floor
        passengers.removeIf(passenger -> passenger.getDestinationFloor() == floor);
    }

    public void move(int totalFloors) {
        // Simulate elevator movement and handle loading/unloading passengers
        int floorsToMove = Math.min(4, totalFloors); // Limit movement to 4 floors

        if (direction == DIRECTION_UP) {
            int targetFloor = Math.min(currentFloor + floorsToMove, totalFloors);
            moveToFloor(targetFloor);
        } else if (direction == DIRECTION_DOWN) {
            int targetFloor = Math.max(currentFloor - floorsToMove, 1);
            moveToFloor(targetFloor);
        }
    }
}
