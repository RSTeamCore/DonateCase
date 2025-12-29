package net.ritasister.dc.storage.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@DatabaseTable(tableName = "minecraft_accounts")
public class MinecraftAccounts {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "name", width = 16, canBeNull = false)
    private String nickname;

    @DatabaseField(columnName = "uuid", width = 36, uniqueCombo = true, canBeNull = false)
    private String uuid;
}