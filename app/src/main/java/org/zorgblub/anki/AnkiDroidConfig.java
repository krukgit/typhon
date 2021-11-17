package org.zorgblub.anki;

public final class AnkiDroidConfig {
    // Name of model which will be created in AnkiDroid
    public static final String MODEL_NAME = "Typhon";
    // Optional space separated list of tags to add to every note
    public static final String TAGS = "Typhon";
    // List of field names that will be used in AnkiDroid model
    public static final String[] FIELDS = {"Word","Meaning","Sentence", "Audio"};
    // List of card names that will be used in AnkiDroid (one for each direction of learning)
    public static final String[] CARD_NAMES = {"Reading Vocab"};
    // CSS to share between all the cards (optional). User will need to install the NotoSans font by themselves
    public static final String CSS = ".card {\n" +
            " font-size: 23px\n" +
            " text-align: center;\n" +
            " color: black;\n" +
            " background-color: #FFFAF0;\n" +
            " font-family: yuumichou;\n" +
            "}\n" +
            "@font-face {\n" +
            "font-family: yuumichou;\n" +
            "src: url(_yumin.ttf);\n" +
            "}\n" +
            "\n" +
            ".big { font-size: 42px; }\n" +
            ".small { font-size: 18px;}\n" +
            "\n" +
            ".emph {\n" +
            "font-weight: 600;\n" +
            "background-color: #FFFF66;\n" +
            "}\n";
    // Template for the question of each card
    static final String QFMT1 = "<div class=big>{{Word}}</div>";
    public static final String[] QFMT = {QFMT1};
    // Template for the answer (use identical for both sides)
    static final String AFMT1 = "{{Sentence}}<br><br><br>\n" +
            "{{Meaning}}<br><br>\n" +
            "{{Audio}}\n";
    public static final String[] AFMT = {AFMT1};
    // Define two keys which will be used when using legacy ACTION_SEND intent
    public static final String FRONT_SIDE_KEY = FIELDS[0];
    public static final String BACK_SIDE_KEY = FIELDS[2];
}