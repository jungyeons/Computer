package computer;

import java.util.HashMap;

public class CPU {
	private enum EState{
		eStopped,
		eRunning,
		eWaitting
	}
	public enum EOperator{
		eHalt(0b000000),
		eLod(0b000001),
		eStoN(0b000010),
		eStoR(0b000011),
		eMov(0b000100),
		eAdd(0b000110),
		eSub(0b000111),
		eMul(0b001000),
		eDiv(0b001001),
		eJmp(0b010000),
		eGtj(0b010001),
		eEtj(0b010010),
		ePrint(0b011000),
		eInput(0b011001),
		eFunc(0b011100),
		eBack(0b011101);
		
		private int operatorBit;
		EOperator (int operatorBit) {
			this.operatorBit = operatorBit;
		}
		public int getOperatorBit() {
			return this.operatorBit;
		}
	}
	private Memory memory;
	private ControlBus controlBus;
	public void associate(Memory memory, ControlBus controlBus) { 
		this.memory = memory;
		this.controlBus = controlBus;
	}
	
	private EState eState;
	private ALU alu;
	private HashMap<Integer, Register> registers;
	// Register
	private IR ir;
	public Register mar, mbr;
	public Register pc, cs, ds, ss, ac1, ac2, state, tp, ad;
	public CPU() {
		this.alu = new ALU();
		this.ir = new IR();
		pc = new Register();
		cs = new Register();
		ds = new Register();
		ss = new Register();
		mar = new Register();
		mbr = new Register();
		ac1 = new Register();
		ac2 = new Register();
		state = new Register();
		tp = new Register();
		ad = cs;
		this.registers = new HashMap<Integer, Register>();
		this.registers.put(0b0000, this.pc);
		this.registers.put(0b0001, this.cs);
		this.registers.put(0b0010, this.ds);
		this.registers.put(0b1011, this.ss);
		this.registers.put(0b0011, this.mar);
		this.registers.put(0b0100, this.mbr);
		this.registers.put(0b0110, this.ac1);
		this.registers.put(0b0111, this.ac2);
		this.registers.put(0b1001, this.state);
		this.registers.put(0b1010, this.tp);
	}
	private void fetch() { 
		mar.setValue(ad.getValue()+pc.getValue());
		memory.load();
		ir.setValue(mbr.getValue());
	}
	private void decode() {
		int operator = ir.getOperator();
		for(EOperator eOperator : EOperator.values()) {
			if(eOperator.getOperatorBit() == operator) {
				ir.setEOperator(eOperator);
				break;
			}
		}
	}
	private void execute() {
		pc.setValue(pc.getValue() + 1);
		switch (ir.getEOperator()) {
		case eHalt :
			this.halt();
			break;
		case eLod :
			this.load(ir.getOperand1(), ir.getOperand2());
			break;
		case eStoN :
			this.storeN(ir.getOperand1(), ir.getOperand2());
			break;
		case eStoR :
			this.storeR(ir.getOperand1(), ir.getOperand2());
			break;
		case eMov :
			this.move(ir.getOperand1(), ir.getOperand2());
			break;
		case eAdd :
			this.add(ir.getOperand1(), ir.getOperand2());
			break;
		case eSub :
			this.sub(ir.getOperand1(), ir.getOperand2());
			break;
		case eMul :
			this.mul(ir.getOperand1(), ir.getOperand2());
			break;
		case eDiv :
			this.div(ir.getOperand1(), ir.getOperand2());
			break;
		case eJmp :
			this.jump(ir.getOperand1());
			break;
		case eGtj :
			this.graterThanJump(ir.getOperand1());
			break;
		case eEtj :
			this.equalThanJump(ir.getOperand1());
			break;
		case ePrint :
			this.print(ir.getOperand1());
			break;
		case eInput :
			this.input(ir.getOperand1());
			break;
		case eFunc :
			this.func(ir.getOperand1());
			break;
		case eBack :
			this.back();
			break;
		default:
			break;
		}
	}
	
