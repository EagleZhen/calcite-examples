package hr.fer.zemris.calcite.sql2rel;

public class Employee { // This class is used in CustomSchema.java
    public final int ID;
    public final String NAME;
    public final int DEPTNO;
    public final int SALARY;

    public Employee(int ID, String NAME, int DEPTNO, int SALARY) {
        this.ID = ID;
        this.NAME = NAME;
        this.DEPTNO = DEPTNO;
        this.SALARY = SALARY;
    }
}
