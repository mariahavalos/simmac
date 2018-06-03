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
		readyProcesses = new ArrayList<Process>();
	}
	
	public void run(){
		currentProcesses = null;
		boolean quit = false;
		
		switchProcesses();
		while (!quit){
			boolean status = cpu.executeInstruction();
			clock += 1;
			if (status == true){
				if (readyProcesses.size() > 0){
					currentProcesses = null;
					switchProcesses();
				}
				else{
					quit = true;
				}
			}
			if (clock >= this.value && !quit){
				switchProcesses();
			}
		}
	}
	
	public void switchProcesses(){
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
		System.out.println("Switching processes.");
		System.out.println("Next proccess: " + currentProcesses.id);
		print(); 
	}
	
	public void loadProcess(int []processes){
		int startingAddress = loadAddress;
		if (loadAddress + processes.length >= cpu.memorySize){
			System.exit(0);
		}
		for (int i = 0; i < processes.length; i++){
			cpu.memory[loadAddress + i] = processes[i];
			loadAddress += processes.length;
			Process process = new Process(startingAddress, processes.length, readyProcesses.size());
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
