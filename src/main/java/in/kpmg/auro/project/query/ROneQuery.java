package in.kpmg.auro.project.query;

public class ROneQuery {

    public static String microScholorshipQuizAverageScore = "SELECT\n" +
            "\tquiz_count AS quiz_range,\n" +
            "\tCOUNT(DISTINCT user_id) AS num_students,\n" +
            "\tAVG(score) AS average_score\n" +
            "FROM(\n" +
            "\tSELECT\n" +
            "\t\ted.user_id,\n" +
            "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
            "\t\tAVG(ed.score) AS score\n" +
            "\tFROM\n" +
            "\t\texam_details ed\n" +
            "\tJOIN\n" +
            "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "\tLEFT JOIN\n" +
            "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "\tLEFT JOIN\n" +
            "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "\tJOIN\n" +
            "\t\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "\tWHERE\n" +
            "\t\ted.attempted = 1\n" +
            "\t\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\tAND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\tAND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\tAND ed.subject = ? -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\tAND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "        \tAND sd.state_id = 1 -- Example state filter\n" +
            "        \tAND sd.district_id = 1 -- Example district filter\n" +
            "        \tAND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        \tAND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        \tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        \tAND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        \tAND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "\tAND sed.cwsn = 1\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "    \t-- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "\tGROUP BY\n" +
            "\t\ted.user_id\n" +
            ") AS exam_stats\n" +
            "GROUP BY quiz_count\n" +
            "ORDER BY quiz_count;\n";



    public static String subjectWiseBreakdownAverageScore = "SELECT\n" +
            "\ted.subject,\n" +
            "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
            "\tAVG(score) AS average_score\n" +
            "FROM\n" +
            "\texam_details ed\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN(\n" +
            "\tSELECT\n" +
            "\t\tuser_id,\n" +
            "        AVG(amount) as avg_scholarship\n" +
            "\tFROM\n" +
            "\t\tstudent_wallet\n" +
            "\tWHERE\n" +
            "\t\tamount_status IN ('2','4','5')\n" +
            "\tGROUP BY\n" +
            "\t\tuser_id\n" +
            ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\t\ted.attempted = 1\n" +
            "\t\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\tAND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\tAND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\tAND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\tAND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\tAND sd.state_id = 1 -- Example state filter\n" +
            "        AND sd.district_id = 1 -- Example district filter\n" +
            "        AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "AND sed.cwsn = 1\n" +
            "AND ped.child_mother_qualification = 1\n" +
            "    \t-- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "GROUP BY ed.subject\n" +
            "ORDER BY ed.subject;\n";

    public static String gradeWiseAverageScore= "SELECT\n" +
            "\tsm.grade,\n" +
            "\tAVG(ed.score) AS average_score,\n" +
            "    COUNT(DISTINCT ed.user_id) AS num_students_attempting\n" +
            "FROM\n" +
            "\texam_details ed\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN(\n" +
            "\tSELECT\n" +
            "\t\tuser_id,\n" +
            "        AVG(amount) as avg_scholarship\n" +
            "\tFROM\n" +
            "\t\tstudent_wallet\n" +
            "\tWHERE\n" +
            "\t\tamount_status IN ('2','4','5')\n" +
            "\tGROUP BY\n" +
            "\t\tuser_id\n" +
            ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\t\ted.attempted = 1\n" +
            "\t\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\tAND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "        -- AND sed.cwsn = 1\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "   \t-- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "GROUP BY sm.grade\n" +
            "ORDER BY sm.grade;\n";


