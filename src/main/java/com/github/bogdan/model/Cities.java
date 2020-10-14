package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "cities")
public class Cities {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(unique = true)
    private String city;

}
