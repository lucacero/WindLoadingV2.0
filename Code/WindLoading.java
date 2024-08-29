// Luca Cero
// CSE 122
// TAs: Connor Sun & Abigail Williams 
// 8/15/24

import java.io.*;
import java.util.*;

// The WindLoading class is responsible for calculating and comparing wind loads
// on structures. It accepts user inputs for various structural and 
// environmental parameters, calculates the wind load and compares it against
// safety factors and load capacities to determine structural safety. It also
// supports saving the parameters and results to a file.

public class WindLoading{
    private final List<String> parameters;
    private final Scanner console;
    private double height;
    private double width;
    private double length;
    private double windVelocity;
    private double airDensity;
    private double safetyFactor;
    private double yieldStrength;
    private double averageShearStrength;
    private double elasticModulus;
    private double loadCapacity;

    // Constructs a WindLoading object for the following parameters
    // parameters: A list of strings which represent the parameters used for
    //             the calculations
    // console: A scanner used to scan the user input.
    
    public WindLoading(List<String> parameters, Scanner console){
        this.parameters = parameters;
        this.console = console;
    }

    // Behavior: Clears the existing list of parameters and adds all elements
    // from the provided list to the current list of parameters.
    //
    // Exceptions: None 
    //
    // Returns: None
    //
    // Parameters: parameters: A list of strings representing the new material 
    //                         data which is set to replace the current list 
    //                         of parameters.

    public void setMaterialData(List<String> parameters){
        this.parameters.clear();
        this.parameters.addAll(parameters);
    }

    // Behavior: The getUserInput method prompts the user to enter values for
    // all necessary parameters and then assigns the value to the corresponding
    // field.
    //
    // Exceptions: None 
    //
    // Returns: None 
    //
    // Parameters: None

    public void getUserInput(){
        System.out.println("Please enter the values for the following "
                            + "parameters:");

        for(int i = 0; i < parameters.size(); i += 4){
            String property = parameters.get(i);
            String min = parameters.get(i + 1);
            String max = parameters.get(i + 2);
            String unit = parameters.get(i + 3);

            double value = getValidInput(property, min, max, unit);

            if(property.startsWith("Height")){
                this.height = value;
            }
            else if(property.startsWith("Width")){
                this.width = value;
            }
            else if(property.startsWith("Length")){
                this.length = value;
            }
            else if(property.startsWith("Wind Velocity")){
                this.windVelocity = value / 3.6;
            }
            else if(property.startsWith("Air Density")){
                this.airDensity = value;
            }
            else if(property.startsWith("Safety Factor")){
                this.safetyFactor = value;
            }
            else if(property.startsWith("Yield Strength")){
                this.yieldStrength = value;
            }
            else if(property.startsWith("Average Shear Strength")){
                this.averageShearStrength = value;
            }
            else if(property.startsWith("Elastic Modulus")){
                this.elasticModulus = value;
            }
            else if(property.startsWith("Load Capacity")){
                this.loadCapacity = value;
            }

            System.out.println(property + " set to: " + value + " " + unit);
            System.out.println();
        }
    }

    // Behavior: The getValidInput method prompts the user to input a numeric 
    // value for a specific parameter and ensures that the value is within the
    // given range. 
    //
    // Exceptions: None 
    // 
    // Returns: A double value which represents a valid numeric input.
    //
    // Parameters: property: A string which represents the name of the 
    //                       parameter being set.
    //             minStr: A string representing the minimum allowable value. 
    //             maxStr: A string representing the maximum allowable value.
    //             unit: A string representing the unit of measurement for the
    //                   parameter.

    public double getValidInput(String property, String minStr, String maxStr,
                                 String unit){
        double min = Double.parseDouble(minStr);
        double max = Double.parseDouble(maxStr);

        while(true){
            System.out.print("Enter the value for " + property + 
                            " (" + min + " - " + max + " " + unit + " ): ");
            double value = console.nextDouble();

            console.nextLine();

            if(value >= min && value <= max){
                return value;
            }
            else{
                System.out.println("Invalid input. Enter a value between " 
                                + min + " and " + max + " " + unit + ".");
                System.out.println();
            }
        }
    }

