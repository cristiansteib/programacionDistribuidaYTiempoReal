public class StandardDeviation {

    public static double calculateSD(long numArray[]) {
        double standardDeviation = 0.0;
        double mean = StandardDeviation.calculateMean(numArray);
        for (double num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation / numArray.length);
    }

    public static double calculateMean(long numArray[]){
        double sum = 0.0;
        for (double num : numArray) {
            sum += num;
        }
        return (sum / numArray.length);
    }
}