package pl.lodz.p.cti.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Statements {

    public static final String OBJECT_WITH_GIVEN_NAME_ALREADY_EXISTS = "Istnieje już obiekt w bazie danych z nazwą \"PARAM\"";
    public static final String SAVE_OBJECT_WITH_GIVEN_NAME_SUCCESS = "Udało się zapisać obiekt w bazie danych z nazwą \"PARAM\"";
    public static final String CHOSEN_OBJECT_REMOVED = "Udało sie usunąć wybrany obiekt!";
    public static final String SAVE_COLLECTION_WITH_GIVEN_NAME_SUCCESS = "Udało się zapisać kolekcję w bazie danych z nazwą \"PARAM\"";
    public static final String CHOSEN_COLLECTION_REMOVED = "Udało sie usunąć wybraną kolekcję!";
    public static final String SAVE_PRESENTATION_WITH_GIVEN_NAME_SUCCESS = "Udało się zapisać prezentację w bazie danych";
    public static final String PRESENTATION_WITH_GIVEN_DATA_ALREADY_EXISTS = "Prezentacja dla podanego telewizora i podanej godziny już istnieje w bazie danych!";

    private static String PARAM = "PARAM";

    public static String generateStatement(String statement, String... params) {
        String string = statement;
        for (String param : params) {
            string = string.replaceFirst(PARAM, param);
        }
        return string;
    }
}
