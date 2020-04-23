package com.company.clinic.web.screens.pet;

import com.haulmont.charts.gui.components.charts.Chart;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.screen.*;
import com.company.clinic.entity.Pet;

import javax.inject.Inject;
import java.util.UUID;

@UiController("clinic_Pet.browse")
@UiDescriptor("pet-browse.xml")
@LookupComponent("petsTable")
@LoadDataBeforeShow
public class PetBrowse extends StandardLookup<Pet> {

    @Inject
    private Notifications notifications;
    @Inject
    private DataManager dataManager;
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe("petsPieChart")
    public void onPetsPieChartSliceClick(Chart.SliceClickEvent event) {

        UUID petId = event.getEntityNN().getValue("petId");

        Pet pet = dataManager.load(Pet.class).id(petId).optional().orElse(null);

        screenBuilders.editor(Pet.class, this).editEntity(pet).withOpenMode(OpenMode.DIALOG).build().show();

    }
}