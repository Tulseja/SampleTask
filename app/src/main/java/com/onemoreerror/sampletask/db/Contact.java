package com.onemoreerror.sampletask.db;

import org.greenrobot.greendao.annotation.*;

import java.io.Serializable;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "CONTACT".
 */
@Entity
public class Contact implements Serializable {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String number;

    @NotNull
    private String image;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Contact() {
    }

    public Contact(Long id) {
        this.id = id;
    }

    @Generated
    public Contact(Long id, String name, String number, String image) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getNumber() {
        return number;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNumber(@NotNull String number) {
        this.number = number;
    }

    @NotNull
    public String getImage() {
        return image;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setImage(@NotNull String image) {
        this.image = image;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
