package local.vng.setting;

public final class Settings {
    /**
     * В случае большого неотсортированного входящего файла рекомендуется использовать
     * дефолные значения максимального размера файла и буфера чтения файла.
     * Максимальный размер файла - это размер до размера которого не будет происходить его фрагментация.
     * Максимальный размер буфера - это размер фрагмента при фрагментации.
     * <p>
     * Возможен ручной ввод значений через ключи -f(Максимальный размер файла) и -b(Максимальный размер буфера)
     */
    public final static long DEFAULT_MAX_FILE_SIZE = 1073741824;
    public final static long MIN_MAX_FILE_SIZE = 10485760;
    public final static long DEFAULT_BUFFER_SIZE = 50000000;
    public final static long MIN_BUFFER_SIZE = 50000;
    public final static String CORRUPTED_FILENAME = "corruptedData.txt";
    public final static String UNSORTED_FILENAME = "unsorted.txt";

    private Settings() {
    }
}
