package in.kpmg.auro.project.query;

public class SuperQuery {

    public static String superQuery = "SELECT\n" +
            "\ted.student_id,\n" +
            "    ed.user_id,\n" +
            "    ed.score,\n" +
            "    ed.exam_name,\n" +
            "    ed.quiz_attempt,\n" +
            "    ed.eklavvya_exam_id,\n" +
            "    ed.subject,\n" +
            "    ed.attempted \n" +
//            "    sm.grade,\n" +
//            "    sw.amount_status,\n" +
//            "    sw.transaction_date,\n" +
//            "    sed.school_location,\n" +
//            "    sed.social_group,\n" +
//            "    sd.state_id,\n" +
//            "    sd.district_id,\n" +
//            "    sd.gender,\n" +
//            "    sd.dob,\n" +
//            "    sd.education_board,\n" +
//            "    sd.school_management\n" +
            "FROM\n" +
            "\texam_details ed ; " ;
//            "JOIN\n" +
//            "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
//            "JOIN\n" +
//            "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
//            "JOIN\n" +
//            "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
//            "JOIN\n" +
//            "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
//            "ORDER BY\n" +
//            "\ted.user_id;";
}
