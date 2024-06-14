package hr.fer.zemris.calcite.sql2rel;

class Employee { // This class is used in CustomSchema.java
    public final int id;
    public final String name;
    public final int deptno;
    public final int salary;

    public Employee(int id, String name, int deptno, int salary) {
        this.id = id;
        this.name = name;
        this.deptno = deptno;
        this.salary = salary;
    }
}
