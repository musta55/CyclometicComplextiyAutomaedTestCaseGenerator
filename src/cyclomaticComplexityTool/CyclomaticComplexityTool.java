package cyclomaticComplexityTool;

import graph.Node;
import projectOfDataClass.Statement;
import statementAnalyzer.Analyzer;

import java.util.ArrayList;

public class CyclomaticComplexityTool {
    private ArrayList<Node> graph;
    private Analyzer analyzer;

    public CyclomaticComplexityTool(ArrayList<Statement> statements) {
        analyzer = new Analyzer();
        analyzer.analyzeMethod(statements);
        graph = analyzer.getGraph();

    }

    public ArrayList<Node> getGraph() {
        return graph;
    }

    public int calculateComplexity(){
        int nodes = graph.size();
        int edges = 0;

        for (Node node:graph) {
            edges += node.getTotalChild();
        }

        int complexity = edges - nodes + 2;
		System.out.println(nodes + " " + edges);
        return complexity;    }
}
