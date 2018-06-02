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
	 * @return boolean, whether there was an error
	 */
	public boolean readFromMemory(){
		return (storageAddressRegister + startingAddress < memorySize && 
				storageAddressRegister + startingAddress >= 0);
	}
	
	public static void main(String[] args) {

	}

}
