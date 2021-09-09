package com.androidmonitor.rosuhavelund;

import java.util.ArrayList;

public class EvaluationResult {
    private Boolean satisfied;
    private ArrayList<TraceEvent> satisfyingEvents;
    private String message;

    public EvaluationResult(String message) {
        this.satisfied = false;
        this.satisfyingEvents = new ArrayList<TraceEvent>();
        this.message = message;
    }

    public void SetSatisfied(Boolean satisfied) { this.satisfied = satisfied; }
    public Boolean Satisfied() { return this.satisfied; }
    public ArrayList<TraceEvent> SatisfyingEvents() { return this.satisfyingEvents; }
    public String Message() { return this.message; }
}
