package pl.team.sm_project.budgetmanager;


public class WebConfig {

    public static final String GLOBAL_URL = ""; //domena aplikacji

    public static final String BUDGET_GET_ALL = "/api/budgets";
    public static final String BUDGET_ADD ="/api/budget/add";
    public static final String BUDGET_GET = "/api/budget/";

    public static final String BUDGET_UPDATE = "/api/budget/edit/";
    public static final String BUDGET_DELETE = "/api/budget/delete/";

    public static final String KEY_BUDGET_ID = "id";
    public static final String KEY_BUDGET_NAME = "name";
    public static final String KEY_BUDGET_DATE = "date";

    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_DATE = "date";

    public static final String BUDGET_ID = "budget_id";
}
