package com.company.clinic.web.screens.main;

import com.company.clinic.core.VisitService;
import com.company.clinic.entity.Pet;
import com.company.clinic.entity.Visit;
import com.haulmont.cuba.gui.Fragments;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.app.main.MainScreen;

import javax.inject.Inject;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


@UiController("extMainScreen")
@UiDescriptor("ext-main-screen.xml")
@LoadDataBeforeShow
public class ExtMainScreen extends MainScreen {


    @Inject
    private CollectionLoader<Pet> petsDl;
    @Inject
    private CollectionLoader<Visit> visitsDl;
    @Inject
    private LookupField<Pet> petSelector;
    @Inject
    private DateField<LocalDateTime> dateSelector;
    @Inject
    private UserSession userSession;
    @Inject
    private VisitService visitService;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Fragments fragments;

    @Subscribe("schedule")
    public void onSchedule(Action.ActionPerformedEvent event) {
        visitService.scheduleVisit(petSelector.getValue(), dateSelector.getValue(), userSession.getUser());
        visitsDl.load();

        petSelector.setValue(null);
        dateSelector.setValue(null);
    }

    @Subscribe("visitsCalendar")
    public void onVisitsCalendarCalendarEventClick(Calendar.CalendarEventClickEvent<LocalDateTime> event) {

        Visit visit = (Visit) event.getEntity();

        if (visit == null) {
            return;
        }

        Screen screen = screenBuilders.editor(Visit.class, this)
                .editEntity(visit)
                .withOpenMode(OpenMode.DIALOG)
                .build();

        screen.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.getCloseAction() == WINDOW_COMMIT_AND_CLOSE_ACTION) {
                visitsDl.load();
            }
        });

        screen.show();
    }

    @Subscribe("refresh")
    public void onRefresh(Action.ActionPerformedEvent event) {
        visitsDl.load();
        petsDl.load();
    }


}