	private void halt() {
		this.eState = EState.eStopped;
	}
	private void load(int address, int register) {
		address = address & 0x7FF;
		register = register & 0x7FF;
		Register targetRegister = this.registers.get(register);
		this.mar.setValue(address);
		this.memory.load();
		targetRegister.setValue(this.mbr.getValue());
	}
	private void storeN(int address, int number) {
		address = address & 0x7FF;
		this.mar.setValue(address);
		this.mbr.setValue(number);
		this.memory.store();
	}
	private void storeR(int address, int register) {
		address = address & 0x7FF;
		register = register & 0x7FF;
		Register targetRegister = this.registers.get(register);
		this.mar.setValue(address);
		this.mbr.setValue(targetRegister.getValue());
		this.memory.store();
	}
	private void move(int register, int number) {
		register = register & 0x7FF;
		Register targetRegister = this.registers.get(register);
		targetRegister.setValue(number);
	}
	private void add(int register1, int register2) {
		register1 = register1 & 0x7FF;
		Register targetRegister1 = this.registers.get(register1);
		register2 = register2 & 0x7FF;
		Register targetRegister2 = this.registers.get(register2);
		this.ac1.setValue(targetRegister1.getValue());
		this.ac2.setValue(targetRegister2.getValue());
		this.alu.add();
	}
	private void sub(int register1, int register2) {
		register1 = register1 & 0x7FF;
		Register targetRegister1 = this.registers.get(register1);
		register2 = register2 & 0x7FF;
		Register targetRegister2 = this.registers.get(register2);
		this.ac1.setValue(targetRegister1.getValue());
		this.ac2.setValue(targetRegister2.getValue());
		this.alu.sub();
	}
	private void mul(int register1, int register2) {
		register1 = register1 & 0x7FF;
		Register targetRegister1 = this.registers.get(register1);
		register2 = register2 & 0x7FF;
		Register targetRegister2 = this.registers.get(register2);
		this.ac1.setValue(targetRegister1.getValue());
		this.ac2.setValue(targetRegister2.getValue());
		this.alu.mul();
	}
	private void div(int register1, int register2) {
		register1 = register1 & 0x7FF;
		Register targetRegister1 = this.registers.get(register1);
		register2 = register2 & 0x7FF;
		Register targetRegister2 = this.registers.get(register2);
		this.ac1.setValue(targetRegister1.getValue());
		this.ac2.setValue(targetRegister2.getValue());
		this.alu.div();
	}
	private void jump(int lineNumber) {
		this.pc.setValue(lineNumber);
	}
	private void graterThanJump(int lineNumber) {
		if(this.ac1.getValue() > 0) this.pc.setValue(lineNumber);
	}
	private void equalThanJump(int lineNumber) {
		if(this.ac1.getValue() == 0) this.pc.setValue(lineNumber);
	}
	private void print(int register) {
		register = register & 0x7FF;
		Register targetRegister = this.registers.get(register);
		int value = targetRegister.getValue();
		String valueBit = String.format("%11s", Integer.toBinaryString(value)).replace(" ", "0");
		valueBit = "01"+valueBit;
		value = Integer.parseInt(valueBit, 2);
		this.mbr.setValue(value);
		this.memory.ioStore();
		this.controlBus.send(0b0100);
	}
	private void input(int register) {
		register = register & 0x7FF;
		Register targetRegister = this.registers.get(register);
		this.controlBus.send(0b1000);
		this.memory.ioLoad();
		int value = this.mbr.getValue();
		value = value & 0x7FF;
		targetRegister.setValue(value);
	}
	private void func(int function) {
		this.controlBus.send(0b1100);
		this.tp.setValue(this.pc.getValue());
		this.ad = this.ss;
		this.pc.setValue((this.cs.getValue()+function)-this.ss.getValue());
	}
	private void back() {
		this.pc.setValue(tp.getValue());
		this.ad = this.cs;
	}
	private void checkInterrupt() {
		if(this.state.getValue() == 1) {
			System.out.println("<Catch Interrupt>");
			this.memory.ioLoad();
			int value = this.mbr.getValue();
			String valueBit = String.format("%11s", Integer.toBinaryString(value)).replace(" ", "0");
			valueBit = "01"+valueBit;
			value = Integer.parseInt(valueBit, 2);
			this.mbr.setValue(value);
			this.memory.ioStore();
			this.controlBus.send(0b0100);
			this.state.setValue(0);
		}
	}
	public void start() {
		this.eState = EState.eRunning;
		this.run();
	}
	public void run() {
		while (this.eState == EState.eRunning) {
			this.fetch();
			this.decode();
			this.execute();
			this.checkInterrupt();
		}
		this.eState = EState.eWaitting;
	}
	public void waitting() {
		while(this.eState == EState.eWaitting) {
			this.checkInterrupt();
		}
	}
	public class Register {
		protected int value;
		public Register() {
			value = 0;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		
	}
	private class IR extends Register{
		private int operator, operand1, operand2;
		private EOperator eOperator;
		
		public void setEOperator(EOperator eOperator) {
			this.eOperator = eOperator;
		}
		public EOperator getEOperator() {
			return eOperator;
		}
		public int getOperator() {
			operator = value >>> 26;
			return operator;
		}
		public int getOperand1() {
			operand1 = (value << 6) >>> 19;
			return operand1;
		}
		public int getOperand2() {
			operand2 = (value << 19) >>> 19;
			return operand2;
		}
	}
	private class ALU{
		public void add() {
			int result = ac1.getValue()+ac2.getValue();
			ac1.setValue(result);
		}
		public void sub() {
			int result = ac1.getValue()-ac2.getValue();
			ac1.setValue(result);
		}
		public void mul() {
			int result = ac1.getValue()*ac2.getValue();
			ac1.setValue(result);
		}
		public void div() {
			int result = ac1.getValue()/ac2.getValue();
			ac1.setValue(result);
		}
	}
}

