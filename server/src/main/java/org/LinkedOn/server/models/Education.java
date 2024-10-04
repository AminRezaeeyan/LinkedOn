package org.LinkedOn.server.models;

import java.sql.Date;

public class Education {
    private String id, userId, field, grade, description, activities;
    private Degree degree;
    private Institute institute;
    private Date startDate, endDate;

    // Default values
    {
        institute = Institute.NONE;
        degree = Degree.NONE;
    }

    public Education(String id, String userId, String field, String grade, String description, String activities, Degree degree, Institute institute, Date startDate, Date endDate) {
        this.id = id;
        this.userId = userId;
        this.field = field;
        this.grade = grade;
        this.description = description;
        this.activities = activities;
        this.degree = degree;
        this.institute = institute;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Default Constructor
    public Education() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public enum Degree {
        HIGH_SCHOOL("High School"),
        ASSOCIATE("Associate's Degree"),
        BACHELOR("Bachelor's Degree"),
        MASTER("Master's Degree"),
        DOCTORATE("Doctorate"),
        MBA("Master of Business Administration"),
        JD("Juris Doctor"),
        MD("Doctor of Medicine"),
        PHD("Doctor of Philosophy"),
        CERTIFICATE("Certificate"),
        DIPLOMA("Diploma"),
        NONE("None");

        private final String displayName;

        Degree(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Institute {
        // Top 50 Global Universities
        HARVARD_UNIVERSITY("Harvard University"),
        STANFORD_UNIVERSITY("Stanford University"),
        MIT("Massachusetts Institute of Technology"),
        CALTECH("California Institute of Technology"),
        UNIVERSITY_OF_CAMBRIDGE("University of Cambridge"),
        UNIVERSITY_OF_OXFORD("University of Oxford"),
        UNIVERSITY_OF_CHICAGO("University of Chicago"),
        PRINCETON_UNIVERSITY("Princeton University"),
        COLUMBIA_UNIVERSITY("Columbia University"),
        YALE_UNIVERSITY("Yale University"),
        UNIVERSITY_OF_PENNSYLVANIA("University of Pennsylvania"),
        UNIVERSITY_OF_CALIFORNIA_BERKELEY("University of California, Berkeley"),
        UNIVERSITY_OF_MICHIGAN("University of Michigan"),
        UNIVERSITY_OF_TORONTO("University of Toronto"),
        IMPERIAL_COLLEGE_LONDON("Imperial College London"),
        UNIVERSITY_COLLEGE_LONDON("University College London"),
        UNIVERSITY_OF_EDINBURGH("University of Edinburgh"),
        UNIVERSITY_OF_MELBOURNE("University of Melbourne"),
        UNIVERSITY_OF_SYDNEY("University of Sydney"),
        UNIVERSITY_OF_HONG_KONG("University of Hong Kong"),
        NATIONAL_UNIVERSITY_OF_SINGAPORE("National University of Singapore"),
        PEKING_UNIVERSITY("Peking University"),
        TSINGHUA_UNIVERSITY("Tsinghua University"),
        UNIVERSITY_OF_TOKYO("University of Tokyo"),
        KYOTO_UNIVERSITY("Kyoto University"),
        SEOUL_NATIONAL_UNIVERSITY("Seoul National University"),
        UNIVERSITY_OF_COPENHAGEN("University of Copenhagen"),
        UNIVERSITY_OF_ZURICH("University of Zurich"),
        ETH_ZURICH("ETH Zurich"),
        UNIVERSITY_OF_AMSTERDAM("University of Amsterdam"),
        UNIVERSITY_OF_MUNICH("Ludwig Maximilian University of Munich"),
        UNIVERSITY_OF_HEIDELBERG("Heidelberg University"),
        UNIVERSITY_OF_PARIS("Sorbonne University"),
        UNIVERSITY_OF_BARCELONA("University of Barcelona"),
        UNIVERSITY_OF_MADRID("Complutense University of Madrid"),
        UNIVERSITY_OF_SAO_PAULO("University of São Paulo"),
        UNIVERSITY_OF_BUENOS_AIRES("University of Buenos Aires"),
        UNIVERSITY_OF_CAPE_TOWN("University of Cape Town"),
        UNIVERSITY_OF_JOHANNESBURG("University of Johannesburg"),
        UNIVERSITY_OF_QUEENSLAND("University of Queensland"),
        UNIVERSITY_OF_AUCKLAND("University of Auckland"),
        UNIVERSITY_OF_OTAGO("University of Otago"),
        UNIVERSITY_OF_WATERLOO("University of Waterloo"),
        UNIVERSITY_OF_BRITISH_COLUMBIA("University of British Columbia"),
        UNIVERSITY_OF_ALBERTA("University of Alberta"),
        UNIVERSITY_OF_MONTREAL("University of Montreal"),
        MCGILL_UNIVERSITY("McGill University"),

        // Top 20 Universities in Iran
        UNIVERSITY_OF_TEHRAN("University of Tehran"),
        SHARIF_UNIVERSITY_OF_TECHNOLOGY("Sharif University of Technology"),
        TEHRAN_UNIVERSITY_OF_MEDICAL_SCIENCES("Tehran University of Medical Sciences"),
        SHAHID_BEHESHTI_UNIVERSITY_OF_MEDICAL_SCIENCES("Shahid Beheshti University of Medical Sciences"),
        FERDOWSI_UNIVERSITY_OF_MASHHAD("Ferdowsi University of Mashhad"),
        MASHHAD_UNIVERSITY_OF_MEDICAL_SCIENCES("Mashhad University of Medical Sciences"),
        ISFAHAN_UNIVERSITY_OF_TECHNOLOGY("Isfahan University of Technology"),
        TARBIAT_MODARES_UNIVERSITY("Tarbiat Modares University"),
        SHAHID_BEHESHTI_UNIVERSITY("Shahid Beheshti University"),
        UNIVERSITY_OF_ISFAHAN("University of Isfahan"),
        ISFAHAN_UNIVERSITY_OF_MEDICAL_SCIENCES("Isfahan University of Medical Sciences"),
        IRAN_UNIVERSITY_OF_SCIENCE_AND_TECHNOLOGY("Iran University of Science and Technology"),
        SHIRAZ_UNIVERSITY_OF_MEDICAL_SCIENCES("Shiraz University of Medical Sciences"),
        TABRIZ_UNIVERSITY_OF_MEDICAL_SCIENCES("Tabriz University of Medical Sciences"),
        AMIRKABIR_UNIVERSITY_OF_TECHNOLOGY("Amirkabir University of Technology"),
        SHIRAZ_UNIVERSITY("Shiraz University"),
        KHAJE_NASIR_TOOSI_UNIVERSITY_OF_TECHNOLOGY("Khaje Nasir Toosi University of Technology"),
        UNIVERSITY_OF_TABRIZ("University of Tabriz"),
        UNIVERSITY_OF_GUILAN("University of Guilan"),
        UNIVERSITY_OF_KASHAN("University of Kashan"),
        NONE("None");

        private final String displayName;

        Institute(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
