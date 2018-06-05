package simmac;

public class Processor {

	public final static int base = 0x0000;
	public final static int add =  0x0001;
	public final static int subtract =  0x0002;
	public final static int load =  0x0003;
	public final static int loadImmediate =  0x0004;
	public final static int store =  0x0005;
	public final static int branch =  0x0006;
	public final static int conditionalBranch =  0x0007;
	public final static int halt =  0x0008;
	
	/** 
	 * Function that gets the op code based on value and returns the hex value.
	 * 
	 * @param opCode
	 * @return int, -1 if opcode is not valid
	 */
	public static int getOpCode(String opCode){
		opCode = opCode.toLowerCase();
		
		switch(opCode){
			case "base":
				return base;
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
	
	/**
	 * Function that parses the operand, if applicable.
	 * 
	 * @param opCode
	 * @param line
	 * @param fileName
	 * @param operand
	 * @return operandValue, if applicable
	 */
	public static Integer parse(int opCode, int line, String fileName, String operand){
		switch(opCode){
			case base:
				if (operand.matches("[+-]?\\d+")){
					int value = Integer.parseInt(operand);
					return value;
				}
			case store:
			case load:
			case loadImmediate:
				if (operand.matches("[+-]?\\d+")){
					int value = Integer.parseInt(operand);
					if (value >= -32767 && value < 32767){
						return value;
					}
					else{
						System.out.println( operand + " is longer than 16 bits.");
						return null;
					}
				}
				else{
					System.out.println(operand + " is an invalid number.");
					return null;
				}
			case add:
			case subtract:
			case branch:
			case conditionalBranch:
				if (operand.matches("\\d+")){
					int value = Integer.parseInt(operand);
					if (value >= 0 && value < 32767){
						return value;
					}
					else{
						System.out.println(operand + " is longer than 16 bits.");
						return null;
					}
				}
				else{
					System.out.println(operand + " is an invalid number.");
					return null;
				}
			case halt:
			default:
				return null;
		}
	}
	
	/**
	 * Function that parses the instruction per line of the input file.
	 * 
	 * @param lineNumber
	 * @param fileName
	 * @param line
	 * @return conjoined op code and operand values
	 */
	public static Integer parseInstruction(int lineNumber, String fileName, String line){
		String [] lineArray = line.split("\\s+");
		if (lineArray.length > 2  || lineArray[0].length() == 0){
			System.out.println("Invalid Syntax."); 
			return null;
		}
		int opCodeValue = getOpCode(lineArray[0]);
		if (opCodeValue == -1){
			System.out.println("Invalid OP Code."); 
			return null;
		}
		if (opCodeValue == halt){
			if (lineArray.length > 1){
				System.out.println("Halt doesn't require operands."); 
				return null;
			}
			return (opCodeValue << 16); 
		}
		
		else{
			Integer op = parse(opCodeValue, lineNumber, fileName, lineArray[1]);
			if (op == null){
				return null;
			}
			else{
				return (opCodeValue << 16 | op);
			}
		}
	}
}

