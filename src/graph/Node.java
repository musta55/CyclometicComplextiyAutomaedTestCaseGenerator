package graph;

import java.util.ArrayList;

import projectOfDataClass.Statement;

public class Node {

    private ArrayList<Node> childList = new ArrayList<>();
    private int nodeNumber;
    private ArrayList<Statement> statements = new ArrayList<>();
    private Node parentNode;
    public boolean isLoop = false;
    public boolean isIf = false;
    public boolean isElse = false;
    public boolean isElseIf = false;
    public boolean parentOfIf = false;
    public boolean isDoWhileLoop = false;
    public boolean isWhileLoop = false;
    public boolean isForLoop = false;

    public void setParentNode(Node parent) {
        parentNode = parent;
    }

    public Node getParentNode() {
        return parentNode;
    }


    public Node(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }


    public void addChild(Node child) {
        childList.add(child);
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    public void printStatement() {
        System.out.println("Statement Size : " + statements.size());
        for(int i = 0; i < statements.size(); i++)
            System.out.println(statements.get(i).getStatement());

        System.out.println();
    }

    public void printChild() {
        System.out.print("Node number:  " + nodeNumber + "   >>>   ");
        for(int i = 0; i < childList.size(); i++) {
            System.out.print(childList.get(i)+ "   ");
        }
        System.out.println();
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public boolean isChild(Node child) {

        for (Node n:childList) {
            if(n == child){
                return true;
            }
        }


        return false;
    }

    public Node getParent() {
        return parentNode;
    }

    public ArrayList<Node> getChildList() {
        return childList;
    }

    public ArrayList<Integer> getChildListNumbers(){
        ArrayList<Integer> childListNumbers = new ArrayList<>();
        for (Node n: childList) {
            childListNumbers.add(n.getNodeNumber());
        }
        return childListNumbers;
    }

    public int getTotalChild() {
        return childList.size();
    }


    @Override
    public String toString() {
        return getString();
    }

    private String getString() {
        return "\n\nNODE : " + nodeNumber +
                "\nStatements = " + statements +
                "\nChildList = " + getChildListNumbers() +
//				"\n ParentNode=" + parentNode +
                "\n\n isLoop \t\t= " + isLoop +
                "\n isIf \t\t\t= " + isIf +
                "\n isForLoop \t\t= " + isForLoop +
                "\n isWhileLoop \t= " + isWhileLoop +
                "\n isElse \t\t= " + isElse +
                "\n isElseIf \t\t= " + isElseIf +
                "\n parentOfIf \t= " + parentOfIf +
                "\n isDoWhileLoop \t= " + isDoWhileLoop +
                "\n";
    }
}
