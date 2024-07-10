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
            "WHERE\n" +
            "\t\ted.attempted = 1\n" +
            "\t\tAND ed.quiz_attempt IN (1,2,3)\n" +
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
            "\tWHERE\n" +
            "\t\ted.attempted = 1\n" +
            "        AND sw.amount_status IN ('2','4','5')\n" +
            "        AND aqn.subject = 'Maths'\n" +
            "        AND sm.grade = 5\n" +
            "        AND lm.language_id = 1\n" +
            "        -- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
            "\t\t-- AND sd.state_id = 1 -- Example state filter\n" +
            "        -- AND sd.district_id = 1 -- Example district filter\n" +
            "        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
            "        -- AND sd.gender = 'M' -- Example gender filter (M and F)\n" +
            "        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
            "        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
            "        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "        -- AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)\n" +
            "\tLIMIT 10000) AS subquery\n" +
            "GROUP BY\n" +
            "\tsubquery.quiz_name\n" +
            "ORDER BY\n" +
            "\tsubquery.quiz_name;\n";




}

