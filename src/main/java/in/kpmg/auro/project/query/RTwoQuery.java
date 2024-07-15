package in.kpmg.auro.project.query;

public class RTwoQuery {


    public static String studentStrengthOfTheClassroom = "SELECT\n" +
            "\tCASE\n" +
            "\t\tWHEN sed.classroom_strength = 1 THEN 'Below 15 Students'\n" +
            "        \t\tWHEN sed.classroom_strength = 2 THEN '16-25 Students'\n" +
            "        \t\tWHEN sed.classroom_strength = 3 THEN '26-35 Students'\n" +
            "        \t\tWHEN sed.classroom_strength = 4 THEN 'Above 35 Students'\n" +
            "\tEND AS classroom_strength,\n" +
            "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "   \t AVG(ed.score) AS avg_score\n" +
            "FROM\n" +
            "\tstudent_extra_data sed\n" +
            "JOIN\n" +
            "\texam_details ed ON sed.user_id = ed.user_id\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\ted.attempted = 1\n" +
            "\tAND sw.amount_status IN ('2','4','5') \n" +
            "\t-- AND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN '2024-01-01' AND '2024-06-31') -- Select the required date range\n" +
            "\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "\t-- AND sd.district_id = 1 -- Example district filter\n" +
            "\t-- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "\t-- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "\t-- AND sed.cwsn=1 -- Example:It will be yes or no i.e. 1/0\n" +
            "\t-- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "\t-- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "\t-- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "    -- AND ped.child_father_qualification = 1\n" +
            "GROUP BY\n" +
            "\tsed.classroom_strength\n" +
            "ORDER BY\n" +
            "\tFIELD(classroom_strength, 'Below 15 Students','16-25 Students','26-35 Students','Above 35 Students')\n";



    public static String academicStream = "SELECT \n" +
            "    sed.subject_stream AS academic_stream,\n" +
            "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "    AVG(ed.score) AS avg_score\n" +
            "FROM\n" +
            "\tstudent_extra_data sed\n" +
            "JOIN\n" +
            "\texam_details ed ON sed.user_id = ed.user_id\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\ted.attempted = 1\n" +
            "\tAND sw.amount_status IN ('2','4','5')\n" +
            "\t-- AND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN '2024-01-01' AND '2024-06-31') -- Select the required date range\n" +
            "\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "\t-- AND sd.district_id = 1 -- Example district filter\n" +
            "\t-- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "\t-- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "\t-- AND sed.cwsn=1 -- Example:It will be yes or no i.e. 1/0\n" +
            "\t-- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "\t-- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "\t-- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "    -- AND ped.child_father_qualification = 1\n" +
            "GROUP BY \n" +
            "    sed.subject_stream\n" +
            "ORDER BY \n" +
            "    sed.subject_stream;\n";



    public static String socialGroups= "SELECT \n" +
            "    sed.social_group,\n" +
            "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "    AVG(ed.score) AS avg_score\n" +
            "FROM\n" +
            "\tstudent_extra_data sed\n" +
            "JOIN\n" +
            "\texam_details ed ON sed.user_id = ed.user_id\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\ted.attempted = 1\n" +
            "\tAND sw.amount_status IN ('2','4','5')\n" +
            "\t-- AND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN '2024-01-01' AND '2024-06-31') -- Select the required date range\n" +
            "\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "\t-- AND sd.district_id = 1 -- Example district filter\n" +
            "\t-- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "\t-- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "\t-- AND sed.cwsn=1 -- Example:It will be yes or no i.e. 1/0\n" +
            "\t-- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "\t-- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "\t-- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "    -- AND ped.child_father_qualification = 1\n" +
            "GROUP BY \n" +
            "    sed.social_group\n" +
            "ORDER BY \n" +
            "    sed.social_group;\n";


    public static String studentWithAccessToBankAccount = "SELECT \n" +
            "    sed.student_access_to_bank,\n" +
            "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "    AVG(ed.score) AS avg_score\n" +
            "FROM\n" +
            "\tstudent_extra_data sed\n" +
            "JOIN\n" +
            "\texam_details ed ON sed.user_id = ed.user_id\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\ted.attempted = 1\n" +
            "\tAND sw.amount_status IN ('2','4','5')\n" +
            "\t-- AND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN '2024-01-01' AND '2024-06-31') -- Select the required date range\n" +
            "\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "\t-- AND sd.district_id = 1 -- Example district filter\n" +
            "\t-- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "\t-- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "\t-- AND sed.cwsn=1 -- Example:It will be yes or no i.e. 1/0\n" +
            "\t-- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "\t-- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "\t-- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "    -- AND ped.child_father_qualification = 1\n" +
            "GROUP BY \n" +
            "    sed.student_access_to_bank\n" +
            "ORDER BY \n" +
            "    sed.student_access_to_bank;\n";



    public static String studentsEngagementExtracurricularActivitiesInSchool = "SELECT \n" +
            "    sed.extra_curricular_activity,\n" +
            "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "    AVG(ed.score) AS avg_score\n" +
            "FROM\n" +
            "\tstudent_extra_data sed\n" +
            "JOIN\n" +
            "\texam_details ed ON sed.user_id = ed.user_id\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\ted.attempted = 1\n" +
            "\tAND sw.amount_status IN ('2','4','5')\n" +
            "\t-- AND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN '2024-01-01' AND '2024-06-31') -- Select the required date range\n" +
            "\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "\t-- AND sd.district_id = 1 -- Example district filter\n" +
            "\t-- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "\t-- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "\t-- AND sed.cwsn=1 -- Example:It will be yes or no i.e. 1/0\n" +
            "\t-- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "\t-- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "\t-- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "    -- AND ped.child_father_qualification = 1\n" +
            "GROUP BY \n" +
            "    sed.extra_curricular_activity\n" +
            "ORDER BY \n" +
            "    sed.extra_curricular_activity;\n";


    public static String studentsInLeadershipPositionsInSchoolClubsInTheSchool = "SELECT \n" +
            "    sed.is_student_in_leadership_position,\n" +
            "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "    AVG(ed.score) AS avg_score\n" +
            "FROM\n" +
            "\tstudent_extra_data sed\n" +
            "JOIN\n" +
            "\texam_details ed ON sed.user_id = ed.user_id\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\ted.attempted = 1\n" +
            "\tAND sw.amount_status IN ('2','4','5')\n" +
            "\t-- AND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN '2024-01-01' AND '2024-06-31') -- Select the required date range\n" +
            "\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "\t-- AND sd.district_id = 1 -- Example district filter\n" +
            "\t-- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "\t-- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "\t-- AND sed.cwsn=1 -- Example:It will be yes or no i.e. 1/0\n" +
            "\t-- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "\t-- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "\t-- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "    -- AND ped.child_father_qualification = 1\n" +
            "GROUP BY \n" +
            "    sed.is_student_in_leadership_position\n" +
            "ORDER BY \n" +
            "    sed.is_student_in_leadership_position;\n";


}
