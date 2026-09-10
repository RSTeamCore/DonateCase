package net.ritasister.dc.storage.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@DatabaseTable(tableName = "donate_cases")
public class DonateCaseEntity {

    @DatabaseField(id = true, columnName = "name", width = 16, canBeNull = false)
    private String id;

    @DatabaseField(columnName = "name", width = 16, canBeNull = false)
    private String player;

    @DatabaseField(columnName = "case_name", width = 16, canBeNull = false)
    private String caseName;

    @DatabaseField(columnName = "keys_count", width = 8, canBeNull = false)
    private int keysCount;

}