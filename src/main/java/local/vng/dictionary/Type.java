package local.vng.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum Type {
    TYPE_INT("Integer type for input", Comparator.comparing(i -> (int) i), Integer::parseInt),
    TYPE_STRING("String type for input", Comparator.comparing(i -> (String) i), i -> i);
    private final String description;
    private final Comparator<Object> comparator;
    private final Function<String, Object> convertFun;

}
