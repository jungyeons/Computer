package computer;

import java.util.Scanner;
import java.util.Vector;

public class Keyboard {

	private ControlBus controlBus;
	private DMA dma;
	public void associate(ControlBus controlBus, DMA dma) {
		this.controlBus = controlBus;
		this.dma = dma;
	}
	private int deviceCode = 2;
	private Scanner scanner;
	
	public Vector<Integer> keyboardMemory;
	
	public Keyboard() {
		this.keyboardMemory = new Vector<Integer>();
		this.scanner = new Scanner(System.in);
	}
	
	public void input() {
		System.out.print("Keyboard Input: ");
		int value = this.scanner.nextInt();
		this.keyboardMemory.add(value);
		this.dma.store(this.deviceCode);
	}

	public void interrupt() {
		System.out.println("@@Interrupt@@");
		input();
		this.controlBus.send(0b0001);
	}
}
