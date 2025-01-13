package com.UniSim.game.Resources;

public class ResourceManager {
    private int money;
    private int students;
    private int staff;
    private static final int MAX_STUDENTS = 1000;
    private static final int MAX_STAFF = 100;

    public ResourceManager() {
        this.money = 1000;
        this.students = 0;
        this.staff = 0;
    }

    public int getMoney() {
        return money;
    }

    public int getStudents() {
        return students;
    }

    public int getStaff() {
        return staff;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public boolean spendMoney(int amount) {
        if (amount > money) {
            return false;
        }
        money -= amount;
        return true;
    }

    public boolean addStudents(int count) {
        if (students + count > MAX_STUDENTS) {
            return false;
        }
        students += count;
        return true;
    }

    public boolean addStaff(int count) {
        if (staff + count > MAX_STAFF) {
            return false;
        }
        staff += count;
        return true;
    }
} 