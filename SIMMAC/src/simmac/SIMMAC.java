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
	public void setMemory(){
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
		boolean readable = (storageAddressRegister + startingAddress < memorySize && 
				storageAddressRegister + startingAddress >= 0);
		if (readable){
			storageDataRegister = memory[startingAddress + storageAddressRegister];
		}
		
		return (!readable);
	}
	
	/**
	 * Function that writes to memory using storage address register.
	 * 
	 * @return boolean, false if unable to write
	 */
	public boolean writeToMemory(){
		boolean writable = (storageAddressRegister + startingAddress < memorySize && 
				storageAddressRegister + startingAddress >= 0);
		if (writable){
			memory[startingAddress + storageAddressRegister] = storageDataRegister;
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
		storageAddressRegister = psiar;
		if (readFromMemory()){
			return true;
		}
		else{
			instructionRegister = storageDataRegister;
			storageDataRegister = instructionRegister & 0xFFFF;
			csiar = instructionRegister >> 16;
			return false;
		}
	}
	
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
	
	public void loadImmediate(){
		accumulator = psiar + 1;
		psiar = accumulator; 
		accumulator = storageDataRegister;
		csiar = 0;
	}
	
	public void branch(){
		psiar = storageDataRegister;
		csiar = 0;
	}

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

}
