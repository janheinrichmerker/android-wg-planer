package com.heinrichreimersoftware.wg_planer.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubjectFactory {

    public int defaultSubjectColor = 0xfff0f0f0;
    private HashMap<String, Subject> subjects;
    private List<String> multiTeacherSubjectShorthands;
    private Pattern pattern;

    public SubjectFactory() {
        subjects = new HashMap<>();
        addSubject(new Subject("De", "Deutsch", 0xffffbb33));
        addSubject(new Subject("Ma", "Mathematik", 0xffff8800));
        addSubject(new Subject("Ek", "Erdkunde", 0xffb6dd40));
        addSubject(new Subject("VS", "Verfügungsstunde", 0xff81eecf));
        addSubject(new Subject("En", "Englisch", 0xffcc0000));
        addSubject(new Subject("Bi", "Biologie", 0xff669900));
        addSubject(new Subject("NW", "Naturwissenschaften", 0xff578200));
        addSubject(new Subject("Ph", "Physik", 0xffff8acd));
        addSubject(new Subject("Ch", "Chemie", 0xff3d3d3d));
        addSubject(new Subject("Mu", "Musik", 0xffaa66cc));
        addSubject(new Subject("Po", "Politik", 0xff33b5e5));
        addSubject(new Subject("PW", "Politik", 0xff33b5e5));
        addSubject(new Subject("Ge", "Geschichte", 0xff109a09));
        addSubject(new Subject("Ku", "Kunst", 0xff604105));
        addSubject(new Subject("If", "Informatik", 0xffa66bb3));
        addSubject(new Subject("Re", "Ev. Religion", 0xffcccccc));
        addSubject(new Subject("Rk", "Kath. Religion", 0xffcccccc));
        addSubject(new Subject("WN", "Werte/Normen", 0xffcccccc));
        addSubject(new Subject("Pi", "Philosophie", 0xffcccccc));
        addSubject(new Subject("AG", "Arbeitsgemeinschaft", 0xffa5a5a5));
        addSubject(new Subject("Sf", "Seminarfach", 0xffb5b5b5));
        addSubject(new Subject("Sp", "Sport", 0xffa5a5a5));
        addSubject(new Subject("La", "Latein", 0xffbebebe));
        addSubject(new Subject("Gr", "Griechisch", 0xffbebebe));
        addSubject(new Subject("Fr", "Französisch", 0xffa33ec0));
        addSubject(new Subject("Sn", "Spanisch", 0xffff6d00));
        addSubject(new Subject("Öko", "Öko-Praktikum", 0xff669900));
        addSubject(new Subject("FLa", "Förder-Latein", 0xffbebebe));
        addSubject(new Subject("FMa", "Förder-Mathematik", 0xffff8800));
        addSubject(new Subject("FDe", "Förder-Deutsch", 0xffffbb33));
        addSubject(new Subject("FEn", "Förder-Englisch", 0xffcc0000));
        addSubject(new Subject("DBes", "Dienstbesprechung", 0xffa5a5a5));

        multiTeacherSubjectShorthands = new ArrayList<>();
        addMultiTeacherSubjectShorthand("La");
        addMultiTeacherSubjectShorthand("Fr");
        addMultiTeacherSubjectShorthand("Sn");
        addMultiTeacherSubjectShorthand("Gr");
        addMultiTeacherSubjectShorthand("Re");
        addMultiTeacherSubjectShorthand("Rk");
        addMultiTeacherSubjectShorthand("WN");

        pattern = Pattern.compile("^([a-zäöüß]{2,})([0-9])?$", Pattern.CASE_INSENSITIVE);
    }

    public void addSubject(Subject subject) {
        addSubject(subject, false);
    }

    public void addSubject(Subject subject, boolean replace) {
        if (!subjects.containsKey(subject.getShorthand())) {
            subjects.put(subject.getShorthand().toLowerCase(), subject);
        } else if (replace) {
            subjects.remove(subject.getShorthand().toLowerCase());
            subjects.put(subject.getShorthand(), subject);
        }
    }

    public void addMultiTeacherSubjectShorthand(String shorthand) {
        multiTeacherSubjectShorthands.add(shorthand.toLowerCase());
    }

    public Subject fromShorthand(String shorthand) {
        String shorthandLowercase = shorthand.toLowerCase();

        Matcher matcher = pattern.matcher(shorthandLowercase);
        if (matcher.find()) {
            String parsedShorthand = matcher.group(1);

            String subjectNumber = null;
            if (matcher.group(2) != null && !matcher.group(2).equals("")) {
                subjectNumber = matcher.group(2);
            }

            Subject rawSubject = subjects.get(parsedShorthand);
            if (rawSubject != null) {
                String fullName = rawSubject.getFullName();
                if (subjectNumber != null) {
                    fullName += " " + subjectNumber;
                }

                return new Subject(shorthand, fullName, rawSubject.getColor());
            }
        }
        return new Subject(shorthand, shorthand, defaultSubjectColor);
    }

    public boolean isMultiTeacherSubject(Subject subject) {
        String shorthand = subject.getShorthand().toLowerCase();
        Matcher matcher = pattern.matcher(shorthand);
        if (matcher.find()) {
            String parsedShorthand = matcher.group(1);
            if (multiTeacherSubjectShorthands.contains(parsedShorthand)) {
                return true;
            }
        }
        return false;
    }
}