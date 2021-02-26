import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//Route calculation class to determine total distance traveled based on a file of long/lats
public class RouteCalculator {

    private List<int[]> latLongList = new ArrayList<>(); //A 2d arraylist of lat/long coordinates in semicircles
    private static final int earthRadius = 6371 * 1000;
    private int listSize;

    //Function Name: RouteCalculator
    //Parameters: None
    //Purpose: default constructor
    //Returns: Nothing
    public RouteCalculator(){
        this.listSize = 0;
    }

    //Function Name: semicircleToDegreeConversion
    //Parameters: Semicircle value you wish to convert
    //Purpose: converts semicircles to degrees
    //Returns: degree value as a double
    public double semicircleToDegreeConversion(int semicircleValue){
        double twoPowed = Math.pow(2.0,31);
        return semicircleValue * (180.0/twoPowed);
    }

    //Function Name: semicircleToMeterConversion
    //Parameters: Semicircle value you wish to convert
    //Purpose: Converts semicircles to meters
    //Returns: meter value as a double
    private double semicircleToMeterConversion(double semicircleValue){

        return semicircleValue / 107.173;
    }

    //Function Name: haversineConversion
    //Parameters: 2 sets of latitudes and longitude coordinates
    //Purpose: Calculates the distance between two latitude and longitude coordinates using the haversine function
    //Returns: Distance between the two points as a double
    private double haversineConversion(int lat1, int long1, int lat2, int long2){
        double a, c, d;
        double lat1Radians, lat2Radians, deltaLatRadians, deltaLongRadians;

        //Conversions to radians
        lat1Radians = semicircleToDegreeConversion(lat1) * Math.PI / 180;
        lat2Radians = semicircleToDegreeConversion(lat2) * Math.PI / 180;

        deltaLatRadians  = semicircleToDegreeConversion(lat2 - lat1)   * Math.PI /180;
        deltaLongRadians = semicircleToDegreeConversion(long2 - long1) * Math.PI /180;

        //A function
        a = Math.pow(Math.sin(deltaLatRadians / 2.0),2 ) +
                (Math.cos(lat1Radians) * Math.cos(lat2Radians) *
                Math.pow(Math.sin(deltaLongRadians / 2.0),2));

        //C function
        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0-a));

        //End distance calculation
        d = earthRadius * c;
        return d;
    }


    //Function Name: displayLatLongList
    //Parameters: None
    //Purpose: To display the list of read in lat/long coordinates for debugging
    //Returns: nothing
    public void displayLatLongList(){
        int size = latLongList.size();
        int i;
        int[] latLongRow;
        for(i = 0; i < size; i++){
            latLongRow = latLongList.get(i);
            System.out.println("Row: " + i + " Lat: " + latLongRow[0] + " Long: " + latLongRow[1]);
        }
    }

    //Function Name: readGPSCoordinatesFile
    //Parameters: Filename of the file you wish to load
    //Purpose: Read in semicircle values for lat/long from a file then stores it in latLongList
    //Returns: Nothing
    private void readGPSCoordinatesFile(String filename){
        try
        {
            //Instantiation
            File latLongFile = new File(filename);
            Scanner fileReader = new Scanner(latLongFile);
            String fileLine;
            String latitude;
            String longitude;

            //Reading loop
            while (fileReader.hasNextLine()) {
                fileLine = fileReader.nextLine();
                String[] parsedString = fileLine.split(",");

                latitude  =  parsedString[0].trim();
                longitude =  parsedString[1].trim();

                int[] latLong = {Integer.parseInt(latitude),Integer.parseInt(longitude)};
                latLongList.add(latLong);
            }
            //Set size
            this.listSize = latLongList.size();

        }
        //Error catching
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }

    }

    //Function Name: calcRouteDistance
    //Parameters: Filename to pass to the readGPSCoordinatesFile function
    //Purpose: Calls readGPSCoordinates then computes the total distance between each point
    //Returns: total distance as a double
    public double calcRouteDistance(String filename){
        readGPSCoordinatesFile(filename);
        double totalDistance = 0;

        if(listSize < 2){

            System.out.println("calcRouteDistance unable to compute distance");
            return -1;

        }else{

            int i;
            int[] set1;
            int[] set2;

            //Run through the lat/long list so that the points connect with each other then computes the total distance
            for(i = 1; i < listSize; i++){
                set1 = latLongList.get(i-1);
                set2 = latLongList.get(i);

                totalDistance = haversineConversion(set1[0],set1[1],set2[0],set2[1]) + totalDistance;

            }
        }

        return totalDistance;
    }
}
