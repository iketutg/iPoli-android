package io.ipoli.android.quest.persistence;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Bus;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.ipoli.android.app.persistence.BaseFirebasePersistenceService;
import io.ipoli.android.challenge.data.Challenge;
import io.ipoli.android.quest.data.Quest;
import io.ipoli.android.quest.data.RepeatingQuest;
import io.ipoli.android.quest.data.SubQuest;
import io.ipoli.android.reminders.data.Reminder;

import static io.ipoli.android.app.utils.DateUtils.toStartOfDay;
import static io.ipoli.android.app.utils.DateUtils.toStartOfDayUTC;


/**
 * Created by Venelin Valkov <venelin@curiousily.com>
 * on 7/27/16.
 */
public class FirebaseQuestPersistenceService extends BaseFirebasePersistenceService<Quest> implements QuestPersistenceService {

    public FirebaseQuestPersistenceService(Context context, Bus eventBus) {
        super(context, eventBus);
    }

    @Override
    protected Class<Quest> getModelClass() {
        return Quest.class;
    }

    @Override
    protected String getCollectionName() {
        return "quests";
    }

    @Override
    public void listenForUnplanned(OnDatabaseChangedListener<List<Quest>> listener) {

    }

    @Override
    public void findPlannedNonAllDayBetween(LocalDate startDate, LocalDate endDate, OnDatabaseChangedListener<List<Quest>> listener) {

    }

    @Override
    public List<Quest> findAllCompletedNonAllDayBetween(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<Quest> findAllPlannedAndStartedToday() {
        return null;
    }

    @Override
    public List<Quest> findAllIncompleteToDosBefore(LocalDate localDate) {
        return null;
    }

    @Override
    public List<Quest> findAllCompletedWithStartTime(RepeatingQuest repeatingQuest) {
        return null;
    }

    @Override
    public long countCompleted(RepeatingQuest repeatingQuest, LocalDate fromDate, LocalDate toDate) {
        return 0;
    }

    @Override
    public long countCompleted(RepeatingQuest repeatingQuest) {
        return 0;
    }

    @Override
    public void findAllNonAllDayForDate(LocalDate currentDate, OnDatabaseChangedListener<List<Quest>> listener) {

        Date startDate = toStartOfDay(currentDate);
        Date endDate = toStartOfDay(currentDate.plusDays(1));
        Date startDateUTC = toStartOfDayUTC(currentDate);
        Date endDateUTC = toStartOfDayUTC(currentDate.plusDays(1));

        DatabaseReference ref = getCollectionReference();

        Query endAt = ref.orderByChild("endDate/time").startAt(startDateUTC.getTime()).endAt(endDateUTC.getTime());

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Quest> quests = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    quests.add(snapshot.getValue(getModelClass()));
                }

                listener.onDatabaseChanged(quests);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        valueListeners.put(endAt.getRef(), valueEventListener);
        endAt.addValueEventListener(valueEventListener);
    }

    @Override
    public void findAllNonAllDayCompletedForDate(LocalDate currentDate, OnDatabaseChangedListener<List<Quest>> listener) {

    }

    @Override
    public void findAllNonAllDayIncompleteForDate(LocalDate currentDate, OnDatabaseChangedListener<List<Quest>> listener) {

    }

    @Override
    public List<Quest> findAllForRepeatingQuest(RepeatingQuest repeatingQuest) {
        return null;
    }

    @Override
    public long countAllForRepeatingQuest(RepeatingQuest repeatingQuest, LocalDate startDate, LocalDate endDate) {
        return 0;
    }

    @Override
    public List<Quest> findAllNonAllDayIncompleteForDateSync(LocalDate currentDate) {
        return null;
    }

    @Override
    public Quest findByExternalSourceMappingId(String source, String sourceId) {
        return null;
    }

    @Override
    public List<Quest> findAllUpcomingForRepeatingQuest(LocalDate startDate, RepeatingQuest repeatingQuest) {
        return null;
    }

    @Override
    public long countAllCompletedWithPriorityForDate(int priority, LocalDate date) {
        return 0;
    }

    @Override
    public List<Quest> findAllForChallenge(Challenge challenge) {
        return null;
    }

    @Override
    public Quest findByReminderId(String reminderId) {
        return null;
    }

    @Override
    public void findAllIncompleteOrMostImportantForDate(LocalDate now, OnDatabaseChangedListener<List<Quest>> listener) {

    }

    @Override
    public void saveReminders(Quest quest, List<Reminder> reminders) {

    }

    @Override
    public void saveSubQuests(Quest quest, List<SubQuest> subQuests) {

    }

    @Override
    public Date findNextUncompletedQuestEndDate(RepeatingQuest repeatingQuest) {
        return null;
    }

    @Override
    public Date findNextUncompletedQuestEndDate(Challenge challenge) {
        return null;
    }

    @Override
    public void findIncompleteNotRepeatingForChallenge(Challenge challenge, OnDatabaseChangedListener<List<Quest>> listener) {

    }

    @Override
    public List<Quest> findIncompleteNotRepeatingNotForChallenge(String query, Challenge challenge) {
        return null;
    }

    @Override
    public void findAllCompleted(Challenge challenge, OnDatabaseChangedListener<List<Quest>> listener) {
//        Query query = getCollectionReference().orderByChild("challengeId")

    }

    @Override
    public long countCompleted(Challenge challenge, LocalDate start, LocalDate end) {
        return 0;
    }

    @Override
    public long countCompleted(Challenge challenge) {
        return 0;
    }

    @Override
    public long countNotRepeating(Challenge challenge) {
        return 0;
    }

    @Override
    public long countNotDeleted(Challenge challenge) {
        return 0;
    }
}
