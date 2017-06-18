package pl.lodz.p.cti.utils;

public class Statements {
    public static final String OBJECT_WITH_GIVEN_NAME_ALREADY_EXISTS = "Istnieje już objekt w bazie danych z nazwą \"PARAM\"";
    public static final String SAVE_OBJECT_WITH_GIVEN_NAME_SUCCESS = "Udało się zapisać obiekt w bazie danych z nazwą \"PARAM\"" ;
    public static final String CHOSEN_OBJECT_REMOVED = "Udało sie usunąć wybrany objekt!";
    public static final String SAVE_COLLECTION_WITH_GIVEN_NAME_SUCCESS = "Udało się zapisać kolekcję w bazie danych z nazwą \"PARAM\"" ;
    public static final String CHOSEN_COLLECTION_REMOVED = "Udało sie usunąć wybraną kolekcję!";
    public static final String SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS = "Udało się zapisać prezentację w bazie danych z nazwą \"PARAM\"" ;
    public static final String CHOSEN_PRESENTATION_REMOVED = "Udało sie usunąć wybraną prezentację!";

    public static String PARAM = "PARAM";

    public static String generateStatement(String statement, String... params){
        String string = statement;
        for(String param : params) {
            string = string.replaceFirst(PARAM, param);
        }
        return string;
    }
}
