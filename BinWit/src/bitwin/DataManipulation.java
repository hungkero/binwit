package bitwin;

import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import bitwin.MainFrame.DataType;

public class DataManipulation {
	
	public final String[] operators = {"+","-","*","/","%","(",")","^","|","&","~","!"};
	public static DataManipulation dataManipulation;
	
	private StringListener stringErrorListener;

//	private String   data;
//	private DataType dataType;

	public DataManipulation() {
	}
	
	public static DataManipulation getInst() {
		if (dataManipulation == null) {
			dataManipulation = new DataManipulation();
			return dataManipulation;
		}
		return dataManipulation;
	}
	
	public long getRawDecData(String str) {
		long rawData;

		rawData = 0;
		
		if (hasOperatorChar(str)) {
			rawData = calcOperation(str);
		}
		else if (isDec(str)) {
			rawData = parsingDecStrtoDecData(str); 
		}
		else if (isHex(str)) {
			rawData = parsingHexStrtoDecData(str);
		}
		else if (isBin(str)) {
			rawData = parsingBinStrtoDecData(str);
		}

		//error check, current limitation of the app
		if (hasBitwiseNOT(str)) {
			stringErrorListener.textDetect("Currently Bitwise NOT is not supported");
		}
		else if (hasOperatorChar(str) & (Long.toBinaryString(rawData).length() > 52)) {
			stringErrorListener.textDetect("Math operation calculation with data bit width bigger than 52bit is not supported");
		}
		else {
			stringErrorListener.textDetect("");
		}
		
		return rawData;
	}
	
	private long calcOperation(String str) {
		long calcResult;
		ArrayList<String> operationInfo;
		StringBuilder operationRawDecDataList;
		calcResult = 0;

		operationInfo = getOperandsnOperator(str); // return the operands and operator in a List of Strings
		operationRawDecDataList = new StringBuilder();
		
		for(String str_itr: operationInfo) {
			boolean prevNotOperation = false;
			if (hasOperatorChar(str_itr)) {
				if (str_itr.contains("~") | str_itr.contains("!")) {
					prevNotOperation = true;
				}
				else {
					operationRawDecDataList.append(str_itr);
				}
			}
			else {
				if (prevNotOperation) {
					// should add special calculation for not operator to care about the bit width of variable when execute NOT operator
					operationRawDecDataList.append(Long.toString(getRawDecData(str_itr)));
				}
				else {
					operationRawDecDataList.append(Long.toString(getRawDecData(str_itr)));
				}
				prevNotOperation = false;
			}
		}
		
		String expression = operationRawDecDataList.toString();
		
//		System.out.println(expression);

		// manipulate string for exponential operator
		expression = expression.replace("^", ";"); // change to ; for easier regex
		expression = expression.replace("**", ";");
		expression = expression.replaceAll("([0-9]+);([0-9]+)", "Math.pow($1,$2)");
		
		if (!hasUnCloseBrackets(expression)) {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("ECMAScript");
			Object result = null;
			try {
//				expression = "BigInt(72057594037927936)+BigInt(2)";
				result = engine.eval(expression);
				if (result instanceof Double) {
					calcResult = ((Double) result).longValue()	;
				}
				else if (result != null) {
					calcResult = Long.parseLong(result.toString());
				}
			} catch (ScriptException e) {
				System.out.println("WAIT for next operand");
			}
			System.out.println(calcResult);
		}

		return calcResult;
	}

	private boolean hasUnCloseBrackets(String str) {
		
		return false;
	}

	private ArrayList<String> getOperandsnOperator(String str) {
		ArrayList<String> operationInfo = new ArrayList<>();
		int first_operator_idx = 0;
		
		for(String str_itr: operators) {
			first_operator_idx = str.indexOf(str_itr);
			if (first_operator_idx != -1) {
				break;
			}
		}
		
		if (first_operator_idx == -1) {
			operationInfo.add(str);
		}
		else {
			if (first_operator_idx != 0) {
				operationInfo.add(str.substring(0, first_operator_idx));
			}
			operationInfo.add(String.valueOf(str.charAt(first_operator_idx)));
			if (first_operator_idx != str.length()-1) {
				operationInfo.addAll(getOperandsnOperator(str.substring(first_operator_idx+1)));  // --> recursion
			}
		}
		
		return operationInfo;
	}

