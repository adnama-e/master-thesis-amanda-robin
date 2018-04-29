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

@DynamoDBTable(tableName = "brum-mobilehub-858586794-Overview")

public class OverviewDO {
    private String _userId;
    private String _driveId;
    private String _duration;
    private Double _ecoScore;
    private Double _improvementScore;

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
    @DynamoDBAttribute(attributeName = "duration")
    public String getDuration() {
        return _duration;
    }

    public void setDuration(final String _duration) {
        this._duration = _duration;
    }
    @DynamoDBAttribute(attributeName = "ecoScore")
    public Double getEcoScore() {
        return _ecoScore;
    }

    public void setEcoScore(final Double _ecoScore) {
        this._ecoScore = _ecoScore;
    }
    @DynamoDBAttribute(attributeName = "improvementScore")
    public Double getImprovementScore() {
        return _improvementScore;
    }

    public void setImprovementScore(final Double _improvementScore) {
        this._improvementScore = _improvementScore;
    }

}