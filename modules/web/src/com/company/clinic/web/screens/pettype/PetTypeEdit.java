package com.company.clinic.web.screens.pettype;

import com.haulmont.cuba.gui.screen.*;
import com.company.clinic.entity.PetType;

@UiController("clinic_PetType.edit")
@UiDescriptor("pet-type-edit.xml")
@EditedEntityContainer("petTypeDc")
@LoadDataBeforeShow
@DialogMode(height = "auto", width = "auto", forceDialog = true, closeable = true)
public class PetTypeEdit extends StandardEditor<PetType> {
}