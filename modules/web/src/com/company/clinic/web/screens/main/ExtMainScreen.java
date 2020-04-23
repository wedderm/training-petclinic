package com.company.clinic.web.screens.main;

import com.company.clinic.core.VisitService;
import com.company.clinic.entity.Pet;
import com.company.clinic.entity.Visit;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.app.main.MainScreen;

import javax.inject.Inject;
import java.time.LocalDateTime;


@UiController("extMainScreen")
@UiDescriptor("ext-main-screen.xml")
@LoadDataBeforeShow
public class ExtMainScreen extends MainScreen {


    @Inject
    private CollectionLoader<Pet> petsDl;
    @Inject
    private CollectionLoader<Visit> visitsDl;
    @Inject
    private UserSession userSession;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private DataManager dataManager;
    @Inject
    private DataContext dataContext;
    @Inject
    private VisitService visitService;
    @Inject
    private InstanceContainer<Visit> visitDc;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        initNewVisit();
    }

    @Subscribe("schedule")
    public void onSchedule(Action.ActionPerformedEvent event) {
        dataContext.commit();
        initNewVisit();

        visitsDl.load();
        petsDl.load();
    }

    private void initNewVisit() {
        Visit visit = dataContext.merge(dataManager.create(Visit.class));
        visitDc.setItem(visit);
        visit.setVeterinarian(visitService.findVetByUser(userSession.getUser()));
        visit.setHoursSpent(1);
        visit.setAmount(visitService.calculateAmount(visit));
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
