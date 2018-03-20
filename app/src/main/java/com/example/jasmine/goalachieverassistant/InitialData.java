package com.example.jasmine.goalachieverassistant;

import com.example.jasmine.goalachieverassistant.Models.ListCategory;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by jasmine on 19/03/18.
 */

public class InitialData implements Realm.Transaction {
    @Override
    public void execute(Realm realm) {
        List<ListCategory> taskCategory = new ArrayList<>();
        ListCategory model = new ListCategory();
        model.setId(1+ System.currentTimeMillis());
        model.setName("Inbox");
        taskCategory.add(model);

        model = new ListCategory();
        model.setId(2 + System.currentTimeMillis());
        model.setName("Projects");
        taskCategory.add(model);

        model = new ListCategory();
        model.setId(3 + System.currentTimeMillis());
        model.setName("Family");
        taskCategory.add(model);

        model = new ListCategory();
        model.setId(4 + System.currentTimeMillis());
        model.setName("Work");
        taskCategory.add(model);

        model = new ListCategory();
        model.setId(5 + System.currentTimeMillis());
        model.setName("Personal");
        taskCategory.add(model);

        model = new ListCategory();
        model.setId(6 + System.currentTimeMillis());
        model.setName("Travel");
        taskCategory.add(model);

        model = new ListCategory();
        model.setId(6 + System.currentTimeMillis());
        model.setName("Shopping");
        taskCategory.add(model);

        for (ListCategory realmModel : taskCategory) {
            realm.insertOrUpdate(realmModel);
        }
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof InitialData;
    }

    @Override
    public int hashCode() {
        return InitialData.class.hashCode();
    }
}
