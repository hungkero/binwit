package bitwin;

import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class DataManipulation {
	
	public final String[] operators = {"+","-","*","/","%","(",")","^","|","&","~","!","<",">"};
	private static DataManipulation dataManipulation;
	
	private StringListener stringErrorListener;
	
	private boolean dataInputHasError;

	public DataManipulation() {
	}
	
	public static DataManipulation getInst() {
		if (dataManipulation == null) {
			dataManipulation = new DataManipulation();
			return dataManipulation;
		}
		return dataManipulation;
	}
	
	public double getRawDecData(String str) {
		double rawData;

		rawData = 0;
		dataInputHasError = false;
		
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
		else {
			if (str.length() > 0) {
				dataInputHasError = true;
				stringErrorListener.textDetect("Number format ERROR");
			}
		}

		//error check, current limitation of the app
//		if (hasBitwiseNOT(str)) {
//			stringErrorListener.textDetect("Currently Bitwise NOT is not supported");
//		}
		if (str.contains("^")) {
			stringErrorListener.textDetect("^ is bitwise XOR operator, use ** for exponential operator i.e 2**10 = 1024");
		}
		else if (dataInputHasError) {
			// place holder to maintain error message at stringErrorListener
		}
		else {
			stringErrorListener.textDetect("");
		}
		
		return rawData;
	} 
	
	private double calcOperation(String str) {
		double calcResult;
		ArrayList<String> operationInfo;
		StringBuilder operationRawDecDataList;
		calcResult = 0;

		operationInfo = getOperandsnOperator(str); // return the operands and operator in a List of Strings

		operationRawDecDataList = new StringBuilder(); // convert all the data in operationInfo from any format (hex, bin) to dec format (or float)

		boolean prevStrIsInvertOperation = false;
		boolean hasFloatNum = false;
		for(String str_itr: operationInfo) {
			if (isFloat(str_itr)) {
				hasFloatNum = true;
			}
			if (hasOperatorChar(str_itr)) {
				if (str_itr.contains("~") | str_itr.contains("!")) {
					prevStrIsInvertOperation = true;
				}
				else {
					operationRawDecDataList.append(str_itr);
				}
			}
			else {
				if (prevStrIsInvertOperation) {
					if (hasBitWidthNumber(str_itr)) {
						dataInputHasError = false;
						stringErrorListener.textDetect("");
						long decData = ((Double)getRawDecData(str_itr)).longValue(); //if prev is invert, then no float is allowed
						int dataBitWidth = Integer.parseInt(str_itr.substring(0,str_itr.indexOf("'")));
						long invertDecData = dataBitWidthStrip(dataBitWidth, ~decData);

						operationRawDecDataList.append(Long.toString(invertDecData)+"n");

						prevStrIsInvertOperation = false;
					}
					else {
						stringErrorListener.textDetect("Number must have bit width to be able to be inverted i.e : 16'hcafe, 3'b010, 32'd12345 ");
						dataInputHasError = true;
					}
				}
				else {
					if (hasFloatNum) {
						operationRawDecDataList.append(Double.toString(getRawDecData(str_itr)));
					}
					else {
						long tmpRawRecData = ((Double)getRawDecData(str_itr)).longValue();
						operationRawDecDataList.append(Long.toString(tmpRawRecData)+"n");
					}
				}
			}
		}
		
		String expression = operationRawDecDataList.toString();
		
		

		// manipulate string for exponential operator
//		expression = expression.replace("^", ";"); // change to ; for easier regex
//		expression = expression.replace("**", ";");
//		expression = expression.replaceAll("([0-9]+);([0-9]+)", "Math.pow($1,$2)");
//		expression = expression.replaceAll("\\^", "**"); 
		
		if (hasFloatNum) {
			expression = expression.replace("n", " ");
		}
		
		if (!hasUnCloseBrackets(expression)) {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("graal.js");
			Object result = null;
			try {
                //expression = "72057594037927936n+2n";
//				expression = "2**2";
//				expression = "258>>2";
				System.out.println(expression);
				result = engine.eval(expression);
				if (result instanceof Double) {
					calcResult = (Double) result;
//					System.out.println("result after" + calcResult);
				}
				else if (result != null) {
					calcResult = Long.parseLong(result.toString());
				}
			} catch (ScriptException e) {
				System.out.println("WAIT for next operand");
			}
		}

		return calcResult;
	}

	private boolean hasBitWidthNumber(String str_itr) {
		if (str_itr.matches("^[0-9]{1,4}\'[h,d,b][0-9a-f_]+")) {
			return true;
		}
		return false;
	}

	private boolean hasUnCloseBrackets(String str) {
		
		return false;
	}

	private ArrayList<String> getOperandsnOperator(String str) {
		ArrayList<String> operationInfo = new ArrayList<>();
		int first_operator_idx = -1;
		int smallest_operator_idx = -1;
		
		for(String str_itr: operators) {
			first_operator_idx = str.indexOf(str_itr);

			if (first_operator_idx != -1) {
				if ((smallest_operator_idx == -1) | (first_operator_idx < smallest_operator_idx)) {
					smallest_operator_idx = first_operator_idx;
				}
			} 
		}
		first_operator_idx = smallest_operator_idx;
		
		if (first_operator_idx == -1) {
			// str does not has operators
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

	public float parsingDecStrtoDecData(String str) {
		float decData;
		decData = 0;

		if (str.matches("^\'d[0-9]+")) {
			decData = Long.parseUnsignedLong(str.substring(2,str.length()));
		}
		else if (str.matches("[0-9]+.[0-9]+")) {
			decData = Float.parseFloat(str);
		}
		else if (str.matches("[0-9]+")) {
			decData = Long.parseUnsignedLong(str);
		}
		else if (str.matches("^[0-9]{1,4}\'d[0-9]+")) {
			long fullDecData = Long.parseUnsignedLong(str.substring(str.indexOf("'")+2,str.length()));
			int dataBitLength = Integer.parseInt(str.substring(0,str.indexOf("'")));
			decData = dataBitWidthStrip(dataBitLength, fullDecData);
		}
		else {
			dataInputHasError = true;
			stringErrorListener.textDetect("Number format ERROR");
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
			
			decData = dataBitWidthStrip(dataBitLength, fullDecData);
		}
		else {
			dataInputHasError = true;
			stringErrorListener.textDetect("Number format ERROR");
		}
		return decData;
	}

	public long parsingBinStrtoDecData (String str) {

		long decData;
		decData = 0;

		str = str.replace("_", "");
		str = str.replace("-", "");

		if(str.matches("^\'b[01]+")){
			decData = Long.parseUnsignedLong(str.substring(2,str.length()), 2);
		}
		else if (str.matches("^[0-9]{1,4}\'b[01]+")) {
			long fullDecData = Long.parseUnsignedLong(str.substring(str.indexOf("'")+2,str.length()), 2);
			int dataBitLength = Integer.parseInt(str.substring(0,str.indexOf("'")));
			
			decData = dataBitWidthStrip(dataBitLength, fullDecData);
		}
		else {
			dataInputHasError = true;
			stringErrorListener.textDetect("Number format ERROR");
			System.out.println("ERROR");
		}
		return decData;
	}

	public boolean isHex (String str) {
		if(str.matches("[0-9]*[a-f]+[0-9a-z_]*")
				| str.matches("^0x[0-9a-f_-]+")
				| str.matches("^\'h[0-9a-f_-]+")
				| str.matches("^[0-9]{1,3}\'h[0-9a-f_-]+")) {
			return true;
		}
		return false;
	}
	
	public boolean isDec (String str) {
		if (str.matches("^\'d[0-9]+")
				|str.matches("[0-9.]+")
				|str.matches("^[0-9]{1,3}\'d[0-9]+")) {
			return true;
		}
		return false;
	}
	
	public boolean isFloat (String str) {
		if (str.matches("[0-9]+.[0-9]+")) {
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
		if (!str.matches("[0-9a-f*x+-/%\'h_()|&~!^><]+")
				| str.contains(",")
//				| str.contains(".")
				) {
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

	public long dataBitWidthStrip(int dataBitWidth, long decData) {
		long decStripped;

		String binStr;
		String binStrippedStr;
		
		binStr = Long.toBinaryString(decData);
		
		if (binStr.length() < dataBitWidth) {
			binStrippedStr = binStr;
		}
		else {
			binStrippedStr = binStr.substring(binStr.length() - dataBitWidth, binStr.length());
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
