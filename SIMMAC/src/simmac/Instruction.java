package simmac;

public class Instruction {

	public final static int add =  0x0001;
	public final static int subtract =  0x0002;
	public final static int load =  0x0003;
	public final static int loadImmediate =  0x0004;
	public final static int store =  0x0005;
	public final static int branch =  0x0006;
	public final static int conditionalBranch =  0x0007;
	public final static int halt =  0x0008;
	
	public int getOpCode(String opCode){
		opCode = opCode.toLowerCase();
		
		switch(opCode){
			case "lda":
				return load;
			case "str":
				return store;
			case "add":
				return add;
			case "sub":
				return subtract;
			case "ldi":
				return loadImmediate;
			case "brh":
				return branch;
			case "cbr":
				return conditionalBranch;
			case "halt":
				return halt;
			default:
				return -1;
		}
	}
	
	public Integer parseOperand(int opCode, int line, String fileName, String operand){
		switch(opCode){
			case load:
			case store:
			case add:
			case subtract:
			case loadImmediate:
				if (operand.matches("[+-]?\\d+")){
					int value = Integer.parseInt(operand);
					if (value >= -32767 && value < 32767){
						return value;
					}
					else{
						System.out.println("Error in " + fileName + " , line: " + line +
							"the number: " + operand + " is longer than 16 bits.");
						return null;
					}
				}
				else{
					System.out.println("Error in " + fileName + " , line: " + line +
							" is an invalid number: " + operand );
				}
			case branch:
			case conditionalBranch:
				if (operand.matches("\\d+")){
					int value = Integer.parseInt(operand);
					if (value >= 0 && value < 32767){
						return value;
					}
					else{
						System.out.println("Error in " + fileName + " , line: " + line +
							"the number: " + operand + " is longer than 16 bits.");
						return null;
					}
				}
				else{
					System.out.println("Error in " + fileName + " , line: " + line +
							" is an invalid number: " + operand );
				}
			case halt:
			default:
				return null;
		}
	}
}

