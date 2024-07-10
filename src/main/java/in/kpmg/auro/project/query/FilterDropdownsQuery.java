package in.kpmg.auro.project.query;

public class FilterDropdownsQuery {

//    -- Distinct Grades
    public static String grades= "SELECT DISTINCT grade FROM student_master;";

//-- Distinct Subjects
    public static String subjects = "SELECT DISTINCT subject FROM exam_details;";

//-- Distinct School Locations
    public static String schoolLocation ="SELECT DISTINCT \n" +
        "    CASE \n" +
        "        WHEN school_location = 1 THEN 'Rural'\n" +
        "        WHEN school_location = 2 THEN 'Urban'\n" +
        "    END AS school_location\n" +
        "FROM student_extra_data;\n";

    public static String socialGroup = "SELECT DISTINCT \n" +
            "    CASE \n" +
            "        WHEN social_group = 1 THEN 'SC'\n" +
            "        WHEN social_group = 2 THEN 'ST'\n" +
            "        WHEN social_group = 3 THEN 'OBC'\n" +
            "        WHEN social_group = 4 THEN 'General'\n" +
            "        WHEN social_group = 5 THEN 'Other'\n" +
            "    END AS social_group\n" +
            "FROM student_extra_data;\n";

    public static String states = "SELECT DISTINCT state_id,state_name FROM state_master;";

    public static String districts = "SELECT DISTINCT district_id ,district_name FROM state_district_master;";

    public static String genders= "SELECT DISTINCT gender FROM student_demographic;";

    public static String ageGroups= "SELECT DISTINCT \n" +
            "    CASE \n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) <= 6 THEN '0-6'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 7 AND 10 THEN '7-10'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 11 AND 13 THEN '11-13'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 14 AND 15 THEN '14-15'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 16 AND 17 THEN '16-17'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) > 17 THEN '>17'\n" +
            "    END AS age_group\n" +
            "FROM student_demographic;\n";


    public static String educationalBoard="SELECT DISTINCT education_board FROM student_demographic;";


    public static String schoolManagement ="SELECT DISTINCT school_management FROM student_demographic;";

    public static String transactionDate = "SELECT DISTINCT DATE(transaction_date) AS transaction_date FROM student_wallet;";

    public static String amountStatus = "SELECT DISTINCT \n" +
            "    CASE \n" +
            "        WHEN amount_status = '2' THEN 'Status 2'\n" +
            "        WHEN amount_status = '4' THEN 'Status 4'\n" +
            "        WHEN amount_status = '5' THEN 'Status 5'\n" +
            "    END AS amount_status\n" +
            "FROM student_wallet;\n";

    public static String topics= "SELECT DISTINCT quiz_name FROM auro_quiz_name;";

    public static String classroomStrength = "SELECT DISTINCT \n" +
            "    CASE \n" +
            "        WHEN classroom_strength = 1 THEN 'Below 15 students'\n" +
            "        WHEN classroom_strength = 2 THEN '16-25 students'\n" +
            "        WHEN classroom_strength = 3 THEN '26-35 students'\n" +
            "        WHEN classroom_strength = 4 THEN 'Above 35 students'\n" +
            "    END AS classroom_strength\n" +
            "FROM student_extra_data;\n";


    public static String childMotherEducation = "SELECT DISTINCT child_mother_qualification FROM parent_extra_data;" ;

    public static String childFatherEducation = "SELECT DISTINCT child_father_qualification FROM parent_extra_data;" ;


}