	public long parsingDecStrtoDecData(String str) {
		long decData;
		decData = 0;

		if (str.matches("^\'d[0-9]+")) {
			decData = Long.parseUnsignedLong(str.substring(3,str.length()));
		}
		else if (str.matches("[0-9]+")) {
			decData = Long.parseUnsignedLong(str);
		}
		else if (str.matches("^[0-9]{1,4}\'d[0-9]+")) {
			long fullDecData = Long.parseUnsignedLong(str.substring(str.indexOf("'")+2,str.length()));
			int dataBitLength = Integer.parseInt(str.substring(0,str.indexOf("'")));
			decData = dataBitLengthStrip(dataBitLength, fullDecData);
		}
		return decData;
	}

	public long parsingHexStrtoDecData(String str) {
		long decData;
		decData = 0;

		str = str.replace("_", "");
		str = str.replace("-", "");

		if (str.matches("[0-9]*[a-f]+[0-9a-z]*")){
			decData = Long.parseUnsignedLong(str, 16);
		}
		else if(str.matches("^0x[0-9a-f]+") | str.matches("^\'h[0-9a-f]+")) {
			decData = Long.parseUnsignedLong(str.substring(2,str.length()), 16);
		}
		else if(str.matches("^[0-9]{1,4}\'h[0-9a-f]+")) {
			long fullDecData = Long.parseUnsignedLong(str.substring(str.indexOf("'")+2,str.length()), 16);
			int dataBitLength = Integer.parseInt(str.substring(0,str.indexOf("'")));
			
			decData = dataBitLengthStrip(dataBitLength, fullDecData);
		}
		return decData;
	}

	public long parsingBinStrtoDecData (String str) {

		long decData;
		decData = 0;

		str = str.replace("_", "");
		str = str.replace("-", "");

		if(str.matches("^\'b[01_-]+")){
			decData = Long.parseUnsignedLong(str.substring(2,str.length()), 2);
		}
		else if (str.matches("^[0-9]{1,4}\'b[01]+")) {
			long fullDecData = Long.parseUnsignedLong(str.substring(str.indexOf("'")+2,str.length()), 2);
			int dataBitLength = Integer.parseInt(str.substring(0,str.indexOf("'")));
			
			decData = dataBitLengthStrip(dataBitLength, fullDecData);
		}
		return decData;
	}

	public boolean isHex (String str) {
		if(str.matches("[0-9]*[a-f]+[0-9a-z]*")
				| str.matches("^0x[0-9a-f_-]+")
				| str.matches("^\'h[0-9a-f_-]+")
				| str.matches("^[0-9]{1,3}\'h[0-9a-f_-]+")) {
			return true;
		}
		return false;
	}
	
	public boolean isDec (String str) {
		if (str.matches("^\'d[0-9]+")
				|str.matches("[0-9]+")
				|str.matches("^[0-9]{1,3}\'d[0-9]+")) {
			return true;
		}
		return false;
	}
	
	public boolean isBin (String str) {
		if(str.matches("^\'b[01_-]+")
				|str.matches("^[0-9]{1,3}\'b[01_-]+")) {
			return true;
		}
		return false;
	}
	
	public boolean hasUnAllowChar (String str) {
		if (!str.matches("[0-9a-f*x+-/%\'h_()|&~!^]+")
				| str.contains(",")
				| str.contains(".")
				) {
//			System.out.println("UNALLOW");
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean hasOperatorChar (String str) {
		for (String str_itr: operators) {
			if (str.contains(str_itr)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasBitwiseNOT(String str) {
		if (str.contains("~")  | str.contains("!")) {
			return true;
		}
		return false;
	}

	public long dataBitLengthStrip(int dataBitLength, long decData) {
		long decStripped;

		String binStr;
		String binStrippedStr;
		
		binStr = Long.toBinaryString(decData);
		
		if (binStr.length() < dataBitLength) {
			binStrippedStr = binStr;
		}
		else {
			binStrippedStr = binStr.substring(binStr.length() - dataBitLength, binStr.length());
		}

		decStripped = Long.parseLong(binStrippedStr, 2);
		
		return decStripped;
	}


	public void setStringErrorListener(StringListener stringErrorListener) {
		this.stringErrorListener = stringErrorListener;
	}
	
	public void setErrorText(String str) {
		stringErrorListener.textDetect(str);
	}
	
		
}
