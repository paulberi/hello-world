package se.metria.matdatabas.service.common;

import java.math.BigDecimal;
import java.util.*;

public class ChangeLog {
	private List<String> diffs = new ArrayList<>();
	private boolean showValues;

	public ChangeLog(boolean showValues) {
		this.showValues = showValues;
	}

	public boolean isEmpty() {
		return diffs.isEmpty();
	}

	public String toString() {
		return String.join(", ", diffs);
	}

	public boolean evaluatePropertyChange(String label, Object first, Object second) {
		return evaluatePropertyChange(label, first, second, showValues);
	}

	public boolean evaluatePropertyChange(String label, Object first, Object second, boolean showValues) {
		if (first instanceof BigDecimal && second instanceof BigDecimal) {
			BigDecimal firstBigDecimal = (BigDecimal) first;
			BigDecimal secondBigDecimal = (BigDecimal) second;
			if (firstBigDecimal.compareTo(secondBigDecimal) != 0) {
				formatPropertyChange(label, first, second, showValues);
			}
			return true;
		} else if (!Objects.equals(first, second)) {
			formatPropertyChange(label, first, second, showValues);
			return true;
		}
		return false;
	}

	public <T> boolean evaluatePropertyChangeCollection(String labelAdd, String labelRemove, Collection<T> before, Collection<T> after) {
		var removed = new HashSet<>(before);
		removed.removeAll(after);

		var added = new HashSet<>(after);
		added.removeAll(before);

		if (!removed.isEmpty()) {
			diffs.add(String.format(labelRemove + ": %s", removed.toString()));
		}
		if (!added.isEmpty()) {
			diffs.add(String.format(labelAdd + ": %s", added.toString()));
		}

		return !(removed.isEmpty() && added.isEmpty());
	}

	private void formatPropertyChange(String label, Object first, Object second, boolean showValues) {
		if (showValues) {
			diffs.add(String.format(label + ": %s => %s", first, second));
		} else {
			diffs.add(label);
		}
	}

	public boolean evaluateSizeDiff(String label, Collection first, Collection second) {
		return evaluateSizeDiff(label, first.size(), second.size());
	}

	public boolean evaluateSizeDiff(String label, Integer first, Integer second) {
		if (!first.equals(second)) {
			return diffs.add(String.format(label + ": %s st => %s st", first, second));
		}
		return false;
	}

	public void addEntry(String label) {
		diffs.add(label);
	}
}
