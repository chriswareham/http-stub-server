package au.com.sensis.stubby.service.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchResult implements Comparable<MatchResult> {

    private List<MatchField> fields = new ArrayList<MatchField>();

    private boolean matches = true;

    private int score;

    public void addField(final MatchField field) {
        fields.add(field);
        matches = matches && field.getMatchType() == MatchType.MATCH;
        score += field.score();
    }

    public List<MatchField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public boolean matches() {
        return matches;
    }

    public int score() {
        return score;
    }

    @Override
    public String toString() {
        return fields.toString();
    }

    @Override
    public int compareTo(final MatchResult other) {
        // highest score first
        return (score < other.score) ? 1 : (score > other.score) ? -1 : 0;
    }
}
