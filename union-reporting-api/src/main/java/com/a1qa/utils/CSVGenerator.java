package com.a1qa.utils;

public class CSVGenerator {
    private String data = "";
    private static final String DATA_SEPARATOR = ",";
    private static final String LINE_SEPARATOR = "\n";

    public void addLine(Object ... entities) {
        String line = "";
        for (int i = 0; i < entities.length; i++) {
            String stringData;
            stringData = String.valueOf(entities[i]);
            if (stringData.contains(DATA_SEPARATOR)) {
                stringData = "\"" + stringData + "\"";
            }
            line += stringData + (i != entities.length - 1?DATA_SEPARATOR:"");
        }
        data += line + LINE_SEPARATOR;
    }

    public String getData() {
        return data.trim();
    }
}
