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
//            "\t\tAND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range\n" +
//            "\t\tAND sm.grade IN (5,6,7) -- Select the grade from 1-12\n" +
            "\t\tAND ed.subject = ? -- Subjects can be Hindi, English, Mathematics, etc.\n" +
//            "\t\tAND sed.school_location = 1 -- here '1' = Rural and '2' = Urban\n" +
//            "        \tAND sd.state_id = 1 -- Example state filter\n" +
//            "        \tAND sd.district_id = 1 -- Example district filter\n" +
//            "        \tAND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)\n" +
//            "        \tAND sd.gender = 'M' -- Example gender filter (M and F)\n" +
//            "        \tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)\n" +
//            "        \tAND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)\n" +
//            "        \tAND sd.school_management = 'Government School' -- Example management (Government School, Private School)\n" +
            "\tGROUP BY\n" +
            "\t\ted.user_id\n" +
            ") AS exam_stats\n" +
            "GROUP BY quiz_count\n" +
            "ORDER BY quiz_count;\n";



}

