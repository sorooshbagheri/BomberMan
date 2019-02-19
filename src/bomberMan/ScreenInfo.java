package bomberMan;

import java.awt.*;

public abstract class ScreenInfo {
    private static GraphicsEnvironment localGraphicsEnvironment;
    private static GraphicsDevice defaultScreenDevice;
    private static DisplayMode currentDisplayMode;
    private static DisplayMode [] displayModes;
    private static int screenResolution;

    static {
        refresh();
    }

    public static void refresh(){
        setLocalGraphicsEnvironment();
        setDefaultScreenDevice();
        setCurrentDisplayMode();
        setDisplayModes();
        setScreenResolution();
    }

    public static GraphicsEnvironment getLocalGraphicsEnvironment() {
        return localGraphicsEnvironment;
    }

    private static void setLocalGraphicsEnvironment() {
        ScreenInfo.localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    }

    public static GraphicsDevice getDefaultScreenDevice() {
        return defaultScreenDevice;
    }

    private static void setDefaultScreenDevice() {
        ScreenInfo.defaultScreenDevice = localGraphicsEnvironment.getDefaultScreenDevice();
    }

    public static DisplayMode getCurrentDisplayMode() {
        return currentDisplayMode;
    }

    private static void setCurrentDisplayMode() {
        ScreenInfo.currentDisplayMode = defaultScreenDevice.getDisplayMode();
    }

    public static DisplayMode[] getDisplayModes() {
        return displayModes;
    }

    private static void setDisplayModes() {
        ScreenInfo.displayModes = defaultScreenDevice.getDisplayModes();
    }

    private static void setScreenResolution(){
        screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
    }

    /**
     * @return Screen resolution in dots-per-inch
     */
    public static int getScreenResolution(){
        return screenResolution;
    }
}
