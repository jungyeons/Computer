package computer;

public class Main {

	public static void main(String[] args) {
		CPU cpu = new CPU();
		Memory memory = new Memory();
		Loader loader = new Loader();
		ControlBus controlBus = new ControlBus();
		Monitor monitor = new Monitor();
		Keyboard keyboard = new Keyboard();
		DMA dma = new DMA();
		
		cpu.associate(memory, controlBus);
		memory.associate(cpu.mar, cpu.mbr);
		loader.associate(memory, cpu.ds, cpu.cs, cpu.ss);
		controlBus.associate(cpu, monitor, keyboard, loader);
		monitor.associate(dma);
		keyboard.associate(controlBus, dma);
		dma.associate(memory.ioMemory, monitor.monitorMemory, keyboard.keyboardMemory);
		
		loader.load();
		cpu.start();
		cpu.waitting();
	}

}
