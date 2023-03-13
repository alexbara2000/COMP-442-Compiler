package SemanticAnalysis.Actions;

import SemanticAnalysis.Visitor.Visitor;

public class FunctionHeadTailAction implements SemanticAction{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "function head tail";
    }
}
