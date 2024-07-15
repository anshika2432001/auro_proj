package in.kpmg.auro.project.query;

public class RThreeQuery {


    public static String studentLearningStylePreferences= "SELECT \n" +
            "\tsed.learning_preference AS learning_style, \n" +
            "    COUNT(DISTINCT ed.user_id) AS num_students, \n" +
            "    AVG(ed.score) as avg_score\n" +
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
            "\tsed.learning_preference \n" +
            "ORDER BY \n" +
            "\tsed.learning_preference;\n";


    public static String studentPreferencesOnCollaborativeLearningStyle = "SELECT\n" +
            "\tsed.collaborative_learning_style AS learning_approach,\n" +
            "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "\tAVG(ed.score) AS avg_score\n" +
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
            "GROUP BY\n" +
            "\tsed.collaborative_learning_style\n" +
            "ORDER BY\n" +
            "\tsed.collaborative_learning_style;\n";


    public static String childrenWhoReadOtherMaterialsInAdditionToTextbooks = "SELECT\n" +
            "\tsed.extra_learning_materials AS extra_education,\n" +
            "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "\tAVG(ed.score) AS avg_score\n" +
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
            "GROUP BY\n" +
            "\tsed.extra_learning_materials\n" +
            "ORDER BY\n" +
            "\tsed.extra_learning_materials;\n";


    public static String hoursOfIndividualStudyPracticePerDay = "SELECT\n" +
            "   sed.daily_study_hours AS practice_per_day,\n" +
            "   COUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "\tAVG(ed.score) AS avg_score\n" +
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
            "GROUP BY\n" +
            "\tsed.daily_study_hours\n" +
            "ORDER BY\n" +
            "\tsed.daily_study_hours\n";


    public static String paidPrivateTuitionHours = "SELECT\n" +
            "\tsed.private_tuition_hours AS tuition_time,\n" +
            "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "\tAVG(ed.score) AS avg_score\n" +
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
            "GROUP BY\n" +
            "\tsed.private_tuition_hours\n" +
            "ORDER BY\n" +
            "\tsed.private_tuition_hours;\n";



    public static String studentHoursSpentOnMobilePhonesStudy = "SELECT\n" +
            "\tsed.time_spent_on_mobile_study AS mobile_hours_study,\n" +
            "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "\tAVG(ed.score) AS avg_score\n" +
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
            "GROUP BY\n" +
            "\tsed.time_spent_on_mobile_study\n" +
            "ORDER BY\n" +
            "\tsed.time_spent_on_mobile_study;\n";

    public static String childrenHavingAccessToDigitalDevicesAtHome ="SELECT\n" +
            "\tsed.access_to_mobile_at_home AS mobile_access,\n" +
            "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "\tAVG(ed.score) AS avg_score\n" +
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
            "GROUP BY\n" +
            "\tsed.access_to_mobile_at_home\n" +
            "ORDER BY\n" +
            "\tsed.access_to_mobile_at_home;\n";


    public static String studentsUsingLearningAppsAtHome = "SELECT\n" +
            "\tsed.learning_apps_at_home AS remote_learning,\n" +
            "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "\tAVG(ed.score) AS avg_score\n" +
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
            "GROUP BY\n" +
            "\tsed.learning_apps_at_home\n" +
            "ORDER BY\n" +
            "\tsed.learning_apps_at_home;\n";

}