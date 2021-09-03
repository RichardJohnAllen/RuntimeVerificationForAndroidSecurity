/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The EvaluationResult class is a composition of fields detailing the result of
evaluating a formula.

===============================================================================

Copyright (C) 2021  Richard John Allen

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

===============================================================================
*/

package com.androidmonitor.reverserosuhavelund;

import java.util.ArrayList;

public class EvaluationResult {
    private String message;
    private ArrayList<TraceEvent> satisfyingEvents;

    public EvaluationResult(String message) {
        this.message = message;
        this.satisfyingEvents = new ArrayList<TraceEvent>();
    }

    public boolean Satisfied() { return this.satisfyingEvents.size() > 0; }

    public String Message() { return this.message; }

    public void SetSatisfyingEvents(ArrayList<TraceEvent> events) {
        this.satisfyingEvents = (events != null) ? events : new ArrayList<TraceEvent>();
    }

    public ArrayList<TraceEvent> SatisfyingEvents() { return this.satisfyingEvents; }
}