package nu.epsilon.rss.common;

/**
 *
 * @author Pär Sikö
 */
public class PerformanceTuning {

    private static boolean useNanoSource = true;
    private static int resolution = 0;
    private static boolean cacheImages = true;
    private static boolean useCompatibleImages = true;
    private static boolean useOpaqueImages = true;
    private static boolean useGradients = true;
    private static boolean useSoftClip = false;

    private PerformanceTuning() {
    }

    public static boolean isUseGradients() {
        return useGradients;
    }

    public static void setUseGradients(boolean useGradients) {
        PerformanceTuning.useGradients = useGradients;
    }

    public static boolean isCacheImages() {
        return cacheImages;
    }

    public static void setUseCacheImages(boolean cache) {
        cacheImages = cache;
    }

    public static int getResolution() {
        return resolution;
    }

    public static void setResolution(int res) {
        resolution = res;
    }

    public static boolean isUseCompatibleImages() {
        return useCompatibleImages;
    }

    public static void setUseCompatibleImages(boolean useCompatible) {
        useCompatibleImages = useCompatible;
    }

    public static boolean isUseNanoSource() {
        return useNanoSource;
    }

    public static void setUseNanoSource(boolean useNano) {
        useNanoSource = useNano;
    }

    public static boolean isUseOpaqueImages() {
        return useOpaqueImages;
    }

    public static void setUseOpaqueImages(boolean useOpaqueImages) {
        PerformanceTuning.useOpaqueImages = useOpaqueImages;
    }

    public static boolean isUseSoftClip() {
        return useSoftClip;
    }

    public static void setUseSoftClip(boolean useSoftClip) {
        PerformanceTuning.useSoftClip = useSoftClip;
    }
}
