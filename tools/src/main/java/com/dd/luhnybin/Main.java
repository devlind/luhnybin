package com.dd.luhnybin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.squareup.crazybin.Masker;

public class Main {

	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "US-ASCII"));
			
			LuhnyBin lb = new LuhnyBin();
			
			String inputLine = null;
			while((inputLine = in.readLine()) != null) {
				System.out.println(lb.applyMask(inputLine));
			}
			
			System.exit(0);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
}
