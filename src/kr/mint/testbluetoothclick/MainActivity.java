package kr.mint.testbluetoothclick;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity
{
   private MediaRecorder _recorder;
   private AudioManager _audioManager;
   private TextView _textFilename, _textStatus;
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
//      Intent serviceIntent = new Intent(getApplicationContext(), MediaButtonMonitorService.class);
//      startService(serviceIntent);
      
      _textFilename = (TextView) findViewById(R.id.text_filename);
      _textStatus = (TextView) findViewById(R.id.text_state);
      
      Button btn1 = (Button) findViewById(R.id.btn_start);
      btn1.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            start();
         }
      });
      
      Button btn2 = (Button) findViewById(R.id.btn_stop);
      btn2.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            stop();
         }
      });
      
      Log.w("MainActivity.java | onCreate", "|" + getIntent().getAction() + "|");
      String action = getIntent().getAction();
      if (action.equals(getPackageName() + "." + Consts.KEY_TOGGLE_RECORD_STATE))
         toggleRecordState();
   }
   
   
   @Override
   protected void onNewIntent(Intent intent)
   {
      super.onNewIntent(intent);
      Log.w("MainActivity.java | onNewIntent", "|" + intent.getAction() + "|");
      String action = intent.getAction();
      if (action.equals(getPackageName() + "." + Consts.KEY_TOGGLE_RECORD_STATE))
         toggleRecordState();
   }
   
   
   private void toggleRecordState()
   {
//      Intent serviceIntent = new Intent(getApplicationContext(), MediaButtonMonitorService.class);
//      stopService(serviceIntent);
      
      Log.w("MainActivity.java | toggleRecordState", "| recording? " + PreferenceUtil.isRecording() + "|");
      if (PreferenceUtil.isRecording())
         stop();
      else
         start();
   }
   
   
   private void start()
   {
      File path = new File(Environment.getExternalStorageDirectory() + "/VoiceRecord");
      if (!path.exists())
         path.mkdirs();
      
      Log.w("BluetoothReceiver.java | startRecord", "|" + path.toString() + "|");
      
      File file = null;
      try
      {
         file = File.createTempFile("voice_", ".m4a", path);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      Log.w("BluetoothReceiver.java | startRecord", "|" + file.toString() + "|");
      _textFilename.setText(file.toString());
      
      _audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      _audioManager.startBluetoothSco();
      
      Runnable runn = new Runnable()
      {
         @Override
         public void run()
         {
//            stop();
         }
      };
      Handler handler = new Handler();
      handler.postDelayed(runn, 3000);
      
      try
      {
         _textStatus.setText("recording");
         PreferenceUtil.putRecordState(true);
         
         _recorder = new MediaRecorder();
         _recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
         _recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
         _recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
         _recorder.setOutputFile(file.toString());
         _recorder.prepare();
         _recorder.start();
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   
   private void stop()
   {
      Log.w("MainActivity.java | stop", "|" + "================== stop record" + "|");
      
      try
      {
         _textStatus.setText("stop");
         PreferenceUtil.putRecordState(false);
         _audioManager.stopBluetoothSco();
         
         _recorder.stop();
         _recorder.release();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
