package pl.team.sm_project.budgetmanager;


public class WebConfig {

    public static final String GLOBAL_URL = "http://192.168.1.139:8000"; //domena aplikacji

    public static final String BUDGET_GET_ALL = "/api/budgets";
    public static final String BUDGET_ADD ="/api/budget/add";
    public static final String BUDGET_GET = "/api/budget/";
    public static final String BUDGET_UPDATE = "/api/budget/edit/";
    public static final String BUDGET_DELETE = "/api/budget/delete/";

    public static final String SOURCE_GET_ALL = "/api/sources";
    public static final String SOURCE_ADD ="/api/sources/add";
    public static final String SOURCE_GET = "/api/source/";
    public static final String SOURCE_UPDATE = "/api/source/edit/";
    public static final String SOURCE_DELETE = "/api/source/delete/";

    public static final String KEY_BUDGET_ID = "id";
    public static final String KEY_BUDGET_NAME = "name";
    public static final String KEY_BUDGET_DATE = "date";
    public static final String KEY_BUDGET_VALUE = "value";
    public static final String KEY_BUDGET_TYPE = "type_id";
    public static final String KEY_BUDGET_USER = "user_id";
    public static final String KEY_BUDGET_SOURCE = "source_id";
    public static final String KEY_BUDGET_COMMENT = "comment";

    public static final String KEY_SOURCE_ID = "id";
    public static final String KEY_SOURCE_NAME = "name";
    public static final String KEY_SOURCE_VALUE = "value";
    public static final String KEY_SOURCE_TYPE = "type_id";
    public static final String KEY_SOURCE_COMMENT = "comment";

    public static final String TAG_JSON_ARRAY = "result";

    public static final String BUDGET_TAG_ID = "id";
    public static final String BUDGET_TAG_NAME = "name";
    public static final String BUDGET_TAG_DATE = "date";
    public static final String BUDGET_TAG_VALUE = "value";
    public static final String BUDGET_TAG_TYPE = "type";
    public static final String BUDGET_TAG_USER = "user";
    public static final String BUDGET_TAG_SOURCE = "source";
    public static final String BUDGET_TAG_COMMENT = "comment";

    public static final String INCOME_ID = "1";
    public static final String EXPENSE_ID = "2";

    public static final String SOURCE_TAG_ID = "id";
    public static final String SOURCE_TAG_NAME = "name";
    public static final String SOURCE_TAG_DATE = "date";
    public static final String SOURCE_TAG_VALUE = "value";
    public static final String SOURCE_TAG_COMMENT = "comment";
    public static final String SOURCE_TAG_TYPE = "type";

    public static final String BUDGET_ID = "budget_id";
    public static final String SOURCE_ID = "source_id";
}
