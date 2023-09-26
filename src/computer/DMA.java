package computer;

import java.util.Vector;

public class DMA {

	private Vector<Integer> ioMemory, monitorMemory, keyboardMemory;
	public void associate(Vector<Integer> ioMemory, Vector<Integer> monitorMemory, Vector<Integer> keyboardMemory) {
		this.ioMemory = ioMemory;
		this.monitorMemory = monitorMemory;
		this.keyboardMemory = keyboardMemory;
	}
	
	public DMA() {
		
	}

	public void load(int deviceCode) {
		switch(deviceCode){
		case 1:
			for(int value : this.ioMemory) {
				if(value >>> 11 == 0b01) {
					value = value & 0x7FF;
					this.monitorMemory.add(value);
				}	
			}
			break;
		case 2:
			for(int value : this.ioMemory) {
				if(value >>> 11 == 0b10) {
					value = value & 0x7FF;
					this.keyboardMemory.add(value);
				}	
			}
			break;
		default:
			break;
		}
	}
	public void store(int deviceCode) {
		switch(deviceCode){
		case 1:
			break;
		case 2:
			for(int value : this.keyboardMemory) {
				String valueBit = String.format("%11s", Integer.toBinaryString(value)).replace(" ", "0");
				valueBit = "00"+valueBit;
				value = Integer.parseInt(valueBit, 2);
				this.ioMemory.add(value);
			}
			this.keyboardMemory.clear();
			break;
		default:
			break;
		}
	}
}
