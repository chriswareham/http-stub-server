package au.com.sensis.stubby.service.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MatchResultTest {

    @Mock
    private MatchField field1;
    @Mock
    private MatchField field2;
    @Mock
    private MatchField field3;

    private MatchResult result1;
    private MatchResult result2;

    @Before
    public void before() {
        Mockito.when(field1.score()).thenReturn(1);
        Mockito.when(field2.score()).thenReturn(2);
        Mockito.when(field3.score()).thenReturn(3);

        Mockito.when(field1.getMatchType()).thenReturn(MatchType.MATCH_FAILURE);
        Mockito.when(field2.getMatchType()).thenReturn(MatchType.MATCH);
        Mockito.when(field3.getMatchType()).thenReturn(MatchType.MATCH);

        result1 = new MatchResult();
        result2 = new MatchResult();

        result1.addField(field1);

        result2.addField(field2);
        result2.addField(field3);
    }

    @Test
    public void testOrder() { // best score first
        List<MatchResult> fields = new ArrayList<MatchResult>(Arrays.asList(result1, result2));

        Collections.sort(fields);

        Assert.assertEquals(5, fields.get(0).score()); // assert adds up score
        Assert.assertEquals(1, fields.get(1).score());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(result2.matches());
    }

    @Test
    public void testDoesntMatch() {
        Assert.assertFalse(result1.matches());
    }
}
