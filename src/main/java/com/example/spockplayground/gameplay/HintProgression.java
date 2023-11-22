package com.example.spockplayground.gameplay;

class HintProgression {

    public static final String Filler = "_";
    private final String secretWord;

    HintProgression(String secretWord) {
        this.secretWord = secretWord;
    }

    String nextHint(int attempts) {
        var hint = new StringBuilder();
        revealSecretLength(hint);
        revealLetterFromLeft(attempts, hint);
        revealLetterFromRight(attempts, hint);
        return hint.toString();
    }

    private void revealSecretLength(StringBuilder hint) {
        hint.append(Filler.repeat(secretWord.length()));
    }

    private void revealLetterFromLeft(int attempts, StringBuilder hint) {
        var left = (attempts + 1) / 2;
        for (int i = 0; i < left; i++) {
            hint.setCharAt(i, secretWord.charAt(i));
        }
    }

    private void revealLetterFromRight(int attempts, StringBuilder hint) {
        int lastIndex = secretWord.length() - 1;
        var right = attempts / 2;
        for (int i = right - 1; i >= 0; i--) {
            hint.setCharAt(lastIndex - i, secretWord.charAt(lastIndex - i));
        }
    }

}
