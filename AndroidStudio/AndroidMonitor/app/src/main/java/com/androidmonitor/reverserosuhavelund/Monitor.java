/*
===============================================================================

Android Monitor
Monitors Android devices for information theft by colluding applications.

The Monitor class is an aggregation of components responsible for
evaluating a formula over a trace.

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

import com.androidmonitor.reverserosuhavelund.expressions.*;
import com.androidmonitor.reverserosuhavelund.dynamicconditions.*;

public class Monitor {

	private String formula;
	private DynamicConditions dynamicConditions;
	private String message;
	private ArrayList<TraceEvent> satisfyingEvents;
	private Register now;
	private Register previous;

	public Monitor(String formula, String message) {

		this(formula, message, new DefaultConditions());
	}

	public Monitor(String formula, String message, DynamicConditions conditions) {

		this.formula = formula;
		this.message = message;
		this.dynamicConditions = (conditions != null) ? conditions : new DefaultConditions();
		this.satisfyingEvents = new ArrayList<TraceEvent>();

		Expression previousTree = ExpressionFactory.Instance().Tree(this.formula, null);
		Expression nowTree = ExpressionFactory.Instance().Tree(this.formula, previousTree);

		this.previous = new Register(previousTree.FlattenBreadthFirst());
		this.now = new Register(nowTree.FlattenBreadthFirst());
	}

	public EvaluationResult Evaluate(TraceEvent traceEvent) {

		EvaluationResult result = new EvaluationResult(this.message);

		if (this.now.Evaluate(traceEvent.Event())) {
			this.satisfyingEvents.add(traceEvent);
		};
		this.previous.Update(this.now);

		if (this.previous.Satisfied()) {
			result.SetSatisfyingEvents(this.dynamicConditions.Met(this.satisfyingEvents));
		}

		return result;
	}

	public void Reset() {
		
		this.now.Reset();
		this.previous.Reset();

		this.satisfyingEvents.clear();
	}
}
