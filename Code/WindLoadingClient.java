import java.util.*;
import java.io.*;

// The WindLoadingClient class serves as the main client for interacting with
// the WindLoading application. It allows users to view and input building 
// parameters, material data, calculate wind load, and compare it with material
// properties.

public class WindLoadingClient{
    public static void main(String args[]) throws FileNotFoundException{

        File buildingList = new File("BuildingParameters.txt");
        File woodList = new File("WoodData.txt");
        File concreteList = new File("ConcreteData.txt");
        File brickList = new File("BrickData.txt");
        File stoneList = new File("StoneData.txt");
        File steelList = new File("SteelData.txt");
        
        Scanner console = new Scanner(System.in);

        intro();

        List<String> buildingData = new ArrayList<String>();
        List<String> woodData = new ArrayList<String>();
        List<String> concreteData = new ArrayList<String>();
        List<String> brickData = new ArrayList<String>();
        List<String> stoneData = new ArrayList<String>();
        List<String> steelData = new ArrayList<String>();

        WindLoading windLoading = null;

        System.out.print
           ("What would you like to do (Bu, W, C, Br, St, El, B, M, S, Q)? ");
        String input = console.nextLine().toUpperCase();
        System.out.println();

        while(!input.startsWith("Q")){
            if(input.startsWith("BU")){
                buildingData = fileProcess(buildingList);
            }
            else if(input.startsWith("W")){
                woodData = fileProcess(woodList);
            }
            else if(input.startsWith("C")){
                concreteData = fileProcess(concreteList);
            }
            else if(input.startsWith("BR")){
                brickData = fileProcess(brickList);
            }
            else if(input.startsWith("ST")){
                stoneData = fileProcess(stoneList);
            }
            else if(input.startsWith("EL")){
                steelData = fileProcess(steelList);
            }
            else if(input.startsWith("B")){
                if(windLoading == null){
                    windLoading = new WindLoading(buildingData, console);
                }
                windLoading.getUserInput();
                windLoading.compareWindLoadToCapacityFactors();
            }
            else if(input.startsWith("M")){
                windLoading = MaterialParameters(windLoading, woodData,
                            concreteData, brickData, stoneData,
                            steelData, console);                
            }
            else if(input.startsWith("S")){
                if(windLoading != null){
                    boolean saved = 
                            windLoading.saveParametersToFile("UserInputs.txt");
                    if(saved){
                        System.out.println("User data successfully saved.");
                    }
                    else{
                        System.out.println("User data failed to save.");
                    }
                }
            }
            else{
                System.out.println("Please enter a valid input.");
            }

            System.out.println();
            System.out.print
                ("What would you like to do "
                + "(Bu, W, C, Br, St, El, B, M, S, Q)? ");
            input = console.nextLine().toUpperCase();      
        }
    }

    // Behavior: The intro method introduces the program for the user 
    // Parameters: None 
    // Returns: None 
    // Exceptions: None

    public static void intro(){
        System.out.println("Welcome to the Wind Loading of Structures " 
                + "Calculator!");
        System.out.println("This is version 2.0, as development of this "
               + "program is still continuing.");
        System.out.println("This program is allows the user to choose "
                + "custom parameters for their structure, the air conditions, "
                + "as well as the material type and its properties.");
        System.out.println();
        System.out.println("Menu Options: View (Bu)ilding Data, "
            + "View (W)ood Data, View (C)oncrete Data, View (Br)ick Data, "
            + "View (St)one Data, View (Ste)el Data, (B)uilding Parameters, "
            + "(M)aterial Parameters, (S)ave Custom Data, (Q)uit");
        System.out.println("In order to run the program properly, you should "
        + "view the building data first, and then view one of the material "
        + "datas, then enter your material parameters with the material that "
        + "you scanned through, and then do building parameters which will "
        + "tell you whether your structure survived, and then press the save "
        + "data menu option, which will allow you to save your custom data!");
    }

    // Behavior: The fileProcess method processes a file and extracts info
    // from the file into a list.
    //
    // Exceptions: Throws a FileNotFoundException if the specified file is 
    // not found.
    //
    // Returns: A list containing the extracted property data.
    //
    // Parameters: file: The file from which the property data is to be read.

    public static List<String> fileProcess(File file) 
                                                throws FileNotFoundException{
        List<String> dataSet = new ArrayList<String>();       
        Scanner fileScan = new Scanner(file);

        while(fileScan.hasNextLine()){
            String line = fileScan.nextLine();                   
            if(!line.isEmpty()){
                if(line.contains(",")){
                    String[] parts = line.split(",");
                    if (parts.length == 4){
                        String property = parts[0];
                        String min = parts[1];
                        String max = parts[2];
                        String unit = parts[3];

                        System.out.println(" Property: " + property);
                        System.out.println(" Min: " + min);
                        System.out.println(" Max: " + max);
                        System.out.println(" Unit: " + unit);

                        dataSet.add(property);
                        dataSet.add(min);
                        dataSet.add(max);
                        dataSet.add(unit);
                    }
                }
            }
        }
        return dataSet;
    }

    // Behavior: Allows the user to choose a material type and initializes the 
    // WindLoading object with the selected material data.
    //
    // Exceptions: None 
    //
    // Returns: None 
    //
    // Parameters: windLoading: An instance of the WindLoading class.
    //             woodData: A list containing the data for wood material.
    //             concreteData: A list containing the data for concrete 
    //                           material.
    //             brickData: A list containing the data for brick material.
    //             stoneData: A list containing the data for stone material.
    //             steelData: A list containing the data for steel material.
    //             console: A Scanner object for reading user the input.

    public static WindLoading MaterialParameters(WindLoading windLoading,
            List<String> woodData, List<String> concreteData,
            List<String> brickData, List<String> stoneData,
            List<String> steelData, Scanner console){
        System.out.print("Please choose a material type: ");
        System.out.println();
        System.out.println("W for Wood");
        System.out.println("C for Concrete");
        System.out.println("Br for Brick");
        System.out.println("St for Stone");
        System.out.println("El for Steel");

        String materialChoice = console.nextLine().toUpperCase();
        List<String> selectedMaterial = null;

        if(materialChoice.equals("W")){
            selectedMaterial = woodData;
        } 
        else if(materialChoice.equals("C")){
            selectedMaterial = concreteData;
        } 
        else if(materialChoice.equals("BR")){
            selectedMaterial = brickData;
        } 
        else if(materialChoice.equals("ST")){
            selectedMaterial = stoneData;
        }
        else if(materialChoice.equals("EL")){
            selectedMaterial = steelData;
        }
        
        if(selectedMaterial != null && !selectedMaterial.isEmpty()){
            if(windLoading == null){
                windLoading = new WindLoading(selectedMaterial, console);
            }
            else{
                windLoading.setMaterialData(selectedMaterial);
            }
            windLoading.getUserInput();
        }
        else{
            System.out.println
                ("Material data is not available or no material was selected.");
        }
        return windLoading;
    }
}
