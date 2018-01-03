package me.joshvocal.home.model;

/**
 * Created by josh on 12/29/17.
 */

public class Switch {

    private String name;
    private boolean value;

    public Switch() {
    }

    public Switch(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setValues(Switch newSwitch) {
        name = newSwitch.getName();
        value = newSwitch.getValue();
    }
}
