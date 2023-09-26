package computer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import computer.CPU.Register;

public class Loader {
	private Memory memory;
	private CPU.Register DS;
	private CPU.Register CS;
	private CPU.Register SS;
	private Scanner scanner;
	
	public void associate(Memory memory, Register DS, Register CS, Register SS) {
		this.memory = memory;
		this.DS = DS;
		this.CS = CS;
		this.SS = SS;
	}
	public Loader() {
	}
	
	public void load() {
		try {
			scanner = new Scanner(new File("code/Object code"));
			int[] segmentSizes = parserHeader();
			this.DS.setValue(segmentSizes[0]);
			this.CS.setValue(segmentSizes[1]);
			this.SS.setValue(segmentSizes[2]);
			for(int i=this.DS.getValue();i<this.CS.getValue();i++) {
				this.memory.setProcess(0);
			}
			loadCode();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private int[] parserHeader() {
		String line = scanner.next();
		int headSize = Integer.parseInt(line, 2);
		int[] segmentSizes = new int[headSize];
		for(int i=0;i<headSize;i++) {
			segmentSizes[i] = Integer.parseInt(scanner.next(), 2);
		}
		return segmentSizes;
	}
	private void loadCode() {
		scanner.nextLine();
		while(scanner.hasNext()) {
			String line = scanner.nextLine();
			line = line.trim().replace(" ", "");
			int instruction = Integer.parseInt(line, 2);
			this.memory.setProcess(instruction);
			if(instruction >>> 26 == 0b000000) break;
		}
	}
	
	public void push() {
		while(scanner.hasNext()) {
			String line = scanner.nextLine();
			line = line.trim().replace(" ", "");
			int instruction = Integer.parseInt(line, 2);
			this.memory.setProcess(instruction);
		}
		scanner.close();
	}
}
