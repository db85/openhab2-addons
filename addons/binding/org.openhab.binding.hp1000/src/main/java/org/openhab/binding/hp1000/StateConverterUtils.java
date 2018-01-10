package org.openhab.binding.hp1000;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;

public final class StateConverterUtils {

    @Nullable
    public static Double parseDouble(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Double.valueOf(value);
    }

    public static State toDecimalType(@Nullable Double value) {
        return (value == null) ? UnDefType.NULL : new DecimalType(value);
    }

    public static State toStringType(@Nullable String value) {
        return (value == null) ? UnDefType.NULL : new StringType(value);
    }

    private StateConverterUtils() {
        // utils class
    }
}
