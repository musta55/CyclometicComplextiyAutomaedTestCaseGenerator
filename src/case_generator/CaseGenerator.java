package case_generator;

import graph.Node;
import projectOfDataClass.Statement;
import statementAnalyzer.Analyzer;

import java.util.ArrayList;
import java.util.Objects;

public class CaseGenerator {
    private static int testId = 1;
    private ArrayList<Node> graph;
    private Analyzer analyzer;
    private ArrayList<TestCase> testCases;

    public CaseGenerator(ArrayList<Statement> statements) {
        analyzer = new Analyzer();
        analyzer.analyzeMethod(statements);
        graph = analyzer.getGraph();
        testCases = new ArrayList<>();
        testId = 1;
    }


    public void generateCases(){

        for (Node node:graph) {
//            System.out.println(node);

            if(node.getTotalChild() > 1){
                ArrayList<Node> childList = node.getChildList();
                String alternateString = "";

                Boolean isIfElse = false;
                for (Node childNode:childList) {
                    ArrayList<Statement> tempStatements = childNode.getStatements();


                    if(childNode.isElseIf){
                        String tempString = tempStatements.get(0).getStatement().trim();
                        alternateString  = alterStatementForIf(tempString);
                        generateTestCase(tempString);
                        isIfElse = true;
                    }
                    else if(childNode.isIf){
                        String tempString = tempStatements.get(0).getStatement().trim();

                        alternateString  = alterStatementForIf(tempString);
                        generateTestCase(tempString);
                    }else if(childNode.isElse){
                        generateTestCase(alternateString);
                    }else if(childNode.isWhileLoop){
                        String tempString = tempStatements.get(0).getStatement().trim();
                        alternateString = alterStatementForIf(tempString);
                        generateTestCase(tempString);
                    }else if(childNode.isForLoop){
                        String temporaryString = tempStatements.get(0).getStatement().trim();
                        String tempString ="(" + conditionExtractor(temporaryString) + " )";
                        alternateString = alterStatementForIf(tempString);

//                        System.out.println(tempString);
                        generateTestCase(tempString);

                    }

                }
                if(!isIfElse){
                    generateTestCase(alternateString);
                }
            }
        }

    }

    private String conditionExtractor(String temporaryString) {
        String condition = "";
        int firstIndex = temporaryString.indexOf("(");
        int lastIndex = temporaryString.indexOf(")");

        if(firstIndex == -1){
            return "";
        }else {
            condition = temporaryString.substring(firstIndex+1, lastIndex);
        }

        String forLoopCases[]= condition.split(";");

//        System.out.println(condition);

        return forLoopCases[1];
    }



    public void printTestCases(){
        for (TestCase t: testCases) {
            System.out.println(t);
        }
    }

    private String alterStatementForIf(String tempString) {
        if(tempString.contains("<=")){
            tempString = tempString.replace(" <= ", " > ");
        }else{
            tempString =    tempString.replace(" > ", " <= ");
        }
        if(tempString.contains(">=")){
            tempString = tempString.replace(">=", "<");

        }
        else{
            tempString = tempString.replace(" < ", " >= ");
        }
        if(tempString.contains("==")){
            tempString = tempString.replace(" == ", " != ");
        }else{
            tempString = tempString.replace(" != ", " == ");
        }

        return tempString;
    }

    void generateTestCase(String tempString){
        int firstIndex = tempString.indexOf("(");
        int lastIndex = tempString.indexOf(")");

//        System.out.println(firstIndex + " " + lastIndex);
        String condition;
        if(firstIndex == -1){
            return;
        }else {
            condition = tempString.substring(firstIndex+1, lastIndex);
        }


        String words [] = condition.split(" ");
        ArrayList<String> variables = new ArrayList<>();
        String operator = "";
        for (String w:words) {
            if(isOperator(w)){
                operator = w;
            }else if(w.equals("")){
                continue;
            }else
            {
                variables.add(w);
            }
        }
//        System.out.println(variables + " " + operator);


        TestCase testCase = new TestCase(testId,variables.get(0), operator, variables.get(1));
        testCases.add(testCase);
        testId++;
    }

    private boolean isOperator(String word){
        if (Objects.equals(word, "<")) return true;
        if (Objects.equals(word, ">")) return true;
        if (Objects.equals(word, "<=")) return true;
        if (Objects.equals(word, ">=")) return true;
        if (Objects.equals(word, "==")) return true;
        if (Objects.equals(word, "!=")) return true;
        return false;
    }

}
