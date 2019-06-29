package me.NickP0is0n;

import org.junit.Test;

import static org.junit.Assert.*;

public class StudentTest {

    @Test
    public void addTask() {
        String name = "John";
        String surName = "Smith";
        String grade = "1";
        Student student = new Student(name, surName, grade);
        student.addTask(0, 10);
        assertEquals((int)student.getDoneTasks()[0], 10);
    }

    @Test
    public void getDoneTasks() {
        String name = "John";
        String surName = "Smith";
        String grade = "1";
        Student student = new Student(name, surName, grade);
        int[] emptyDoneTasks = {};
        assertArrayEquals(student.getDoneTasks(), emptyDoneTasks);
    }
}
