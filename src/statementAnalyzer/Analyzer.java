package statementAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import graph.Node;
import pattern.PatternMatcher;
import projectOfDataClass.Statement;

public class Analyzer {

    private PatternMatcher patternMatcher;
    Map<Integer, Boolean> isUsed;
    ArrayList<Node> graph;
    public Analyzer() {
        patternMatcher = new PatternMatcher();
        isUsed = new HashMap<>();
    }

    public void analyzeMethod(ArrayList<Statement> method) {

        int i = 0;
        int nodeNumber = 1;

        graph = new ArrayList<>();
        Stack<Node> nodeStack = new Stack<>();
        Stack<Node> parentOfIf = new Stack<>();

        Stack<Node> parentOfEndParenthesis = new Stack<>();

        Node currentNode = new Node(nodeNumber);
        nodeNumber++;
        graph.add(currentNode);
        nodeStack.push(currentNode);

        while (i < method.size()) {
            // System.out.println(method.get(i).getLineNumber() + " "
            // +method.get(i).getStatement());

            if (isElseIfStatement(method.get(i).getStatement())) {

                Node elseIfNode = new Node(nodeNumber);
                nodeNumber++;
                graph.add(elseIfNode);

                Node parentOfifNode = parentOfIf.pop();
                parentOfifNode.addChild(elseIfNode);
                elseIfNode.setParentNode(parentOfifNode);
                elseIfNode.addStatement(method.get(i));

                if (paranthesisFound(method.get(i).getStatement())) {

                    elseIfNode.isElseIf = true;

                    parentOfEndParenthesis.push(elseIfNode);
                    nodeStack.push(elseIfNode);

                } else if (paranthesisFound(method.get(i + 1).getStatement())) {
                    elseIfNode.isElseIf = true;
                    i++;
                    elseIfNode.addStatement(method.get(i));
                    parentOfEndParenthesis.push(elseIfNode);
                    nodeStack.push(elseIfNode);

                } else {
                    i++;
                    elseIfNode.addStatement(method.get(i));

                    if (isElseStatement(method.get(i + 1).getStatement())
                            || isElseIfStatement(method.get(i + 1).getStatement())) {

                        parentOfIf.add(parentOfifNode);

                    } else {

                        Node nextNode = new Node(nodeNumber);
                        nodeNumber++;
                        graph.add(nextNode);

//						ArrayList<Integer> childList = parentOfifNode.getChildList();
                        ArrayList<Node> childList = parentOfifNode.getChildList();
                        for (int index1 = 0; index1 < childList.size(); index1++) {
                            for (int index2 = 0; index2 < graph.size(); index2++) {
                                if (graph.get(index2).equals(childList.get(index1))) {
                                    graph.get(index2).addChild(nextNode);
                                }
                            }
                        }



                        parentOfifNode.addChild(nextNode);
                        nodeStack.add(nextNode);
                    }

                }

            }

            else if (isElseStatement(method.get(i).getStatement())) {

                Node elseNode = new Node(nodeNumber);
                nodeNumber++;

                graph.add(elseNode);

                Node parentOfifNode = parentOfIf.pop();
                parentOfifNode.addChild(elseNode);
                elseNode.setParentNode(parentOfifNode);
                elseNode.addStatement(method.get(i));

                if (paranthesisFound(method.get(i).getStatement())) {
                    elseNode.isElse = true;

                    parentOfEndParenthesis.push(elseNode);
                    nodeStack.push(elseNode);
                    // i++;

                } else if (paranthesisFound(method.get(i + 1).getStatement())) {
                    i++;
                    elseNode.addStatement(method.get(i));
                    elseNode.isElse = true;

                    parentOfEndParenthesis.push(elseNode);
                    nodeStack.push(elseNode);
                    // i++;

                } else {

                    i++;
                    elseNode.addStatement(method.get(i));
                    i++;

                    Node nextNode = new Node(nodeNumber);
                    nodeNumber++;

                    ArrayList<Node> childList = parentOfifNode.getChildList();

                    for (int index1 = 0; index1 < childList.size(); index1++) {
                        for (int index2 = 0; index2 < graph.size(); index2++) {
                            if (graph.get(index2).equals(childList.get(index1))) {
                                graph.get(index2).addChild(nextNode);
                            }
                        }
                    }

                    graph.add(nextNode);
                    nextNode.setParentNode(elseNode);

                    nodeStack.add(nextNode);
                    nextNode.addStatement(method.get(i));

                }

            }

            else if (isIfStateent(method.get(i).getStatement())) {

                Node previousNode = nodeStack.pop();

                currentNode = new Node(nodeNumber);
                currentNode.isIf = true;
                nodeNumber++;

                currentNode.setParentNode(previousNode);
                previousNode.addChild(currentNode);

                graph.add(currentNode);

                if (paranthesisFound(method.get(i).getStatement())) {

                    currentNode.addStatement(method.get(i));
                    parentOfIf.push(previousNode);
                    parentOfEndParenthesis.push(currentNode);
                    nodeStack.push(currentNode);
                } else if (paranthesisFound(method.get(i + 1).getStatement())) {
                    currentNode.addStatement(method.get(i));
                    i++;
                    currentNode.addStatement(method.get(i));

                    parentOfIf.push(previousNode);
                    parentOfEndParenthesis.push(currentNode);
                    nodeStack.push(currentNode);

                } else {

                    currentNode.addStatement(method.get(i));
                    i++;

                    if (isElseStatement(method.get(i + 1).getStatement())
                            || isElseIfStatement(method.get(i + 1).getStatement())) {

                        currentNode.addStatement(method.get(i));
                        parentOfIf.add(currentNode.getParent());

                    } else {

                        currentNode.addStatement(method.get(i));
                        i++;

                        Node newNode = new Node(nodeNumber);
                        nodeNumber++;
                        currentNode.addChild(newNode);
                        currentNode.getParent().addChild(newNode);
                        graph.add(newNode);
                        nodeStack.push(newNode);

                    }

                }

            }

            else if (isForloopStarting(method.get(i).getStatement())
                    || isWhileloopStarting(method.get(i).getStatement())) {
                Node previousNode = nodeStack.pop();
                currentNode = new Node(nodeNumber);
                previousNode.addChild(currentNode);

                nodeNumber++;

                currentNode.setParentNode(previousNode);
                currentNode.addStatement(method.get(i));

                graph.add(currentNode);

                if (paranthesisFound(method.get(i).getStatement())) {

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    nodeStack.push(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode);
                    if(isForloopStarting(method.get(i).getStatement())){
                        currentNode.isForLoop =true;
                    }
                    else {
                        currentNode.isWhileLoop = true;
                    }
                    currentNode.isLoop = true;
                    parentOfEndParenthesis.push(currentNode);

                } else if (paranthesisFound(method.get(i + 1).getStatement())) {

                    i++;

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    nodeStack.push(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode);
                    currentNode.isLoop = true;
                    if(isForloopStarting(method.get(i).getStatement())){
                        currentNode.isForLoop =true;
                    }
                    else {
                        currentNode.isWhileLoop = true;
                    }
                    parentOfEndParenthesis.push(currentNode);

                } else {
                    Node nestedNode = new Node(nodeNumber);

                    currentNode.addChild(nestedNode);
                    graph.add(nestedNode);
                    nodeNumber++;
                    nestedNode.addStatement(method.get(i + 1));
                    i += 2;

                    nestedNode.setParentNode(currentNode);
                    nestedNode.addChild(currentNode);

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode);
                    nestedNode.addChild(newNode);
                    nodeStack.push(newNode);
                    continue;
                }

            }

            else if (isDoWhileloopStarting(method.get(i).getStatement())) {
                Node previousNode = nodeStack.pop();
                currentNode = new Node(nodeNumber);
                previousNode.addChild(currentNode);
                nodeNumber++;

                currentNode.setParentNode(previousNode);
                currentNode.addStatement(method.get(i));

                graph.add(currentNode);

                if (paranthesisFound(method.get(i).getStatement())) {

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    nodeStack.push(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode);
                    currentNode.isDoWhileLoop = true;
                    parentOfEndParenthesis.push(currentNode);

                } else if (paranthesisFound(method.get(i + 1).getStatement())) {

                    i++;

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    nodeStack.push(newNode);

                    newNode.setParentNode(currentNode);
                    currentNode.addChild(newNode);
                    currentNode.isDoWhileLoop = true;
                    parentOfEndParenthesis.push(currentNode);

                } else {
                    Node nestedNode = new Node(nodeNumber);

                    currentNode.addChild(nestedNode);
                    graph.add(nestedNode);
                    nodeNumber++;
                    nestedNode.addStatement(method.get(i + 1));
                    i += 2;

                    nestedNode.addStatement(method.get(i));
                    i++;

                    nestedNode.setParentNode(currentNode);
                    nestedNode.addChild(currentNode);

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);

                    newNode.setParentNode(nestedNode);
                    nestedNode.addChild(newNode);
                    nodeStack.push(newNode);
                    continue;
                }

            }