    // Behavior: The compareWindLoadToCapacityFactors method compares the
    // calculated wind load to various strength parameters (tensile, shear,
    // flexural, and buckling strength) to determine if the structure can
    // withstand the wind load.
    // 
    // Exceptions: None
    // 
    // Returns: None
    //
    // Parameters: None
        
    public void compareWindLoadToCapacityFactors(){
        double alpha = 1.2;
        double beta = 0.6;
        
        double dragCoefficient = alpha * (this.height / this.width) + beta 
                                    * (this.height / this.length);
        System.out.println("The drag coefficient is: " + dragCoefficient);
        System.out.println();
        
        double windPressure = 0.5 * this.airDensity * dragCoefficient 
                            * Math.pow(this.windVelocity, 2);
        System.out.println("The wind pressure is: " + windPressure + " N/m^2");
        System.out.println();
        
        double crossSectionalArea = this.height * this.width;
        System.out.println("The cross sectional area is: " 
                            + crossSectionalArea + " m^2");
        System.out.println();
        
        double windLoad = windPressure * crossSectionalArea;
        System.out.println("\nWind Load: " + windLoad + " N");
        System.out.println();

        double tensileStrength = this.yieldStrength * crossSectionalArea;
        System.out.println("The tensile strength is: " + tensileStrength 
                        + " N");
        System.out.println();

        double shearStrength = this.averageShearStrength * crossSectionalArea;
        System.out.println("The shear strength is: " + shearStrength + " N");
        System.out.println();

        int floors = (int)(this.height / 4);
        System.out.println("There are " + floors + " floors in your building");
        System.out.println();

        double floorArea = this.length * this.width;
        System.out.println("The floor area is: " + floorArea + " m^2");
        System.out.println();

        double floorLoad = 10.0;

        double totalLoad = floors * floorArea * floorLoad;
        System.out.println("The total load is: " + totalLoad + " N");
        System.out.println();

        int beamAmount = (int)(totalLoad / this.loadCapacity);
        System.out.println("There are " + beamAmount 
                        + " beams in your building");
        System.out.println();

        double beamHeight = this.height / beamAmount;
        System.out.println("The beam height is: " + beamHeight + " m");
        System.out.println();

        double beamWidth = beamHeight / 2;
        System.out.println("The beam width is: " + beamWidth + " m");
        System.out.println();

        double momentInertia = (beamWidth * Math.pow(beamHeight, 3)) / 12;
        System.out.println("The moment of inertia is: " + momentInertia 
                            + " m^4");
        System.out.println();

        double maximumFlexuralStrength = (this.yieldStrength * momentInertia) 
                                                        / beamWidth;
        System.out.println("The maximum flexural strength is: " 
                                            + maximumFlexuralStrength + " N"); 
        System.out.println();
     
        double columnEffectiveLengthFactor = 1.0;
    
        double bucklingStrength = 
                (Math.pow(Math.PI,2) * this.elasticModulus * momentInertia)
                / Math.pow(columnEffectiveLengthFactor * beamHeight, 2);
        System.out.println("The buckling strength is: " + bucklingStrength 
                        + "  N");
        System.out.println();
    
        double safetyTensileStrength = tensileStrength / this.safetyFactor;
        System.out.println("The tensile strength with a safety facter of " 
                        + this.safetyFactor + " is: " + safetyTensileStrength 
                        + " N");
        System.out.println();
    
        double safetyShearStrength = shearStrength / this.safetyFactor;
        System.out.println("The shear strength with a safey factor of " 
                        + this.safetyFactor + " is: " + safetyShearStrength 
                        + " N");
        System.out.println();
    
        double safetyMaximumFlexuralStrength = maximumFlexuralStrength 
                                                / this.safetyFactor;
        System.out.println("The maximum flexural strength with a safety factor "
                        + "of " + this.safetyFactor + " is: " 
                        + safetyMaximumFlexuralStrength + " N");
        System.out.println();
    
        double safetyBucklingStrength = bucklingStrength / this.safetyFactor;
        System.out.println("The buckling strength with a safety factor of "
                         + this.safetyFactor + " is: " + safetyBucklingStrength 
                         + " N");
        System.out.println();

        if(windLoad >= tensileStrength && windLoad >= safetyTensileStrength){
            System.out.println("Structural failure will occur due to a lack of "
                                + "tensile strength.");
            System.out.println();
        } 
        else if(windLoad < tensileStrength && 
                windLoad >= safetyTensileStrength){
            System.out.println("The structure would survive, but is not deemed "
                                + "safe due to a lack of tensile strength.");
            System.out.println();
        } 
        else{
            System.out.println("The structure is deemed safe, as the wind load "
                                + "is less than the tensile strength.");
            System.out.println();
        }

        if(windLoad >= shearStrength && windLoad >= safetyShearStrength){
            System.out.println("Structural failure will occur due to a lack of " 
                                + "shear strength.");
            System.out.println();
        } 
        else if(windLoad < shearStrength && windLoad >= safetyShearStrength){
            System.out.println("The structure would survive, but is not deemed "
                                + "safe due to a lack of shear strength.");
            System.out.println();
        } 
        else{
            System.out.println("The structure is deemed safe, as the wind load "
                                + "is less than the shear strength.");
            System.out.println();
        }

        if(windLoad >= maximumFlexuralStrength 
                    && windLoad >= safetyMaximumFlexuralStrength){
            System.out.println("Structural failure will occur due to a lack of "
                                + "maximum flexural strength.");
            System.out.println();
        } 
        else if(windLoad < maximumFlexuralStrength 
                        && windLoad >= safetyMaximumFlexuralStrength){
            System.out.println("The structure would survive, but is not deemed "
                                + "safe due to a lack of maximum flexural "
                                + "strength.");
            System.out.println();
        } 
        else{
            System.out.println("The structure is deemed safe, as the wind load " 
                                + "is less than the maximum flexural "
                                + "strength.");
            System.out.println();
        }

        if(windLoad >= bucklingStrength && windLoad >= safetyBucklingStrength){
            System.out.println("Structural failure will occur due to a lack of " 
                                + "buckling strength.");
            System.out.println();
        } 
        else if(windLoad < bucklingStrength 
                        && windLoad >= safetyBucklingStrength){
            System.out.println("The structure would survive, but is not deemed "
                                + "safe due to a lack of buckling strength.");
            System.out.println();
        } 
        else{
            System.out.println("The structure is deemed safe, as the wind load "
                                + "is less than the buckling strength.");
            System.out.println();
        }
    }

