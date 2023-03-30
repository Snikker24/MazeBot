package Blocks;

public class Loop implements CodeBlock {

    private volatile boolean condition;
    private Instruction instruction;
    private int count;

    public Loop( Instruction loopInstruction, int count){

        this.instruction =loopInstruction;
        this.count=count;
    }

    @Override
    public void run() {

        condition=true;

        if(count>=0){

            for (int i = 0; i < count; i++) {

                if (condition)
                    instruction.run();
                else
                    break;
            }

        }
        else {

            while(condition){
                instruction.run();
            }

        }

        condition=false;

    }

    public void exit(){
        condition=false;
    }
}
