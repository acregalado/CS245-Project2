import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.File;

public class ElevatorSim {
    private static List<Passengers> allPassengers;
    private String structures;
    private int floors;
    private float passengers;
    private int elevators;
    private int elevatorCapacity;
    private int duration;
    private static Elevator elevator;

   public static void main(String[] args) {
        ElevatorSim elevatorSim = new ElevatorSim();

        // Read properties from file or use defaults
        Properties properties = elevatorSim.readProperties(args);

        // Use properties to configure the simulation
        elevatorSim.configureSimulation(properties);

        // Run the simulation
        elevatorSim.runSimulation();

        // After the simulation, collect wait durations and perform analysis
        Analysis analysis = new Analysis();

        for (Passengers passenger : allPassengers) {
            if (passenger.getConveyanceTime() != -1) {
                int waitDuration = passenger.getConveyanceTime() - passenger.getArrivalTime();
                analysis.addWaitDuration(waitDuration);
            }
        }

        // Report results
        System.out.println("Average Wait Duration: " + analysis.calculateAverageWaitDuration());
        System.out.println("Longest Wait Duration: " + analysis.findLongestWaitDuration());
        System.out.println("Shortest Wait Duration: " + analysis.findShortestWaitDuration());
    }

    private Properties readProperties(String[] args) {
        Properties properties = new Properties();

        System.out.println(new File("db.default").getAbsolutePath());

        // Load default properties from "db.default" using FileReader and BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader("db.default"))) {
            properties.load(reader);
        } catch (IOException e) {
            System.err.println("Error reading default properties from db.default");
            e.printStackTrace();
        }

        // Check if a file is specified in the command line and override default properties
        if (args.length > 0) {
            try (FileReader userInput = new FileReader(args[0]);
                 BufferedReader reader = new BufferedReader(userInput)) {
                // Load user-provided properties, overriding defaults
                properties.load(reader);
            } catch (IOException e) {
                System.err.println("Error reading properties from file: " + args[0]);
                e.printStackTrace();
            }
        }

        // Set class fields based on properties
        this.structures = properties.getProperty("structures");
        this.floors = Integer.parseInt(properties.getProperty("floors"));
        this.passengers = Float.parseFloat(properties.getProperty("passengers"));
        this.elevators = Integer.parseInt(properties.getProperty("elevators"));
        this.elevatorCapacity = Integer.parseInt(properties.getProperty("elevatorCapacity"));
        this.duration = Integer.parseInt(properties.getProperty("duration"));

        return properties;
    }


    private void configureSimulation(Properties properties) {
        // Use properties to configure the simulation
        allPassengers = new ArrayList<>();

        // Example: Assuming you have a Floors and Elevator class
        int totalFloors = Integer.parseInt(properties.getProperty("floors"));
        int elevatorCapacity = Integer.parseInt(properties.getProperty("elevatorCapacity"));
        int duration = Integer.parseInt(properties.getProperty("duration"));

        Floors floors = new Floors(totalFloors);
        elevator = new Elevator(1, elevatorCapacity);
    }

    private void runSimulation() {
        // Implement simulation logic

        // Example: Generating passengers for the simulation
        int totalFloors = this.floors;
        double probabilityOfAppearance = this.passengers;
        int duration = this.duration;

        for (int tick = 0; tick < duration; tick++) {
            System.out.println("Tick: " + tick + ", Elevator Current Floor: " + elevator.getCurrentFloor());
            for (int floor = 1; floor <= totalFloors; floor++) {
                if (Passengers.shouldAppear(probabilityOfAppearance)) {
                    Passengers passenger = Passengers.generateRandomPassenger(floor, totalFloors, tick);
                    allPassengers.add(passenger);
                }
            }

            // Move the elevator and handle loading/unloading passengers
            elevator.move(totalFloors);

            // Additional simulation logic, elevator movement, etc.
            // For example, when the elevator picks up and drops off passengers, update conveyance time:
            for (Passengers passenger : allPassengers) {
                if (passenger.getConveyanceTime() == -1) {
                    // Simulate elevator movement and passenger conveyance here...
                    // Placeholder: Set the conveyance time when the passenger is picked up
                    simulateElevatorMovementAndConveyance(elevator, passenger, tick);
                }
            }

            // Debugging: Print passenger details after each tick
            for (Passengers passenger : allPassengers) {
                System.out.println("Tick: " + tick + ", Passenger: " + passenger.getStartFloor() + " -> " + passenger.getDestinationFloor() +
                        ", Arrival Time: " + passenger.getArrivalTime() + ", Conveyance Time: " + passenger.getConveyanceTime());
            }
        }

        // Debugging: Print all passengers at the end of the simulation
        System.out.println("All Passengers:");
        for (Passengers passenger : allPassengers) {
            System.out.println("Passenger: " + passenger.getStartFloor() + " -> " + passenger.getDestinationFloor() +
                    ", Arrival Time: " + passenger.getArrivalTime() + ", Conveyance Time: " + passenger.getConveyanceTime());
        }
    }

    private void simulateElevatorMovementAndConveyance(Elevator elevator, Passengers passenger, int tick) {
        int currentFloor = elevator.getCurrentFloor();
        int destinationFloor = passenger.getDestinationFloor();

        // Debugging: Print elevator and passenger details
        System.out.println("Tick: " + tick + ", Elevator Current Floor: " + currentFloor);
        System.out.println("Tick: " + tick + ", Passenger: " + passenger.getStartFloor() + " -> " + passenger.getDestinationFloor() +
                ", Arrival Time: " + passenger.getArrivalTime() + ", Conveyance Time: " + passenger.getConveyanceTime());

        // Simulate elevator movement
        if (currentFloor < destinationFloor) {
            elevator.moveToFloor(currentFloor + 1);
        } else if (currentFloor > destinationFloor) {
            elevator.moveToFloor(currentFloor - 1);
        }

        // Check if the elevator is at the destination floor
        if (currentFloor == destinationFloor && passenger.getConveyanceTime() == -1) {
            // Set the conveyance time when the passenger is dropped off
            passenger.setConveyanceTime(tick);
        }
    }
}
