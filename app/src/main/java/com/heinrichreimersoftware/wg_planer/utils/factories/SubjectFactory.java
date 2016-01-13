package com.heinrichreimersoftware.wg_planer.utils.factories;

import com.heinrichreimersoftware.wg_planer.structure.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubjectFactory {

    public static final int DEFAULT_SUBJECT_COLOR = 0xfff0f0f0;
    private static HashMap<String, Subject> subjects = null;
    private static List<String> multiTeacherSubjectShorthands = null;
    private static Pattern pattern;

    public SubjectFactory() {
        if (subjects == null) {
            subjects = new HashMap<>();
            addSubject(new Subject.Builder().shorthand("De").fullName("Deutsch").color(0xffffbb33).build());
            addSubject(new Subject.Builder().shorthand("Ma").fullName("Mathematik").color(0xffff8800).build());
            addSubject(new Subject.Builder().shorthand("Ek").fullName("Erdkunde").color(0xffb6dd40).build());
            addSubject(new Subject.Builder().shorthand("VS").fullName("Verfügungsstunde").color(0xff81eecf).build());
            addSubject(new Subject.Builder().shorthand("En").fullName("Englisch").color(0xffcc0000).build());
            addSubject(new Subject.Builder().shorthand("Bi").fullName("Biologie").color(0xff669900).build());
            addSubject(new Subject.Builder().shorthand("NW").fullName("Naturwissenschaften").color(0xff578200).build());
            addSubject(new Subject.Builder().shorthand("Ph").fullName("Physik").color(0xffff8acd).build());
            addSubject(new Subject.Builder().shorthand("Ch").fullName("Chemie").color(0xff3d3d3d).build());
            addSubject(new Subject.Builder().shorthand("Mu").fullName("Musik").color(0xffaa66cc).build());
            addSubject(new Subject.Builder().shorthand("Po").fullName("Politik").color(0xff33b5e5).build());
            addSubject(new Subject.Builder().shorthand("PW").fullName("Politik").color(0xff33b5e5).build());
            addSubject(new Subject.Builder().shorthand("Ge").fullName("Geschichte").color(0xff109a09).build());
            addSubject(new Subject.Builder().shorthand("Ku").fullName("Kunst").color(0xff604105).build());
            addSubject(new Subject.Builder().shorthand("If").fullName("Informatik").color(0xffa66bb3).build());
            addSubject(new Subject.Builder().shorthand("Re").fullName("Ev. Religion").color(0xffcccccc).build());
            addSubject(new Subject.Builder().shorthand("Rk").fullName("Kath. Religion").color(0xffcccccc).build());
            addSubject(new Subject.Builder().shorthand("WN").fullName("Werte/Normen").color(0xffcccccc).build());
            addSubject(new Subject.Builder().shorthand("Pi").fullName("Philosophie").color(0xffcccccc).build());
            addSubject(new Subject.Builder().shorthand("AG").fullName("Arbeitsgemeinschaft").color(0xffa5a5a5).build());
            addSubject(new Subject.Builder().shorthand("Sf").fullName("Seminarfach").color(0xffb5b5b5).build());
            addSubject(new Subject.Builder().shorthand("Sp").fullName("Sport").color(0xffa5a5a5).build());
            addSubject(new Subject.Builder().shorthand("La").fullName("Latein").color(0xffbebebe).build());
            addSubject(new Subject.Builder().shorthand("Gr").fullName("Griechisch").color(0xffbebebe).build());
            addSubject(new Subject.Builder().shorthand("Fr").fullName("Französisch").color(0xffa33ec0).build());
            addSubject(new Subject.Builder().shorthand("Sn").fullName("Spanisch").color(0xffff6d00).build());
            addSubject(new Subject.Builder().shorthand("Öko").fullName("Öko-Praktikum").color(0xff669900).build());
            addSubject(new Subject.Builder().shorthand("FLa").fullName("Förder-Latein").color(0xffbebebe).build());
            addSubject(new Subject.Builder().shorthand("FMa").fullName("Förder-Mathematik").color(0xffff8800).build());
            addSubject(new Subject.Builder().shorthand("FDe").fullName("Förder-Deutsch").color(0xffffbb33).build());
            addSubject(new Subject.Builder().shorthand("FEn").fullName("Förder-Englisch").color(0xffcc0000).build());
            addSubject(new Subject.Builder().shorthand("DBes").fullName("Dienstbesprechung").color(0xffa5a5a5).build());
        }

        if (multiTeacherSubjectShorthands == null) {
            multiTeacherSubjectShorthands = new ArrayList<>();
            addMultiTeacherSubjectShorthand("La");
            addMultiTeacherSubjectShorthand("Fr");
            addMultiTeacherSubjectShorthand("Sn");
            addMultiTeacherSubjectShorthand("Gr");
            addMultiTeacherSubjectShorthand("Re");
            addMultiTeacherSubjectShorthand("Rk");
            addMultiTeacherSubjectShorthand("WN");
            addMultiTeacherSubjectShorthand("sp");
        }

        if (pattern == null) {
            pattern = Pattern.compile("^([a-zäöüß]{2,})([0-9])?$", Pattern.CASE_INSENSITIVE);
        }
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

                return new Subject.Builder()
                        .shorthand(shorthand)
                        .fullName(fullName)
                        .color(rawSubject.getColor())
                        .build();
            }
        }
        return new Subject.Builder()
                .shorthand(shorthand)
                .fullName(shorthand)
                .color(DEFAULT_SUBJECT_COLOR)
                .build();
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