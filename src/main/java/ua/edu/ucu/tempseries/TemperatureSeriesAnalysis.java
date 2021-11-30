package ua.edu.ucu.tempseries;

import java.util.Arrays;
import java.util.InputMismatchException;

public class TemperatureSeriesAnalysis {
    private double[] temperature;
    private int lastFreeIndex;

    public TemperatureSeriesAnalysis() {
        temperature = new double[]{};
        lastFreeIndex = -1;
    }

    public TemperatureSeriesAnalysis(double[] temperatureSeries) {
        if(temperatureSeries.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }

        double[] filteredTemperatures = new double[temperatureSeries.length];
        lastFreeIndex = 0;
        InputMismatchException wrongInput = null;
        for(double temp: temperatureSeries) {
            if(temp < -273) {
                wrongInput = new InputMismatchException("Value " + temp + " is lower than -273°");
            } else {
                filteredTemperatures[lastFreeIndex] = temp;
                lastFreeIndex++;
            }
        }
        temperature = filteredTemperatures;

        if(wrongInput != null) {
            throw wrongInput;
        }
    }

    public double average() {
        if(lastFreeIndex == -1) {
            throw new IllegalArgumentException("Array is empty");
        }

        double sum = 0;
        for(int i = 0; i < lastFreeIndex; i++) {
            sum += temperature[i];
        }

        return sum / lastFreeIndex;
    }

    public double deviation() {
        double averageValue = average();
        double result = 0;

        for(int i = 0; i < lastFreeIndex; i++) {
            result += Math.abs(temperature[i] - averageValue) * Math.abs(temperature[i] - averageValue);
        }

        return result;
    }

    public double min() {
        return findTempClosestToValue(-273);
    }

    public double max() {
        return findTempClosestToValue(273);
    }

    public double findTempClosestToZero() {
        return findTempClosestToValue(0);
    }

    public double findTempClosestToValue(double tempValue) {
        if(lastFreeIndex == -1) {
            throw new IllegalArgumentException("Array is empty");
        }

        double minDifference = Double.MAX_VALUE;
        double closestTemp = 0;
        for(int i = 0; i < lastFreeIndex; i++) {
            if(Math.abs(temperature[i] - tempValue) < minDifference) {
                minDifference = Math.abs(temperature[i] - tempValue);
                closestTemp = temperature[i];
            }
        }

        return closestTemp;
    }

    public double[] findTempsLessThen(double tempValue) {
        double[] less = new double[lastFreeIndex];
        int currIndex = 0;
        for(int i = 0; i < lastFreeIndex; i++) {
            if(temperature[i] < tempValue) {
                less[currIndex] = temperature[i];
                currIndex++;
            }
        }

        return Arrays.copyOf(less, currIndex);
    }

    public double[] findTempsGreaterThen(double tempValue) {
        double[] greater = new double[lastFreeIndex];
        int currIndex = 0;
        for(int i = 0; i < lastFreeIndex; i++) {
            if(temperature[i] > tempValue) {
                greater[currIndex] = temperature[i];
                currIndex++;
            }
        }

        return Arrays.copyOf(greater, currIndex);
    }

    public TempSummaryStatistics summaryStatistics() {
        double avgTemp = average();
        double devTemp = deviation();
        double minTemp = min();
        double maxTemp = max();

        return new TempSummaryStatistics(avgTemp, devTemp, minTemp, maxTemp);
    }

    public int addTemps(double... temps) {
        if(lastFreeIndex == -1) {
            temperature = new double[1];
            lastFreeIndex = 0;
        }

        InputMismatchException wrongInput = null;
        for(double temp: temps) {
            if(temp < -273) {
                wrongInput = new InputMismatchException("Value " + temp + " is lower than -273°");
            } else {
                if(lastFreeIndex == temperature.length) {
                    double[] resized = new double[temperature.length * 2];
                    for(int i = 0; i < lastFreeIndex; i++) {
                        resized[i] = temperature[i];
                    }
                    resized[lastFreeIndex] = temp;
                    temperature = resized;
                }else {
                    temperature[lastFreeIndex] = temp;
                }
                lastFreeIndex++;
            }
        }

        if(wrongInput != null) {
            throw wrongInput;
        }

        int sum = 0;
        for(int i = 0; i < lastFreeIndex; i++) {
            sum += temperature[i];
        }

        return sum;
    }
}
