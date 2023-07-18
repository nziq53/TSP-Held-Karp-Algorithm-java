import java.util.ArrayList;
import java.util.List;

public class CombinationGenerator {

  public static List<int[]> generateCombinations(int m, int n) {
    List<int[]> combinations = new ArrayList<>();
    int[] currentCombination = new int[n];
    generateCombinationsUtil(combinations, currentCombination, 0, 1, m, n);
    return combinations;
  }

  private static void generateCombinationsUtil(List<int[]> combinations, int[] currentCombination, int currentIndex,
      int startValue, int m, int n) {
    if (currentIndex == n) {
      combinations.add(currentCombination.clone());
      return;
    }

    for (int i = startValue; i <= m; i++) {
      currentCombination[currentIndex] = i;
      generateCombinationsUtil(combinations, currentCombination, currentIndex + 1, i + 1, m, n);
    }
  }

  public static void main(String[] args) {
    int m = 9; // mの値を指定
    int n = 3; // nの値を指定

    List<int[]> combinations = generateCombinations(m, n);

    // 結果の表示
    for (int[] combination : combinations) {
      for (int value : combination) {
        System.out.print(value + " ");
      }
      System.out.println();
    }
  }
}
