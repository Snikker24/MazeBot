package Blocks;

import Blocks.CodeBlock;
import Blocks.Instruction;

import java.util.LinkedHashMap;

public class MultipleCondition implements CodeBlock {



    private Object switchInstance;
    private LinkedHashMap<Object, Instruction> cases;

    public MultipleCondition(Object switchInstance){
       this. switchInstance=switchInstance;
       cases=new LinkedHashMap<>();
    }


    public void addCase(Object instance, Instruction instruction){
        cases.put(instance,instruction);
    }

    public void removeCase(Object instance){

        cases.remove(instance);

    }

    public Object[] getCases(){
        return cases.keySet().toArray(new Object[0]);
    }

    @Override
    public void run() {

        for(Object o:cases.keySet()){

            if(o.equals(switchInstance)) {

                cases.get(o).run();
                break;

            }

        }

    }
}
