package by.bsu.client.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandValidator {

    public boolean isValidRegisterString(String str) {
        Pattern pattern = Pattern.compile("^/register\\s+(client|agent)\\s+[a-zA-Z]{4,}\\s*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str.trim());
        return matcher.matches();
    }

}
