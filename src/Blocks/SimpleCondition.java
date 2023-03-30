package Blocks;

import Blocks.CodeBlock;
import Blocks.Instruction;

public class SimpleCondition implements CodeBlock {

    private Instruction ifStatement;
    private Instruction elseStatement;
    private boolean condition;

    public SimpleCondition(Instruction ifStatement, Instruction elseStatement, boolean condition){

        this.ifStatement=ifStatement;
        this.elseStatement=elseStatement;
        this.condition=condition;

    }


    @Override
    public void run() {

        if(condition){
            ifStatement.run();
        }else
            elseStatement.run();

    }
}
