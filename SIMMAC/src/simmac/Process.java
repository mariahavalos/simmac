package simmac;

public class Process {

	public int id;
	public int accumulator;
	public int psiar;
	public int startingAddress;
	public int endingAddress;
	
	/**
	 * Constructor for the process object, which sets the id (number in queue), starting address, psiar, 
	 * ending address (memory limit), and the default accumulator value of 0.
	 * 
	 * @param startingAddress
	 * @param psiar
	 * @param size
	 * @param id
	 */
	public Process(int startingAddress, int psiar, int size, int id){
		this.id = id;
		this.startingAddress = startingAddress;
		accumulator = 0;
		this.psiar = psiar;
		endingAddress = startingAddress + size;
		
	}

}
