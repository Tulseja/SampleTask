package com.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyGenerator {
    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.onemoreerror.sampletask.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void addTables(final Schema schema) {
        addContactEntity(schema);
    }

    private static Entity addContactEntity(final Schema schema){
        Entity contact = schema.addEntity("Contact");
        contact.addIdProperty().primaryKey().autoincrement();
        contact.addStringProperty("name").notNull();
        contact.addStringProperty("number").notNull();
        contact.addStringProperty("image").notNull();
        return contact ;
    }


}
