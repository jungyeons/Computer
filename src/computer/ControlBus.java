package computer;

public class ControlBus {

	private CPU cpu;
	private Monitor monitor;
	private Keyboard keyboard;
	private Loader loader;
	public void associate(CPU cpu, Monitor monitor, Keyboard keyboard, Loader loader) {
		this.cpu = cpu;
		this.monitor = monitor;
		this.keyboard = keyboard;		
		this.loader = loader;
	}
	
	public ControlBus() {
		
	}

	public void send(int code) {
		switch(code) {
		case 0b0100:
			this.monitor.print();
			break;
		case 0b1000:
			this.keyboard.input();
			break;
		case 0b0001:
			this.cpu.state.setValue(1);
			break;
		case 0b1100:
			this.loader.push();
			break;
		default:
			break;
		}
		
	}

}
