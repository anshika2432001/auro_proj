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


//    public static String childMotherEducation = "SELECT DISTINCT child_mother_qualification FROM parent_extra_data;" ;
//
//    public static String childFatherEducation = "SELECT DISTINCT child_father_qualification FROM parent_extra_data;" ;


    public static String childMotherEducation = "SELECT\n" +
            "\tDISTINCT ped.child_mother_qualification,\n" +
            "\tCASE\n" +
            "\t\tWHEN ped.child_mother_qualification = 1 THEN 'No schooling'\n" +
            "        \tWHEN ped.child_mother_qualification = 2 THEN 'Class I - V'\n" +
            "        \tWHEN ped.child_mother_qualification = 3 THEN 'Class VI - X'\n" +
            "        \tWHEN ped.child_mother_qualification = 4 THEN 'Class XI - XII'\n" +
            "        \tWHEN ped.child_mother_qualification = 5 THEN 'Graduate'\n" +
            "       \t\tWHEN ped.child_mother_qualification = 6 THEN 'Post Graduate'\n" +
            "        \tWHEN ped.child_mother_qualification = 7 THEN 'PhD'\n" +
            "        \tELSE NULL\n" +
            "\t\tEND AS mother_education\n" +
            "FROM\n" +
            "\tparent_extra_data ped;" ;

    public static String childFatherEducation = "SELECT\n" +
            "\tDISTINCT ped.child_father_qualification,\n" +
            "\tCASE\n" +
            "\t\tWHEN ped.child_father_qualification = 1 THEN 'No schooling'\n" +
            "        \tWHEN ped.child_father_qualification = 2 THEN 'Class I - V'\n" +
            "        \tWHEN ped.child_father_qualification = 3 THEN 'Class VI - X'\n" +
            "        \tWHEN ped.child_father_qualification = 4 THEN 'Class XI - XII'\n" +
            "        \tWHEN ped.child_father_qualification = 5 THEN 'Graduate'\n" +
            "        \tWHEN ped.child_father_qualification = 6 THEN 'Post Graduate'\n" +
            "        \tWHEN ped.child_father_qualification = 7 THEN 'PhD'\n" +
            "        \tELSE NULL\n" +
            "\t\tEND AS father_education\n" +
            "FROM\n" +
            "\tparent_extra_data ped;" ;


    public static String language = "SELECT language_id, language_name FROM language_master ORDER BY language_id;";

    public static String cwsn = "select distinct \n" +
            "\tcwsn,\n" +
            "    CASE\n" +
            "\t\tWHEN cwsn = '0' THEN 'No'\n" +
            "        WHEN cwsn = '1' THEN 'Yes'\n" +
            "\tEND AS cwsn_status\n" +
            "from \n" +
            "\tstudent_extra_data;";


    public static String householdIncome = "SELECT\n" +
            "\tDISTINCT ped.household_income,\n" +
            "\tCASE\n" +
            "\t\tWHEN ped.household_income = 1 THEN 'Over INR 30 Lakhs'\n" +
            "        WHEN ped.household_income = 2 THEN 'INR 5 Lakhs - 30 Lakhs'\n" +
            "        WHEN ped.household_income = 3 THEN 'INR 1.25 Lakhs - 5 Lakhs'\n" +
            "        WHEN ped.household_income = 4 THEN 'Less than INR 1.25 Lakhs'\n" +
            "\tEND AS income_household\n" +
            "FROM\n" +
            "\tparent_extra_data ped\n" +
            "WHERE\n" +
            "\tped.household_income IN ('1','2','3','4');";

    public static String genderTeachers= "select distinct \n" +
            "\tCASE\n" +
            "\t\tWhen gender = 'Male' Then 'Male'\n" +
            "\t\tWHEN gender = 'Female' THEN 'Female'\n" +
            "\t\tWHEN gender = 'Others' THEN 'Others'\n" +
            "    END AS gender\n" +
            "from teacher_master\n" +
            "WHERE gender IN ('Male', 'Female', 'Others');";

    public static String qualificationTeachers="select distinct\n" +
            "\tqualification,\n" +
            "\tCASE\n" +
            "\t\twhen qualification = 1 then 'Below Secondary'\n" +
            "        when qualification = 2 then 'Secondary'\n" +
            "        when qualification = 3 then 'Higher Secondary'\n" +
            "        when qualification = 4 then 'Graduate'\n" +
            "        when qualification = 5 then 'Post Graduate'\n" +
            "        when qualification = 6 then 'M.Phil.'\n" +
            "        when qualification = 7 then 'Ph.D.'\n" +
            "        when qualification = 8 then 'Post-Doctoral'\n" +
            "\tEND as qualification_name\n" +
            "FROM\n" +
            "\tteacher_extra_data;";

    public static String modeOfEmploymentTeacher = "select distinct\n" +
            "\temployment_nature,\n" +
            "\tCASE\n" +
            "\t\twhen employment_nature = 1 then 'Regular'\n" +
            "        when employment_nature = 2 then 'Contract'\n" +
            "        when employment_nature = 3 then 'Part-Time/Guest'\n" +
            "\tEND as employment_nature_name\n" +
            "FROM\n" +
            "\tteacher_extra_data;";

    public static String schoolCategory="select\n" +
            "\tdistinct\n" +
            "    school_category,\n" +
            "\t\tCASE\n" +
            "\t\t\tWHEN school_category = 1 THEN 'Primary only with grades 1 to 5 (PRY)'\n" +
            "            WHEN school_category = 2 THEN 'Upper Primary with grades 1 to 8 (PRY-UPR)'\n" +
            "            WHEN school_category = 3 THEN 'Higher Secondary with grades 1 to 12 (PRY-UPR-SEC-HSEC)'\n" +
            "            WHEN school_category = 4 THEN 'Upper Primary only with grades 6 to 8 (UPR)'\n" +
            "            WHEN school_category = 5 THEN 'Higher Secondary with grades 6 to 12 (UPR-SEC-HSEC)'\n" +
            "            WHEN school_category = 6 THEN 'Secondary with grades 1 to 10 (PRY-UPR-SEC)'\n" +
            "            WHEN school_category = 7 THEN 'Secondary with grades 6 to 10 (UPR-SEC)'\n" +
            "            WHEN school_category = 8 THEN 'Secondary only with grades 9 & 10 (SEC)'\n" +
            "            WHEN school_category = 10 THEN 'Higher Secondary with grades 9 to 12 (SEC-HSEC)'\n" +
            "            WHEN school_category = 11 THEN 'Hr. Sec. /Jr. College only with grades 11 & 12 (HSEC)'\n" +
            "            WHEN school_category = 12 THEN 'Pre-Primary Only (PRE)'\n" +
            "\t\tEND AS school_category\n" +
            "FROM\n" +
            "\tstudent_extra_data\n" +
            "order by school_category;\n";

    public static String schoolType= "select\n" +
            "\tdistinct\n" +
            "    school_type,\n" +
            "\t\tCASE\n" +
            "\t\t\tWHEN school_type = 1 THEN 'Girls'\n" +
            "            WHEN school_type = 2 THEN 'Boys'\n" +
            "            WHEN school_type = 3 THEN 'Co-ed'\n" +
            "\t\tEND AS school_type\n" +
            "FROM\n" +
            "\tstudent_extra_data;\n";
}
