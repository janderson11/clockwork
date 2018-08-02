package app.main.util;

public class ComputerAi {
    //returns percent chance that computer will play
    public static int makeDecision(int machineVal, int lowestVal, int secondLowest, boolean haventPassed, int aggro) {
        if (lowestVal - machineVal <= 2) { //vSmall
            if (secondLowest - machineVal <=2) {
                return haventPassed ? (aggro) : 100 + aggro;
            }
            if (secondLowest - machineVal <=5) {
                return haventPassed ? (60 + (int)(.4 * aggro)) : (100 + (int)(.4 * aggro));
            }
            if (secondLowest - machineVal <=10) {
                return haventPassed ? (90 + (int)(.2 * aggro)) : (100 + (int)(.2 * aggro));
            }
            if (secondLowest - machineVal <=17) {
                return haventPassed ? (100 + (int)(.1 * aggro)) : (100 + (int)(.1 * aggro));
            }
            if (secondLowest - machineVal > 17) {
                return (100);
            }
        }
        if (lowestVal - machineVal <= 5) { //Small
            if (secondLowest - machineVal <=2) {
                return haventPassed ? (40 + aggro) : (95 + aggro);
            }
            if (secondLowest - machineVal <=5) {
                return haventPassed ? (80 + (int)(.4 * aggro)) : (100 + (int)(.4 * aggro));
            }
            if (secondLowest - machineVal <=10) {
                return haventPassed ? (90 + (int)(.2 * aggro)) : (100 + (int)(.2 * aggro));
            }
            if (secondLowest - machineVal <=17) {
                return haventPassed ? (100 + (int)(.1 * aggro)) : (100 + (int)(.1 * aggro));
            }
            if (secondLowest - machineVal > 17) {
                return haventPassed ? (100 + (int)(.1 * aggro)) : (100 + (int)(.1 * aggro));
            }
        }
        if (lowestVal - machineVal <= 10) { //Medium
            if (secondLowest - machineVal <=2) {
                return haventPassed ? (5 + aggro) : (80 + aggro);
            }
            if (secondLowest - machineVal <=5) {
                return haventPassed ? (5 + (int)(.4 * aggro)) : (80 + (int)(.4 * aggro));
            }
            if (secondLowest - machineVal <=10) {
                return haventPassed ? (5 + (int)(.2 * aggro)) : (80 + (int)(.2 * aggro));
            }
            if (secondLowest - machineVal <=17) {
                return haventPassed ? (5 + (int)(.1 * aggro)) : (80 + (int)(.1 * aggro));
            }
            if (secondLowest - machineVal > 17) {
                return haventPassed ? (5 + (int)(.1 * aggro)) : (80 + (int)(.1 * aggro));
            }
        }
        if (lowestVal - machineVal <= 17) { //Large
            if (secondLowest - machineVal <=2) {
                return haventPassed ? 0 : (20 + aggro);
            }
            if (secondLowest - machineVal <=5) {
                return haventPassed ? 0 : (20 + (int)(.4 * aggro));
            }
            if (secondLowest - machineVal <=10) {
                return haventPassed ? 0 : (20 + (int)(.2 * aggro));
            }
            if (secondLowest - machineVal <=17) {
                return haventPassed ? 0 : (20 + (int)(.1 * aggro));
            }
            if (secondLowest - machineVal > 17) {
                return haventPassed ? 0 : (20 + (int)(.1 * aggro));
            }
        }
        if (lowestVal - machineVal > 17) { //xLarge
            return 0;
        }
        return 50 + (2 * aggro); //never gets here
    }
}
