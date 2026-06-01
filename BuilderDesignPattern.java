class Student {
    private int id; // fixed
    private String phoneNumber; // optional
    private String address; // optional
    private String email; // optional

    private Student(int id, String phoneNumber, String address, String email) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }

    void print() {
        System.out.println(id + " -- " + phoneNumber + " " + address + " " + email);
    }

    static class StudentBuilder {
        private int id;
        private String phoneNumber;
        private String address;
        private String email;

        public StudentBuilder(int id) {
            this.id = id;
        }

        public StudentBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public StudentBuilder setAddress(String address) {
            this.address = address;
            return this;
        }
    
        public StudentBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Student build() {
            return new Student(id, phoneNumber, address, email);
        }
        
    }

}


public class BuilderDesignPattern {
    public static void main(String[] args) {
        Student student = new Student.StudentBuilder(-5)
        .setPhoneNumber("999998888777")
        .build();
        student.print();
    }
}
