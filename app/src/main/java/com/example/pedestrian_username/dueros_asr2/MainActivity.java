package com.example.pedestrian_username.dueros_asr2;

import com.baidu.speech.VoiceRecognitionService;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements RecognitionListener, View.OnClickListener {

    private TextView textView;
    private Button button;
    private SpeechRecognizer speechRecognizer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        // 创建识别器
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, new ComponentName(this, VoiceRecognitionService.class));
        // 注册监听器
        speechRecognizer.setRecognitionListener(this);
    }
    // 开始识别
    void startASR() {
        Intent recognizerIntent = new Intent();
//        bindParams(intent);
        recognizerIntent.putExtra("grammar", "asset:///baidu_speech_grammar.bsg");
        speechRecognizer.startListening(recognizerIntent);
        textView.setText("识别开始");
    }
    void bindParams(Intent intent) {
        // 设置识别参数
    }
    public void onReadyForSpeech(Bundle params) {
        // 准备就绪
        textView.setText("准备");
    }
    @Override
    public void onBeginningOfSpeech() {
        // 开始说话处理
        textView.setText("开始说话");
    }
    public void onRmsChanged(float rmsdB) {
        // 音量变化处理
        textView.setText("正在说话");
    }
    public void onBufferReceived(byte[] buffer) {
        // 录音数据传出处理
    }
    public void onEndOfSpeech() {
        // 说话结束处理
        textView.setText("结束说话");
    }
    public void onError(int error) {
        // 出错处理
        textView.setText("出错"+error);
    }
    public void onResults(Bundle results) {
        // 最终结果处理
        String origin_result = results.getString("origin_result");
        try {
            JSONObject jsonObject = new JSONObject(origin_result);
            JSONObject result = jsonObject.optJSONObject("result");
            JSONObject content = jsonObject.getJSONObject("content");
            String raw_text = result.optString("raw_text");
            if(raw_text != "")
                textView.setText("识别结果(数组形式): " + raw_text);
            else
                textView.setText("识别结果(数组形式): " + content.getJSONArray("item").get(0));
        } catch (JSONException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    public void onPartialResults(Bundle partialResults) {
        // 临时结果处理
    }
    public void onEvent(int eventType, Bundle params) {
        // 处理事件回调
        textView.setText("事件");
    }

    @Override
    public void onClick(View view) {
        startASR();
    }
}
