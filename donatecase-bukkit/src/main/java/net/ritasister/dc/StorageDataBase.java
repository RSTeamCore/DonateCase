package net.ritasister.dc;

public class StorageDataBase {

    private String nickName;
    private String case_name;
    private int keys_count;

    public StorageDataBase(final String nickName, final String case_name, final int keys_count) {
        this.nickName=nickName;
        this.case_name=case_name;
        this.keys_count=keys_count;;
    }
}