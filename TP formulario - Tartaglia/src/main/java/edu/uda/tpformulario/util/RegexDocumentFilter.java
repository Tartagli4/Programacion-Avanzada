package edu.uda.tpformulario.util;

import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RegexDocumentFilter extends DocumentFilter {
    private final Pattern pattern;
    private final int maxLength;
    private final boolean uppercase;

    public RegexDocumentFilter(String regex, int maxLength) {
        this(regex, maxLength, false);
    }

    public RegexDocumentFilter(String regex, int maxLength, boolean uppercase) {
        this.pattern = Pattern.compile(regex);
        this.maxLength = maxLength;
        this.uppercase = uppercase;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
            throws BadLocationException {
        replace(fb, offset, 0, text, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null) {
            return;
        }

        String processedText = uppercase ? text.toUpperCase() : text;
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset) + processedText + currentText.substring(offset + length);

        if (newText.length() <= maxLength && pattern.matcher(newText).matches()) {
            super.replace(fb, offset, length, processedText, attrs);
        }
    }
}
