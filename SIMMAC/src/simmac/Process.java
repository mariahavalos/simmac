package simmac;

public class Process {

	public int id;
	public int accumulator;
	public int psiar;
	public int startingAddress;
	public int endingAddress;
	
	public Process(int startingAddress, int size, int id){
		this.id = id;
		this.startingAddress = startingAddress;
		accumulator = 0;
		psiar = 0;
		endingAddress = startingAddress + size;
	}

}
