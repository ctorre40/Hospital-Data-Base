package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Main {
    private Connection connection = null;
    private static Main instance = null;
    String url = "jdbc:mysql://localhost:3306/hospital_system"; // database string url
    String username = "root"; // your database user
    String password = ""; // your database password

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        // write your code here

        Main db = Main.getInstance();

        db.readPersonFile("C:\\Users\\ctreb\\IdeaProjects\\Checkpoint3\\src\\com\\company\\person.txt");
        db.readTreatmentFile("C:\\Users\\ctreb\\IdeaProjects\\Checkpoint3\\src\\com\\company\\treatment.txt");
        db.readAdditionalDoctorFile("C:\\Users\\ctreb\\IdeaProjects\\Checkpoint3\\src\\com\\company\\additionaldoctor.txt");

    }

    private void readPersonFile(String fileURL) throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileURL));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arrayPerson = line.split(",");
            if (arrayPerson[0].equals("P")) {
                insertPatient(line);
            } else {
                insertEmployee(line);
            }
        }
    }

    private void readTreatmentFile(String fileURL) throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileURL));
        String line;
        while((line = bufferedReader.readLine())!= null){
            insertTreatment(line);
        }
    }

    private void readAdditionalDoctorFile(String fileURL) throws IOException, SQLException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileURL));
        String line;
        while((line = bufferedReader.readLine()) != null){
            insertAdditionalDoctor(line);
        }
    }


    private void insertPatient(String line) throws SQLException {
        String[] arrayPatient = line.split(",");
        String[] arrayInpatient = line.split(",");
        Boolean flag1 = checkIfPatientExists(arrayPatient[2]);


        //last_name is unique for everyone
        if(flag1){
            System.out.println("Patient is already in the system. Patient will now be admitted.");
            String category = arrayInpatient[0];
            String first_name = arrayInpatient[1];
            String last_name = arrayInpatient[2];
            String room_number = arrayInpatient[3];
            String emergency_contact = arrayInpatient[4];
            String emergency_contact_number = arrayInpatient[5];
            String policy_number = arrayInpatient[6];
            String policy_company = arrayInpatient[7];
            String primary_doctor = arrayInpatient[8];
            String initial_diagnosis = arrayInpatient[9];
            String arrival_date = arrayInpatient[10];
            String discharge_date = arrayInpatient[11];
            String emp_fk_ID = arrayInpatient[12];
            String treatment_ID = arrayInpatient[13];


            String insertInpatientQuery = "INSERT INTO inpatient VALUES(default,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insertInpatientQuery);
            ps.setString(1, category);
            ps.setString(2, first_name);
            ps.setString(3, last_name);
            ps.setString(4, room_number);
            ps.setString(5, emergency_contact);
            ps.setString(6, emergency_contact_number);
            ps.setString(7, policy_number);
            ps.setString(8, policy_company);
            ps.setString(9, primary_doctor);
            ps.setString(10, initial_diagnosis);
            ps.setString(11, arrival_date);
            ps.setString(12, discharge_date);
            ps.setString(13, emp_fk_ID);
            ps.setString(14, treatment_ID);
            ps.executeUpdate();
            ps.close();

        }
        else {

            System.out.println("This patient is new.");
            String category = arrayPatient[0];
            String first_name = arrayPatient[1];
            String last_name = arrayPatient[2];
            String room_number = arrayPatient[3];
            String emergency_contact = arrayPatient[4];
            String emergency_contact_number = arrayPatient[5];
            String policy_number = arrayPatient[6];
            String policy_company = arrayPatient[7];
            String primary_doctor_last = arrayPatient[8];
            String initial_diagnosis = arrayPatient[9];
            String arrival_date = arrayPatient[10];
            String discharge_date = arrayPatient[11];
            String fKey_employee_ID = arrayPatient[12];
            String treat_ID = arrayPatient[13];

            String insertPatientQuery = "INSERT INTO patient VALUES(default,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps1 = connection.prepareStatement(insertPatientQuery);
            ps1.setString(1, category);
            ps1.setString(2, first_name);
            ps1.setString(3, last_name);
            ps1.setString(4, room_number);
            ps1.setString(5, emergency_contact);
            ps1.setString(6, emergency_contact_number);
            ps1.setString(7, policy_number);
            ps1.setString(8, policy_company);
            ps1.setString(9, primary_doctor_last);
            ps1.setString(10, initial_diagnosis);
            ps1.setString(11, arrival_date);
            ps1.setString(12, discharge_date);
            ps1.setString(13, fKey_employee_ID);
            ps1.setString(14, treat_ID);
            ps1.executeUpdate();
            ps1.close();
        }

    }

    private boolean checkIfPatientExists(String last_name) throws SQLException {
        boolean flag1 = false;
        String checkPatient = "SELECT * FROM patient WHERE last_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(checkPatient);
        preparedStatement.setString(1, last_name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            flag1 = true;
        }
        return flag1;
    }

    private void insertEmployee(String line) throws SQLException {
        String[] arrayEmployee = line.split(",");

        // check if employee already exists in the database
        Boolean flag = checkIfEmployeeExists(arrayEmployee[2]);
        if (flag) {
            System.out.println("Employee already exists. Please enter a new value.");
        }
        else {

            String category = switch(arrayEmployee[0]) {
                case "D" -> "doctor";
                case "A" -> "administration";
                case "N" -> "nurse";
                case "T" -> "technician";
                default -> "";
            };

            String first_name = arrayEmployee[1];
            String last_name = arrayEmployee[2];


            String employeeInsertQuery = "INSERT INTO employee VALUES(default, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(employeeInsertQuery);
            preparedStatement.setString(1, category);
            preparedStatement.setString(2, first_name);
            preparedStatement.setString(3, last_name);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Employee has been successfully added");
        }
    }

    private Boolean checkIfEmployeeExists(String last_name) throws SQLException {
        boolean flag = false;
        String checkEmployee = "SELECT * FROM employee WHERE last_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(checkEmployee);
        preparedStatement.setString(1, last_name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            flag = true;
        }
        return flag;
    }

    private void insertTreatment(String line) throws SQLException {
        String[] arrayTreatment = line.split(",");
        String category = switch(arrayTreatment[2]){
            case "P" -> "procedure";
            case "M" -> "medicine";
            default -> "";
        };
        String patient_name = arrayTreatment[0];
        String employee_name = arrayTreatment[1];
        String treatment_name = arrayTreatment[3];
        String treatment_time = arrayTreatment[4];
        String employee_ID_forKey = arrayTreatment[5];


        String treatmentInsertQuery = "INSERT INTO treatment VALUES(default,?,?,?,?,?,?)";
        PreparedStatement ps2 = connection.prepareStatement(treatmentInsertQuery);
        ps2.setString(1, patient_name);
        ps2.setString(2, employee_name);
        ps2.setString(3, category);
        ps2.setString(4, treatment_name);
        ps2.setString(5, treatment_time);
        ps2.setString(6, employee_ID_forKey);
        ps2.executeUpdate();
        ps2.close();
        System.out.println("Treatment has been added!");

    }

    public void insertAdditionalDoctor(String line) throws SQLException {
        String[] additionalDoctorArray = line.split(",");

        String patient_last = additionalDoctorArray[0];
        String doctor_last = additionalDoctorArray[1];

        String additionalDoctorInsertQuery = "INSERT INTO additionalDoctor VALUES(?,?)";

        PreparedStatement ps3 = connection.prepareStatement(additionalDoctorInsertQuery);
        ps3.setString(1, patient_last);
        ps3.setString(2, doctor_last);
        ps3.executeUpdate();
        ps3.close();

    }

    public Main() throws ClassNotFoundException, SQLException {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
            }
    private static Main getInstance () throws SQLException, ClassNotFoundException {
                if (instance == null) {
                    instance = new Main();
                }
                return instance;
            }
}

