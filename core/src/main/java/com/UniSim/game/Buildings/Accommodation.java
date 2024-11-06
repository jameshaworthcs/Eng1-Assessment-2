package com.UniSim.game.Buildings;

public class Accommodation extends Building {

    private int students;

    public Accommodation(String name, float cost, String picture, float lakeBonus, float width, float height, int students) {
        super(name, cost, picture, lakeBonus, width, height);
        this.students = students;
    }

    public int getStudents() {
        return students;
    }
}
