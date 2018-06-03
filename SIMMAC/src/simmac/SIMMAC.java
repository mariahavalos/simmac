package simmac;

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
	 * Function that sets default values to csiar, psiar, accumulator, and memory
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
	 * Function that reads from memory using storage address register.
	 * 
	 * @return boolean, false if unable to read
	 */
	public boolean readFromMemory(){
		boolean readable = ((storageAddressRegister + startingAddress) <= endingAddress && 
				storageAddressRegister + startingAddress >= 0);
		//System.out.println("Looking for memory at memory[" + (storageAddressRegister + startingAddress) + "]");
		//System.out.println("Memory is [" + memory[(storageAddressRegister + startingAddress)] + "]");
		//System.out.println("Memory limit is: " + endingAddress);
		if (readable){
			storageDataRegister = memory[(storageAddressRegister + startingAddress)];
		}
		return (!readable);
	}
	
	/**
	 * Function that writes to memory using storage address register.
	 * 
	 * @return boolean, false if unable to write
	 */
	public boolean writeToMemory(){
		boolean writable = ((storageAddressRegister + startingAddress) <= endingAddress && 
				storageAddressRegister + startingAddress >= 0);
		if (writable){
			memory[storageAddressRegister + startingAddress] = storageDataRegister;
		}
		
		return (!writable);
	}
	
	/**
	 * Function that loads value
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
	 * Function that stores value
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
	 * Function that fetches instruction
	 * 
	 * @return boolean, whether or not instruction could be fetched
	 */
	public boolean instructionFetch() {
		System.out.println("Fetching....");
		storageAddressRegister = psiar;
		if (readFromMemory()){
			return true;
		}
		System.out.println("...FOUND! Processing...");
		instructionRegister = storageDataRegister;
		csiar = instructionRegister >> 16;
		storageDataRegister = instructionRegister & 0xFFFF;
		return false;
	}
	
	/**
	 * Function that adds values
	 * 
	 * @return boolean, whether or not can't add value
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
	 * Function that subtract values
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
	 * function that loads immediate value
	 */
	public void loadImmediate(){
		accumulator = psiar + 1;
		psiar = accumulator; 
		accumulator = storageDataRegister;
		csiar = 0;
	}
	
	/**
	 * Function that branches
	 */
	public void branch(){
		psiar = storageDataRegister;
		csiar = 0;
	}

	/**
	 * Function that branches conditionally
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
	 * Function that prints out contents("dumps")
	 */
	public void dumpContents(){
		System.out.println("Registers:");
		System.out.printf("ACC = %08X\tPSIAR = %04X\tSAR = %04X\tSDR = %08X\n",
				accumulator,psiar,storageAddressRegister,storageDataRegister);
		System.out.printf("TMPR = %08X\tCSIAR = %04X\tIR = %04X\tMIR = %04X\n",
				temporaryRegister,csiar,instructionRegister, microInstructionRegister);
		
		System.out.println("Memory:");
		for (int i = 0; i < memorySize; i++){
			if (i > 0 && i%8 == 0){
				System.out.println(i);
				System.out.printf("%08X",memory[i]);
			}
		}
	}
	
	/**
	 * Function that executes instruction
	 * 
	 * @return boolean, halt or error depending on outcome
	 */
	public boolean executeInstruction(){
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
				System.out.println("End of Job");
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
