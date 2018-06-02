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
	 * @return boolean, whether value was read
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

}
