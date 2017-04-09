package io.ipoli.android.app;

import android.text.TextUtils;
import android.util.Pair;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.List;

import io.ipoli.android.Constants;
import io.ipoli.android.quest.data.Quest;
import io.ipoli.android.quest.data.RepeatingQuest;
import io.ipoli.android.quest.data.SourceMapping;
import io.ipoli.android.quest.generators.CoinsRewardGenerator;
import io.ipoli.android.quest.generators.ExperienceRewardGenerator;
import me.everything.providers.android.calendar.Event;

/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 5/11/16.
 */
public class AndroidCalendarEventParser {

    private final ExperienceRewardGenerator experienceRewardGenerator;
    private final CoinsRewardGenerator coinsRewardGenerator;

    public AndroidCalendarEventParser(ExperienceRewardGenerator experienceRewardGenerator, CoinsRewardGenerator coinsRewardGenerator) {
        this.experienceRewardGenerator = experienceRewardGenerator;
        this.coinsRewardGenerator = coinsRewardGenerator;
    }

    private boolean isRepeatingAndroidCalendarEvent(Event e) {
        return !TextUtils.isEmpty(e.rRule) || !TextUtils.isEmpty(e.rDate);
    }

    public Pair<List<Quest>, List<RepeatingQuest>> parse(List<Event> events) {
        List<Quest> quests = new ArrayList<>();
        List<RepeatingQuest> repeatingQuests = new ArrayList<>();
        for (Event e : events) {
            //allDay?
            if (e.deleted) {
                continue;
            }
            if (isRepeatingAndroidCalendarEvent(e)) {
                repeatingQuests.add(parseRepeatingQuest(e));
            } else {
                quests.add(parseQuest(e));
            }
        }

        return new Pair<>(quests, repeatingQuests);
    }

    private Quest parseQuest(Event e) {
        Quest q = new Quest(e.title);
        DateTimeZone timeZone = DateTimeZone.getDefault();
        if (!TextUtils.isEmpty(e.eventTimeZone)) {
            try {
                timeZone = DateTimeZone.forID(e.eventTimeZone);
            } catch (Exception ex) {
                // not known timezone
            }
        }
        DateTime startDateTime = new DateTime(e.dTStart, timeZone);
        DateTime endDateTime = new DateTime(e.dTend, timeZone);
        q.setAllDay(e.allDay);
        q.setDuration(Minutes.minutesBetween(startDateTime, endDateTime).getMinutes());
        q.setStartMinute(startDateTime.getMinuteOfDay());
        q.setStartDateFromLocal(startDateTime.toLocalDate().toDate());
        q.setEndDateFromLocal(endDateTime.toLocalDate().toDate());
        q.setSource(Constants.SOURCE_ANDROID_CALENDAR);
        q.setSourceMapping(SourceMapping.fromGoogleCalendar(e.calendarId, e.id));

        if (endDateTime.toLocalDate().isBefore(new LocalDate())) {
//            q.setCompletedAtDate(new Date(e.dTend));
//            q.setCompletedAtMinute(Time.of(q.getCompletedAtDate()).toMinutesAfterMidnight());
            q.setExperience(experienceRewardGenerator.generate(q));
            q.setCoins(coinsRewardGenerator.generate(q));
        }
//            if (TextUtils.isEmpty(e.originalId)) {
//                questPersistenceService.save(q);
//                return;
//            }

        return q;
    }

    private RepeatingQuest parseRepeatingQuest(Event event) {
        RepeatingQuest rq = new RepeatingQuest(event.title);
        return rq;
    }
}