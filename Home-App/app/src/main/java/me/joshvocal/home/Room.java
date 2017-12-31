package me.joshvocal.home;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 12/30/17.
 */

public class Room {

    private List<Switch> switchList = new ArrayList<>();

    public Room() {
    }

    public Room(List<Switch> switchList) {
        this.switchList = switchList;
    }

    public List<Switch> getSwitchList() {
        return switchList;
    }

    public void setSwitchList(List<Switch> switchList) {
        this.switchList = switchList;
    }
}
