package com.heinrichreimersoftware.wg_planer.sync;

import android.content.Context;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;

import com.csvreader.CsvReader;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.authentication.ParseComServerAuthenticate;
import com.heinrichreimersoftware.wg_planer.data.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.exceptions.Base64Exception;
import com.heinrichreimersoftware.wg_planer.exceptions.NetworkException;
import com.heinrichreimersoftware.wg_planer.exceptions.UnknownUsernameException;
import com.heinrichreimersoftware.wg_planer.exceptions.WrongCredentialsException;
import com.heinrichreimersoftware.wg_planer.exceptions.WrongPasswordException;
import com.heinrichreimersoftware.wg_planer.structure.AuthToken;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.structure.Subject;
import com.heinrichreimersoftware.wg_planer.structure.SubjectFactory;
import com.heinrichreimersoftware.wg_planer.structure.Teacher;
import com.heinrichreimersoftware.wg_planer.structure.TeacherSubject;
import com.heinrichreimersoftware.wg_planer.structure.User;
import com.heinrichreimersoftware.wg_planer.utils.CalendarUtils;
import com.heinrichreimersoftware.wg_planer.utils.ClassesUtils;
import com.heinrichreimersoftware.wg_planer.utils.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.heinrichreimersoftware.wg_planer.Constants.IDESK_URL;
import static com.heinrichreimersoftware.wg_planer.Constants.REPRESENTATIONS_URL_TODAY;
import static com.heinrichreimersoftware.wg_planer.Constants.REPRESENTATIONS_URL_TOMORROW;
import static com.heinrichreimersoftware.wg_planer.Constants.TIMETABLE_BASE_URL;
import static com.heinrichreimersoftware.wg_planer.Constants.TIMETABLE_NAV_URL;
import static com.heinrichreimersoftware.wg_planer.Constants.USER_INFO_URL;

public class ParseComServerAccessor {


