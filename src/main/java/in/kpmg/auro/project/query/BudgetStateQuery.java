package in.kpmg.auro.project.query;

public class BudgetStateQuery {

    public static String fundsAllocated="select\n" +
            "\tstm.state_name,\n" +
            "      sb.funds_allocated_regarding_scert as funds_allocated\n" +
            "from\n" +
            "\tstate_budget sb\n" +
            "join\n" +
            "\tstate_master stm ON sb.state_id = stm.state_id\n" +
            "ORDER BY\n" +
            "\tstm.state_name ";

    public static String publicExpenditure="select\n" +
            "\tstm.state_name,\n" +
            "    \t(sb.public_expenditure_on_education/sb.public_expenditure_in_state)*100 as education_expenditure_percentage\n" +
            "from\n" +
            "\tstate_budget sb\n" +
            "join\n" +
            "\tstate_master stm ON sb.state_id = stm.state_id\n" +
            "ORDER BY\n" +
            "\tstm.state_name;";

    public static String samagraSikshaFundsApproved= "select\n" +
            "\tstm.state_name,\n" +
            "    \tsb.percentage_of_samagra_shiksha_funds_approved\n" +
            "from\n" +
            "\tstate_budget sb\n" +
            "join\n" +
            "\tstate_master stm ON sb.state_id = stm.state_id\n" +
            "ORDER BY\n" +
            "\tstm.state_name;";

    public static String samagraSikshaFundsReceived="select\n" +
            "\tstm.state_name,\n" +
            "       \tsb.percentage_of_samagra_shiksha_funds_received\n" +
            "from\n" +
            "\tstate_budget sb\n" +
            "join\n" +
            "\tstate_master stm ON sb.state_id = stm.state_id\n" +
            "ORDER BY\n" +
            "\tstm.state_name;";
}
