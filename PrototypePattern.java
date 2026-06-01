interface Prototype<T> {
    T clone();
}

class Employee implements Prototype<Employee> {
    private String name;
    private String department;
    private String company;
    private int salary;

    public Employee(String name, String department, String company, int salary) {
        this.name = name;
        this.department = department;
        this.company = company;
        this.salary = salary;
    }

    // Copy Constructor
    public Employee(Employee employee) {
        this.name = employee.name;
        this.department = employee.department;
        this.company = employee.company;
        this.salary = employee.salary;
    }

    @Override
    public Employee clone() {
        return new Employee(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", department='"
                + department + '\'' +
                ", company='"
                + company + '\'' +
                ", salary="
                + salary +
                '}';
    }
}

public class PrototypePattern {
    public static void main(String[] args) {
        Employee softwareEngineerTemplate =
                new Employee(
                        "",
                        "Engineering",
                        "Google",
                        100000
                );

        // Clone 1
        Employee emp1 = softwareEngineerTemplate.clone();
        emp1.setName("Vishwajeet");
        emp1.setSalary(150000);

        // Clone 2
        Employee emp2 = softwareEngineerTemplate.clone();
        emp2.setName("Rahul");
        emp2.setSalary(140000);

        System.out.println(emp1);
        System.out.println(emp2);
    }
}
