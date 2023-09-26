package computer;

import java.util.Vector;

public class Memory {
		private Vector<Integer> memory;
		
		private CPU.Register MAR;
		private CPU.Register MBR;
		
		public Vector<Integer> ioMemory;
		public Memory() {
			this.memory = new Vector<Integer>();
			this.ioMemory = new Vector<Integer>();
		}
		public void associate(CPU.Register MAR, CPU.Register MBR) {
			this.MAR = MAR;
			this.MBR = MBR;
		}
		public void setProcess(int instruction) {
			this.memory.add(instruction);
		}
		public void load() {
			int address = MAR.getValue();
			MBR.setValue(this.memory.get(address));
		}
		public void store() {
			int address = MAR.getValue();
			int value = MBR.getValue();
			this.memory.set(address, value);
		}
		public void ioLoad() {
			MBR.setValue(this.ioMemory.get(this.ioMemory.size()-1));
		}
		public void ioStore() {
			this.ioMemory.add(MBR.getValue());
		}
}
