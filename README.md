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

     dependencies {
       ...
       compile 'com.zcy:rotateimageview:1.0.0'
     }

```