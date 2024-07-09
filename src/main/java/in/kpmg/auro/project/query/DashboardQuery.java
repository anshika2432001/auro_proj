package in.kpmg.auro.project.query;

public class DashboardQuery {

    public static String countOfStudent="select stm.state_name,\n" +
            "    count(distinct sd.user_id) as num_students \n" +
            "FROM \n" +
            "\tstudent_demographic sd \n" +
            "JOIN\n" +
            "\tstate_master stm ON sd.state_id = stm.state_id \n" +
            "GROUP BY\n" +
            "\tstm.state_name\n" +
            "ORDER BY\n" +
            "\tstm.state_name;";

    public static String countOfParents="select \n" +
            "\tstm.state_name,\n" +
            "    count(distinct pm.user_id) as num_parents\n" +
            "FROM\n" +
            "\tparent_master pm\n" +
            "JOIN\n" +
            "\tstate_master stm ON pm.state_id = stm.state_id\n" +
            "GROUP BY\n" +
            "\tstm.state_name\n" +
            "ORDER BY\n" +
            "\tstm.state_name;";

    public static String countOfTeachers= "select \n" +
            "\tstm.state_name,\n" +
            "    count(distinct tm.user_id) as num_teachers\n" +
            "FROM\n" +
            "\tteacher_master tm\n" +
            "JOIN\n" +
            "\tstate_master stm ON tm.state_id = stm.state_id\n" +
            "GROUP BY\n" +
            "\tstm.state_name\n" +
            "ORDER BY\n" +
            "\tstm.state_name;";

    public static String countOfSchools="select \n" +
            "\tstm.state_name,\n" +
            "    count(distinct sm.SCHOOL_CODE) as num_schools\n" +
            "FROM\n" +
            "\tschoolmaster sm\n" +
            "JOIN\n" +
            "\tstate_master stm ON sm.stateid = stm.state_id\n" +
            "GROUP BY\n" +
            "\tstm.state_name\n" +
            "ORDER BY\n" +
            "\tstm.state_name;";
}
//1) Students count
//
//        select
//        stm.state_name,
//        count(distinct sd.user_id) as num_students
//        FROM
//        student_demographic sd
//        JOIN
//        state_master stm ON sd.state_id = stm.state_id
//        JOIN
//        student_extra_data sed ON sd.user_id = sed.user_id
//        JOIN
//        student_master sm ON sd.user_id = sm.user_id
//        JOIN
//        exam_details ed ON sd.user_id = ed.user_id
//        JOIN
//        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id
//        JOIN(
//        SELECT
//        user_id,
//        AVG(amount) as avg_scholarship
//        FROM
//        student_wallet
//        WHERE
//        amount_status IN ('2','4','5')
//        GROUP BY
//        user_id
//        ) avg_sch ON sd.user_id = avg_sch.user_id
//        JOIN
//        user_master um ON sd.user_id = um.user_id
//        LEFT JOIN
//        parent_extra_data ped ON um.parent_id = ped.user_id
//        WHERE
//        ed.attempted = 1
//        AND sw.amount_status IN ('2','4','5') -- Displays only the approved quizzes
//        -- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31' -- Select the required date range
//        -- AND sm.grade IN (5,6,7) -- Select the grade from 1-12
//        -- AND ed.subject = 'Mathematics' -- Subjects can be Hindi, English, Mathematics, etc.
//        -- AND sed.school_location = 1 -- here '1' = Rural and '2' = Urban
//        -- AND sd.state_id = 1 -- Example state filter
//        -- AND sd.district_id = 1 -- Example district filter
//        -- AND sed.social_group = 1 -- Example social group filter (1: SC, 2: ST, 3: OBC, 4: General, 5: Other)
//        AND sd.gender = 'M' -- Example gender filter (M and F)
//        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13 -- Example age group filter (6, 6-10, 11-13, 14-15, 16-17, >17)
//        -- AND sd.education_board = 'CBSE' -- Example board (CBSE, State Board, ICSE. International Board, Others, Both CBSE and State Board)
//        -- AND sd.school_management = 'Government School' -- Example management (Government School, Private School)
//        AND avg_sch.avg_scholarship BETWEEN 50 AND 100 -- Example average micro scholarhip filter (dynamic range)
//        -- AND sed.cwsn = 0
//        AND ped.child_father_qualification = 2
//        -- AND ped.child_mother_qualification = 2
//        GROUP BY
//        stm.state_name
//        ORDER BY
//        stm.state_name;
//
//
//
//        2) Parent Count
//
//        select
//        stm.state_name,
//        count(distinct pm.user_id) as num_parents
//        FROM
//        parent_master pm
//        JOIN
//        user_master um ON pm.user_id = um.parent_id
//        JOIN
//        student_demographic sd ON um.user_id = sd.user_id
//        JOIN
//        state_master stm ON sd.state_id = stm.state_id
//        JOIN
//        student_extra_data sed ON sd.user_id = sed.user_id
//        JOIN
//        student_master sm ON sd.user_id = sm.user_id
//        JOIN
//        exam_details ed ON sd.user_id = ed.user_id
//        JOIN
//        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id
//        JOIN
//        parent_extra_data ped ON pm.user_id = ped.user_id
//        WHERE
//        ed.attempted = 1
//        -- AND sw.amount_status IN ('2','4','5')
//        -- AND sw.transaction_date BETWEEN '2023-01-01' AND '2023-12-31'
//        -- pm.state_id = 4
//        -- AND pm.district_id = 1
//        -- ped.child_mother_qualification = 1
//        -- AND ped.child_father_qualification = 1
//        -- AND sed.school_location = 1
//        -- AND sed.social_group = 1
//        -- AND sd.state_id = 1
//        -- AND sd.district_id = 1
//        AND sd.gender = 'M'
//        -- AND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN 11 and 13
//        -- AND sd.education_board = 0
//        -- AND sd.school_management = 1
//        -- AND sm.grade = 1
//        GROUP BY
//        stm.state_name
//        ORDER BY
//        stm.state_name;
//
//
//        3) Teacher Count
//
//        select
//        stm.state_name,
//        count(distinct tm.user_id) as num_teachers
//        FROM
//        teacher_master tm
//        JOIN
//        teacher_student_mapping tsm ON tm.teacher_id = tsm.teacher_id
//        JOIN
//        student_demographic sd ON tsm.student_id = sd.user_id
//        JOIN
//        state_master stm ON sd.state_id = stm.state_id
//        WHERE
//        sd.education_board = 0
//        -- AND sd.school_management = 0
//        -- AND sm.grade = 1
//        GROUP BY
//        stm.state_name
//        ORDER BY
//        stm.state_name;
//
//        4) School Count
//
//        select
//        stm.state_name,
//        count(distinct sm.SCHOOL_CODE) as num_schools
//        FROM
//        schoolmaster sm
//        JOIN
//        state_master stm ON sm.stateid = stm.state_id
//        GROUP BY
//        stm.state_name
//        ORDER BY
//        stm.state_name;