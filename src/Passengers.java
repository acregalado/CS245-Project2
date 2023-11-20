import java.util.Random;

public class Passengers implements Comparable<Passengers> {
    private static final Random RANDOM = new Random();

    private int startFloor;
    private int destinationFloor;
    private int arrivalTime;
    private int conveyanceTime;

    public Passengers(int startFloor, int destinationFloor) {
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getConveyanceTime() {
        return conveyanceTime;
    }

    public static Passengers generateRandomPassenger(int currentFloor, int totalFloors, int tick) {
        // Generate a random destination floor different from the current floor
        int destinationFloor = currentFloor;

        while (destinationFloor == currentFloor) {
            destinationFloor = RANDOM.nextInt(totalFloors) + 1;
        }

        Passengers passenger = new Passengers(currentFloor, destinationFloor);
        passenger.setArrivalTime(tick); // Set the arrival time
        return passenger;
    }

    public static boolean shouldAppear(double probabilityOfAppearance) {
        return RANDOM.nextDouble() < probabilityOfAppearance;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setConveyanceTime(int conveyanceTime) {
        this.conveyanceTime = conveyanceTime;
    }

    @Override
    public int compareTo(Passengers otherPassenger) {
        // Compare passengers based on their destination floors
        return Integer.compare(this.destinationFloor, otherPassenger.destinationFloor);
    }
}
