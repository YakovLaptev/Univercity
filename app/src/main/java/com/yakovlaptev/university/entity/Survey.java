package com.yakovlaptev.university.entity;

public class Survey {

    private int id;
    private String institute;
    private String department;
    private String specialtyCode;
    private String specialty;
    private int curse;
    private String studyForm;
    private String answer1;
    private String answer2;

    public Survey() {
    }

    @Override
    public String toString() {
        return "(" + id +
                ")\n Институт:" + institute +
                "\n Кафедра:" + department +
                "\n Код специальности:'" + specialtyCode +
                "\n Специальность:" + specialty +
                "\n Курс:" + curse +
                "\n Форма обучения:" + studyForm+
                "\n Ответ 1:" + answer1 +
                "\n Ответ 2:" + answer2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstitute() {
        return this.institute;
    }

    public void setInstitute(String value) {
        this.institute = value;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String value) {
        this.department = value;
    }

    public String getSpecialtyCode() {
        return this.specialtyCode;
    }

    public void setSpecialtyCode(String value) {
        this.specialtyCode = value;
    }

    public String getSpecialty() {
        return this.specialty;
    }

    public void setSpecialty(String value) {
        this.specialty = value;
    }

    public int getCurse() {
        return this.curse;
    }

    public void setCurse(int value) {
        this.curse = value;
    }

    public String getStudyForm() {
        return this.studyForm;
    }

    public void setStudyForm(String value) {
        this.studyForm = value;
    }

    public String getAnswer1() {
        return this.answer1;
    }

    public void setAnswer1(String value) {
        this.answer1 = value;
    }

    public String getAnswer2() {
        return this.answer2;
    }

    public void setAnswer2(String value) {
        this.answer2 = value;
    }
}