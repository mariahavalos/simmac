package simmac;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;


public class Main {

	/**
	 * Function parses file with Simmac instructions
	 */
	public static int[] readFile(String fileName) {
		try{
			ArrayList<Integer> instructions = new ArrayList<Integer>();
			Scanner scanner = new Scanner(new File(fileName)); 
			
			int lineNumber = 1;
			while (scanner.hasNext()){
				String line = scanner.nextLine().trim();
				if (line.length() > 0){
					Integer instruction = Instruction.parseInstruction(lineNumber, fileName, line);
					if (instruction != null){
						instructions.add(instruction);
					}
					else{
						System.out.println("Syntax error in file: " + fileName);
						System.exit(0);
					}
					lineNumber += 1;
				}
			}
			scanner.close();
			int [] instruction = new int[instructions.size()];
			for (int i = 0; i < instructions.size(); i++){
				instruction[i] = instructions.get(i);
			}
			return instruction;
		}
		catch (FileNotFoundException e){
			System.out.println("File: " + fileName + " does not exist.");
			System.exit(0); 
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println("Please provide your file name");
		Scanner scanner = new Scanner(System.in);
		int file = scanner.nextInt();
		SIMMAC cpu = new SIMMAC();
		
		OperatingSystem os = new OperatingSystem(cpu, file);
		if (args.length == 0){
			boolean done = false;
			ArrayList<String> fileNames = new ArrayList();
			
			while(!done){
				System.out.println("Please enter another file to continue");
				fileNames.add(scanner.next());
				System.out.println("Do you want to load another file?");
				String answer = scanner.next();
				
				if ((answer.toLowerCase().equals('n') || answer.toLowerCase().equals("no")) 
						&& (!(answer.toLowerCase().equals("yes")) && !(answer.toLowerCase().equals("yes")))){
					done = true;
				}
			}
			for (int i = 0; i < fileNames.size(); i++){
				int [] osProgram = readFile(fileNames.get(i));
				os.loadProgram(osProgram);
			}
		}
		else{
			for (int i = 0; i < args.length; i++){
				int [] osProgram = readFile(args[i]);
				os.loadProgram(osProgram);
			}
		}
		
		os.run();
	}
}
