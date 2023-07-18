public class JavaExample {
  public static void main(String[] args) {

    double maxHeapSize = Runtime.getRuntime().maxMemory();
    String sizeInReadableForm;

    double kbSize = maxHeapSize / 1024;
    double mbSize = kbSize / 1024;
    double gbSize = mbSize / 1024;

    if (gbSize > 0) {
      sizeInReadableForm = gbSize + " GB";
    } else if (mbSize > 0) {
      sizeInReadableForm = mbSize + " MB";
    } else {
      sizeInReadableForm = kbSize + " KB";
    }

    System.out.println("Maximum Heap Size: " + sizeInReadableForm);
  }
}