    public static final String TEACHER_CSV_URL = "http://heinrichreimersoftware.com/api/wg_planer/teachers/";
    public static final int TODAY = 0;
    public static final int TOMORROW = 1;
    private Context context;
    private OkHttpClient client = new OkHttpClient();
    public ParseComServerAccessor(Context context) {
        this.context = context;

        CookieManager cookieManager = new CookieManager();

        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        client.setCookieHandler(cookieManager);
    }

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            return "";
        }

        return response.body().string();
    }

    public boolean login(String auth) {
        try {
            AuthToken authToken = new AuthToken(auth);
            String newAuth = ParseComServerAuthenticate.login(context, client, authToken.getUsername(), authToken.getPassword());
            if (newAuth != null && newAuth.equals(auth)) {
                return true;
            }
        } catch (NetworkException | UnknownUsernameException | WrongPasswordException | Base64Exception | WrongCredentialsException e) {
            e.printStackTrace();
        }

        return false;
    }

    public User getUserInfo(String auth) {
        Log.d(MainActivity.TAG, "getUserInfo(" + auth + ")");

        if (login(auth)) {
            AuthToken authToken = new AuthToken(auth);
            String username = authToken.getUsername();
            String password = authToken.getPassword();

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setAuthToken(auth);


            String html;

            try {
                html = run(USER_INFO_URL);
            } catch (IOException e) {
                e.printStackTrace();
                return new User();
            }

            Document doc = Jsoup.parse(html, USER_INFO_URL);

            try {
                user.setImageUrl("");

                Elements hac = doc.getElementsByClass("hac");
                if (hac != null && hac.size() > 0) {
                    if (hac.first().getElementsByTag("a").first().attr("href") != null && !hac.first().getElementsByTag("a").first().attr("href").equals("")) {
                        user.setImageUrl(IDESK_URL + "addr/" + hac.first().getElementsByTag("a").first().attr("href"));

                        OkHttpDownloader downloader = new OkHttpDownloader(client);
                        Picasso picasso = new Picasso.Builder(context).downloader(downloader).build();
                        picasso.setIndicatorsEnabled(true);
                        picasso.setLoggingEnabled(true);

                        try {
                            user.setBitmap(picasso.load(user.getImageUrl()).get());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                user.setTitle(doc.getElementsByAttributeValue("name", "store[title]").first().attr("value"));
                String name = doc.getElementsByAttributeValue("name", "store[hidden]").first().getElementsByTag("option").get(0).text();
                String[] names = name.split(" ");
                if (names.length > 1) {
                    String lastName = names[names.length - 1];
                    user.setLastName(lastName);
                    user.setFirstName(name.replace(" " + lastName, ""));
                } else {
                    user.setFirstName(name);
                    user.setLastName("");
                }
                user.setBirthday(doc.getElementsByAttributeValue("name", "store[birthday]").first().attr("value"));
                user.setNickname(doc.getElementsByAttributeValue("name", "store[nickname]").first().attr("value"));
                user.setSchoolClass(doc.getElementsByAttributeValue("name", "store[class]").first().attr("value"));
                user.setStreet(doc.getElementsByAttributeValue("name", "store[street]").first().attr("value"));
                user.setZipCode(doc.getElementsByAttributeValue("name", "store[zipcode]").first().attr("value"));
                user.setCity(doc.getElementsByAttributeValue("name", "store[city]").first().attr("value"));
                user.setCountry(doc.getElementsByAttributeValue("name", "store[country]").first().attr("value"));
                user.setPhone(doc.getElementsByAttributeValue("name", "store[phone]").first().attr("value"));
                user.setMobilePhone(doc.getElementsByAttributeValue("name", "store[mobilephone]").first().attr("value"));
                user.setFax(doc.getElementsByAttributeValue("name", "store[fax]").first().attr("value"));
                user.setMail(doc.getElementsByAttributeValue("name", "store[mail]").first().attr("value"));
                user.setHomepage(doc.getElementsByAttributeValue("name", "store[homepage]").first().attr("value"));
                user.setIcq(doc.getElementsByAttributeValue("name", "store[icq]").first().attr("value"));
                user.setJabber(doc.getElementsByAttributeValue("name", "store[jabber]").first().attr("value"));
                user.setMsn(doc.getElementsByAttributeValue("name", "store[msn]").first().attr("value"));
                user.setSkype(doc.getElementsByAttributeValue("name", "store[skype]").first().attr("value"));
                user.setOid(doc.getElementsByAttributeValue("name", "addr_oid").first().attr("value"));


                Log.d(MainActivity.TAG, "getUserInfo(" + auth + ") finished");
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.e(MainActivity.TAG, doc.html());
                return new User();
            }
            return user;

        }
        return new User();
    }

    public List<Representation> getRepresentations(String auth) {
        Log.d(MainActivity.TAG, "getRepresentations(" + auth + ")");

        if (login(auth)) {
            List<Representation> representations = new ArrayList<>();
            representations.addAll(parseRepresentations(TODAY));
            representations.addAll(parseRepresentations(TOMORROW));
            return representations;
        }
        return new ArrayList<>();
    }

    public List<Representation> parseRepresentations(@Day int day) {
        List<Representation> representations = new ArrayList<>();

        String url;
        if (day == TODAY) {
            Log.d(MainActivity.TAG, "Parsing today's representations...");
            url = REPRESENTATIONS_URL_TODAY;
        } else if (day == TOMORROW) {
            Log.d(MainActivity.TAG, "Parsing tomorrow's representations...");
            url = REPRESENTATIONS_URL_TOMORROW;
        } else {
            return new ArrayList<>();
        }

        String html;
        try {
            html = run(url);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        if (html == null) {
            return new ArrayList<>();
        }

        Document docToday = Jsoup.parse(html, url);

        Element dateElement = docToday.getElementsByClass("mon_title").first();
        if (dateElement == null) {
            return new ArrayList<>();
        }

        String dateString = dateElement.text().split(" ")[0];
        String[] dateStrings = dateString.split("\\.");

        Calendar date = new GregorianCalendar();
        try {
            date.set(Calendar.YEAR, Integer.parseInt(dateStrings[2]));
            date.set(Calendar.MONTH, CalendarUtils.monthNumberToMonth(Integer.parseInt(dateStrings[1])));
            date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStrings[0]));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        /*
        Elements infos = docToday.select("td[colspan=2]");
		String info = "";
		int i = 0;
		while(i < infos.size()){
			info = info + infos.get(i).text();
			i++;
		}
		*/

        int colIndexLessonNumberUnparsed = 0;
        int colIndexFromTeacher = 1;
        int colIndexToTeacher = 2;
        int colIndexSubject = 3;
        int colIndexFromRoom = 4;
        int colIndexToRoom = 5;
        int colIndexFrom = 6;
        int colIndexTo = 7;
        int colIndexDescription = 8;

        /* Detect column order based on table headers */
        Elements firstRowElements = docToday.select("table.mon_list tr.list").get(1).getElementsByTag("th");
        for (int i = 0; i < firstRowElements.size(); i++) {
            String itemText = firstRowElements.get(i).text();
            switch (itemText) {
                case "Stunde":
                    colIndexLessonNumberUnparsed = i;
                    break;
                case "(Lehrer)":
                    colIndexFromTeacher = i;
                    break;
                case "Vertretung":
                    colIndexToTeacher = i;
                    break;
                case "(Fach)":
                    colIndexSubject = i;
                    break;
                case "(Raum)":
                    colIndexFromRoom = i;
                    break;
                case "Raum":
                    colIndexToRoom = i;
                    break;
                case "Vertr. von":
                    colIndexFrom = i;
                    break;
                case "(Le.) nach":
                    colIndexTo = i;
                    break;
                case "Vertretungs-Text":
                    colIndexDescription = i;
                    break;
            }
        }

        Elements representationElements = docToday.select("table.mon_list tr.list.odd, table.mon_list tr.list.even");
        if (representationElements.size() > 0) {
            String schoolClass = "";
            for (Element element : representationElements) {
                Element firstChild = element.child(0);
                if (firstChild.hasClass("inline_header") && firstChild.hasAttr("colspan")) {
                    schoolClass = firstChild.text();
                } else if (!schoolClass.equals("")) {
                    String lessonNumberUnparsed = element.child(colIndexLessonNumberUnparsed).text();
                    int lessonNumber = 0;
                    int firstLessonNumber = 0;
                    int lastLessonNumber = 0;
                    try {
                        lessonNumber = Integer.parseInt(lessonNumberUnparsed);
                    } catch (NumberFormatException ignored) {
                    }
                    if (lessonNumber != 0) {
                        firstLessonNumber = lessonNumber;
                        lastLessonNumber = lessonNumber;
                    } else {
                        String[] lessonNumbersUnparsed = lessonNumberUnparsed.split(" - ");
                        try {
                            firstLessonNumber = Integer.parseInt(lessonNumbersUnparsed[0]);
                            lastLessonNumber = Integer.parseInt(lessonNumbersUnparsed[1]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    SubjectFactory subjectFactory = new SubjectFactory();
                    Subject subject = subjectFactory.fromShorthand(element.child(colIndexSubject).text());
                    String fromTeacher = element.child(colIndexFromTeacher).text();
                    String from = element.child(colIndexFrom).text();
                    String fromRoom = element.child(colIndexFromRoom).text();
                    String toTeacher = element.child(colIndexToTeacher).text();
                    String toRoom = element.child(colIndexToRoom).text();
                    String to = element.child(colIndexTo).text();
                    String description = element.child(colIndexDescription).text();

                    Representation.Builder representationBuilder = new Representation.Builder()
                            .schoolClass(schoolClass)
                            .date(date)
                            .firstLessonNumber(firstLessonNumber)
                            .lastLessonNumber(lastLessonNumber)
                            .subject(subject)
                            .fromTeacher(fromTeacher)
                            .fromRoom(fromRoom)
                            .from(from)
                            .toTeacher(toTeacher)
                            .toRoom(toRoom)
                            .to(to)
                            .description(description);

                    representations.add(representationBuilder.build());
                }
            }
            return representations;
        }
        Log.w(MainActivity.TAG, "No Representations/Parse Error");
        return new ArrayList<>();
    }

    public List<Lesson> getTimetable(String auth) {
        User user = UserContentHelper.getUser(context);

        if (user != null && !user.getSchoolClass().equals("")) {
            return getTimetable(auth, user.getSchoolClass());
        } else {
            return getTimetable(auth, "");
        }
    }

    public List<Lesson> getTimetable(String auth, String classNames) {
        Log.d(MainActivity.TAG + "-Parse", "getTimetable(" + auth + ", " + classNames + ")");
        if (!TextUtils.isEmpty(classNames)) {
            List<Lesson> lessons = new ArrayList<>();

            if (login(auth)) {
                String html;
                try {
                    html = run(TIMETABLE_NAV_URL);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(MainActivity.TAG + "-Parse", "getTimetable short circuit 1");
                    return new ArrayList<>(); // Short circuiting as much errors we can find...
                }

                if (html == null) {
                    Log.d(MainActivity.TAG + "-Parse", "getTimetable short circuit 2");
                    return new ArrayList<>();
                }

                Document docToday = Jsoup.parse(html, TIMETABLE_NAV_URL);

                if (docToday.text().contains("Die angegebene Datei konnte nicht gefunden werden!")) {
                    Log.d(MainActivity.TAG + "-Parse", "getTimetable short circuit 3");
                    return new ArrayList<>();
                }

				/* Week */
                Elements weekOptions = docToday.select("form[name=NavBar] select[name=week] option");
                if (weekOptions == null || weekOptions.isEmpty()) {
                    Log.d(MainActivity.TAG + "-Parse", "getTimetable short circuit 4");
                    return new ArrayList<>();
                }
                String week = weekOptions.last().val();

                Pattern patternWeek = Pattern.compile("(\\d{1,2})\\.(\\d{1,2})\\. - (\\d{1,2})\\.(\\d{1,2})\\.");

                for (Element weekOption : weekOptions) {
                    String weekVal = weekOption.val();
                    String weekString = weekOption.text();
                    Matcher weekMatcher = patternWeek.matcher(weekString);
                    if (weekMatcher.matches()) {
                        try {
                            int startMonth = Integer.parseInt(weekMatcher.group(2));
                            int startDay = Integer.parseInt(weekMatcher.group(1));
                            int endMonth = Integer.parseInt(weekMatcher.group(4));
                            int endDay = Integer.parseInt(weekMatcher.group(3));

                            Calendar calendarStart = new GregorianCalendar();
                            Calendar calendarEnd = new GregorianCalendar();
                            Calendar calendarNow = new GregorianCalendar();
                            while (calendarNow.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                                calendarNow.add(Calendar.DATE, 1);
                            }

                            if (startMonth > 0 && startMonth <= 12 && endMonth > 0 && endMonth <= 12) {
                                calendarStart.set(Calendar.MONTH, startMonth - 1);
                                calendarEnd.set(Calendar.MONTH, endMonth - 1);

                                if (startDay > 0 && startDay <= calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH)
                                        && endDay > 0 && endDay <= calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                                    calendarStart.set(Calendar.DAY_OF_MONTH, startDay);
                                    calendarEnd.set(Calendar.DAY_OF_MONTH, endDay);

                                    if (calendarNow.after(calendarStart) && calendarNow.before(calendarEnd)) {
                                        week = weekVal;
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

				/* Type */
                Elements types = docToday.select("form[name=NavBar] select[name=type] option");
                String typeString = "";
                for (Element type : types) {
                    if (type.text().equals("Klassen")) {
                        typeString = type.val();
                        break;
                    }
                }

				/* Class */
                String classesJs = docToday.select("script[language=JavaScript]").get(1).toString();

                Pattern patternClassesJs = Pattern.compile(".*var classes = \\[(.*?)\\];.*", Pattern.DOTALL);
                Matcher matcherClassesJs = patternClassesJs.matcher(classesJs);
                classesJs = matcherClassesJs.replaceAll("$1");

                String[] classesArray = classesJs.split(",");

                List<Integer> classNumbers = new ArrayList<>();
                for (int j = 0; j < classesArray.length; j++) {
                    String currentClassName = classesArray[j].replace("\"", "");

                    String[] classNamesArray = classNames.split(", *");
                    for (String className : classNamesArray) {
                        if (currentClassName.startsWith(className)) {
                            classNumbers.add(j + 1);
                        }
                    }
                }

                Log.d(MainActivity.TAG + "-Parse", "classNumbers: " + classNumbers);

                for (Integer classNumber : classNumbers) {
                    Log.d(MainActivity.TAG, "classNumber: " + classNumber);
                    if (week != null && typeString != null) {
                        String timetableUrl = buildTimetableUrl(week, typeString, classNumber);

                        Log.i(MainActivity.TAG, "timetableUrl: " + timetableUrl);

                        lessons = ClassesUtils.mergeLessons(lessons, parseTimetable(timetableUrl));
                    }
                }
                return lessons;
            }
        }
        Log.d(MainActivity.TAG + "-Parse", "getTimetable short circuit 5");
        return new ArrayList<>();
    }

    private String buildTimetableUrl(String week, String typeString, int classNumber) {
        return TIMETABLE_BASE_URL +
                week + "/" +
                typeString + "/" +
                typeString + Utils.leadingNull(classNumber, 5) + ".htm";
    }

    private List<Lesson> parseTimetable(String timetableUrl) {
        Log.d(MainActivity.TAG + "-Parse", "parseTimetable: " + timetableUrl);
        List<Lesson> lessons = new ArrayList<>();

        String html;
        try {
            html = run(timetableUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        if (html == null) {
            return new ArrayList<>();
        }

        Log.d(MainActivity.TAG + "-Parse", "html=\"" + html + "\"");

        Document docTimetable = Jsoup.parse(html, timetableUrl);

        Elements timetableTableRows = docTimetable.select("body > center > table").get(0).select(" > tbody > tr");

        if (timetableTableRows.size() < 2) {
            Log.w(MainActivity.TAG, "No lessons/Parse error");
        } else {

            int[] lessonLengthCount = new int[7];
            lessonLengthCount[Calendar.MONDAY] = 0;
            lessonLengthCount[Calendar.TUESDAY] = 0;
            lessonLengthCount[Calendar.WEDNESDAY] = 0;
            lessonLengthCount[Calendar.THURSDAY] = 0;
            lessonLengthCount[Calendar.FRIDAY] = 0;

            for (int i = 1; i < timetableTableRows.size(); i++) {
                if (i % 2 != 0) {
                    int lessonNumber = (i - 1) / 2 + 1;
                    Element row = timetableTableRows.get(i);

                    Elements timetableTableCells = row.children();
                    if (timetableTableCells.size() < 1) {
                        Log.w(MainActivity.TAG, "Parse error");
                    } else {
                        for (int j = 1; j < timetableTableCells.size(); j++) {
                            Element timetableTableCell = timetableTableCells.get(j);
                            int lessonLength = Integer.parseInt(timetableTableCell.attr("rowspan")) / 2;

                            int day = -1;
                            int dayOffset = 0;

                            if (j == 1) {
                                if (lessonLengthCount[Calendar.MONDAY] < lessonNumber) {
                                    day = Calendar.MONDAY;
                                } else {
                                    dayOffset++;
                                }
                            }
                            if (j + dayOffset == 2) {
                                if (lessonLengthCount[Calendar.TUESDAY] < lessonNumber) {
                                    day = Calendar.TUESDAY;
                                } else {
                                    dayOffset++;
                                }
                            }
                            if (j + dayOffset == 3) {
                                if (lessonLengthCount[Calendar.WEDNESDAY] < lessonNumber) {
                                    day = Calendar.WEDNESDAY;
                                } else {
                                    dayOffset++;
                                }
                            }
                            if (j + dayOffset == 4) {
                                if (lessonLengthCount[Calendar.THURSDAY] < lessonNumber) {
                                    day = Calendar.THURSDAY;
                                } else {
                                    dayOffset++;
                                }
                            }
                            if (j + dayOffset == 5) {
                                if (lessonLengthCount[Calendar.FRIDAY] < lessonNumber) {
                                    day = Calendar.FRIDAY;
                                }
                            }

                            lessonLengthCount[day] += lessonLength;

                            /* Parse contained Subjects */
                            if (!TextUtils.isEmpty(timetableTableCell.text()) || timetableTableCell.text().contains("Vor Schulbeginn")) {
                                List<TeacherSubject> subjects = new ArrayList<>();

                                Elements subjectRows = timetableTableCell.select(" > table > tbody > tr");
                                for (Element subjectRow : subjectRows) {
                                    Elements subjectCells = subjectRow.select(" > td");

                                    String subjectShorthand = "";
                                    String teacher = "";
                                    String room = "";

                                    if (subjectCells.size() > 0) {
                                        subjectShorthand = subjectCells.get(0).text().replace(".", "");
                                        if (subjectCells.size() > 1) {
                                            teacher = subjectCells.get(1).text();
                                            if (subjectCells.size() > 2) {
                                                room = subjectCells.get(2).text();
                                            }
                                        }
                                    }

                                    SubjectFactory subjectFactory = new SubjectFactory();
                                    Subject rawSubject = subjectFactory.fromShorthand(subjectShorthand);

                                    TeacherSubject subject = new TeacherSubject(
                                            rawSubject.getShorthand(),
                                            rawSubject.getFullName(),
                                            rawSubject.getColor(),
                                            teacher,
                                            room
                                    );
                                    if (subject.getShorthand() != null
                                            && subject.getTeacher() != null
                                            && subject.getRoom() != null) {
                                        subjects.add(subject);
                                    }
                                }
                                if (subjects.size() > 0) {
                                    Lesson.Builder lessonBuilder = new Lesson.Builder()
                                            .day(day)
                                            .firstLessonNumber(lessonNumber)
                                            .lastLessonNumber(lessonNumber + lessonLength - 1)
                                            .subjects(subjects);
                                    lessons.add(lessonBuilder.build());
                                }
                            } else {
                                Log.d(MainActivity.TAG + "-Parse", "No lesson in this cell");
                            }
                        }
                    }
                }
            }
        }
        return lessons;
    }

    public List<Teacher> getTeachers(String auth) {
        Log.d(MainActivity.TAG, "getTeacher(" + auth + ")");

        if (login(auth)) {

            List<Teacher> teachers = new ArrayList<>();


            String html;
            try {
                html = run(TEACHER_CSV_URL);
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }

            if (html == null) {
                return new ArrayList<>();
            }

            CsvReader reader = new CsvReader(new InputStreamReader(Utils.stringToInputStream(html)));
            try {
                while (reader.readRecord()) {
                    Teacher teacher = new Teacher();
                    teacher.setShorthand(reader.get(0));
                    teacher.setLastName(reader.get(1));
                    teacher.setFirstName(reader.get(2));
                    teacher.setWebLink(reader.get(3));

                    teachers.add(teacher);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return teachers;
        }
        return new ArrayList<>();
    }

    @IntDef({TODAY, TOMORROW})
    public @interface Day {
    }

}