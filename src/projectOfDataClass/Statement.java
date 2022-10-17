package projectOfDataClass;

public class Statement {

    private String statement;
    private int lineNumber;

    public Statement(String statement, int lineNumber) {
        this.statement=statement;
        this.lineNumber=lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getStatement() {
        return statement;
    }

    @Override
    public String toString() {
        return
//				"STATEMENT :" + "\n" +
//				"LineNumber = " + lineNumber +
                "\n\t\t Statement = " + statement  + "\n" ;
    }
}
