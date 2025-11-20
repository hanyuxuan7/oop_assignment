package tutorials.tut1;

public class Dice {
    private Double DiceValue;

    public Dice() {
        DiceValue = Math.random() * 6 + 1;
    }

    public void setDiceValue() {
        DiceValue = Math.random() * 6 + 1;
    }

    public int getDiceValue() {
        return DiceValue.intValue();
    }

    public void printDiveValue() {
        System.out.println("Dice Value: " + getDiceValue());
    }
}
