package com.dd.luhnybin;

public class LuhnyBin {

	private static final int MIN_LENGTH = 14;
	private static final int MAX_LENGTH = 16;
	
	private static final char MASKED_CHAR = 'X';
	private static final char HYPHEN = '-';
	private static final char SPACE = ' ';
	
	private int stringLength;
	private int currentLuhnIndex;
	private int currentLuhnLength;
	
	public LuhnyBin(){}
	
	public final String applyMask(String stringToMask) {
		
		char[] chars = stringToMask.toCharArray();
		
		stringLength = chars.length;
		
		int numbersStart = 0,
			maskLoopCount = 0;
		
		boolean digitSequence = false;
		
		for (int i = 0; i < stringLength; i++) {
			if (digitSequence) {
				if ((!Character.isDigit(chars[i]) && !isValidCreditCardCharacter(chars[i]))
						|| i == stringLength - 1) {
					digitSequence = false;
					
					luhnCheck(numbersStart, i, chars);

					maskLoopCount = Math.min(stringLength, currentLuhnLength - 1);
					
					for (int j = 0; j < maskLoopCount; j++) {
						if (Character.isDigit(chars[j + currentLuhnIndex])) {
							chars[j + currentLuhnIndex] = MASKED_CHAR;
						}
					}
				}
			} else {
				digitSequence = Character.isDigit(chars[i]);
				if (digitSequence) {
					numbersStart = i;
				}
			}
		}
		
		return new String(chars);
	}
	
	private final void luhnCheck(int start, int stop, char[] chars) {
		int length = stop - start,
			right = stop,
			intermediateProduct = 0,
			luhnTotal = 0,
			digitCount = 0,
			delimiterCount = 0,
			lastLuhnIndex = -1;

		currentLuhnIndex = 0;
		currentLuhnLength = 0;
		
		if (length < MIN_LENGTH - 1) {
			return;
		}
		
		boolean countDelimiters = false,
			    reset = true;
		
		for (int i = start + length; i >= 0; i--) {
			if (Character.isDigit(chars[i])) {

				countDelimiters = true;
				
				if (digitCount++ % 2 != 0) {
					intermediateProduct = Character.getNumericValue(chars[i]) * 2;
					if (intermediateProduct > 9) {
						luhnTotal += 1 + (intermediateProduct % 10);
					} else {
						luhnTotal += intermediateProduct;
					}
				} else {
					luhnTotal += Character.getNumericValue(chars[i]);
				}

				if (digitCount >= MIN_LENGTH && digitCount <= MAX_LENGTH && luhnTotal % 10 == 0) {
					lastLuhnIndex = i;
					currentLuhnIndex = lastLuhnIndex;
					
					if (reset) {
						currentLuhnLength += (digitCount + delimiterCount + 1);
					} else {
						reset = false;
						currentLuhnLength = digitCount + delimiterCount + 1;
					}
				} else if (digitCount > MAX_LENGTH) {
					if (lastLuhnIndex != -1) {
						currentLuhnIndex = lastLuhnIndex;
						
						if (luhnTotal % 10 == 0) {
							currentLuhnLength += digitCount + delimiterCount;
						} else {
							reset = true;
						}
						
						i = lastLuhnIndex;
					} else {
						i = --right;
					}
					
					if (i > 0 && i < MAX_LENGTH) {
						i = MAX_LENGTH;
					}
					
					luhnTotal = 0;
					lastLuhnIndex = -1;
					digitCount = 0;
				}
				
			} else if (countDelimiters && isValidCreditCardCharacter(chars[i])) {
				delimiterCount++;
			} else if (!isValidCreditCardCharacter(chars[i])) {
				delimiterCount = 0;
				countDelimiters = false;
			}
		}
		
		if (currentLuhnLength == 0 && lastLuhnIndex != -1) {
			currentLuhnLength = digitCount + delimiterCount;
		}
	}
	
	private final boolean isValidCreditCardCharacter(char c) {
		return c == HYPHEN || c == SPACE;
	}
}
