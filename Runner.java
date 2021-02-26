import java.util.Scanner;

//Main runner class
public class Runner {

    //Function Name: loop
    //Parameters:
    //Purpose: infinite loop to allow for user input
    //Returns: nothing
    public static void loop(){
        String fileInput;
        double distance;
        Scanner input = new Scanner(System.in);

        System.out.println("Please enter a file to compute:");
        while(true){
            fileInput = input.nextLine();
            RouteCalculator garminCalculation = new RouteCalculator();

            //Quit check
            if(fileInput.equals("quit") || fileInput.equals("Quit")){
                break;
            }

            //Distance calc call
            distance = garminCalculation.calcRouteDistance(fileInput);

            //Error check
            if(distance != -1.0){
                String formattedDistance = String.format("%.4f", distance);
                System.out.println("Distance: " + formattedDistance + " meters");
            }

            System.out.println("Exit the program by typing Quit/quit.");
        }

        System.out.println("Exiting the program.");
    }
    public static void main(String[] args) {

        loop();

    }
}