            else if(endParanthesisOfDoWhileFound(method.get(i).getStatement()) && !parentOfEndParenthesis.isEmpty()) {
                Node startOfParanthesis = parentOfEndParenthesis.pop();
                Node previousNode = nodeStack.pop();

                Node newNode = new Node(nodeNumber);
                nodeNumber++;
                graph.add(newNode);
                previousNode.addChild(newNode);
                previousNode.addStatement(method.get(i));
                newNode.setParentNode(previousNode);
                nodeStack.push(newNode);
                previousNode.addChild(startOfParanthesis);

            }

            else if (endParanthesisFound(method.get(i).getStatement()) && !parentOfEndParenthesis.isEmpty()) {

                Node startOfParenthesis = parentOfEndParenthesis.pop();

                if (startOfParenthesis.isLoop) {

                    Node previousNode = nodeStack.pop();

                    Node newNode = new Node(nodeNumber);
                    nodeNumber++;
                    graph.add(newNode);
                    previousNode.addChild(newNode);
                    startOfParenthesis.addChild(newNode);
                    newNode.setParentNode(previousNode);
                    nodeStack.push(newNode);
                    previousNode.addChild(startOfParenthesis);

                }

                else if (startOfParenthesis.isElse) {
                    Node nextNode = new Node(nodeNumber);
                    nodeNumber++;

                    Node parentOfifNode = startOfParenthesis.getParent();

                    ArrayList<Node> childList = parentOfifNode.getChildList();

                    for (int index1 = 0; index1 < childList.size(); index1++) {
                        for (int index2 = 0; index2 < graph.size(); index2++) {
                            if (graph.get(index2).equals(childList.get(index1))) {
                                graph.get(index2).addChild(nextNode);
                            }
                        }
                    }

                    graph.add(nextNode);
                    nextNode.setParentNode(startOfParenthesis);
                    startOfParenthesis.addStatement(method.get(i));
                    nodeStack.add(nextNode);

                }

                else if (startOfParenthesis.isIf) {

                    Node parentOfifNode = startOfParenthesis.getParent();

                    if (isElseStatement(method.get(i + 1).getStatement())
                            || isElseIfStatement(method.get(i + 1).getStatement())) {

                        startOfParenthesis.addStatement(method.get(i));

                    } else {

                        Node nextNode = new Node(nodeNumber);
                        nodeNumber++;

                        parentOfifNode.addChild(nextNode);
                        startOfParenthesis.addChild(nextNode);
                        nextNode.setParentNode(startOfParenthesis);
                        startOfParenthesis.addStatement(method.get(i));

                        nodeStack.add(nextNode);
                        graph.add(nextNode);

                    }
                }

                else if (startOfParenthesis.isElseIf) {

                    if (isElseStatement(method.get(i + 1).getStatement())
                            || isElseIfStatement(method.get(i + 1).getStatement())) {
                        startOfParenthesis.addStatement(method.get(i));
                        parentOfIf.push(startOfParenthesis.getParent());

                    } else {
                        startOfParenthesis.addStatement(method.get(i));

                        Node nextNode = new Node(nodeNumber);
                        nodeNumber++;

                        Node parentOfifNode = startOfParenthesis.getParent();

                        ArrayList<Node> childList = parentOfifNode.getChildList();

                        for (int index1 = 0; index1 < childList.size(); index1++) {
                            for (int index2 = 0; index2 < graph.size(); index2++) {
                                if (graph.get(index2).equals(childList.get(index1))) {
                                    graph.get(index2).addChild(nextNode);
                                }
                            }
                        }

                        graph.add(nextNode);
                        nextNode.setParentNode(startOfParenthesis);

                        nodeStack.add(nextNode);
                        startOfParenthesis.getParent().addChild(nextNode);

                    }

                }

            }

