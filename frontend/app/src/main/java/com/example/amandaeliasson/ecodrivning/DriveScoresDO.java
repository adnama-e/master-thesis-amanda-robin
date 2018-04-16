package com.example.amandaeliasson.ecodrivning;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "brum-mobilehub-858586794-DriveScores")

public class DriveScoresDO {
    private String _userId;
    private String _driveId;
    private List<String> _datetime;
    private List<String> _score;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "driveId")
    @DynamoDBAttribute(attributeName = "driveId")
    public String getDriveId() {
        return _driveId;
    }

    public void setDriveId(final String _driveId) {
        this._driveId = _driveId;
    }
    @DynamoDBAttribute(attributeName = "datetime")
    public List<String> getDatetime() {
        return _datetime;
    }

    public void setDatetime(final List<String> _datetime) {
        this._datetime = _datetime;
    }
    @DynamoDBAttribute(attributeName = "score")
    public List<String> getScore() {
        return _score;
    }

    public void setScore(final List<String> _score) {
        this._score = _score;
    }

}
