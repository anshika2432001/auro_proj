package in.kpmg.auro.project.query;

public class FilterDropdownsQuery {

    public static String grades= "SELECT DISTINCT grade FROM student_master WHERE grade BETWEEN 1 AND 12 ORDER BY grade;";

    public static String subjects = "SELECT DISTINCT subject FROM exam_details ORDER BY subject;";

    public static String schoolLocation ="SELECT DISTINCT school_location, \n" +
            "CASE \n" +
            "   WHEN school_location = 1 THEN 'Rural'\n" +
            "   WHEN school_location = 2 THEN 'Urban'\n" +
            "   ELSE 'Unknown'\n" +
            "END AS location_name\n" +
            "FROM student_extra_data\n" +
            "ORDER BY school_location;";

    public static String socialGroup = "SELECT DISTINCT social_group, \n" +
            "CASE \n" +
            "   WHEN social_group = '1' THEN 'SC'\n" +
            "   WHEN social_group = '2' THEN 'ST'\n" +
            "   WHEN social_group = '3' THEN 'OBC'\n" +
            "   WHEN social_group = '4' THEN 'General'\n" +
            "   WHEN social_group = '5' THEN 'Other'\n" +
            "   ELSE 'Unknown'\n" +
            "END AS social_group_name\n" +
            "FROM student_extra_data\n" +
            "ORDER BY social_group;";

    public static String states = "SELECT state_id, state_name FROM state_master ORDER BY state_id;";

    public static String districts = "SELECT district_id, district_name, state_id FROM state_district_master ORDER BY district_id;";

    public static String genders= "SELECT DISTINCT gender, \n" +
            "    CASE \n" +
            "        WHEN gender = 'M' THEN 'Male'\n" +
            "        WHEN gender = 'F' THEN 'Female'\n" +
            "        WHEN gender = 'O' THEN 'Other'\n" +
            "        WHEN gender = '' THEN 'Prefer Not to say'\n" +
            "        ELSE 'Unknown'\n" +
            "    END AS gender_name\n" +
            "FROM \n" +
            "    student_demographic\n" +
            "WHERE \n" +
            "    gender IS NOT NULL\n" +
            "ORDER BY \n" +
            "    gender;\n";

    public static String ageGroups= "SELECT DISTINCT \n" +
            "    CASE \n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) <= 6 THEN 'Upto 6'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 7 AND 10 THEN '6-10'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 11 AND 13 THEN '11-13'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 14 AND 15 THEN '14-15'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 16 AND 17 THEN '16-17'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) > 17 THEN '>17'\n" +
            "    END AS age_group\n" +
            "FROM \n" +
            "    student_demographic\n" +
            "WHERE \n" +
            "    dob IS NOT NULL\n" +
            "    AND CASE \n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) <= 6 THEN 'Upto 6'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 7 AND 10 THEN '6-10'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 11 AND 13 THEN '11-13'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 14 AND 15 THEN '14-15'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN 16 AND 17 THEN '16-17'\n" +
            "        WHEN TIMESTAMPDIFF(YEAR, dob, CURDATE()) > 17 THEN '>17'\n" +
            "        ELSE NULL\n" +
            "    END IS NOT NULL\n" +
            "ORDER BY \n" +
            "    age_group;\n";


    public static String educationalBoard="SELECT DISTINCT education_board, \n" +
            "CASE \n" +
            "   WHEN education_board = '1' THEN 'CBSE'\n" +
            "   WHEN education_board = '2' THEN 'State Board'\n" +
            "   WHEN education_board = '3' THEN 'ICSE'\n" +
            "   WHEN education_board = '4' THEN 'International Board'\n" +
            "   WHEN education_board = '5' THEN 'Others'\n" +
            "   WHEN education_board = '6' THEN 'Both CBSE and State Board'\n" +
            "   ELSE 'Unknown'\n" +
            "END AS education_board_name\n" +
            "FROM student_demographic\n" +
            "ORDER BY education_board;\n";


    public static String schoolManagement ="SELECT DISTINCT school_management, \n" +
            "CASE \n" +
            "   WHEN school_management = '1' THEN 'Department of Education'\n" +
            "   WHEN school_management = '2' THEN 'Tribal/Social Welfare Department'\n" +
            "   WHEN school_management = '3' THEN 'Local body'\n" +
            "   WHEN school_management = '4' THEN 'Pvt. Aided'\n" +
            "   WHEN school_management = '5' THEN 'Pvt. Unaided'\n" +
            "   WHEN school_management = '6' THEN 'Others'\n" +
            "   WHEN school_management = '7' THEN 'Central Govt.'\n" +
            "   WHEN school_management = '8' THEN 'Unrecognized'\n" +
            "   WHEN school_management = '97' THEN 'Madarsa recognized (by Wakf board/Madarsa Board)'\n" +
            "   WHEN school_management = '98' THEN 'Madarsa unrecognized'\n" +
            "   ELSE 'Unknown'\n" +
            "END AS school_management_name\n" +
            "FROM student_demographic\n" +
            "ORDER BY school_management;";

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

    public static String language = "SELECT language_id, language_name FROM language_master ORDER BY language_id;";

}
