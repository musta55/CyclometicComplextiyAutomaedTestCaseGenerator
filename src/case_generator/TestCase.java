package case_generator;

public class TestCase {
    private int id ;
    private String variable;
    private String condition;
    private String comparedWith;

    public TestCase(int id,String variable, String condition, String comparedWith) {
        this.variable = variable;
        this.condition = condition;
        this.comparedWith = comparedWith;
        this.id = id;
    }

    @Override
    public String toString() {
        return "TestCase : " +
                + id +
                " -> " + variable  +
                " " + condition  +
                " " + comparedWith  ;
    }

}
