package simmac;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;


public class Main {

	/**
	 * Function parses file with SIMMAC instructions and specifications.
	 * 
	 * @throws FileNotFoundException 
	 */
	public static int[] readFile(String fileName) throws FileNotFoundException {
		try{
			ArrayList<Integer> instructions = new ArrayList<Integer>();
			Scanner scanner = new Scanner(new File(fileName)); 
			
			int lineNumber = 1;
			while (scanner.hasNext()){
				String line = scanner.nextLine().trim();
				if (line.length() > 0){
					Integer instruction = Processor.parseInstruction(lineNumber, fileName, line);
					if (instruction != null){
						instructions.add(instruction);
					}
					else{
						System.out.println("Syntax error in " + fileName);
						scanner.close();
						return (null); 
					}
					lineNumber += 1;
				}
			}
			scanner.close();
			int [] instruction = new int[instructions.size()];
			for (int i = 0; i < instructions.size(); i++){
				instruction[i] = instructions.get(i);
				/* //Uncomment to show all op codes being brought in.
				 * 
				 * System.out.println("OPCode: " + instruction[i]);
				*/
			}
			return instruction;
		}
		catch (FileNotFoundException e){
			System.out.println(fileName + " does not exist.");
		}
		return null;
	}
	
	/**
	 * Main running function for the program. Takes in quantum value (number of cycles per instruction)
	 * and an array of file names. Runs best on command line.
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {	
		System.out.println("Please enter a quantum value.");
		Scanner scanner = new Scanner(System.in);
		if (scanner.hasNextInt()){
			int value = scanner.nextInt();
			PrintWriter writer = new PrintWriter("output.txt");
			writer.print("Quantum Value: " + value + "\n");
			writer.close();
			
			SIMMAC cpu = new SIMMAC();
			
			OS os = new OS(value, cpu);
			if (args.length == 0){
				boolean done = false;
				ArrayList<String> fileNames = new ArrayList <String>();
				
				while(!done){
					System.out.println("Please enter a file to continue");
					fileNames.add(scanner.next());
					System.out.println("Do you want to load another file?");
					System.out.println("Valid answers are yes, y, no, and n: ");
					String answer = scanner.next();
					
					if (((answer.toLowerCase().equals("n")) || (answer.toLowerCase().equals("no"))) 
							&& (!(answer.toLowerCase().equals("y")) && !(answer.toLowerCase().equals("yes")))){
						done = true;
					}
					else if((answer.toLowerCase().equals("y")) || (answer.toLowerCase().equals("yes"))){
						done = false;
					}
					else{
						System.out.println("Sorry, that's not a valid input! Executing previous inputs!");
						done = true;
					}
				}
				for (int i = 0; i < fileNames.size(); i++){
					int [] osProgram = readFile(fileNames.get(i));
					os.loadProcess(osProgram);
				}
			}
			else{
				for (int i = 0; i < args.length; i++){
					int [] osProgram = readFile(args[i]);
					os.loadProcess(osProgram);
				}
			}
			os.run();
			scanner.close();
		}
		else{
			System.out.println("That's not a number! Aborting simulation.");
		}
	}
}