    public static String topicWiseBreakDownAvgScore= "SELECT\n" +
            "\tsubquery.quiz_name AS topic_name,\n" +
            "    COUNT(DISTINCT subquery.user_id) AS num_students,\n" +
            "    AVG(CASE WHEN subquery.state_id = 1 THEN subquery.score ELSE NULL END) AS avg_score_region,\n" +
            "    AVG(subquery.score) AS avg_score_pan_india\n" +
            "    \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\ted.user_id,\n" +
            "        ed.score,\n" +
            "        ed.exam_name,\n" +
            "        ed.subject,\n" +
            "        ed.eklavvya_exam_id,\n" +
            "        ed.attempted,\n" +
            "        sm.grade,\n" +
            "        aqn.quiz_name,\n" +
            "        sw.amount_status,\n" +
            "        sw.transaction_date,\n" +
            "        sd.state_id,\n" +
            "        sd.district_id,\n" +
            "        sd.gender,\n" +
            "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
            "        sd.education_board,\n" +
            "        sd.school_management,\n" +
            "        sed.school_location,\n" +
            "        sed.social_group,\n" +
            "        aqn.language_id,\n" +
            "        lm.language_name\n" +
            "\tFROM\n" +
            "\t\texam_details ed\n" +
            "\tJOIN\n" +
            "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "\tJOIN\n" +
            "\t\tauro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt\n" +
            "\t\tAND sm.grade = aqn. student_class\n" +
            "        AND ed.subject = aqn.subject\n" +
            "\tJOIN\n" +
            "\t\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "\tJOIN(\n" +
            "\tSELECT\n" +
            "\t\tuser_id,\n" +
            "        AVG(amount) as avg_scholarship\n" +
            "\tFROM\n" +
            "\t\tstudent_wallet\n" +
            "\tWHERE\n" +
            "\t\tamount_status IN ('2','4','5')\n" +
            "\tGROUP BY\n" +
            "\t\tuser_id\n" +
            ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "\tWHERE\n" +
            "\t\ted.attempted = 1\n" +
            "        AND sw.amount_status IN ('2','4','5')\n" +
            "        AND aqn.subject = 'Maths'\n" +
            "        AND sm.grade = 5\n" +
            "        AND lm.language_id = 1\n" +
            "\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\n" +
            "        -- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "        -- AND sed.cwsn = 1\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "   \t-- AND ped.child_father_qualification = 1\n" +
            "\t\n" +
            "\tLIMIT 10000) AS subquery\n" +
            "GROUP BY\n" +
            "\tsubquery.quiz_name\n" +
            "ORDER BY\n" +
            "\tsubquery.quiz_name;\n";

    public static String totalQuizAttempted = "SELECT\n" +
            "\tquiz_range,\n" +
            "    COUNT(DISTINCT user_id) AS num_students,\n" +
            "    AVG(score) AS national_avg_score,\n" +
            "    AVG(CASE WHEN state_id = 1 THEN SCORE ELSE NULL END) AS regional_avg_score\n" +
            "FROM (\n" +
            "\tSELECT\n" +
            "\t\ted.user_id,\n" +
            "        COUNT(ed.eklavvya_exam_id) AS quiz_range,\n" +
            "        AVG(ed.score) AS score,\n" +
            "        sd.state_id\n" +
            "\tFROM\n" +
            "\t\texam_details ed\n" +
            "\tJOIN\n" +
            "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "\tLEFT JOIN\n" +
            "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "\tLEFT JOIN\n" +
            "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN(\n" +
            "\tSELECT\n" +
            "\t\tuser_id,\n" +
            "        AVG(amount) as avg_scholarship\n" +
            "\tFROM\n" +
            "\t\tstudent_wallet\n" +
            "\tWHERE\n" +
            "\t\tamount_status IN ('2','4','5')\n" +
            "\tGROUP BY\n" +
            "\t\tuser_id\n" +
            ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "\tJOIN\n" +
            "\t\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "\tWHERE\n" +
            "\t\ted.attempted = 1\n" +
            "\t\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\tAND ed.subject = 'Maths' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "-- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "-- AND sed.cwsn = 1\n" +
            "AND ped.child_mother_qualification = 1\n" +
            "    \t-- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "\tGROUP BY\n" +
            "\t\ted.user_id\n" +
            ") sub\n" +
            "GROUP BY\n" +
            "\tquiz_range\n" +
            "ORDER BY\n" +
            "\tquiz_range;\n";

    public static String topicWiseBreakdownStudentAttempts = "SELECT\n" +
            "\taqn.quiz_name as topic_name,\n" +
            "    count(distinct case when ed.quiz_attempt = 1 THEN ed.user_id ELSE NULL END) as num_students_attempt_1,\n" +
            "    count(distinct case when ed.quiz_attempt = 2 THEN ed.user_id ELSE NULL END) as num_students_attempt_2,\n" +
            "    count(distinct case when ed.quiz_attempt = 3 THEN ed.user_id ELSE NULL END) as num_students_attempt_3,\n" +
            "    avg(case when ed.quiz_attempt = 1 AND sd.state_id = 1 THEN ed.score ELSE NULL END) AS avg_score_attempt_1_region,\n" +
            "    avg(case when ed.quiz_attempt = 2 AND sd.state_id = 1 THEN ed.score ELSE NULL END) AS avg_score_attempt_2_region,\n" +
            "    avg(case when ed.quiz_attempt = 3 AND sd.state_id = 1 THEN ed.score ELSE NULL END) AS avg_score_attempt_3_region,\n" +
            "    avg(case when ed.quiz_attempt = 1 THEN ed.score ELSE NULL END) as average_score_attempt_1_india,\n" +
            "    avg(case when ed.quiz_attempt = 2 THEN ed.score ELSE NULL END) as average_score_attempt_2_india,\n" +
            "    avg(case when ed.quiz_attempt = 3 THEN ed.score ELSE NULL END) as average_score_attempt_3_india\n" +
            "FROM\n" +
            "\texam_details ed\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tauro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt\n" +
            "    AND sm.grade = aqn.student_class\n" +
            "    AND ed.subject = aqn.subject\n" +
            "JOIN\n" +
            "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
            "JOIN\n" +
            "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN(\n" +
            "\tSELECT\n" +
            "\t\tuser_id,\n" +
            "        AVG(amount) as avg_scholarship\n" +
            "\tFROM\n" +
            "\t\tstudent_wallet\n" +
            "\tWHERE\n" +
            "\t\tamount_status IN ('2','4','5')\n" +
            "\tGROUP BY\n" +
            "\t\tuser_id\n" +
            ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\t\ted.attempted = 1\n" +
            "\t\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\tAND lm.language_id = 1\n" +
            "\t\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\tAND ed.subject = 'Maths' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\tAND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "        -- AND aqn.quiz_name = 'Numbers'\n" +
            "\t-- AND sed.cwsn = 1\n" +
            "AND ped.child_mother_qualification = 1\n" +
            "    \t-- AND ped.child_father_qualification = 1\n" +
            "GROUP BY\n" +
            "\taqn.quiz_name\n" +
            "ORDER BY\n" +
            "\taqn.quiz_name;\n";


    public static String topicWiseBreakdownNoOfMicroscholarshipQuizzes="SELECT\n" +
            "\taqn.quiz_name AS topic_name,\n" +
            "\tCOUNT(DISTINCT subquery.user_id) AS num_students,\n" +
            "\tAVG(CASE WHEN subquery.state_id = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_state,\n" +
            "\tAVG(subquery.avg_score) as avg_score_nation\n" +
            "FROM (\n" +
            "\tSELECT\n" +
            "\t\ted.user_id,\n" +
            "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
            "\t\tAVG(ed.score) AS avg_score,\n" +
            "\t\tsd.state_id,\n" +
            "        ed.exam_name,\n" +
            "        ed.subject,\n" +
            "        sm.grade\n" +
            "\tFROM \n" +
            "\t\texam_details ed\n" +
            "\tJOIN\n" +
            "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "\tJOIN \n" +
            "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "\tJOIN\n" +
            "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "\tLEFT JOIN\n" +
            "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "\tLEFT JOIN\n" +
            "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "\tJOIN \n" +
            "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "\tJOIN(\n" +
            "\t\tSELECT\n" +
            "\t\t\tuser_id,\n" +
            "        \tAVG(amount) as avg_scholarship\n" +
            "\t\tFROM\n" +
            "\t\t\tstudent_wallet\n" +
            "\t\tWHERE\n" +
            "\t\t\tamount_status IN ('2','4','5')\n" +
            "\t\tGROUP BY\n" +
            "\t\t\tuser_id\n" +
            "\t) avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "JOIN\n" +
            "\t\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "\n" +
            "\tWHERE\n" +
            "\t\t\ted.attempted = 1\n" +
            "\t\t\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\t\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\t\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\t\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\t\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\t\tAND sd.state_id = 1 -- Example state filter\n" +
            "        \t\t-- AND sd.district_id = 1 -- Example district filter\n" +
            "        \t\t-- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        \t\t-- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        \t\t-- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "       \t\t \t-- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        \t\t-- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        \t\t-- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "\t\t-- AND sed.cwsn = 1\n" +
            "\t\tAND ped.child_mother_qualification = 1\n" +
            "    \t\t-- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "    \tGROUP BY\n" +
            "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
            "\t\tLIMIT 10000\n" +
            ") AS subquery\n" +
            "JOIN\n" +
            "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
            "\tAND subquery.grade = aqn.student_class\n" +
            "\tAND subquery.subject = aqn.subject\n" +
            "JOIN\n" +
            "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
            "WHERE\n" +
            "\tlm.language_id = 1\n" +
            "GROUP BY\n" +
            "\taqn.quiz_name\n" +
            "ORDER BY\n" +
            "\taqn.quiz_name;\n";

    public static String topPerformingTopics = "SELECT\n" +
            "\taqn.quiz_name as topic_name,\n" +
            "    count(distinct subquery.user_id) AS num_students_nation,\n" +
            "    AVG(CASE WHEN subquery.state_id = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_region,\n" +
            "    AVG(subquery.avg_score) AS avg_score_nation\n" +
            "FROM (\n" +
            "\tSELECT\n" +
            "\t\ted.user_id,\n" +
            "        COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
            "        AVG(ed.score) AS avg_score,\n" +
            "        sd.state_id,\n" +
            "        ed.exam_name,\n" +
            "        ed.subject,\n" +
            "        sm.grade\n" +
            "\tFROM exam_details ed\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN(\n" +
            "\tSELECT\n" +
            "\t\tuser_id,\n" +
            "        AVG(amount) as avg_scholarship\n" +
            "\tFROM\n" +
            "\t\tstudent_wallet\n" +
            "\tWHERE\n" +
            "\t\tamount_status IN ('2','4','5')\n" +
            "\tGROUP BY\n" +
            "\t\tuser_id\n" +
            ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "\tWHERE\n" +
            "\t\t-- ed.attempted = 1\n" +
            "\t\t-- sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\t-- sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "        -- AND aqn.quiz_name = 'Numbers' -- Select the topic name as in the db\n" +
            "        -- AND sed.cwsn = 1\n" +
            "AND ped.child_mother_qualification = 1\n" +
            "    \t-- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "\tGROUP BY\n" +
            "\t\ted.user_id, sd.state_id, ed.exam_name, ed.subject, sm.grade\n" +
            "\tLIMIT 10000\n" +
            ") AS subquery\n" +
            "JOIN\n" +
            "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
            "    AND subquery.grade = aqn.student_class\n" +
            "    AND subquery.subject = aqn.subject\n" +
            "JOIN\n" +
            "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
            "WHERE\n" +
            "\tlm.language_id = 1\n" +
            "GROUP BY\n" +
            "\taqn.quiz_name\n" +
            "ORDER BY\n" +
            "\tavg_score_nation DESC\n" +
            "LIMIT 12;\n";

    public static String weakPerformingTopics="SELECT\n" +
            "\taqn.quiz_name as topic_name,\n" +
            "    count(distinct subquery.user_id) AS num_students_nation,\n" +
            "    AVG(CASE WHEN subquery.state_id = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_region,\n" +
            "    AVG(subquery.avg_score) AS avg_score_nation\n" +
            "FROM (\n" +
            "\tSELECT\n" +
            "\t\ted.user_id,\n" +
            "        COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
            "        AVG(ed.score) AS avg_score,\n" +
            "        sd.state_id,\n" +
            "        ed.exam_name,\n" +
            "        ed.subject,\n" +
            "        sm.grade\n" +
            "\tFROM exam_details ed\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN(\n" +
            "\tSELECT\n" +
            "\t\tuser_id,\n" +
            "        AVG(amount) as avg_scholarship\n" +
            "\tFROM\n" +
            "\t\tstudent_wallet\n" +
            "\tWHERE\n" +
            "\t\tamount_status IN ('2','4','5')\n" +
            "\tGROUP BY\n" +
            "\t\tuser_id\n" +
            ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "\t WHERE\n" +
            "\t\t-- ed.attempted = 1\n" +
            "\t\t-- sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\t-- sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "        -- AND aqn.quiz_name = 'Numbers' -- Select the topic name as in the db\n" +
            "        -- AND sed.cwsn = 1\n" +
            "AND ped.child_mother_qualification = 1\n" +
            "    \t-- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "\tGROUP BY\n" +
            "\t\ted.user_id, sd.state_id, ed.exam_name, ed.subject, sm.grade\n" +
            "\tLIMIT 10000\n" +
            ") AS subquery\n" +
            "JOIN\n" +
            "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
            "    AND subquery.grade = aqn.student_class\n" +
            "    AND subquery.subject = aqn.subject\n" +
            "JOIN\n" +
            "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
            "WHERE\n" +
            "\tlm.language_id = 1\n" +
            "GROUP BY\n" +
            "\taqn.quiz_name\n" +
            "ORDER BY\n" +
            "avg_score_nation\t ASC\n" +
            "LIMIT 12;\n";


    public static String coreRetakePractice = "SELECT\n" +
            "\tquiz_attempt,\n" +
            "    COUNT(DISTINCT CASE WHEN sd.state_id = 1 THEN ed.user_id ELSE NULL END) AS num_students_region,\n" +
            "    COUNT(DISTINCT ed.user_id) AS num_students_pan_india,\n" +
            "    AVG(CASE WHEN sd.state_id = 1 THEN ed.score ELSE NULL END) AS avg_score_region,\n" +
            "    AVG(ed.score) AS avg_score_pan_india\n" +
            "    \n" +
            "FROM\n" +
            "\texam_details ed\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN(\n" +
            "\tSELECT\n" +
            "\t\tuser_id,\n" +
            "        AVG(amount) as avg_scholarship\n" +
            "\tFROM\n" +
            "\t\tstudent_wallet\n" +
            "\tWHERE\n" +
            "\t\tamount_status IN ('2','4','5')\n" +
            "\tGROUP BY\n" +
            "\t\tuser_id\n" +
            ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\t\ted.attempted = 1\n" +
            "\t\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "        -- AND sed.cwsn = 1\n" +
            "AND ped.child_mother_qualification = 1\n" +
            "    -- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "GROUP BY quiz_attempt\n" +
            "ORDER BY quiz_attempt;\n";


    public static String coreRetakeImprovement = "SELECT\n" +
            "\ted.quiz_attempt,\n" +
            "    AVG(CASE\n" +
            "\t\tWHEN ed.quiz_attempt IN (2,3) AND sd.state_id = 1 THEN (ed.score - ed_prev.score) / ed_prev.score * 100\n" +
            "        ELSE NULL\n" +
            "\tEND) AS avg_improvement_region,\n" +
            "\tAVG(CASE\n" +
            "\t\tWHEN ed.quiz_attempt IN (2,3) THEN (ed.score - ed_prev.score) / ed_prev.score * 100\n" +
            "        ELSE NULL\n" +
            "\tEND) AS avg_improvement_nation\n" +
            "FROM\n" +
            "\texam_details ed\n" +
            "JOIN\n" +
            "\texam_details ed_prev ON ed.user_id = ed_prev.user_id AND ed.quiz_attempt = ed_prev.quiz_attempt+1\n" +
            "JOIN\n" +
            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "JOIN\n" +
            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
            "JOIN\n" +
            "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
            "JOIN\n" +
            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
            "LEFT JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id\n" +
            "LEFT JOIN\n" +
            "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
            "JOIN(\n" +
            "\tSELECT\n" +
            "\t\tuser_id,\n" +
            "        AVG(amount) as avg_scholarship\n" +
            "\tFROM\n" +
            "\t\tstudent_wallet\n" +
            "\tWHERE\n" +
            "\t\tamount_status IN ('2','4','5')\n" +
            "\tGROUP BY\n" +
            "\t\tuser_id\n" +
            ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
            "JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "WHERE\n" +
            "\t\ted.attempted = 1\n" +
            "\t\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "\t-- AND sed.cwsn = 1\n" +
            "AND ped.child_mother_qualification = 1\n" +
            "    \t-- AND ped.child_father_qualification = 1\n" +
            "\t\n" +
            "GROUP BY quiz_attempt\n" +
            "ORDER BY quiz_attempt;\n";

    public static String subjectWiseBreakdownImprovement = "SELECT \n" +
            "    subquery.subject,\n" +
            "    COUNT(DISTINCT CASE WHEN subquery.state_id = 1 THEN subquery.user_id ELSE NULL END) AS num_students_state,\n" +
            "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
            "    (AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
            "    (AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state,\n" +
            "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_nation,\n" +
            "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_nation\n" +
            "FROM (\n" +
            "    SELECT \n" +
            "        ed.user_id,\n" +
            "        ed.subject,\n" +
            "        ed.score,\n" +
            "        ed.quiz_attempt,\n" +
            "        sw.amount_status,\n" +
            "        sm.grade,\n" +
            "        sed.school_location,\n" +
            "        sd.state_id,\n" +
            "        sd.district_id,\n" +
            "        sd.gender,\n" +
            "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
            "        sd.education_board,\n" +
            "        sd.school_management,\n" +
            "        sw.transaction_date\n" +
            "    FROM \n" +
            "        exam_details ed\n" +
            "    JOIN \n" +
            "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "    JOIN\n" +
            "        student_master sm ON ed.user_id = sm.user_id\n" +
            "    JOIN\n" +
            "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
            "    JOIN\n" +
            "        student_demographic sd ON ed.user_id = sd.user_id\n" +
            "    JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "     LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "\n" +
            "    WHERE \n" +
            "        \ted.attempted = 1\n" +
            "\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "\t-- AND sed.cwsn = 1\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "    -- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "    LIMIT 10000\n" +
            ") AS subquery\n" +
            "GROUP BY \n" +
            "    subject\n" +
            "ORDER BY \n" +
            "    subject;\n";

    public static String gradeWiseBreakdownImprovement = "SELECT \n" +
            "    subquery.grade,\n" +
            "    COUNT(DISTINCT CASE WHEN subquery.state_id = 1 THEN subquery.user_id ELSE NULL END) AS num_students_state,\n" +
            "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
            "    (AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
            "    (AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state,\n" +
            "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_nation,\n" +
            "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_nation\n" +
            "FROM (\n" +
            "    SELECT \n" +
            "        ed.user_id,\n" +
            "        ed.subject,\n" +
            "        ed.score,\n" +
            "        ed.quiz_attempt,\n" +
            "        sw.amount_status,\n" +
            "        sm.grade,\n" +
            "        sed.school_location,\n" +
            "        sd.state_id,\n" +
            "        sd.district_id,\n" +
            "        sd.gender,\n" +
            "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
            "        sd.education_board,\n" +
            "        sd.school_management,\n" +
            "        sw.transaction_date\n" +
            "    FROM \n" +
            "        exam_details ed\n" +
            "    JOIN \n" +
            "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "    JOIN\n" +
            "        student_master sm ON ed.user_id = sm.user_id\n" +
            "    JOIN\n" +
            "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
            "    JOIN\n" +
            "        student_demographic sd ON ed.user_id = sd.user_id\n" +
            "    JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "    LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "    WHERE \n" +
            "        ed.attempted = 1\n" +
            "\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "\t-- AND sed.cwsn = 1\n" +
            "\tAND ped.child_mother_qualification = 1\n" +
            "-- AND ped.child_father_qualification = 1\n" +
            "    LIMIT 10000\n" +
            ") AS subquery\n" +
            "GROUP BY \n" +
            "    subquery.grade\n" +
            "ORDER BY \n" +
            "    subquery.grade;\n";


    public static String topicWiseBreakdownImprovement = "SELECT \n" +
            "    subquery.quiz_name AS topic_name,\n" +
            "    COUNT(DISTINCT CASE WHEN subquery.state_id = 1 THEN subquery.user_id ELSE NULL END) AS num_students_state,\n" +
            "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
            "    (AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
            "    (AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.state_id = 1 AND subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state,\n" +
            "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_nation,\n" +
            "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_nation\n" +
            "FROM (\n" +
            "    SELECT \n" +
            "        ed.user_id,\n" +
            "        ed.score,\n" +
            "        ed.quiz_attempt,\n" +
            "        sw.amount_status,\n" +
            "        sm.grade,\n" +
            "        sd.state_id,\n" +
            "        sd.district_id,\n" +
            "        sd.gender,\n" +
            "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
            "        sd.education_board,\n" +
            "        sd.school_management,\n" +
            "        sw.transaction_date,\n" +
            "        aqn.quiz_name\n" +
            "    FROM \n" +
            "        exam_details ed\n" +
            "    JOIN \n" +
            "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
            "    JOIN\n" +
            "        student_master sm ON ed.user_id = sm.user_id\n" +
            "    JOIN\n" +
            "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
            "    JOIN\n" +
            "        student_demographic sd ON ed.user_id = sd.user_id\n" +
            "    JOIN\n" +
            "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND ed.subject = aqn.subject AND sm.grade = aqn.student_class\n" +
            "    JOIN\n" +
            "\tuser_master um ON sd.user_id = um.user_id\n" +
            "    LEFT JOIN\n" +
            "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
            "    WHERE \n" +
            "        ed.attempted = 1\n" +
            "\tAND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes\n" +
            "\t-- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
            "\t-- AND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t-- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.\n" +
            "\t-- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "\t-- AND sed.cwsn = 1\n" +
            "AND ped.child_mother_qualification = 1\n" +
            "    \t-- AND ped.child_father_qualification = 1\n" +
            "\n" +
            "    LIMIT 10000\n" +
            ") AS subquery\n" +
            "GROUP BY \n" +
            "    subquery.quiz_name\n" +
            "ORDER BY \n" +
            "    subquery.quiz_name;\n";
}