            else {
                if (nodeStack.isEmpty())
                    break;

                Node tempNode = nodeStack.pop();
                tempNode.addStatement(method.get(i));
                nodeStack.push(tempNode);
            }

            i++;
        }
    }

    public ArrayList<Node> getGraph() {
        return graph;
    }

    public void showGraphConditions(ArrayList<Node> graph){
        System.out.println(graph);
    }
    public void analyzerOutput(){
        printGraphInfo(graph);
        if(graph.size() > 9)
            printOtherInfoLarge(graph);
        else
            printOtherInfo(graph);
    }

    private void printGraphInfo( ArrayList<Node> graph ){
        for (int index = 0; index < graph.size(); index++) {
            graph.get(index).printChild();
            graph.get(index).printStatement();
        }
    }
    private void printOtherInfo(ArrayList<Node> graph){
        System.out.println();
        System.out.println("---------------------------------------------------------------");
        System.out.print("");

        System.out.print("\t\t\t\t\t");
        for (int index = 0; index < graph.size(); index++)
            System.out.print(index + 1 + "\t");
        System.out.println();

        System.out.print("\t\t\t\t\t");
        for (int index = 0; index < graph.size(); index++)
            System.out.print("-\t");
        System.out.println();

        for (int index1 = 0; index1 < graph.size(); index1++) {
            Node node = graph.get(index1);

            System.out.print("Node Number:  " + node.getNodeNumber() + "\t\t");
//			for (int index2 = 0; index2 < graph.size(); index2++) {
//				if (node.isChild(index2 + 1)) {
//					System.out.print("1\t");
//				} else {
//					System.out.print("0\t");
//				}
//			}

            System.out.println();
        }
    }

    private void printOtherInfoLarge(ArrayList<Node> graph) {
        System.out.println();
        System.out.println("---------------------------------------------------------------");
        System.out.print("");

        System.out.print("\t\t\t\t\t\t\t");
        for (int index = 0; index < graph.size(); index++)
            System.out.print(index + 1 + "\t");
        System.out.println();

        System.out.print("\t\t\t\t\t\t\t");
        for (int index = 0; index < graph.size(); index++)
            System.out.print("-\t");
        System.out.println();

        for (int index1 = 0; index1 < graph.size(); index1++) {
            Node node = graph.get(index1);

            System.out.print("Node Number:  " + node.getNodeNumber() + " - \t\t\t");
//			for (int index2 = 0; index2 < graph.size(); index2++) {
//				if (node.isChild(index2 + 1)) {
//					System.out.print("1\t");
//				} else {
//					System.out.print("0\t");
//				}
//			}

            System.out.println();
        }
    }

    private boolean isDoWhileloopStarting(String statement) {
        return patternMatcher.isMatch("^(\\s)*do", statement);
    }

    private boolean isWhileloopStarting(String statement) {
        return patternMatcher.isMatch("^(\\s)*while", statement);
    }

    private boolean isElseIfStatement(String statement) {
        return patternMatcher.isMatch("^(\\s)*else if", statement);
    }

    private boolean isElseStatement(String statement) {
        return patternMatcher.isMatch("^(\\s)*else", statement);
    }

    private boolean isIfStateent(String statement) {
        return patternMatcher.isMatch("^(\\s)*if", statement);
    }

    private boolean endParanthesisFound(String statement) {
        return patternMatcher.isMatch("\\}(\\s)*$", statement);
    }

    private boolean endParanthesisOfDoWhileFound(String statement) {

        return patternMatcher.isMatch("^(\\s)*\\}(\\s)*while", statement);
        //return patternMatcher.isMatch("\\}(\\s)*$", statement);
    }

    private boolean paranthesisFound(String statement) {
        return patternMatcher.isMatch("\\{(\\s)*$", statement);
    }

    private boolean isForloopStarting(String statement) {
        return patternMatcher.isMatch("^(\\s)*for", statement);
    }

}
