package simmac;

public class Process {

	public int id;
	public int accumulator;
	public int psiar;
	public int startingAddress;
	public int endingAddress;
	
	public Process(int startingAddress, int psiar, int size, int id){
		this.id = id;
		this.startingAddress = startingAddress;
		accumulator = 0;
		this.psiar = psiar;
		endingAddress = startingAddress + size;
		
	}

}
