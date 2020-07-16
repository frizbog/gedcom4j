package org.gedcom4j.writer;

import junit.framework.TestCase;
import org.gedcom4j.exception.*;
import org.gedcom4j.model.*;
import org.junit.Test;

import java.util.*;

public class AbstractEmitterTest extends TestCase {
    @Test
    public void testEmitLinesOfText() throws WriterCancelledException {
        GedcomWriter writer = new GedcomWriter(new Gedcom());

        AbstractEmitter<Collection<NoteRecord>> uut =
            new AbstractEmitter<Collection<NoteRecord>>(writer, 0, null) {
                @Override
                protected void emit() {
                }
            };

        List<String> lines = new ArrayList<>();
        lines.add("line 1");
        lines.add("line 2");
        lines.add("line 3");

        uut.emitLinesOfText(0, "@N99@", "NOTE", lines);

        assertEquals("0 @N99@ NOTE line 1", writer.lines.get(0));
        assertEquals("1 CONT line 2", writer.lines.get(1));
        assertEquals("1 CONT line 3", writer.lines.get(2));
    }
}
