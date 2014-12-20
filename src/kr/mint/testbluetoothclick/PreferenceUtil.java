package kr.mint.testbluetoothclick;

public class PreferenceUtil extends BasePreferenceUtil
{
   private static final String PRESSED_KEY = "pressed_key";
   private static final String RECORD = "record";
   
   
   public static void putPressedKey(int $key)
   {
      put(PRESSED_KEY, $key);
   }
   
   
   public static int pressedKey()
   {
      return get(PRESSED_KEY, -999);
   }
   
   
   public static void putRecordState(boolean $state)
   {
      put(RECORD, $state);
   }
   
   
   public static boolean isRecording()
   {
      return get(RECORD, false);
   }
}
