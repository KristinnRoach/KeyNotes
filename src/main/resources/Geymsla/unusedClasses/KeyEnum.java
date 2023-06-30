package Geymsla.unusedClasses;

import javafx.scene.input.KeyCode;

public enum KeyEnum {
    C5(KeyCode.DIGIT1, 36, "C5"),
    D5(KeyCode.DIGIT2, 38, "D5"),
    E5(KeyCode.DIGIT3, 40, "E5"),
    F5(KeyCode.DIGIT4, 41, "F5"),
    G5(KeyCode.DIGIT5, 43, "G5"),
    A5(KeyCode.DIGIT6, 45, "A5"),
    B5(KeyCode.DIGIT7, 47, "B5"),
    C6(KeyCode.DIGIT8, 48, "C6"),
    D6(KeyCode.DIGIT9, 50, "D6"),
    E6(KeyCode.DIGIT0, 52, "E6"),
    Q(KeyCode.Q, 24, "C4"),
    W(KeyCode.W, 26, "D4"),
    E(KeyCode.E, 28, "E4"),
    R(KeyCode.R, 29, "F4"),
    T(KeyCode.T, 31, "G4"),
    Y(KeyCode.Y, 33, "A4"),
    U(KeyCode.U, 35, "B4"),
    I(KeyCode.I, 36, "C5"),
    O(KeyCode.O, 38, "D5"),
    P(KeyCode.P, 40, "E5"),
    A(KeyCode.A, 12, "C3"),
    S(KeyCode.S, 14, "D3"),
    D(KeyCode.D, 16, "E3"),
    F(KeyCode.F, 17, "F3"),
    G(KeyCode.G, 19, "G3"),
    H(KeyCode.H, 21, "A3"),
    J(KeyCode.J, 23, "B3"),
    K(KeyCode.K, 24, "C4"),
    L(KeyCode.L, 26, "D4"),
    SEMICOLON(KeyCode.SEMICOLON, 28, "E4"),
    Z(KeyCode.Z, 0, "C2"),
    X(KeyCode.X, 2, "D2"),
    C(KeyCode.C, 4, "E2"),
    V(KeyCode.V, 5, "F2"),
    B(KeyCode.B, 7, "G2"),
    N(KeyCode.N, 9, "A2"),
    M(KeyCode.M, 11, "B2"),
    COMMA(KeyCode.COMMA, 12, "C3"),
    PERIOD(KeyCode.PERIOD, 14, "D3"),
    SLASH(KeyCode.SLASH, 16, "E3");

    private final KeyCode keyCode;
    private final int keyIndex;
    private final String noteName;

    KeyEnum(KeyCode keyCode, int keyIndex, String noteName) {
        this.keyCode = keyCode;
        this.keyIndex = keyIndex;
        this.noteName = noteName;
    }

    public void transposeEnum(int semitones){

    }
    public KeyCode getKeyCode() {
        return keyCode;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public String getNoteName() {
        return noteName;
    }
}
