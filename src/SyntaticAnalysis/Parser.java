package SyntaticAnalysis;

import Common.Errors.CompilerError;
import Common.Errors.ErrorLogger;
import Common.Errors.ErrorType;
import Common.Token.Token;
import Common.Token.TokenType;
import LexicalAnalysis.LexDriver;
import LexicalAnalysis.LexicalAnalyzer;
import Common.Nodes.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser {
    //input
    public LexicalAnalyzer input;
    private final ArrayList<String> derivations = new ArrayList<>();
    private final Map<String, ArrayList<TokenType>> followSet = new HashMap<>();
    private final Map<String, ArrayList<TokenType>> firstSet = new HashMap<>();
    private final ArrayList<String> nullable = new ArrayList<>();
    private final ArrayList<String> endable = new ArrayList<>();

    //Stack
    Stack<String> stack= new Stack<>();
    Stack<Node> astStack = new Stack<>();
    Map<String, String> data;
    ArrayList<TokenType> terminals = new ArrayList<>();
    ArrayList<String> nonTerminals = new ArrayList<>();

    StringBuilder outDerivation = new StringBuilder();
    StringBuilder outAST = new StringBuilder();


    public Parser(String fileToParse) throws Exception {
        LexDriver.main(new String[]{fileToParse});
        populateFirstAndFollowSet();
        
        this.input= LexDriver.lexicalAnalyzer;

        String csvFile = "src/Common/Grammar/grammarTable.csv";
        String line = "";
        String csvSplitBy = ",";
        data = new HashMap<>();

        //populating gramar table
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                nonTerminals.add(values[0]);

                for (int col = 0; col < values.length; col++) {
                    data.put(row + "," + col, values[col]);
                    if (row == 0){
                        AddTerminals(values[col]);
                    }
                }

                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateFirstAndFollowSet() {
        String firstSetFile = "src/Common/Grammar/firstfollowSet.csv";
        String line = "";

        //populating the first and follow set
        try (BufferedReader br = new BufferedReader(new FileReader(firstSetFile))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] initialSplit = line.split(",");
                String key = initialSplit[0];
                if(initialSplit[3].contains("yes")){
                    nullable.add(key.toUpperCase());
                }
                if(initialSplit[3].contains("no")){
                    endable.add(key.toUpperCase());
                }
                String[] firstSplit = initialSplit[1].split(" ");
                String[] followSplit = initialSplit[2].split(" ");

                ArrayList<TokenType> firstValues = new ArrayList<>();
                for(int i=0; i<firstSplit.length; i++){
                    if(firstSplit[i].contains("∅") || firstSplit[i].contains("eof")){
                        firstValues.add(TokenType.EPSILON);
                    }
                    else{
                        String valueToAdd = firstSplit[i].toUpperCase();
                        switch (valueToAdd) {
                            case "RPAR" -> firstValues.add(TokenType.CLOSEPAR);
                            case "LPAR" -> firstValues.add(TokenType.OPENPAR);
                            case "RCURBR" -> firstValues.add(TokenType.CLOSECUBR);
                            case "LCURBR" -> firstValues.add(TokenType.OPENCUBR);
                            case "NEQ" -> firstValues.add(TokenType.NOTEQ);
                            case "RSQBR" -> firstValues.add(TokenType.CLOSESQBR);
                            case "LSQBR" -> firstValues.add(TokenType.OPENSQBR);
                            case "FLOATLIT" -> firstValues.add(TokenType.FLOATNUM);
                            case "INTLIT" -> firstValues.add(TokenType.INTNUM);
                            case "ARROW" -> firstValues.add(TokenType.RETURNTYPE);
                            case "EQUAL" -> firstValues.add(TokenType.ASSIGN);
                            case "CONSTRUCTORKEYWORD" -> firstValues.add(TokenType.CONSTRUCTOR);
                            case "SR" -> firstValues.add(TokenType.SCOPEOP);
                            default -> firstValues.add(TokenType.valueOf(valueToAdd));
                        }
                    }
                }
                firstSet.put(key, firstValues);

                ArrayList<TokenType> followValues = new ArrayList<>();
                for(int i=0; i<followSplit.length; i++){
                    if(followSplit[i].contains("∅") || followSplit[i].contains("eof")){
                        followValues.add(TokenType.EPSILON);
                    }
                    else{
                        String valueToAdd = followSplit[i].toUpperCase();
                        switch (valueToAdd) {
                            case "RPAR" -> followValues.add(TokenType.CLOSEPAR);
                            case "LPAR" -> followValues.add(TokenType.OPENPAR);
                            case "RCURBR" -> followValues.add(TokenType.CLOSECUBR);
                            case "LCURBR" -> followValues.add(TokenType.OPENCUBR);
                            case "NEQ" -> followValues.add(TokenType.NOTEQ);
                            case "RSQBR" -> followValues.add(TokenType.CLOSESQBR);
                            case "LSQBR" -> followValues.add(TokenType.OPENSQBR);
                            case "FLOATLIT" -> followValues.add(TokenType.FLOATNUM);
                            case "INTLIT" -> followValues.add(TokenType.INTNUM);
                            case "ARROW" -> followValues.add(TokenType.RETURNTYPE);
                            case "EQUAL" -> followValues.add(TokenType.ASSIGN);
                            case "CONSTRUCTORKEYWORD" -> followValues.add(TokenType.CONSTRUCTOR);
                            case "SR" -> followValues.add(TokenType.SCOPEOP);
                            default -> followValues.add(TokenType.valueOf(valueToAdd));
                        }
                    }
                }
                followSet.put(key, followValues);
            }
        } catch (Exception e) {
        }
    }

    private void AddTerminals(String value) {

        //some terminals are not the same name, we need to map them to the correct tokens
        try {
            value = value.toUpperCase();
            switch (value) {
                case "$" -> terminals.add(TokenType.EPSILON);
                case "RPAR" -> terminals.add(TokenType.CLOSEPAR);
                case "LPAR" -> terminals.add(TokenType.OPENPAR);
                case "RCURBR" -> terminals.add(TokenType.CLOSECUBR);
                case "LCURBR" -> terminals.add(TokenType.OPENCUBR);
                case "NEQ" -> terminals.add(TokenType.NOTEQ);
                case "RSQBR" -> terminals.add(TokenType.CLOSESQBR);
                case "LSQBR" -> terminals.add(TokenType.OPENSQBR);
                case "FLOATLIT" -> terminals.add(TokenType.FLOATNUM);
                case "INTLIT" -> terminals.add(TokenType.INTNUM);
                case "ARROW" -> terminals.add(TokenType.RETURNTYPE);
                case "EQUAL" -> terminals.add(TokenType.ASSIGN);
                case "CONSTRUCTORKEYWORD" -> terminals.add(TokenType.CONSTRUCTOR);
                case "SR" -> terminals.add(TokenType.SCOPEOP);
                default -> terminals.add(TokenType.valueOf(value.toUpperCase()));
            }
        }
        catch (Exception e){
            terminals.add(TokenType.EMPTY);
        }
    }

    //algorithm
    public Node parse() throws IOException {

        //pushing start on the stack
        stack.push("$");//
        stack.push("START");
        derivations.add("START");

        Token token=input.getNextToken();
        Token previousToken = token;
        String top;
        boolean isValid = true;

        while(!stack.peek().equals("$") && !stack.peek().equals("eof")) {
            try {
                //skip the comments
                while ((token != null && token.getType() == TokenType.BLOCKCMT) || (token != null && token.getType() == TokenType.INLINECMT)) {
                    previousToken = token;
                    token = input.getNextToken();
                }
            } catch (Exception e) {

            }
            //this is only to create the derivation
            if (token == null && nullable.contains(stack.peek()) && !stack.peek().startsWith("SA")) {
                derivations.remove(stack.peek());
                outDerivation.append(String.join(" ", derivations)).append("\n");

                if (endable.contains(stack.peek())) {
                    break;
                }
                stack.pop();
                continue;
            }

            top = stack.peek();

            //makes the ast
            if (top.startsWith("SA")) {
                String semanticAction = stack.pop();
                switch (top){
                    case "SA1" -> astStack.push(new Node(null, new ArrayList<>(), previousToken, 0));
                    case "SA2" -> astStack.push(new Node(null, new ArrayList<>(), new Token(TokenType.EMPTY, "epsilon", token.getLocation()), 0));
                    case "SA3" -> astStack.push(null);
                    case "SA4" -> makeFamily(new ArraySizeNode(null, null,  0));
                    case "SA5" -> makeFamily(new VariableNode(null,null,0));
                    case "SA6" -> makeFamily(new LocalVariableNode(null,null,0));
                    case "SA7" -> makeFamily(new StatementNode(null,null,0));
                    case "SA8" -> makeFamily(new ExpressionNode(null,null,0));
                    case "SA9" -> makeFamily(new ArithmeticExpressionNode(null,null,0));
                    case "SA10" -> makeFamily(new TermNode(null,null,0));
                    case "SA11" -> makeFamily(new FactorNode(null,null,0));
                    case "SA12" -> makeFamily(new RecursiveArithmeticExpressionNode(null,null,0));
                    case "SA13" -> makeFamily(new RecursiveTermNode(null,null,0));
                    case "SA14" -> makeFamily(new RelExpressionNode(null,null,0));
                    case "SA15" -> makeFamily(new IfStatementNode(null,null,0));
                    case "SA16" -> makeFamily(new WhileLoopNode(null,null,0));
                    case "SA17" -> makeFamily(new ReadNode(null,null,0));
                    case "SA18" -> makeFamily(new WriteNode(null,null,0));
                    case "SA19" -> makeFamily(new ReturnNode(null,null,0));
                    case "SA20" -> makeFamily(new FunctionBodyNode(null,null,0));
                    case "SA21" -> makeFamily(new FunctionDefinitionNode(null,null,0));
                    case "SA22" -> makeFamily(new ClassDefinitionNode(null,null,0));
                    case "SA23" -> makeFamily(new StartNode(null,null,0));
                    case "SA24" -> makeFamily(new ProgramNode(null,null,0));
                    case "SA25" -> makeFamily(new InheritanceNode(null,null,0));
                    case "SA26" -> makeFamily(new RecursiveMemberDeclarationNode(null,null,0));
                    case "SA27" -> makeFamily(new MemberDeclarationNode(null,null,0));
                    case "SA28" -> makeFamily(new MemberFunctionDeclarationNode(null,null,0));
                    case "SA29" -> makeFamily(new MemberVariableDeclarationNode(null,null,0));
                    case "SA30" -> makeFamily(new FunctionParamsNode(null,null,0));
                    case "SA31" -> makeFamily(new FunctionHeadNode(null,null,0));
                    case "SA32" -> makeFamily(new FunctionHeadTailNode(null,null,0));
                    case "SA33" -> makeFamily(new ArgumentParamsNode(null,null,0));
                    case "SA34" -> makeFamily(new IndiceNode(null,null,0));
                    case "SA35" -> makeFamily(new IdnestNode(null,null,0));
                    case "SA36" -> makeFamily(new StatementIdnestNode(null,null,0));
                    case "SA37" -> makeFamily(new VariableIdnestNode(null,null,0));
                    case "SA38" -> makeFamily(new FunctionParamsTailNode(null,null,0));
                }
            }
            else {
                outDerivation.append(String.join(" ", derivations)).append("\n");
                //instead of using proper null checks I surrounded stuff with try catches....very poor on my part
                try {
                    if (terminals.contains(getActualTop(top)) && getActualTop(top) == token.getType()) {
                        //if it is the terminal we expect
                        stack.pop();
                        previousToken = token;
                        token = input.getNextToken();
                    } else {
                        //otherwise its an error
                        previousToken = token;
                        token = skipError(token);
                        isValid = false;
                    }
                } catch (Exception e) {
                    //it would fail if terminals does not contain the top
                    try {
                        if (data.get(nonTerminals.indexOf(top) + "," + terminals.indexOf(token.getType())) != null) {
                            //checks to see if it is a non terminal
                            String popedString = stack.pop();
                            int index = derivations.indexOf(popedString);
                            derivations.remove(popedString);
                            inverseRHSMMultiPush(data.get(nonTerminals.indexOf(top) + "," + terminals.indexOf(token.getType())), index);
                        } else {
                            //otherwise it is an error
                            previousToken = token;
                            token = skipError(token);
                            isValid = false;
                        }
                    } catch (Exception b) {
                        //this should never be hit except if we have SA.
                        top = stack.pop();
                    }
                }
            }
        }

        try{
            outAST.append(astStack.peek().toString());
            return astStack.peek();
        }
        catch (Exception e){
            return null;
        }
    }

    private Token skipError(Token token) throws IOException {
        //we log the token that made the error
        ErrorLogger.getInstance().add(new CompilerError(ErrorType.SyntaxError, "[Token Type: "+token.getType()+", Lexeme: "+token.getLexeme()+"].", token.getLocation() ));
        String top = stack.peek();
        Stack<String> tempStack = (Stack<String>)stack.clone();
        if(!followSet.containsKey(tempStack.peek())){
            return input.getNextToken();
        }
        //this is to skip some nullable stack....not sure why I did it, but I know it did not work without
        while(!followSet.containsKey(tempStack.peek())){
            tempStack.pop();
        }
        top = tempStack.peek();

        //this is the traditional skip errors
        if(token == null || followSet.get(top).contains(token.getType())){
            stack = tempStack;
            stack.pop();
        }
        else {
            while ( (!firstSet.get(top).contains(token.getType()) || nullable.contains(top)) && !followSet.get(top).contains(token.getType())){

                token = input.getNextToken();
                if(token == null){
                    return null;
                }
            }
        }
        return token;
    }

    private TokenType getActualTop(String top) {
        switch (top.toUpperCase()) {
            case "$" -> {
                return TokenType.EPSILON;
            }
            case "RPAR" -> {
                return TokenType.CLOSEPAR;
            }
            case "LPAR" -> {
                return TokenType.OPENPAR;
            }
            case "RCURBR" -> {
                return TokenType.CLOSECUBR;
            }
            case "LCURBR" -> {
                return TokenType.OPENCUBR;
            }
            case "NEQ" -> {
                return TokenType.NOTEQ;
            }
            case "RSQBR" -> {
                return TokenType.CLOSESQBR;
            }
            case "LSQBR" -> {
                return TokenType.OPENSQBR;
            }
            case "FLOATLIT" -> {
                return TokenType.FLOATNUM;
            }
            case "INTLIT" -> {
                return TokenType.INTNUM;
            }
            case "ARROW" ->
            {
                return TokenType.RETURNTYPE;
            }
            case "EQUAL" -> {
                return TokenType.ASSIGN;
            }
            case "SR" -> {
                return TokenType.SCOPEOP;
            }
            case "CONSTRUCTORKEYWORD" -> {
                return TokenType.CONSTRUCTOR;
            }
            case "RETURNTYPE" -> {
                throw new RuntimeException();
            }
            default -> {
                return TokenType.valueOf(top.toUpperCase());
            }
        }
    }


    private void inverseRHSMMultiPush(String rule, int index){
        String[] productions = rule.split(" ");

        for (int i = productions.length - 1; i>=2; i--){
            if(productions[i].equals("&epsilon")){
            }
            else{
                stack.push(productions[i]);
            }
        }
        //this is for the derivations
        for (int i = 2; i< productions.length; i++){
            if(productions[i].equals("&epsilon")){
            }
            else{
                derivations.add(index + i -2,productions[i]);
            }
        }
    }

    private Node makeFamily(Node parent){
        //no matter what we will always make a family, to me this is easier and faster to implement
        //we always push epsilon before we push and semantic actions
        ArrayList<Node> childrens = new ArrayList<>();
        while(astStack.peek() != null){
            childrens.add(astStack.pop());
        }
        astStack.pop();

        Collections.reverse(childrens);
        parent.setChildren(childrens);

        for (var child: parent.getChildren()){
            child.setParent(parent);
        }
        parent.updateDepth();

        astStack.push(parent);

        return parent;
    }
}