    // Behavior: The saveParametersToFile method writes all the user defined 
    // values associated with wind loading to a file in order to provide a 
    // record of the data used for the calculations. 
    //
    // Exceptions: Throws a FileNotFoundException if the file cannot be found.
    //
    // Returns: Returns true if the parameters are successfully saved to the 
    // file, and false if not.
    //
    // Parameters: fileName: The name of the file to save the parameters.

    public boolean saveParametersToFile(String fileName) 
                            throws FileNotFoundException{
        File saveFile = new File(fileName);

        PrintStream output = createPrintStream(saveFile);
        if(output == null){
            System.out.print("Failed to create output stream.");
            return false;
        }

        output.println("User Inputs for Wind Loading Calculations:");
        output.printf("Height: %.2f meters%n", this.height);
        output.printf("Width: %.2f meters%n", this.width);
        output.printf("Length: %.2f meters%n", this.length);
        output.printf("Wind Velocity: %.2f m/s%n", this.windVelocity);
        output.printf("Air Density: %.2f kg/m^3%n", this.airDensity);
        output.printf("Safety Factor: %.2f%n", this.safetyFactor);
        output.printf("Yield Strength: %.2f MPa%n", this.yieldStrength);
        output.printf("Average Shear Strength: %.2f MPa%n", 
                    this.averageShearStrength);
        output.printf("Elastic Modulus: %.2f GPa%n", this.elasticModulus);
        output.printf("Load Capacity: %.2f N%n", this.loadCapacity);

        output.close();
        System.out.println("Parameters saved to " + fileName);
        return true;
    }

    // Behavior: The createPrintStream method is a helper method which creates
    // a PrintStream for a given file and checks if the file exists and is 
    // writable.
    //
    // Exceptions: Throws a FileNotFoundException if the file cannot be found.
    //
    // Returns: A PrintStream object if the file is writable; otherwise null.
    //
    // Parameters: fileSave: A file object representing the file to write to.

    private PrintStream createPrintStream(File saveFile) 
                            throws FileNotFoundException{
        if(saveFile.exists() && !saveFile.canWrite()){
            System.out.println("File is not writable.");
            return null;
        }

        return new PrintStream(saveFile);
    }
}