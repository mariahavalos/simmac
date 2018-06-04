package simmac;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SIMMAC {
	
	// memory and addresses
	public int memorySize = 512;
	public int memory[];
	public int startingAddress;
	public int endingAddress;
	
	// registers
	public int accumulator;
	public int psiar;
	public int storageAddressRegister;
	public int storageDataRegister;
	public int temporaryRegister;
	public int csiar;
	public int instructionRegister;
	public int microInstructionRegister;

	/**
	 * Constructor that sets default values to csiar, psiar, accumulator, and memory.
	 */
	public SIMMAC(){
		memory = new int [memorySize];
		startingAddress = 0;
		endingAddress = memorySize;
		accumulator = 0;
		psiar = 0;
		csiar = 0;
	}
	
	/**
	 * Function that reads from memory using storage address register. Data from storage location
	 * of SAR is assigned to SDR.
	 * 
	 * @return boolean, specifies if there was an error.
	 */
	public boolean readFromMemory(){
		boolean readable = ((storageAddressRegister) <= endingAddress && 
				storageAddressRegister >= 0);
		
		/*//Uncomment the below to see the memory addresses and contents within those addresses. Helps for 
		 * troubleshooting.

		System.out.println("Looking for memory at memory[" + (storageAddressRegister + startingAddress) + "]");
		System.out.println("Memory is [" + memory[(storageAddressRegister + startingAddress)] + "]");
		System.out.println("Memory limit is: " + endingAddress);
		 */
		
		if (readable){
			storageDataRegister = memory[(storageAddressRegister)];
		}
		return (!readable);
	}
	
	/**
	 * Function that writes to memory using storage address register. Data in SDR is assigned to 
	 * storage location specified by the SAR. 
	 * 
	 * @return boolean, specifies if there was an error. 
	 */
	public boolean writeToMemory(){
		boolean writable = ((storageAddressRegister) <= endingAddress && 
				storageAddressRegister >= 0);
		
		/*//Uncomment the below to see the memory addresses and contents within those addresses. Helps for 
		 * troubleshooting.

		System.out.println("Looking for memory at memory[" + (storageAddressRegister + startingAddress) + "]");
		System.out.println("Memory is [" + memory[(storageAddressRegister + startingAddress)] + "]");
		System.out.println("Memory limit is: " + endingAddress);
		 */
		
		if (writable){
			memory[storageAddressRegister] = storageDataRegister;
		}
		
		return (!writable);
	}
	
	/**
	 * Function that loads value, specified by assignment.
	 * 
	 * @return boolean, whether value was loaded
	 */
	public boolean load(){
		temporaryRegister = accumulator;
		accumulator = psiar + 1;
		psiar = accumulator;
		accumulator = temporaryRegister;
		temporaryRegister = storageDataRegister;
		storageAddressRegister = temporaryRegister;
		
		if (readFromMemory()){
			return true;
		}
		else{
			accumulator = storageDataRegister;
			csiar = 0;
			return false;
		}
		
	}
	/**
	 * Function that stores value, specified by assignment.
	 * 
	 * @return boolean, whether value was stored
	 */
	public boolean store(){
		temporaryRegister = accumulator;
		accumulator = psiar + 1;
		psiar = accumulator;
		accumulator = temporaryRegister;
		temporaryRegister = storageDataRegister;
		storageAddressRegister = temporaryRegister;
		storageDataRegister = accumulator;
		
		if (writeToMemory()){
			return true;
		}
		else{
			csiar = 0;
			return false;
		}
	}
	
	/**
	 * Function that fetches instruction, specified by assignment.
	 * 
	 * @return boolean, whether or not instruction could be fetched
	 */
	public boolean instructionFetch() {
		storageAddressRegister = psiar;
		if (readFromMemory()){
			return true;
		}
		instructionRegister = storageDataRegister;
		csiar = instructionRegister >> 16;
		storageDataRegister = instructionRegister & 0xFFFF;
		return false;
	}
	
	/**
	 * Function that adds values
	 * 
	 * @return boolean, whether or not can't add value, specified by assignment.
	 */
	public boolean add(){
		temporaryRegister = accumulator;
		accumulator = psiar + 1;
		psiar = accumulator;
		accumulator = temporaryRegister;
		temporaryRegister = storageDataRegister;
		storageAddressRegister = temporaryRegister;
		
		if (readFromMemory()){
			return true;
		}
		
		else{
			temporaryRegister = storageDataRegister;
			accumulator = accumulator + temporaryRegister;
			csiar = 0;
			return false;
		}
	}
	
	/**
	 * Function that subtract values, specified by assignment.
	 * 
	 * @return boolean, whether or not can't subtract
	 */
	public boolean subtract(){
		temporaryRegister = accumulator;
		accumulator = psiar + 1;
		psiar = accumulator;
		accumulator = temporaryRegister;
		temporaryRegister = storageDataRegister;
		storageAddressRegister = temporaryRegister;
		
		if (readFromMemory()){
			return true;
		}
		else{
			temporaryRegister = storageDataRegister;
			accumulator = accumulator - temporaryRegister;
			csiar = 0;
			return false;
		}
	}
	
	/**
	 * function that loads immediate value, specified by assignment.
	 */
	public void loadImmediate(){
		accumulator = psiar + 1;
		psiar = accumulator; 
		accumulator = storageDataRegister;
		csiar = 0;
	}
	
	/**
	 * Function that branches, specified by assignment.
	 */
	public void branch(){
		psiar = storageDataRegister;
		csiar = 0;
	}

	/**
	 * Function that branches conditionally, specified by assignment.
	 */
	public void conditionalBranch(){
		if (accumulator == 0){
			psiar = storageDataRegister;
			csiar = 0;
		}
		else{
			temporaryRegister = accumulator;
			accumulator = psiar + 1;
			psiar = accumulator;
			accumulator = temporaryRegister;
			csiar = 0;
		}
	}
	
	/**
	 * Function that prints out contents and memory addresses, used for dumping during error
	 * or during halt.
	 * @throws IOException 
	 */
	public void dumpContents() throws IOException{
		Writer outputFile = new BufferedWriter(new FileWriter("output.txt", true));
		outputFile.append("Registers:" + "\n" );
		outputFile.append("ACC: " + String.format("%08X", accumulator) + "\n");
		outputFile.append("PSIAR: " + String.format("%04X", psiar) + "\n");
		outputFile.append("SAR: " +  String.format("%04X", storageAddressRegister) + "\n");
		outputFile.append("SDR: " +  String.format("%08X", storageDataRegister) + "\n");
		outputFile.append("Memory: " + "\n");
		
		/*// Uncomment to print to console
		 * System.out.println("Registers:");
		 * System.out.printf("ACC = %08X\tPSIAR = %04X\tSAR = %04X\tSDR = %08X\n",
		 * 		accumulator,psiar,storageAddressRegister,storageDataRegister);
		 * System.out.printf("TMPR = %08X\tCSIAR = %04X\tIR = %04X\tMIR = %04X\n",
		 * 		temporaryRegister,csiar,instructionRegister, microInstructionRegister);
		
		 * System.out.println("Memory:");
		*/
		for (int i = 0; i < memorySize; i++){
			if (i > 0 && i%8 == 0){
				outputFile.append(String.valueOf(i));
				outputFile.append(String.format("%08X", memory[i]) + "\n");
				//System.out.println(i);
				//System.out.printf("%08X",memory[i]);
			}
		}
		outputFile.close();
	}
	
	/**
	 * Function that executes instruction. 
	 * Calls instruction fetch to grab the current op code and switches based on op code of
	 * the current process.
	 * Contains halt command. 
	 * 
	 * @return boolean, halt or error depending on outcome
	 * @throws IOException 
	 */
	public boolean executeInstruction() throws IOException{
		boolean halt = false, error = false;
		
		instructionFetch();
		switch(csiar){
		case Instruction.base:
			error = false;
			halt = true;
			break;
			case Instruction.add:
				error = add();
				break;
			case Instruction.subtract:
				error = subtract();
				break;
			case Instruction.load:
				error = load();
				break;
			case Instruction.store:
				error = store();
				break;
			case Instruction.branch:
				error = false;
				break;
			case Instruction.conditionalBranch:
				error = false;
				break;
			case Instruction.loadImmediate:
				error = false;
				break;
			case Instruction.halt:
				dumpContents();
				Writer outputFile = new BufferedWriter(new FileWriter("output.txt", true));
				outputFile.append("\n" + "End of Job" + "\n");
				//System.out.println("\n" + "End of Job");
				outputFile.close();
				halt = true;
				break;
			default:
				dumpContents();
				System.out.printf("Invalid instruction. Terminating process.", instructionRegister);
				halt = true;
		} 
		if (error){
			dumpContents();
			System.out.printf("Invalid memory address. Terminating process.", storageAddressRegister);
		}
		return (halt || error);
	}
}
