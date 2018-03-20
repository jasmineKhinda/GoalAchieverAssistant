package com.example.jasmine.goalachieverassistant.Models;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.model.Parent;

/**
 * Created by jasmine on 19/03/18.
 */

public class ListCategory extends RealmObject {

    @Required
    @PrimaryKey
    private Long id;
    @Required
    private String name;
    private RealmList<TaskModel> tasks;

    public ListCategory(){

    }

    public void setId(Long id){
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public RealmList<TaskModel> getTaskList(){
        return tasks;
    }

}
