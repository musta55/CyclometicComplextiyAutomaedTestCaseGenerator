package Driver;

import java.util.ArrayList;

import class_analyzer.MethodFinder;
import code_reader.CodeReader;
import cyclomaticComplexityTool.CyclomaticComplexityTool;
import projectOfDataClass.Statement;

public class Test {

    public static void main(String[] args) {
        System.out.println("Control Flow Graph");
        System.out.println("++++++++++++++++++");
        System.out.println();
        System.out.println();

        CodeReader codeReader = new CodeReader();
//		ArrayList<Statement> lines = codeReader.readCode("Codes/code1.java");
        ArrayList<Statement> lines = codeReader.readCode("Codes/test.java");
//		System.out.println(lines);

        MethodFinder finder = new MethodFinder();
        ArrayList<ArrayList<Statement>> allMethods = finder.findMethods(lines);


//		for (ArrayList<Statement> method: allMethods) {
//			System.out.println("New Method");
//			System.out.println(method);
//			System.out.println("\n\n\n");
//		}


        System.out.println("Total Methods :" + allMethods.size());

        //Analyser analyser = new Analyser();
        //analyser.analyzeStatement(allMethods.get(4));

        for (int i = 0; i < allMethods.size(); i++) {
            System.out.println("Method  " + (i+1) + " :");
//			System.out.println(allMethods.get(i));

            System.out.println();

            CyclomaticComplexityTool tool = new CyclomaticComplexityTool(allMethods.get(i));

            System.out.println(tool.getGraph());


        }
        CyclomaticComplexityTool tool = new CyclomaticComplexityTool(allMethods.get(allMethods.size()-1));
        System.out.println( "Cyclomatic complexity of the code is "+ tool.calculateComplexity());




    }
}
