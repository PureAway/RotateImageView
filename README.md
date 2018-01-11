# Android ImageView 旋转动画
          动画实现不借助官方原生动画，自带背景圆形裁剪，效果类似网易云音乐
      主要解决官方动画耗资源、费性能，以及动画暂停后继续播放无法保存上次
      播放位置等问题
      
## 效果图如下

<img src="preview.gif"/>

## 如何使用
```
     // 开始播放旋转动画
     image.setRotate(true);
     // 结束播放旋转动画
     image.setRotate(false);
     // 设置动画播放速度
     image.setSpeed(40);

```

## 引入
```

       UbicAI.init(config, this);
       BaiduTTSPlayer ttsPlayer = new BaiduTTSPlayer.Builder(getApplicationContext())
                .ttsMode(TtsMode.MIX)
                .offlineVoice(OfflineResource.VOICE_FEMALE)
                .build();
//     UbicTTSPlayer ttsPlayer = UbicTTSPlayer.Factory.create();
//     UbicWakener wakener = new UbicWakener.Builder(getApplicationContext())
//              .forceInitKws(true)
//              .isArrayMic(false)
//              .build();
       BaiduWakener wakener = BaiduWakener.Factory.create(getApplicationContext());
//     BaiduRecorder recorder = new BaiduRecorder.Builder(getApplicationContext())
//               .build();
       SoGouRecorder recorder = new SoGouRecorder.Builder(getApplicationContext())
                .setGeoInfo("北京")
                .isArrayMic(false)
                .build();
//     UbicRecorder recorder = UbicRecorder.Factory.create("{\"provider\":\"sogou\"," +
//                "\"vad\":\"true\", \"recogmode\":\"stream\"}");
       UbicTranslator translator = UbicTranslator.Factory.create("{\"domain\":\"kit\"}");

       smartAI = new SmartAI.Builder(this)
                .filterWords("你好优家", "魔镜魔镜")  // 唤醒词过滤
                .audioStreamType(AudioManager.STREAM_MUSIC) // 提示音音频类型
                .autoRecord(true)  // 开启唤醒后自动打开语音识别功能
                .logLevel(L.DEBUG) // 设置日志级别
                .needWakeUp(true) // 设置语音识别前需要被唤醒
                .repeatRecord(true)  // 开启复听模式(一次唤醒多次识别)
                .recorduration(10) // 设置复听模式持续时间
                .requestAudioFocus(true) // 打开唤醒时抢占音频焦点
                .showHintTone(true) // 开启唤醒提示音
                .showNoConnTone(true) // 开启无网络提示音
                .ttsPlayer(ttsPlayer) // 设置tts播放器
                .wakener(wakener) // 设置唤醒者
                .recorder(recorder) // 设置语音识别者
                .translator(translator) // 设置语义解析者
                .listener(listener) // 设置监听器(唤醒，语音识别，语义解析过程及结果监听)
                .build();

       smartAI.start();

       smartAI.playTTS("今天天气很不错");

       smartAI.nlu("xxx");

       smartAI.stopNlu();

       smartAI.stop();


       ubicTTSPlayer = UbicTTSPlayer.Factory.create();
       ubicWakener = new UbicWakener.Builder(this)
                       .forceInitKws(true)
                       .isArrayMic(false)
                       .build();
       ubicRecorder = UbicRecorder.Factory.create("{\"provider\":\"sogou\", " +
                       "\"vad\":\"true\", \"recogmode\":\"stream\"}");
       ubicTranslator = UbicTranslator.Factory.create("{\"domain\":\"kit\"}");


       ubicWakener.startListening(new WakeUpListener() {
                   @Override
                   public void onWakeUp(String wakeUpWord) {
                       L.e(TAG, wakeUpWord);
                       printMsg("唤醒成功，唤醒词 = " + wakeUpWord);
                   }
               });

       if (null != ubicWakener) {
           ubicWakener.stopListening();
       }


       ubicRecorder.startListening(new SpeechCallback() {
                   @Override
                   public void onBeginningOfSpeech() {
                       L.e(TAG, "onBeginningOfSpeech()");
                       printMsg("开始录音");
                   }

                   @Override
                   public void onEndOfSpeech() {
                       L.e(TAG, "onEndOfSpeech()");
                       printMsg("录音结束");
                   }

                   @Override
                   public void onError(int error, String errorMsg) {
                       L.e(TAG, "onError(), error = " + error + ", errorMsg = " + errorMsg);
                       printMsg("ubic语音识别出现错误，errorCode = " + error + ", errorMsg = " + errorMsg);
                   }

                   @Override
                   public void onResult(String word, String nlu) {
                       L.e(TAG, "onResult(), " + word);
                       printMsg("ubic语音识别结果，word = " + word + ", nlu = " + nlu);
                   }
               });

       if (null != ubicRecorder) {
           ubicRecorder.stopListening();
        }

       ubicTranslator.translate(nlu_kit_test, new TranslateCallback() {
                    @Override
                    public void onTranslate(String nluResult) {
                        L.e(TAG, "onTranslate(), " + nluResult);
                        printMsg("ubic语义解析结果，nluResult = " + nluResult);
                    }

                    @Override
                    public void onTranslateError(int errorCode, String errorMsg) {
                        L.e(TAG, "onTranslateError(), errorCode" + errorCode + ", errorMsg = " + errorMsg);
                        printMsg("ubic语义解析出现错误，errorCode = " + errorCode + ", errorMsg = " + errorMsg);
                    }
            });

        if (null != ubicTranslator) {
            ubicTranslator.stopTranslate();
        }

       ubicTTSPlayer.playTTS("今天天气怎么样");

       if(null!=ubicTTSPlayer){
          ubicTTSPlayer.pausePlayer();
        }




```