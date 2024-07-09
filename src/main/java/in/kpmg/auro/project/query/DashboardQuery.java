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
