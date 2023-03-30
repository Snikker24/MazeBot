package Blocks;

public abstract class Instruction implements CodeBlock {

    private volatile boolean status;

    public Instruction(){
        status=false;
    }

    protected abstract void action();

    @Override
    public void run(){
        status=true;
        action();
        status=false;
    }

    public boolean isRunning(){
        return status;
    }

    public static Instruction NullInstruction(){
        return new Instruction() {
            @Override
            protected void action() {

            }
        };
    }

}
