package computer;

import java.util.Vector;

public class Monitor {

	private DMA dma;
	public void associate(DMA dma) {
		this.dma = dma;
	}
	private int deviceCode = 1;
	
	public Vector<Integer> monitorMemory;
	
	public Monitor() {
		this.monitorMemory = new Vector<Integer>();
	}

	public void print() {
		this.dma.load(1);
		System.out.println("____________Monitor____________");
		for(int value : this.monitorMemory) {
			System.out.println(value);
		}
		this.monitorMemory.clear();
			
		System.out.println("_______________________________");
	}

}
