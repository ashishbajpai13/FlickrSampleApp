package com.safepix.DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Photos")
public class PhotoEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "searchQuery")
    private String searchQuery;

    @ColumnInfo(name = "secret")
    private String secret;

    @ColumnInfo(name = "server")
    private String server;

    @ColumnInfo(name = "farm")
    private Integer farm;

    @Ignore
    public PhotoEntity(){
    }

    public PhotoEntity(String searchQuery, String id, String secret, String server, Integer farm) {
        this.searchQuery = searchQuery;
        this.id = id;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getFarm() {
        return farm;
    }

    public void setFarm(Integer farm) {
        this.farm = farm;
    }
}
