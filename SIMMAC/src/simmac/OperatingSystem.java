package simmac;
import java.util.ArrayList;

public class OperatingSystem {

	ArrayList<Process> readyProcesses;
	Process currentProcesses;
	SIMMAC cpu;
	int value, loadAddress, clock;
	
	public OperatingSystem (int value, SIMMAC cpu){
		this.cpu = cpu;
		this.value = value;
		clock = 0;
		loadAddress = 0;
		currentProcesses = null;
		readyProcesses = new ArrayList();
	}
	
	public void run(){
		currentProccesses = null;
		boolean quit = false;
		
		switchProcesses();
		while (!quit){
			boolean status = cpu.executeInstruction();
			clock += 1;
			if (quit == true){
				if (readyProcesses.size() > 0){
					currentProcesses = null;
					switchProcesses();
				}
				else{
					quit = true;
				}
			}
			if (clock >= value && !quit){
				switchProcesses();
			}
		}
	}
	
	public void switchProcesses(){
		if (currentProcesses != null){
			currentProcceses.accumulator = cpu.accumulator;
			currentProcceses.psiar = cpu.psiar;
			readyProcesses.add(currentProcesses);
		}
		
		currentProcesses = readyProcesses.remove(0);
		cpu.accumulator = currentProcess.accumulator;
		cpu.psiar = currentProcesses.psiar;
		cpu.endingAddress = currentProcesses.endingAddress;
		cpu.startingAddress = currentProcesses.startingAddress;
		
		clock = 0;
		System.out.println("Switching processes");
		System.out.println("Next proccess: " + currentProcesses.id);
		print(); 
	}
	
	public void loadProccess(int []processes){
		int startingAddress = loadAddress;
		if (loadAddress + processes.length >= cpu.memorySize){
			System.exit(0);
		}
		
		for (int i = 0; i < processes.length; i++){
			cpu.memory[loadAddress + i] = processes[i];
			loadAddress += processes.length;
			Proccess process = new Process(startingAddress, process.length, readyProcesses.size());
			readyProcesses.add(process);
		}
	}
	
	public void print(){
		System.out.println("Proccess queue: ");
		for (int i = 0; i < readyProcesses.size(); i++){
			System.out.println(readyProcesses.get(i).id + " ");
		}
	}
	
}
