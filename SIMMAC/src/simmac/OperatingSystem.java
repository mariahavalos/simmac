package simmac;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class OperatingSystem {

	ArrayList<Process> readyProcesses;
	Process currentProcesses;
	SIMMAC cpu;
	int quantumValue, loadAddress, clock;
	
	/**
	 * Constructor for OS object. Contains current process and array list of processes
	 * that are ready for fetching/processing (ha). Also sets the default value of clock.
	 * 
	 * @param quantumValue
	 * @param cpu
	 */
	public OperatingSystem (int quantumValue, SIMMAC cpu){
		this.cpu = cpu;
		this.quantumValue = quantumValue;
		clock = 0;
		loadAddress = 0;
		currentProcesses = null;
		readyProcesses = new ArrayList<Process>();
	}
	
	/**
	 * Function that runs the os objects, containing the list of ready processes.
	 * Cycles through until there are no more processes ready to be executed or an
	 * error is thrown, as specified in other classes.
	 * @throws IOException 
	 * 
	 */
	public void run() throws IOException{
		currentProcesses = null;
		boolean quit = false;
		
		switchProcesses();
		while (!quit){
			boolean status = cpu.executeInstruction();
			clock += 1;
			if (status == true && readyProcesses.size() > 0){
				currentProcesses = null;
				switchProcesses();
			}
			if (status == true && readyProcesses.size() == 0){
				quit = true;
			}
			if (clock >= this.quantumValue && !quit){
				switchProcesses();
			}
		}
	}
	
	/**
	 * Function that switches the current process based on the quantum value. Processes are tracked
	 * based off the run() function, and if null, the cpu's variables are set to the current processes' 
	 * variables. This helps to track the op code, operands, and other variables. A lot of the work is
	 * done here.
	 * @throws IOException 
	 * 
	 */
	public void switchProcesses() throws IOException{
		Writer outputFile = new BufferedWriter(new FileWriter("output.txt", true));
		if (currentProcesses != null){
			currentProcesses.accumulator = cpu.accumulator;
			currentProcesses.psiar = cpu.psiar;
			readyProcesses.add(currentProcesses); 
		}
		
		currentProcesses = readyProcesses.remove(0);
		cpu.accumulator = currentProcesses.accumulator;
		cpu.psiar = currentProcesses.psiar;
		cpu.endingAddress = currentProcesses.endingAddress;
		cpu.startingAddress = currentProcesses.startingAddress;
		
		clock = 0;
		outputFile.append("Switching processes." +  "\n" );
		outputFile.append("Next proccess: " + currentProcesses.id + "\n");
		
		/*//Uncomment the below to print to console instead of file.
		 * System.out.println("Switching processes.");
		 * System.out.println("Next proccess: " + currentProcesses.id);
		 * 
		 */
		
		outputFile.close();
		print(); 
	}
	
	/**
	 * Function that loads the processes into a queue for switching and running later on.
	 * @param processes
	 */
	public void loadProcess(int []processes){
		int startingAddress = loadAddress;
		if (loadAddress + processes.length >= cpu.memorySize){
			System.out.println("Invalid memory! Aborting.");
			System.exit(0);
		}
		for (int i = 0; i < processes.length; i++){
			cpu.memory[loadAddress + i] = processes[i];
			int psiar = loadAddress + i;
			
			/*//Uncomment to see locations and values in memory
			 * System.out.println("OP:" + cpu.memory[loadAddress + i] + "\n");
			 * System.out.println("SA: " + startingAddress + "\n");
			 * System.out.println("LOCATION: " + (loadAddress + i) + "\n");
			*/
			loadAddress += processes.length;
			Process process = new Process(startingAddress, psiar, loadAddress, readyProcesses.size());
			readyProcesses.add(process);
		}
	}
	
	/*
	 * Function that prints the process queue.
	 */
	public void print() throws IOException{
		Writer outputFile = new BufferedWriter(new FileWriter("output.txt", true));
		
		/* //Uncomment below to print to console instead of file
		 * System.out.println("Proccess queue: ");
		 */
		outputFile.append("Proccess queue: " + "\n");
		for (int i = 0; i < readyProcesses.size(); i++){
			outputFile.append(readyProcesses.get(i).id + "\n");
			
			/*//Uncomment below to print to conosle instead of file.
			 * System.out.println(readyProcesses.get(i).id + " ");
			 * 
			 */
		}
		outputFile.close();
	}
	
}
